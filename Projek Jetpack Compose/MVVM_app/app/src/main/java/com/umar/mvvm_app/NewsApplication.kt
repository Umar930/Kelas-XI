package com.umar.mvvm_app

import android.app.Application

/**
 * Class Application kustom untuk Aplikasi Berita
 */
class NewsApplication : Application() {
    
    // Inisialisasi ThemeManager sebagai lazy singleton
    val themeManager by lazy { ThemeManager(this) }
    
    override fun onCreate() {
        super.onCreate()
        // Inisialisasi aplikasi lainnya dapat dilakukan di sini
    }
}