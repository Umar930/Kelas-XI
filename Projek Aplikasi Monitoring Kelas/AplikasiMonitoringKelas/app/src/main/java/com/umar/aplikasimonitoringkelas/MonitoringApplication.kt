package com.umar.aplikasimonitoringkelas

import android.app.Application
import android.util.Log
import java.io.File

class MonitoringApplication : Application() {
    companion object {
        private const val TAG = "MonitoringApp"
        
        @Volatile
        private var _instance: MonitoringApplication? = null
        
        val instance: MonitoringApplication
            get() {
                return _instance ?: throw IllegalStateException(
                    "MonitoringApplication not initialized. Make sure android:name is set in AndroidManifest.xml"
                )
            }
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "=================================")
        Log.d(TAG, "MonitoringApplication onCreate() called")
        Log.d(TAG, "Package: ${packageName}")
        Log.d(TAG, "=================================")
        _instance = this

        // Global handler to capture uncaught exceptions and persist stacktrace
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            try {
                val crashDir = File(filesDir, "crashes")
                if (!crashDir.exists()) crashDir.mkdirs()
                val ts = System.currentTimeMillis()
                val crashFile = File(crashDir, "crash_$ts.log")
                val trace = Log.getStackTraceString(throwable)
                crashFile.writeText("Thread: ${thread.name}\n\n$trace")
                Log.e(TAG, "Uncaught exception written to ${crashFile.absolutePath}", throwable)
            } catch (e: Exception) {
                Log.e(TAG, "Failed writing crash file: ${e.message}", e)
            }

            // Let the default behavior continue (kill process)
            val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
            try {
                defaultHandler?.uncaughtException(thread, throwable)
            } catch (ignored: Throwable) {
                android.os.Process.killProcess(android.os.Process.myPid())
                kotlin.system.exitProcess(2)
            }
        }
    }
    
    override fun onTerminate() {
        Log.d(TAG, "MonitoringApplication onTerminate() called")
        super.onTerminate()
    }
}
