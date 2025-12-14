package com.umar.aplikasimonitoringkelas.data.api

import com.umar.aplikasimonitoringkelas.data.model.*
import com.umar.aplikasimonitoringkelas.model.Guru
import com.umar.aplikasimonitoringkelas.model.Kelas
import com.umar.aplikasimonitoringkelas.model.Mapel
import com.umar.aplikasimonitoringkelas.model.Siswa
import com.umar.aplikasimonitoringkelas.model.TahunAjaran
import com.umar.aplikasimonitoringkelas.model.AbsensiGuru
import com.umar.aplikasimonitoringkelas.model.GuruRequest
import com.umar.aplikasimonitoringkelas.model.KelasRequest
import com.umar.aplikasimonitoringkelas.model.MapelRequest
import com.umar.aplikasimonitoringkelas.model.SiswaRequest
import com.umar.aplikasimonitoringkelas.model.TahunAjaranRequest
import com.umar.aplikasimonitoringkelas.model.AbsensiGuruRequest
import retrofit2.http.*

/**
 * Interface untuk mendefinisikan endpoint API
 */
interface ApiService {
    
    // ==================== AUTH ENDPOINTS ====================
    
    /**
     * Endpoint untuk login
     * POST /api/login
     */
    @POST("api/login")
    suspend fun login(
        @Body request: LoginRequest
    ): retrofit2.Response<LoginResponse>
    
    /**
     * Check autentikasi
     * GET /api/check
     */
    @GET("api/check")
    suspend fun checkAuth(): retrofit2.Response<LoginResponse>
    
    /**
     * Logout
     * POST /api/logout
     */
    @POST("api/logout")
    suspend fun logout(): retrofit2.Response<LoginResponse>
    
    // ==================== GURU ENDPOINTS ====================
    
    /**
     * Mendapatkan daftar semua guru
     * GET /api/gurus
     */
    @GET("api/gurus")
    suspend fun getGuruList(): retrofit2.Response<GuruListResponse>
    
    /**
     * Mendapatkan detail guru berdasarkan ID
     * GET /api/gurus/{id}
     */
    @GET("api/gurus/{id}")
    suspend fun getGuruById(
        @Path("id") id: Int
    ): retrofit2.Response<GuruResponse>
    
    /**
     * Menambahkan guru baru
     * POST /api/gurus
     */
    @POST("api/gurus")
    suspend fun createGuru(
        @Body request: GuruRequest
    ): retrofit2.Response<GuruResponse>
    
    /**
     * Mengupdate data guru
     * PUT /api/gurus/{id}
     */
    @PUT("api/gurus/{id}")
    suspend fun updateGuru(
        @Path("id") id: Int,
        @Body request: GuruRequest
    ): retrofit2.Response<GuruResponse>
    
    /**
     * Menghapus guru
     * DELETE /api/gurus/{id}
     */
    @DELETE("api/gurus/{id}")
    suspend fun deleteGuru(
        @Path("id") id: Int
    ): retrofit2.Response<GuruResponse>
    
    // ==================== MAPEL ENDPOINTS ====================
    
    /**
     * Mendapatkan daftar semua mapel
     * GET /api/mapels
     */
    @GET("api/mapels")
    suspend fun getMapelList(): retrofit2.Response<MapelListResponse>
    
    /**
     * Mendapatkan detail mapel berdasarkan ID
     * GET /api/mapels/{id}
     */
    @GET("api/mapels/{id}")
    suspend fun getMapelById(
        @Path("id") id: Int
    ): retrofit2.Response<MapelResponse>
    
    /**
     * Menambahkan mapel baru
     * POST /api/mapels
     */
    @POST("api/mapels")
    suspend fun createMapel(
        @Body request: MapelRequest
    ): retrofit2.Response<MapelResponse>
    
