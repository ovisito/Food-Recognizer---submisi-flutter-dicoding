# Food Recognizer 🍽️✨

Aplikasi Android modern berbasis Jetpack Compose dan Kecerdasan Buatan (AI) yang dirancang untuk membantu pengguna mengidentifikasi makanan, menganalisis kandungan nutrisi secara mendalam menggunakan **Google Gemini API**, serta menampilkan referensi resep kuliner yang relevan dari **TheMealDB API**.

Aplikasi ini menggunakan penyimpanan lokal berbasis **Room Database** untuk menyimpan riwayat hasil pemindaian secara aman, memungkinkan akses offline penuh ke hasil analisis sebelumnya.

---

## 🚀 Fitur Utama

1. **Pemindaian Makanan Cerdas (Kamera & Galeri)**
   * Ambil foto makanan secara langsung lewat kamera terintegrasi atau unggah dari galeri foto perangkat Anda.
2. **Identifikasi Real-Time (Live Camera Feed)**
   * Mode kamera interaktif bertenaga *Machine Learning* lokal untuk memindai dan mengenali hidangan secara real-time.
3. **Analisis Kandungan Nutrisi (Powered by Gemini AI)**
   * Menampilkan rincian nutrisi makro (Kalori, Protein, Karbohidrat, Lemak) dan mikro (Serat) menggunakan kecerdasan buatan Gemini dengan prompt ahli gizi yang terstruktur.
4. **Pencarian Resep Kuliner Referensi (Powered by TheMealDB)**
   * Menampilkan resep hidangan yang cocok secara real-time, lengkap dengan daftar bahan, takaran, dan petunjuk langkah demi langkah pembuatan yang mudah dipahami.
5. **Riwayat Pemindaian Lokal (Room Database)**
   * Menyimpan setiap hasil pemindaian secara otomatis. Riwayat dilengkapi dengan pencarian, visualisasi nutrisi ringkas, dan opsi penghapusan riwayat secara individual maupun massal.
6. **Antarmuka Desain Material 3 Modern**
   * Menggunakan tema **Sleek Indigo & Slate** yang kontras, bersih, ramah aksesibilitas, serta mendukung mode Gelap (*Dark Mode*) dan Terang (*Light Mode*) secara harmonis.

---

## 🛠️ Spesifikasi Teknis & Arsitektur

* **Bahasa Pemrograman**: Kotlin 100%
* **Framework UI**: Jetpack Compose (Material Design 3)
* **Arsitektur**: MVVM (Model-View-ViewModel) dengan Unidirectional Data Flow (UDF)
* **Penyimpanan Lokal**: Room Database (SQLite) dengan KSP Compiler
* **Jaringan & API**: 
  * Google Gemini API (untuk ekstraksi nutrisi kustom dalam format JSON)
  * Retrofit & OkHttp (untuk komunikasi jaringan dan API TheMealDB)
* **Pemuatan Gambar**: Coil (Coroutines Image Loader) untuk performa memori yang optimal
* **Asynchronous Flow**: Kotlin Coroutines & StateFlow untuk manajemen state reaktif

---

## 🔑 Konfigurasi Kunci API (Google Gemini)

Kunci API disimpan secara aman menggunakan **Secrets Gradle Plugin** untuk menghindari kebocoran kode sumber. 

### Langkah Pengaturan API Key:
1. Masuk ke **Secrets panel di Google AI Studio**.
2. Masukkan key-value berikut:
   * **Nama Secret**: `GEMINI_API_KEY`
   * **Nilai**: *[Kunci API Gemini Anda]*
3. Aplikasi akan mendeteksi kunci tersebut secara otomatis di build-time melalui kelas `BuildConfig.GEMINI_API_KEY`.
4. *Catatan*: Jika API Key tidak tersedia atau kosong, aplikasi akan secara otomatis mengaktifkan **Model Simulasi Cerdas (Fallback)** untuk memastikan visualisasi data dan pengujian fitur utama tetap berjalan mulus.

---

## 🎯 Contoh Output Data Nutrisi (Prompt Gemini API)

Aplikasi mengirimkan instruksi sistem khusus ke model Gemini untuk menjamin konsistensi balasan dalam format JSON terstruktur.

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
*Aplikasi menggunakan parser regex dan numerik yang toleran terhadap format string (seperti "45g" atau "350 kkal") untuk mengubahnya menjadi data numerik terstruktur di UI.*

---

## 📸 Antarmuka Pengguna & Navigasi

Aplikasi menggunakan sistem navigasi modern **Type-Safe Navigation Compose** dengan rute berikut:
* `home` (Beranda): Menampilkan banner hero, tombol unggah kamera/galeri, tombol live feed, serta daftar riwayat pemindaian terakhir.
* `result`: Memuat status pemrosesan (*loading*) dan menyajikan hasil analisis nutrisi makro/mikro secara visual beserta petunjuk resep referensi.
* `result_detail/{foodId}`: Menampilkan detail histori pemindaian yang dimuat langsung dari Room Database.
* `live_camera`: Mengaktifkan feed kamera langsung untuk pengenalan real-time menggunakan modul ML.

---

## 💻 Cara Menjalankan & Membangun Project

1. Pastikan Anda menggunakan **Android Studio Jellyfish / Koala** atau versi yang lebih baru.
2. Clone project ini ke lingkungan lokal Anda.
3. Sinkronisasikan Gradle (`gradle sync`).
4. Jalankan perintah kompilasi berikut pada terminal untuk memverifikasi kesiapan build:
   ```bash
   gradle assembleDebug
   ```
5. Pasang aplikasi langsung pada perangkat Android fisik atau Emulator untuk mulai memindai makanan Anda!
