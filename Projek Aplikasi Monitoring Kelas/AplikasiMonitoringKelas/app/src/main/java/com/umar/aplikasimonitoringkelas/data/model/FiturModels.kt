package com.umar.aplikasimonitoringkelas.data.model

import com.google.gson.annotations.SerializedName

// ========== RESPONSE WRAPPER ==========
data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T?
)

// ========== BASIC ENTITIES ==========
data class Guru(
    val id: Int,
    val nama: String,
    @SerializedName("kode_guru") val kodeGuru: String,
    val email: String? = null,
    @SerializedName("no_telepon") val noTelepon: String? = null
)

data class Kelas(
    val id: Int,
    @SerializedName("kode_kelas") val kodeKelas: String,
    @SerializedName("nama_kelas") val namaKelas: String,
    val tingkat: String,
    val jurusan: String,
    val kapasitas: Int? = null,
    @SerializedName("wali_kelas") val waliKelas: String? = null,
    @SerializedName("jumlah_siswa") val jumlahSiswa: Int? = null
)

data class Mapel(
    val id: Int,
    val mapel: String? = null,
    val nama: String? = null,
    @SerializedName("kode_mapel") val kodeMapel: String? = null
)

data class Siswa(
    val id: Int,
    val nis: String,
    val nama: String,
    @SerializedName("jenis_kelamin") val jenisKelamin: String,
    val email: String? = null,
    val status: String? = null
)

// ========== KEHADIRAN GURU ==========
data class KehadiranGuru(
    val id: Int,
    @SerializedName("jadwal_id") val jadwalId: Int,
    @SerializedName("guru_id") val guruId: Int,
    @SerializedName("kelas_id") val kelasId: Int,
    @SerializedName("mapel_id") val mapelId: Int,
    val tanggal: String,
    val status: String, // hadir, tidak_hadir, izin, sakit
    val keterangan: String? = null,
    @SerializedName("input_by_siswa_id") val inputBySiswaId: Int? = null,
    @SerializedName("input_by_kurikulum_id") val inputByKurikulumId: Int? = null,
    val guru: Guru,
    val kelas: Kelas,
    val mapel: Mapel,
    @SerializedName("created_at") val createdAt: String? = null,
    @SerializedName("updated_at") val updatedAt: String? = null
)

data class InputKehadiranRequest(
    @SerializedName("jadwal_id") val jadwalId: Int? = null,
    @SerializedName("jadwal_kelas_id") val jadwalKelasId: Int? = null,
    @SerializedName("guru_id") val guruId: Int? = null,
    @SerializedName("kelas_id") val kelasId: Int,
    @SerializedName("mapel_id") val mapelId: Int? = null,
    val tanggal: String,
    val status: String,
    val keterangan: String? = null
)

// ========== TAMBAH/HAPUS JADWAL ==========
data class TambahJadwalRequest(
    @SerializedName("kelas_id") val kelasId: Int,
    @SerializedName("mapel_id") val mapelId: Int,
    @SerializedName("guru_id") val guruId: Int,
    val hari: String,           // "Senin", "Selasa", dll
    @SerializedName("jam_mulai") val jamMulai: String,    // "08:00"
    @SerializedName("jam_selesai") val jamSelesai: String // "09:30"
)

// Siswa Mapel List Response (untuk endpoint /api/siswa/list-mapel)
data class SiswaMapelListResponse(
    val success: Boolean,
    val message: String,
    val total: Int? = null,
    val data: List<Mapel>? = null
)

// NOTE: MapelListResponse sudah didefinisikan di PlaceholderResponses.kt dan model/Mapel.kt

// ========== NOTIFIKASI ==========
data class Notifikasi(
    val id: Int,
    @SerializedName("kelas_id") val kelasId: Int,
    @SerializedName("guru_id") val guruId: Int,
    @SerializedName("kode_guru") val kodeGuru: String? = null,
    @SerializedName("guru_name") val guruName: String? = null,
    @SerializedName("mapel_id") val mapelId: Int? = null,
    @SerializedName("mapel_name") val mapelName: String? = null,
    val tanggal: String,
    val tipe: String, // izin, sakit, guru_pengganti
    val pesan: String,
    @SerializedName("guru_pengganti_id") val guruPenggantiId: Int? = null,
    @SerializedName("is_read") val isRead: Boolean,
    @SerializedName("created_at") val createdAt: String,
    val guru: Guru,
    val mapel: Mapel? = null,
    @SerializedName("guru_pengganti") val guruPengganti: Guru? = null
)

data class NotifikasiData(
    val notifikasi: List<Notifikasi>,
    @SerializedName("unread_count") val unreadCount: Int
)

