# Food Recognizer 🍽️✨
> **Aplikasi Android Pintar Pendeteksi Makanan, Informasi Gizi & Resep Kuliner Berbasis Kecerdasan Buatan (Google Gemini API & TheMealDB API).**

Aplikasi Android modern yang tangguh dengan performa tinggi yang dirancang khusus untuk mengidentifikasi hidangan makanan dari kamera atau galeri, mendeteksi kandungan gizi lengkap secara akurat melalui integrasi **Google Gemini API**, dan menyajikan resep kuliner yang dipasok langsung dari **TheMealDB API**. 

Aplikasi ini mengusung penyimpanan lokal berbasis **Room Database** sehingga seluruh riwayat pemindaian tersimpan aman secara luring (*offline*) pada perangkat pengguna.

---

## 🎨 Preview & Tata Letak Antarmuka (ASCII Layout)

Berikut adalah visualisasi tata letak antarmuka modern yang dikonfigurasi menggunakan **Jetpack Compose (Material Design 3)**:

```text
========================================================================
[1] TAMPILAN BERANDA (HOME SCREEN)
========================================================================
+----------------------------------------------------------------------+
| 🍽️  Food Recognizer                                             [🔍] |
+----------------------------------------------------------------------+
|  +----------------------------------------------------------------+  |
|  | [ Gambar Hero Banner Estetik Kuliner ]                         |  |
|  |                                                                |  |
|  |  Kenali Makanan Anda                                           |  |
|  |  Dapatkan informasi resep dan nutrisi lengkap dengan AI        |  |
|  +----------------------------------------------------------------+  |
|                                                                      |
|  +-- Unggah atau Ambil Gambar ------------------------------------+  |
|  | [ 📸 Kamera ]                      [ 🖼️  Galeri ]               |  |
|  |                                                                |  |
|  |  [ 📹 Identifikasi Real-Time (Camera Feed) ]                   |  |
|  +----------------------------------------------------------------+  |
|                                                                      |
|  Riwayat Pemindaian                                    [ 3 Item ]    |
|  +----------------------------------------------------------------+  |
|  | [Thumbnail]  Nasi Goreng                           [Conf: 95%] |  |
|  |              🔥 350 kkal  |  🍗 12g P  |  🥑 14g L    [🗑️]      |  |
|  +----------------------------------------------------------------+  |
|  | [Thumbnail]  Sate Ayam                             [Conf: 88%] |  |
|  |              🔥 280 kkal  |  🍗 22g P  |  🥑 10g L    [🗑️]      |  |
|  +----------------------------------------------------------------+  |
+----------------------------------------------------------------------+

========================================================================
[2] TAMPILAN HASIL (RESULT SCREEN)
========================================================================
+----------------------------------------------------------------------+
| ← Hasil Deteksi Makanan                                              |
+----------------------------------------------------------------------+
|  [========= GAMBAR MAKANAN YANG DIUNGGAH ATAU DIPINDAI =========]     |
|  |                                                                  | |
|  | +--------------------------------------------------------------+ | |
|  | | Nasi Goreng                                    Akurasi: 95%  | | |
|  | | Klasifikasi ML Berhasil                       [|||||||||   ] | | |
|  | +--------------------------------------------------------------+ | |
|  +----------------------------------------------------------------+   |
|                                                                       |
|  Kandungan Nutrisi (Gemini AI)                                        |
|  +--------+   +--------+   +--------+   +--------+                    |
|  |  CAL   |   |  PROT  |   |  CARB  |   |  FAT   |                    |
|  |  350   |   |  12g   |   |  45g   |   |  14g   |                    |
|  +--------+   +--------+   +--------+   +--------+                    |
|  [🌿 Serat Makanan (Fiber) : 3g]                                      |
|                                                                       |
|  Resep Referensi (TheMealDB)                                          |
|  +----------------------------------------------------------------+   |
|  | [Recipe-Img]  Nasi Goreng Khas Indonesia                       |   |
|  |               Bahan: Nasi, Kecap Manis, Bawang, Cabai, Telur   |   |
|  |                                                                |   |
|  |               Langkah Pembuatan:                               |   |
|  |               1. Tumis bumbu halus hingga harum...             |   |
|  |               2. Masukkan telur, orak-arik...                  |   |
|  +----------------------------------------------------------------+   |
+----------------------------------------------------------------------+
```

---

## 🛠️ Spesifikasi Teknis & Arsitektur Sistem

Sistem aplikasi ini dibangun dengan standard industri terbaru Android menggunakan teknologi berkinerja tinggi:

| Komponen | Spesifikasi & Teknologi | Keterangan |
| :--- | :--- | :--- |
| **Bahasa Utama** | Kotlin 100% | Mengutamakan keamanan tipe data dan efisiensi memori. |
| **Framework UI** | Jetpack Compose (M3) | Mengimplementasikan sistem warna dinamis, elevasi tonal, dan kelenturan layout modern. |
| **Pola Arsitektur** | MVVM (Model-View-ViewModel) | Pemisahan logika bisnis yang bersih berlandaskan aliran data searah (UDF). |
| **State Flow** | Kotlin StateFlow & Coroutines | Pembaruan UI secara reaktif dan asinkron yang sangat hemat baterai. |
| **Basis Data Lokal** | Room Database (KSP) | Menyimpan histori makanan, kalori, dan jalur media secara persisten. |
| **Konektivitas API** | Retrofit 2 & OkHttp 3 | Penghubung stabil ke server web untuk mendownload resep kuliner. |
| **Pemuatan Gambar** | Coil (Compose Extension) | Melakukan cache gambar secara otomatis dan pemrosesan thread latar belakang. |
| **Navigasi** | Type-Safe Navigation Compose | Navigasi antar-layar yang aman dari error run-time. |

