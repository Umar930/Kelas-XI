package com.umar.aplikasimonitoringkelas

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umar.aplikasimonitoringkelas.kurikulum.KurikulumDashboardActivity

class KurikulumActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Simple redirect to the new Kurikulum dashboard activity
        startActivity(Intent(this, KurikulumDashboardActivity::class.java))
        finish()
    }
}