// ========== JADWAL ==========
data class JadwalKelas(
    val id: Int,
    @SerializedName("jam_mulai") val jamMulai: String,
    @SerializedName("jam_selesai") val jamSelesai: String,
    val hari: String? = null,
    val guru: Guru,
    val mapel: Mapel,
    val kehadiran: KehadiranInfo? = null
)

data class KehadiranInfo(
    val id: Int,
    val status: String,
    val keterangan: String? = null,
    @SerializedName("input_by") val inputBy: String? = null
)

// ========== SISWA JADWAL RESPONSE (Sesuai format API /api/siswa/jadwal) ==========
data class JadwalResponse(
    val success: Boolean,
    val message: String,
    val hari: String? = null,
    @SerializedName("kelas_id") val kelasId: Int? = null,
    val kelas: String? = null,
    val total: Int? = null,
    val data: List<JadwalItem>? = null
)

data class JadwalItem(
    val id: Int,
    @SerializedName("jadwal_id") val jadwalId: Int,
    @SerializedName("kelas_id") val kelasId: Int? = null,
    val kelas: String? = null,
    @SerializedName("guru_id") val guruId: Int? = null,
    val guru: String? = null,
    @SerializedName("kode_guru") val kodeGuru: String? = null,
    @SerializedName("nama_guru") val namaGuru: String? = null,
    @SerializedName("guru_nama") val guruNama: String? = null,
    @SerializedName("mapel_id") val mapelId: Int? = null,
    val mapel: String? = null,
    @SerializedName("nama_mapel") val namaMapel: String? = null,
    @SerializedName("mapel_nama") val mapelNama: String? = null,
    val hari: String? = null,
    @SerializedName("jam_ke") val jamKe: Int = 0,
    @SerializedName("jam_mulai") val jamMulai: String? = null,
    @SerializedName("jam_selesai") val jamSelesai: String? = null,
    val jam: String? = null,
    @SerializedName("status_kehadiran") val statusKehadiran: String? = null, // "hadir", "tidak_hadir", "belum_diisi"
    @SerializedName("sudah_diisi") val sudahDiisi: Boolean = false,
    @SerializedName("request_pengganti") val requestPengganti: Boolean? = null,
    @SerializedName("guru_pengganti_id") val guruPenggantiId: Int? = null,
    @SerializedName("guru_pengganti_nama") val guruPenggantiNama: String? = null,
    val pesan: String? = null
)

// ========== SISWA LIST KEHADIRAN RESPONSE ==========
data class KehadiranResponse(
    val success: Boolean,
    val message: String,
    @SerializedName("kelas_id") val kelasId: Int? = null,
    val kelas: String? = null,
    val tanggal: String? = null,
    val total: Int? = null,
    val data: List<KehadiranItem>? = null
)

data class KehadiranItem(
    val id: Int,
    @SerializedName("kehadiran_id") val kehadiranId: Int? = null,
    @SerializedName("jadwal_id") val jadwalId: Int? = null,
    @SerializedName("kelas_id") val kelasId: Int? = null,
    val kelas: String? = null,
    @SerializedName("guru_id") val guruId: Int? = null,
    val guru: String? = null,
    @SerializedName("kode_guru") val kodeGuru: String? = null,
    @SerializedName("nama_guru") val namaGuru: String? = null,
    @SerializedName("mapel_id") val mapelId: Int? = null,
    val mapel: String? = null,
    @SerializedName("nama_mapel") val namaMapel: String? = null,
    val hari: String? = null,
    @SerializedName("jam_mulai") val jamMulai: String? = null,
    @SerializedName("jam_selesai") val jamSelesai: String? = null,
    val jam: String? = null,
    val tanggal: String? = null,
    val status: String, // "hadir", "tidak_hadir"
    val keterangan: String? = null,
    @SerializedName("input_by") val inputBy: String? = null,
    @SerializedName("has_request_pengganti") val hasRequestPengganti: Boolean = false,
    @SerializedName("request_pengganti") val requestPengganti: RequestPengganti? = null,
    @SerializedName("created_at") val createdAt: String? = null
)

// ========== REQUEST PENGGANTI ==========
data class RequestPengganti(
    val id: Int,
    @SerializedName("guru_pengganti") val guruPengganti: String? = null,
    @SerializedName("guru_pengganti_id") val guruPenggantiId: Int? = null,
    val status: String? = null, // "pending", "approved", "rejected", "aktif"
    @SerializedName("display_guru") val displayGuru: String? = null,
    @SerializedName("display_kode_guru") val displayKodeGuru: String? = null,
    val catatan: String? = null
)