    /**
     * Mengupdate data mapel
     * PUT /api/mapels/{id}
     */
    @PUT("api/mapels/{id}")
    suspend fun updateMapel(
        @Path("id") id: Int,
        @Body request: MapelRequest
    ): retrofit2.Response<MapelResponse>
    
    /**
     * Menghapus mapel
     * DELETE /api/mapels/{id}
     */
    @DELETE("api/mapels/{id}")
    suspend fun deleteMapel(
        @Path("id") id: Int
    ): retrofit2.Response<MapelResponse>
    
    // ==================== TAHUN AJARAN ENDPOINTS ====================
    
    /**
     * Mendapatkan tahun ajaran aktif
     * GET /tahun-ajarans/active
     */
    @GET("tahun-ajarans/active")
    suspend fun getActiveTahunAjaran(): TahunAjaranResponse
    
    /**
     * Mendapatkan daftar semua tahun ajaran
     * GET /api/tahun-ajarans
     */
    @GET("api/tahun-ajarans")
    suspend fun getTahunAjaranList(): retrofit2.Response<TahunAjaranListResponse>
    
    /**
     * Mendapatkan detail tahun ajaran berdasarkan ID
     * GET /api/tahun-ajarans/{id}
     */
    @GET("api/tahun-ajarans/{id}")
    suspend fun getTahunAjaranById(
        @Path("id") id: Int
    ): retrofit2.Response<TahunAjaranResponse>
    
    /**
     * Menambahkan tahun ajaran baru
     * POST /api/tahun-ajarans
     */
    @POST("api/tahun-ajarans")
    suspend fun createTahunAjaran(
        @Body request: TahunAjaranRequest
    ): retrofit2.Response<TahunAjaranResponse>
    
    /**
     * Mengupdate data tahun ajaran
     * PUT /api/tahun-ajarans/{id}
     */
    @PUT("api/tahun-ajarans/{id}")
    suspend fun updateTahunAjaran(
        @Path("id") id: Int,
        @Body request: TahunAjaranRequest
    ): retrofit2.Response<TahunAjaranResponse>
    
    /**
     * Menghapus tahun ajaran
     * DELETE /api/tahun-ajarans/{id}
     */
    @DELETE("api/tahun-ajarans/{id}")
    suspend fun deleteTahunAjaran(
        @Path("id") id: Int
    ): retrofit2.Response<TahunAjaranResponse>
    
    // ==================== KELAS ENDPOINTS ====================
    
    /**
     * Mendapatkan daftar semua kelas
     * GET /api/kelas
     */
    @GET("api/kelas")
    suspend fun getKelasList(): retrofit2.Response<KelasListResponse>
    
    /**
     * Mendapatkan detail kelas berdasarkan ID
     * GET /api/kelas/{id}
     */
    @GET("api/kelas/{id}")
    suspend fun getKelasById(
        @Path("id") id: Int
    ): retrofit2.Response<KelasResponse>
    
    /**
     * Menambahkan kelas baru
     * POST /api/kelas
     */
    @POST("api/kelas")
    suspend fun createKelas(
        @Body request: KelasRequest
    ): retrofit2.Response<KelasResponse>
    
    /**
     * Mengupdate data kelas
     * PUT /api/kelas/{id}
     */
    @PUT("api/kelas/{id}")
    suspend fun updateKelas(
        @Path("id") id: Int,
        @Body request: KelasRequest
    ): retrofit2.Response<KelasResponse>
    
    /**
     * Menghapus kelas
     * DELETE /api/kelas/{id}
     */
    @DELETE("api/kelas/{id}")
    suspend fun deleteKelas(
        @Path("id") id: Int
    ): retrofit2.Response<KelasResponse>
    
    // ==================== SISWA ENDPOINTS ====================
    
    /**
     * Mendapatkan daftar semua siswa
     * GET /api/siswas
     */
    @GET("api/siswas")
    suspend fun getSiswaList(): retrofit2.Response<SiswaListResponse>
    