---

## 🔑 Konfigurasi Kunci API (Google Gemini)

Untuk menjaga kerahasiaan kunci API, proyek ini menggunakan **Secrets Gradle Plugin** yang memisahkan konfigurasi sensitif dari kode sumber utama.

### Langkah-langkah Pengaturan Kunci API:
1. Buka **Secrets Panel** di interface Google AI Studio Anda.
2. Tambahkan kunci baru dengan format:
   * **Nama Secret**: `GEMINI_API_KEY`
   * **Nilai**: *[Kunci API Gemini Anda]*
3. Saat proyek dicompile, plugin akan mendaftarkan nilai ini secara otomatis ke dalam `BuildConfig.GEMINI_API_KEY`.
4. **Mekanisme Fallback Cerdas**: Apabila kunci API kosong atau tidak valid, sistem secara cerdas akan mengaktifkan **Simulator Nutrisi Luring** berdasarkan kamus pencarian kata kunci nama makanan agar fungsionalitas visualisasi aplikasi tetap berjalan maksimal.

---

## 🎯 Detail Output Data Nutrisi (Prompt Gemini API)

Aplikasi mengirimkan instruksi sistem khusus ke model Gemini untuk menjamin konsistensi balasan dalam format JSON terstruktur yang dapat diparsing secara aman.

### System Instruction:
```text
Anda adalah ahli gizi profesional yang bertugas memberikan informasi nutrisi makanan secara akurat. Untuk setiap makanan yang disebutkan, berikan informasi:
1. Kalori (dalam kkal)
2. Karbohidrat (dalam gram)
3. Lemak (dalam gram)
4. Serat (dalam gram)
5. Protein (dalam gram)

Format output harus berupa JSON valid dengan struktur berikut:
{
  "calories": 250,
  "carbs": "30g",
  "fat": "12g",
  "fiber": "5g",
  "protein": "15g"
}
```

### Contoh JSON Response yang Diterima dan Diproses oleh Aplikasi:
```json
{
  "calories": 350,
  "carbs": "45g",
  "fat": "14g",
  "fiber": "3g",
  "protein": "12g"
}
```
*Aplikasi dibekali parser Regex numerik yang tangguh sehingga jika Gemini membalas dalam bentuk string seperti `"30g"` atau `"250 kkal"`, aplikasi tetap berhasil melakukan ekstraksi angka murni untuk diplot pada grafik gizi UI.*

---

## 🚀 Panduan Penggunaan Aplikasi

1. **Memulai Pemindaian**:
   * Ketuk tombol **📸 Kamera** untuk mengambil foto hidangan secara langsung, ATAU
   * Ketuk tombol **🖼️ Galeri** untuk memilih foto makanan yang telah ada di galeri ponsel Anda.
2. **Menampilkan Hasil**:
   * Aplikasi akan menampilkan layar loading dengan status interaktif.
   * Setelah analisis selesai, Anda akan disajikan rincian kandungan nutrisi lengkap (Kalori, Protein, Karbohidrat, Lemak, dan Serat) serta informasi resep masakan referensi di bagian bawah.
3. **Membuka Riwayat**:
   * Di layar utama, Anda dapat melihat daftar riwayat pemindaian terdahulu secara luring.
   * Ketuk salah satu kartu riwayat untuk kembali membuka layar rincian penuh.
   * Anda bisa menghapus item riwayat tertentu dengan menekan tombol **Hapus [🗑️]**, atau membersihkan seluruh riwayat dengan tombol **Hapus Semua Riwayat** di bar atas.

---

## 💻 Panduan Menjalankan & Membangun Proyek

1. Unduh atau clone repositori proyek ini.
2. Pastikan Anda menggunakan **Android Studio Jellyfish / Koala** (atau yang lebih tinggi).
3. Sambungkan perangkat Android fisik (dengan mode USB Debugging aktif) atau jalankan Emulator.
4. Jalankan sinkronisasi Gradle proyek Anda.
5. Jalankan perintah kompilasi dan instalasi berikut melalui terminal:
   ```bash
   gradle assembleDebug
   ```
6. Jalankan unit test lokal untuk menguji kesiapan arsitektur:
   ```bash
   gradle :app:testDebugUnitTest
   ```

---

## ✒️ Kredit Pembuatan

Aplikasi ini dirancang, dioptimalkan, dan dikembangkan secara penuh oleh:

🧑‍💻 **Muhammad Aiyub**
*Software Engineer & Android Developer Expert*

---
*Dibuat dengan penuh dedikasi untuk menghadirkan solusi teknologi pangan berbasis kecerdasan buatan yang ramah guna dan bernilai tinggi.*