// GuruListResponse untuk siswa list guru - menggunakan GuruItem dari package ini
// NOTE: PlaceholderResponses.kt sudah punya GuruListResponse untuk admin, 
// jadi untuk siswa kita pakai SiswaGuruListResponse
data class SiswaGuruListResponse(
    val success: Boolean,
    val message: String,
    val total: Int? = null,
    val data: List<SiswaGuruItem>? = null
)

data class SiswaGuruItem(
    val id: Int,
    @SerializedName("kode_guru") val kodeGuru: String? = null,
    val nama: String,
    val email: String? = null,
    @SerializedName("no_telepon") val noTelepon: String? = null
)

// ========== SISWA LIST KELAS RESPONSE ==========
data class SiswaKelasListResponse(
    val success: Boolean,
    val message: String,
    val total: Int? = null,
    val data: List<KelasItem>? = null
)

data class KelasItem(
    val id: Int,
    @SerializedName("kode_kelas") val kodeKelas: String? = null,
    @SerializedName("nama_kelas") val namaKelas: String? = null,
    val nama: String? = null, // fallback field
    val tingkat: String? = null,
    val jurusan: String? = null,
    val kapasitas: Int? = null,
    @SerializedName("wali_kelas") val waliKelas: String? = null,
    @SerializedName("jumlah_siswa") val jumlahSiswa: Int? = null
)

// ========== SISWA LIST HARI RESPONSE ==========
data class HariListResponse(
    val success: Boolean,
    val message: String,
    val data: List<String>? = null
)

// ========== KELAS WITH SISWA ==========
data class KelasWithSiswa(
    val id: Int,
    @SerializedName("kode_kelas") val kodeKelas: String,
    @SerializedName("nama_kelas") val namaKelas: String,
    val tingkat: String,
    val jurusan: String,
    val kapasitas: Int,
    @SerializedName("wali_kelas") val waliKelas: String? = null,
    @SerializedName("jumlah_siswa") val jumlahSiswa: Int,
    val siswa: List<Siswa>
)

// ========== KELAS SAYA ==========
data class KelasSayaData(
    val kelas: Kelas,
    @SerializedName("jadwal_hari_ini") val jadwalHariIni: List<JadwalKelas>,
    val hari: String
)

// ========== REQUEST GURU PENGGANTI ==========
data class RequestGuruPenggantiRequest(
    @SerializedName("kehadiran_guru_id") val kehadiranGuruId: Int,
    val pesan: String  // Pesan ke kurikulum (required)
)

// Response setelah request guru pengganti
data class RequestPenggantiResponse(
    val id: Int,
    @SerializedName("kehadiran_guru_id") val kehadiranGuruId: Int,
    @SerializedName("guru_tidak_hadir") val guruTidakHadir: String?,
    val kelas: String?,
    val mapel: String?,
    val hari: String?,
    val jam: String?,
    val tanggal: String?,
    val pesan: String?,
    val status: String?,  // "pending", "approved", "rejected"
    @SerializedName("keterangan_status") val keteranganStatus: String?  // "Menunggu kurikulum memilih guru pengganti"
)

// ========== KURIKULUM - KELAS KEHADIRAN ==========
data class KelasKehadiranData(
    val tanggal: String,
    val kelas: List<KelasWithJadwalKehadiran>
)

data class KelasWithJadwalKehadiran(
    val id: Int,
    @SerializedName("kode_kelas") val kodeKelas: String,
    @SerializedName("nama_kelas") val namaKelas: String,
    val tingkat: String,
    val jurusan: String,
    @SerializedName("wali_kelas") val waliKelas: String? = null,
    @SerializedName("jumlah_siswa") val jumlahSiswa: Int,
    val siswa: List<Siswa>? = null,
    @SerializedName("jadwal_hari_ini") val jadwalHariIni: List<JadwalKelas>,
    val hari: String
)

// ========== GURU IZIN/SAKIT ==========
data class GuruIzinSakitRequest(
    @SerializedName("guru_id") val guruId: Int,
    val tanggal: String,
    val status: String, // izin, sakit
    val keterangan: String,
    @SerializedName("jadwal_ids") val jadwalIds: List<Int>
)

// ========== ASSIGN GURU PENGGANTI ==========
data class AssignGuruPenggantiRequest(
    @SerializedName("kehadiran_guru_id") val kehadiranGuruId: Int,
    @SerializedName("guru_pengganti_id") val guruPenggantiId: Int,
    val catatan: String? = null
)

data class GuruPenggantiData(
    @SerializedName("guru_pengganti") val guruPengganti: GuruPengganti,
    val notifikasi: Notifikasi
)