    /**
     * Mendapatkan detail siswa berdasarkan ID
     * GET /api/siswas/{id}
     */
    @GET("api/siswas/{id}")
    suspend fun getSiswaById(
        @Path("id") id: Int
    ): retrofit2.Response<SiswaResponse>
    
    /**
     * Menambahkan siswa baru
     * POST /api/siswas
     */
    @POST("api/siswas")
    suspend fun createSiswa(
        @Body request: SiswaRequest
    ): retrofit2.Response<SiswaResponse>
    
    /**
     * Mengupdate data siswa
     * PUT /api/siswas/{id}
     */
    @PUT("api/siswas/{id}")
    suspend fun updateSiswa(
        @Path("id") id: Int,
        @Body request: SiswaRequest
    ): retrofit2.Response<SiswaResponse>
    
    /**
     * Menghapus siswa
     * DELETE /api/siswas/{id}
     */
    @DELETE("api/siswas/{id}")
    suspend fun deleteSiswa(
        @Path("id") id: Int
    ): retrofit2.Response<SiswaResponse>
    
    // ==================== ABSENSI SISWA ENDPOINTS ====================
    
    /**
     * ✅ ENDPOINT MONITORING - Menampilkan SEMUA siswa (Real-time)
     * Digunakan untuk dashboard monitoring absensi semua siswa
     * GET /api/absensi-siswa/monitoring
     */
    @GET("api/absensi-siswa/monitoring")
    suspend fun getAbsensiMonitoring(
        @Query("status") status: String? = null,
        @Query("tanggal") tanggal: String? = null,
        @Query("periode") periode: String? = null
    ): retrofit2.Response<AbsensiSiswaListResponse>
    
    /**
     * Mendapatkan daftar absensi siswa (auto-filter by siswa_id jika role=siswa)
     * GET /api/absensi-siswa
     */
    @GET("api/absensi-siswa")
    suspend fun getAbsensiSiswaList(): retrofit2.Response<AbsensiSiswaListResponse>
    
    /**
     * Mendapatkan detail absensi siswa berdasarkan ID
     * GET /api/absensi-siswa/{id}
     */
    @GET("api/absensi-siswa/{id}")
    suspend fun getAbsensiSiswaById(
        @Path("id") id: Int
    ): retrofit2.Response<AbsensiSiswaResponse>
    
    /**
     * Menambahkan absensi siswa baru
     * POST /api/absensi-siswa
     */
    @POST("api/absensi-siswa")
    suspend fun createAbsensiSiswa(
        @Body request: AbsensiSiswaRequest
    ): retrofit2.Response<AbsensiSiswaResponse>
    
    /**
     * Mengupdate data absensi siswa
     * PUT /api/absensi-siswa/{id}
     */
    @PUT("api/absensi-siswa/{id}")
    suspend fun updateAbsensiSiswa(
        @Path("id") id: Int,
        @Body request: AbsensiSiswaRequest
    ): retrofit2.Response<AbsensiSiswaResponse>
    
    // ==================== ABSENSI GURU ENDPOINTS ====================
    
    /**
     * Mendapatkan daftar semua absensi guru
     * GET /api/absensi-gurus
     */
    @GET("api/absensi-gurus")
    suspend fun getAbsensiGuruList(): retrofit2.Response<AbsensiGuruListResponse>
    
    /**
     * Mendapatkan detail absensi guru berdasarkan ID
     * GET /api/absensi-gurus/{id}
     */
    @GET("api/absensi-gurus/{id}")
    suspend fun getAbsensiGuruById(
        @Path("id") id: Int
    ): retrofit2.Response<AbsensiGuruResponse>
    
    /**
     * Menambahkan absensi guru baru
     * POST /api/absensi-gurus
     */
    @POST("api/absensi-gurus")
    suspend fun createAbsensiGuru(
        @Body request: AbsensiGuruRequest
    ): retrofit2.Response<AbsensiGuruResponse>
    
