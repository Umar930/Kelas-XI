package com.umar.aplikasimonitoringkelas.data.model

import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Test

class KehadiranDeserializationTest {

    @Test
    fun kodeGuruIsDeserialized() {
        val json = """
        {
          "id": 123,
          "guru_id": 45,
          "kode_guru": "G001",
          "nama_guru": "Budi Santoso",
          "mapel": "Bahasa Indonesia",
          "tanggal": "2025-12-11",
          "status": "sakit",
          "keterangan": "sakit demam"
        }
        """.trimIndent()

        val item = Gson().fromJson(json, KehadiranItem::class.java)

        assertEquals("G001", item.kodeGuru)
    }
}