data class GuruPengganti(
    val id: Int,
    @SerializedName("kehadiran_guru_id") val kehadiranGuruId: Int,
    @SerializedName("guru_pengganti_id") val guruPenggantiId: Int,
    @SerializedName("kelas_id") val kelasId: Int,
    @SerializedName("mapel_id") val mapelId: Int,
    val tanggal: String,
    @SerializedName("jam_mulai") val jamMulai: String? = null,
    @SerializedName("jam_selesai") val jamSelesai: String? = null,
    val status: String, // pending, diterima, ditolak
    val catatan: String? = null,
    @SerializedName("guru_pengganti") val guruPenggantiDetail: Guru? = null
)

// ========== GURU TERSEDIA ==========
data class GuruTersediaData(
    @SerializedName("guru_tersedia") val guruTersedia: List<Guru>,
    @SerializedName("jumlah_tersedia") val jumlahTersedia: Int,
    val hari: String,
    val jam: String
)

// ========== KEPALA SEKOLAH - JADWAL ==========
data class JadwalKelasData(
    val kelas: Kelas,
    val jadwal: List<JadwalHari>
)

data class JadwalHari(
    val hari: String,
    @SerializedName("mata_pelajaran") val mataPelajaran: List<JadwalKelas>
)

// ========== KEPALA SEKOLAH - KEHADIRAN ==========
data class KehadiranGuruData(
    val tanggal: String,
    val kehadiran: List<KehadiranPerKelas>,
    val summary: SummaryKehadiran
)

data class KehadiranPerKelas(
    val kelas: Kelas,
    val kehadiran: List<KehadiranGuruDetail>,
    val summary: SummaryKehadiranKelas
)

data class KehadiranGuruDetail(
    val id: Int,
    val tanggal: String,
    val guru: Guru,
    val mapel: Mapel,
    val status: String,
    val keterangan: String? = null,
    @SerializedName("input_by") val inputBy: InputBy
)

data class InputBy(
    val type: String, // siswa, kurikulum
    val nama: String
)

data class SummaryKehadiranKelas(
    val total: Int,
    val hadir: Int,
    @SerializedName("tidak_hadir") val tidakHadir: Int,
    val izin: Int,
    val sakit: Int
)

data class SummaryKehadiran(
    @SerializedName("total_kelas") val totalKelas: Int,
    @SerializedName("total_kehadiran") val totalKehadiran: Int,
    @SerializedName("total_hadir") val totalHadir: Int,
    @SerializedName("total_tidak_hadir") val totalTidakHadir: Int,
    @SerializedName("total_izin") val totalIzin: Int,
    @SerializedName("total_sakit") val totalSakit: Int
)

// ========== STATISTIK KEHADIRAN ==========
data class StatistikKehadiranData(
    val periode: Periode,
    @SerializedName("statistik_per_guru") val statistikPerGuru: List<StatistikGuru>,
    @SerializedName("statistik_overall") val statistikOverall: StatistikOverall
)

data class Periode(
    @SerializedName("tanggal_mulai") val tanggalMulai: String,
    @SerializedName("tanggal_selesai") val tanggalSelesai: String
)

data class StatistikGuru(
    val guru: Guru,
    val statistik: StatistikDetail
)

data class StatistikDetail(
    val total: Int,
    val hadir: Int,
    @SerializedName("tidak_hadir") val tidakHadir: Int,
    val izin: Int,
    val sakit: Int,
    @SerializedName("persentase_hadir") val persentaseHadir: Double
)

data class StatistikOverall(
    @SerializedName("total_kehadiran") val totalKehadiran: Int,
    @SerializedName("total_hadir") val totalHadir: Int,
    @SerializedName("total_tidak_hadir") val totalTidakHadir: Int,
    @SerializedName("total_izin") val totalIzin: Int,
    @SerializedName("total_sakit") val totalSakit: Int,
    @SerializedName("persentase_hadir") val persentaseHadir: Double
)

// ========== KURIKULUM - REQUEST PENGGANTI ==========
// Request pengganti dari siswa (sesuai API response)
data class KurikulumRequestPengganti(
    val id: Int,
    @SerializedName("kehadiran_guru_id") val kehadiranGuruId: Int,
    val tanggal: String?,
    val status: String?, // "pending", "approved", "rejected"
    val keterangan: String?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("guru_tidak_hadir") val guruTidakHadir: GuruInfo?,
    val jadwal: JadwalInfo?,
    @SerializedName("guru_pengganti") val guruPengganti: GuruInfo?,
    @SerializedName("requested_by") val requestedBy: SiswaInfo?
)