    /**
     * Mengupdate data absensi guru
     * PUT /api/absensi-gurus/{id}
     */
    @PUT("api/absensi-gurus/{id}")
    suspend fun updateAbsensiGuru(
        @Path("id") id: Int,
        @Body request: AbsensiGuruRequest
    ): retrofit2.Response<AbsensiGuruResponse>
    
    /**
     * Menghapus absensi guru
     * DELETE /api/absensi-gurus/{id}
     */
    @DELETE("api/absensi-gurus/{id}")
    suspend fun deleteAbsensiGuru(
        @Path("id") id: Int
    ): retrofit2.Response<AbsensiGuruResponse>
    
    // ==================== ABSENSI SISWA ENDPOINTS ====================
    
    /**
     * Mendapatkan daftar absensi siswa
     * GET /api/absensi-siswa
     */
    @GET("api/absensi-siswa")
    suspend fun getAbsensiSiswaList(
        @Query("status") status: String? = null,
        @Query("tanggal") tanggal: String? = null,
        @Query("kelas_id") kelasId: Int? = null,
        @Query("periode") periode: String? = null
    ): retrofit2.Response<com.umar.aplikasimonitoringkelas.data.model.AbsensiSiswaListResponse>
    
    /**
     * Update absensi siswa
     * PUT /api/absensi-siswa/{id}
     */
    @PUT("api/absensi-siswa/{id}")
    suspend fun updateAbsensiSiswa(
        @Path("id") id: Int,
        @Body request: com.umar.aplikasimonitoringkelas.data.model.AbsensiSiswaUpdateRequest
    ): retrofit2.Response<com.umar.aplikasimonitoringkelas.data.model.AbsensiSiswaResponse>
    
    /**
     * Hapus absensi siswa (soft delete)
     * DELETE /api/absensi-siswa/{id}
     */
    @DELETE("api/absensi-siswa/{id}")
    suspend fun deleteAbsensiSiswa(
        @Path("id") id: Int
    ): retrofit2.Response<com.umar.aplikasimonitoringkelas.data.model.AbsensiSiswaResponse>
    
    /**
     * Reset semua absensi siswa (untuk kurikulum)
     * POST /api/absensi-siswa/reset-all
     */
    @POST("api/absensi-siswa/reset-all")
    suspend fun resetAllAbsensiSiswa(
        @Query("tanggal") tanggal: String? = null,
        @Query("kelas_id") kelasId: Int? = null
    ): retrofit2.Response<com.umar.aplikasimonitoringkelas.data.model.AbsensiSiswaResponse>
    
    /**
     * Lihat history absensi (include deleted)
     * GET /api/absensi-siswa/history
     */
    @GET("api/absensi-siswa/history")
    suspend fun getAbsensiSiswaHistory(
        @Query("status") status: String? = null,
        @Query("kelas_id") kelasId: Int? = null,
        @Query("tanggal") tanggal: String? = null
    ): retrofit2.Response<com.umar.aplikasimonitoringkelas.data.model.AbsensiSiswaListResponse>
    
    /**
     * Statistik absensi siswa
     * GET /api/absensi-siswa/statistics
     */
    @GET("api/absensi-siswa/statistics")
    suspend fun getAbsensiSiswaStatistics(
        @Query("kelas_id") kelasId: Int? = null,
        @Query("periode") periode: String? = null
    ): retrofit2.Response<com.umar.aplikasimonitoringkelas.data.model.AbsensiStatistikResponse>
    
    // ==================== SISWA FITUR ENDPOINTS ====================
    
    /**
     * Get jadwal siswa berdasarkan hari dan kelas
     * GET /api/siswa/jadwal?hari=Senin&kelas_id=1
     */
    @GET("api/siswa/jadwal")
    suspend fun getSiswaJadwal(
        @Query("hari") hari: String,
        @Query("kelas_id") kelasId: Int
    ): retrofit2.Response<JadwalResponse>
    
