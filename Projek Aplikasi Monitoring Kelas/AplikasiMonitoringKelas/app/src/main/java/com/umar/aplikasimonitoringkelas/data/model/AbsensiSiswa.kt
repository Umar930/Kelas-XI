package com.umar.aplikasimonitoringkelas.data.model

import com.google.gson.annotations.SerializedName

data class AbsensiSiswa(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("siswa_id")
    val siswaId: Int,
    
    @SerializedName("siswa")
    val siswa: SiswaDetail,
    
    @SerializedName("tanggal")
    val tanggal: String,
    
    @SerializedName("status")
    val status: String, // "hadir", "sakit", "izin"
    
    @SerializedName("keterangan")
    val keterangan: String?,
    
    @SerializedName("created_at")
    val createdAt: String,
    
    @SerializedName("updated_at")
    val updatedAt: String,
    
    @SerializedName("is_deleted")
    val isDeleted: Boolean? = false,
    
    @SerializedName("deleted_at")
    val deletedAt: String? = null,
    
    // ✅ FIELD VIRTUAL BARU - Dari backend accessor
    @SerializedName("email_siswa")
    val emailSiswa: String? = null,
    
    @SerializedName("nama_siswa")
    val namaSiswa: String? = null,
    
    @SerializedName("nama_kelas")
    val namaKelas: String? = null
)

data class SiswaDetail(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("nama")
    val nama: String,
    
    @SerializedName("nis")
    val nis: String,
    
    @SerializedName("email")
    val email: String? = null,
    
    @SerializedName("jenis_kelamin")
    val jenisKelamin: String? = null, // "L" atau "P"
    
    @SerializedName("kelas")
    val kelas: KelasDetail? = null
)

data class KelasDetail(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("nama")
    val nama: String
)

data class AbsensiSiswaRequest(
    @SerializedName("siswa_id")
    val siswaId: Int? = null, // Nullable - backend akan auto-detect dari token
    
    @SerializedName("tanggal")
    val tanggal: String,
    
    @SerializedName("status")
    val status: String,
    
    @SerializedName("keterangan")
    val keterangan: String?
)

data class AbsensiSiswaUpdateRequest(
    @SerializedName("status")
    val status: String,
    
    @SerializedName("keterangan")
    val keterangan: String?
)

data class AbsensiSiswaResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("data")
    val data: AbsensiSiswa? = null
)

data class AbsensiSiswaListResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("data")
    val data: List<AbsensiSiswa>
)

data class AbsensiStatistik(
    @SerializedName("total")
    val total: Int,
    
    @SerializedName("hadir")
    val hadir: Int,
    
    @SerializedName("sakit")
    val sakit: Int,
    
    @SerializedName("izin")
    val izin: Int,
    
    @SerializedName("persentase_hadir")
    val persentaseHadir: Double
)

data class AbsensiStatistikResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("data")
    val data: AbsensiStatistik
)

data class MessageResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("message")
    val message: String
)