data class GuruInfo(
    val id: Int,
    val nama: String?,
    @SerializedName("kode_guru") val kodeGuru: String? = null,
    @SerializedName("status_kehadiran") val statusKehadiran: String? = null
)

data class JadwalInfo(
    val id: Int? = null,
    val hari: String?,
    @SerializedName("jam_mulai") val jamMulai: String? = null,
    @SerializedName("jam_selesai") val jamSelesai: String? = null,
    val jam: String? = null,
    val mapel: String?,
    val kelas: String?,
    @SerializedName("kelas_id") val kelasId: Int? = null
)

data class SiswaInfo(
    val id: Int,
    val nama: String?,
    val nis: String? = null,
    val kelas: String?
)

data class MapelInfo(
    val id: Int,
    val nama: String?,
    @SerializedName("kode_mapel") val kodeMapel: String? = null
)

data class KelasInfo(
    val id: Int,
    val nama: String?,
    val tingkat: String? = null,
    val jurusan: String? = null
)

// ========== KURIKULUM - DAFTAR GURU ==========
data class DaftarGuruItem(
    @SerializedName("jadwal_id") val jadwalId: Int?,
    val hari: String?,
    val jam: String?,
    val guru: GuruInfo?,
    val mapel: MapelInfo?,
    val kelas: KelasInfo?,
    @SerializedName("status_kehadiran") val statusKehadiran: String?,
    @SerializedName("kehadiran_id") val kehadiranId: Int?,
    val keterangan: String?,
    @SerializedName("input_by") val inputBy: String?,
    @SerializedName("input_by_kurikulum_id") val inputByKurikulumId: Int? = null,
    @SerializedName("input_by_kurikulum_name") val inputByKurikulumName: String? = null,
    @SerializedName("guru_pengganti") val guruPengganti: GuruInfo? = null
)

data class DaftarGuruStatistik(
    val total: Int,
    val hadir: Int,
    @SerializedName("tidak_hadir") val tidakHadir: Int,
    val izin: Int,
    val sakit: Int,
    @SerializedName("belum_diisi") val belumDiisi: Int
)

data class DaftarGuruFilter(
    val hari: String?,
    @SerializedName("kelas_id") val kelasId: Int?,
    val tanggal: String?
)

data class DaftarGuruResponse(
    val success: Boolean,
    val message: String?,
    val data: List<DaftarGuruItem>?,
    val statistik: DaftarGuruStatistik?,
    val filter: DaftarGuruFilter?
)

// ========== KURIKULUM - REQUEST BODIES ==========
data class PilihGuruRequest(
    @SerializedName("request_id") val requestId: Int,
    @SerializedName("guru_pengganti_id") val guruPenggantiId: Int,
    val catatan: String? = null
)

data class TolakRequestBody(
    @SerializedName("request_id") val requestId: Int,
    val alasan: String? = null
)

data class ClearLaporanRequest(
    val status: String? = "all",
    @SerializedName("tanggal_dari") val tanggalDari: String? = null,
    @SerializedName("tanggal_sampai") val tanggalSampai: String? = null
)

// ========== KURIKULUM - GURU TERSEDIA ==========
data class GuruTersediaItem(
    val id: Int,
    val nama: String?,
    @SerializedName("kode_guru") val kodeGuru: String?,
    @SerializedName("status_kehadiran") val statusKehadiran: String? = null
)

data class GuruTersediaResponse(
    @SerializedName("guru_tersedia") val guruTersedia: List<GuruTersediaItem>?,
    @SerializedName("jumlah_tersedia") val jumlahTersedia: Int?
)

// ========== KURIKULUM - LIST RESPONSES ==========
data class KurikulumRequestPenggantiListResponse(
    val success: Boolean,
    val message: String?,
    val data: List<KurikulumRequestPengganti>?
)

// Legacy model - keep for backward compatibility
data class RequestPenggantiItem(
    val id: Int,
    @SerializedName("jadwal_id") val jadwalId: Int,
    @SerializedName("kehadiran_guru_id") val kehadiranGuruId: Int,
    @SerializedName("jam_ke") val jamKe: Int,
    @SerializedName("guru_nama") val guruNama: String?,
    @SerializedName("mapel_nama") val mapelNama: String?,
    @SerializedName("kelas_nama") val kelasNama: String?,
    val pesan: String?,
    val status: String?, // pending, assigned, completed
    @SerializedName("guru_pengganti_id") val guruPenggantiId: Int?,
    @SerializedName("guru_pengganti_nama") val guruPenggantiNama: String?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?
)

data class RequestPenggantiListResponse(
    val success: Boolean,
    val message: String?,
    val data: List<RequestPenggantiItem>?
)