    /**
     * Get list kehadiran guru berdasarkan kelas
     * GET /api/siswa/list-kehadiran?kelas_id=1&tanggal=2025-12-10
     */
    @GET("api/siswa/list-kehadiran")
    suspend fun getSiswaListKehadiran(
        @Query("kelas_id") kelasId: Int,
        @Query("tanggal") tanggal: String? = null
    ): retrofit2.Response<KehadiranResponse>
    
    /**
     * Get list kelas untuk dropdown siswa
     * GET /api/siswa/list-kelas
     */
    @GET("api/siswa/list-kelas")
    suspend fun getSiswaListKelas(): retrofit2.Response<SiswaKelasListResponse>
    
    /**
     * Get list hari untuk dropdown
     * GET /api/siswa/list-hari
     */
    @GET("api/siswa/list-hari")
    suspend fun getSiswaListHari(): retrofit2.Response<HariListResponse>
    
    /**
     * Get list guru untuk dropdown (guru pengganti)
     * GET /api/siswa/list-guru
     */
    @GET("api/siswa/list-guru")
    suspend fun getSiswaListGuru(): retrofit2.Response<SiswaGuruListResponse>
    
    /**
     * Get list mapel untuk dropdown
     * GET /api/siswa/list-mapel
     */
    @GET("api/siswa/list-mapel")
    suspend fun getSiswaListMapel(): retrofit2.Response<SiswaMapelListResponse>
    
    /**
     * Tambah jadwal baru
     * POST /api/siswa/jadwal
     */
    @POST("api/siswa/jadwal")
    suspend fun tambahJadwal(
        @Body request: TambahJadwalRequest
    ): retrofit2.Response<ApiResponse<JadwalKelas>>

    @POST("api/siswa/jadwal")
    suspend fun tambahJadwalRaw(
        @Body request: TambahJadwalRequest
    ): retrofit2.Response<okhttp3.ResponseBody>
    
    /**
     * Hapus jadwal
     * DELETE /api/siswa/jadwal/{id}
     */
    @DELETE("api/siswa/jadwal/{id}")
    suspend fun hapusJadwal(
        @Path("id") id: Int
    ): retrofit2.Response<ApiResponse<Any>>
    
    /**
     * Input kehadiran guru
     * POST /api/siswa/kehadiran-guru
     */
    @POST("api/siswa/kehadiran-guru")
    suspend fun inputKehadiranGuru(
        @Body request: InputKehadiranRequest
    ): retrofit2.Response<ApiResponse<KehadiranGuru>>
    
    /**
     * Request guru pengganti
     * POST /api/siswa/request-guru-pengganti
     */
    @POST("api/siswa/request-guru-pengganti")
    suspend fun requestGuruPengganti(
        @Body request: RequestGuruPenggantiRequest
    ): retrofit2.Response<ApiResponse<Any>>
    
    /**
     * Get notifikasi siswa
     * GET /api/siswa/notifikasi
     */
    @GET("api/siswa/notifikasi")
    suspend fun getNotifikasi(): retrofit2.Response<ApiResponse<NotifikasiData>>
    
    /**
     * Mark notifikasi as read
     * PUT /api/siswa/notifikasi/{id}/read
     */
    @PUT("api/siswa/notifikasi/{id}/read")
    suspend fun markNotifikasiAsRead(
        @Path("id") id: Int
    ): retrofit2.Response<ApiResponse<Notifikasi>>
    
    /**
     * Get semua kelas dengan siswa
     * GET /api/siswa/kelas-semua
     */
    @GET("api/siswa/kelas-semua")
    suspend fun getSemuaKelas(): retrofit2.Response<ApiResponse<List<KelasWithSiswa>>>
    
    /**
     * Get kelas siswa sendiri
     * GET /api/siswa/kelas-saya
     */
    @GET("api/siswa/kelas-saya")
    suspend fun getKelasSaya(): retrofit2.Response<ApiResponse<KelasSayaData>>
    
    // ==================== KURIKULUM FITUR ENDPOINTS ====================
    
    /**
     * Filter kelas dengan status kehadiran guru
     * GET /api/kurikulum/kelas-kehadiran
     */
    @GET("api/kurikulum/kelas-kehadiran")
    suspend fun getKelasKehadiran(
        @Query("tanggal") tanggal: String? = null,
        @Query("kelas_id") kelasId: Int? = null
    ): retrofit2.Response<ApiResponse<KelasKehadiranData>>
    
    /**
     * Input guru izin/sakit
     * POST /api/kurikulum/guru-izin-sakit
     */
    @POST("api/kurikulum/guru-izin-sakit")
    suspend fun inputGuruIzinSakit(
        @Body request: GuruIzinSakitRequest
    ): retrofit2.Response<ApiResponse<Any>>
    
    /**
     * Assign guru pengganti
     * POST /api/kurikulum/assign-guru-pengganti
     */
    @POST("api/kurikulum/assign-guru-pengganti")
    suspend fun assignGuruPengganti(
        @Body request: AssignGuruPenggantiRequest
    ): retrofit2.Response<ApiResponse<GuruPenggantiData>>
    
    /**
     * Get guru tersedia
     * GET /api/kurikulum/guru-tersedia
     */
    @GET("api/kurikulum/guru-tersedia")
    suspend fun getGuruTersedia(
        @Query("hari") hari: String,
        @Query("jam_mulai") jamMulai: String,
        @Query("jam_selesai") jamSelesai: String,
        @Query("tanggal") tanggal: String
    ): retrofit2.Response<ApiResponse<GuruTersediaData>>
    
    // ==================== KEPALA SEKOLAH FITUR ENDPOINTS ====================
    
    /**
     * Lihat jadwal semua kelas
     * GET /api/kepala-sekolah/jadwal
     */
    @GET("api/kepala-sekolah/jadwal")
    suspend fun getJadwalKelas(
        @Query("kelas_id") kelasId: Int? = null,
        @Query("hari") hari: String? = null
    ): retrofit2.Response<ApiResponse<List<JadwalKelasData>>>
    
    /**
     * Lihat kehadiran guru
     * GET /api/kepala-sekolah/kehadiran-guru
     */
    @GET("api/kepala-sekolah/kehadiran-guru")
    suspend fun getKehadiranGuru(
        @Query("tanggal") tanggal: String? = null,
        @Query("kelas_id") kelasId: Int? = null,
        @Query("guru_id") guruId: Int? = null,
        @Query("status") status: String? = null
    ): retrofit2.Response<ApiResponse<KehadiranGuruData>>
    
    /**
     * Statistik kehadiran guru
     * GET /api/kepala-sekolah/statistik-kehadiran
     */
    @GET("api/kepala-sekolah/statistik-kehadiran")
    suspend fun getStatistikKehadiran(
        @Query("tanggal_mulai") tanggalMulai: String? = null,
        @Query("tanggal_selesai") tanggalSelesai: String? = null
    ): retrofit2.Response<ApiResponse<StatistikKehadiranData>>
    
    // ==================== KURIKULUM ENDPOINTS ====================
    
    // ========== TAB 1: GURU PENGGANTI ==========
    
    /**
     * Get list request pengganti dari siswa
     * GET /api/kurikulum/request-pengganti
     */
    @GET("api/kurikulum/request-pengganti")
    suspend fun getKurikulumRequestPengganti(
        @Query("status") status: String? = "pending",
        @Query("tanggal") tanggal: String? = null,
        @Query("kelas_id") kelasId: Int? = null
    ): retrofit2.Response<KurikulumRequestPenggantiListResponse>
    
    /**
     * Pilih guru pengganti untuk request
     * POST /api/kurikulum/pilih-guru-pengganti
     */
    @POST("api/kurikulum/pilih-guru-pengganti")
    suspend fun pilihGuruPengganti(
        @Body request: PilihGuruRequest
    ): retrofit2.Response<ApiResponse<KurikulumRequestPengganti>>
    
    /**
     * Tolak request pengganti
     * POST /api/kurikulum/tolak-request-pengganti
     */
    @POST("api/kurikulum/tolak-request-pengganti")
    suspend fun tolakRequestPengganti(
        @Body request: TolakRequestBody
    ): retrofit2.Response<ApiResponse<Any>>
    
    /**
     * Get guru yang tersedia untuk mengganti
     * GET /api/kurikulum/guru-tersedia
     */
    @GET("api/kurikulum/guru-tersedia")
    suspend fun getKurikulumGuruTersedia(
        @Query("hari") hari: String,
        @Query("jam_mulai") jamMulai: String,
        @Query("jam_selesai") jamSelesai: String,
        @Query("tanggal") tanggal: String
    ): retrofit2.Response<ApiResponse<GuruTersediaResponse>>
    
    // ========== TAB 2: DAFTAR GURU ==========
    
    /**
     * Get daftar guru dengan status kehadiran
     * GET /api/kurikulum/daftar-guru
     */
    @GET("api/kurikulum/daftar-guru")
    suspend fun getDaftarGuru(
        @Query("hari") hari: String? = null,
        @Query("kelas_id") kelasId: Int? = null,
        @Query("tanggal") tanggal: String? = null
    ): retrofit2.Response<DaftarGuruResponse>
    
    // ========== TAB 3: LAPORAN ==========
    
    /**
     * Get laporan request pengganti
     * GET /api/kurikulum/laporan-request
     */
    @GET("api/kurikulum/laporan-request")
    suspend fun getLaporanRequest(
        @Query("tanggal_dari") tanggalDari: String? = null,
        @Query("tanggal_sampai") tanggalSampai: String? = null,
        @Query("kelas_id") kelasId: Int? = null,
        @Query("status") status: String? = null
    ): retrofit2.Response<KurikulumRequestPenggantiListResponse>
    
    /**
     * Hapus satu laporan
     * DELETE /api/kurikulum/laporan-request/{id}
     */
    @DELETE("api/kurikulum/laporan-request/{id}")
    suspend fun hapusLaporan(
        @Path("id") id: Int
    ): retrofit2.Response<ApiResponse<Any>>
    
    /**
     * Clear semua laporan
     * HTTP DELETE /api/kurikulum/clear-laporan
     */
    @HTTP(method = "DELETE", path = "api/kurikulum/clear-laporan", hasBody = true)
    suspend fun clearAllLaporan(
        @Body request: ClearLaporanRequest
    ): retrofit2.Response<ApiResponse<Any>>
    
    // ========== HELPER ==========
    
    /**
     * Get list kelas untuk filter
     * GET /api/kurikulum/list-kelas
     */
    @GET("api/kurikulum/list-kelas")
    suspend fun getKurikulumListKelas(): retrofit2.Response<ApiResponse<List<KelasInfo>>>
    
    /**
     * Get list hari untuk filter
     * GET /api/kurikulum/list-hari
     */
    @GET("api/kurikulum/list-hari")
    suspend fun getKurikulumListHari(): retrofit2.Response<ApiResponse<List<String>>>
    
    // Legacy endpoint - keep for backward compatibility
    @GET("api/kurikulum/request-pengganti")
    suspend fun getRequestPengganti(
        @Query("kelas_id") kelasId: Int? = null,
        @Query("status") status: String? = null,
        @Query("tanggal") tanggal: String? = null
    ): retrofit2.Response<RequestPenggantiListResponse>
    
    @PUT("api/kurikulum/clear-laporan/{jadwalId}")
    suspend fun clearLaporan(
        @Path("jadwalId") jadwalId: Int
    ): retrofit2.Response<ApiResponse<Any>>
}

