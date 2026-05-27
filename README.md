## Inventory Management – Toko Buku (PT. Eureka Bookhouse)

Aplikasi **Inventory Management** berbasis **Java Swing + MySQL** untuk toko buku (PT. Eureka Bookhouse).

Fitur utama:

- **Master Data**: Barang (buku & ATK), Kategori, Merk/Penerbit, Satuan, Rak, Gudang, Supplier.
- **Transaksi**: Barang Masuk, Barang Keluar (penjualan), Stok Gudang, Stok Opname.
- **Dashboard**: Grafik barang masuk/keluar, low stock, TOP item, dll.
- **Reporting**: Laporan barang masuk/keluar, stok, stok opname (JasperReports).
- **Autentikasi User**: Admin, Gudang, Staff.

---

## 1. Kebutuhan Sistem

- **Java**: JDK 17 (minimal 11, disarankan 17)
- **NetBeans**: disarankan NetBeans 17+ (Java with Ant, bukan Maven/Gradle)
- **MySQL 8**: via **Docker** (`docker-compose`) atau **local MySQL** (Homebrew/installer)
- **Git** (opsional, untuk clone dari GitHub)

---

## 2. Cara Clone Project

```bash
git clone https://github.com/alwianrian28/inventory_management.git
cd inventory_management
```

Struktur penting:

- `src/` – source code Java
- `resources/` – resource (font, dll.)
- `db/inventory_management.sql` – struktur + dummy data toko buku (seed)
- `docker-compose.yml` – MySQL 8 + auto-import SQL
- `.env.example` – contoh konfigurasi environment
- `src/com/inventory/config/config.properties.example` – contoh config file Java

---

## 3. Konfigurasi Database

### 3.1. Opsi A – MySQL via Docker (direkomendasikan)

1. **Siapkan `.env`**

   Di root project:

   ```bash
   cp .env.example .env
   ```

   Contoh isi:

   ```env
   MYSQL_ROOT_PASSWORD=inventory_root
   MYSQL_DATABASE=inventory_management
   MYSQL_PORT=3307

   DB_HOST=localhost
   DB_PORT=3307
   DB_NAME=inventory_management
   DB_USER=root
   DB_PASSWORD=inventory_root
   ```

   Jika perlu, ubah password sesuai kebutuhan (pastikan sinkron dengan `DB_PASSWORD` dan `MYSQL_ROOT_PASSWORD`).

2. **Jalankan MySQL di Docker**

   Pastikan **Docker Desktop** sudah jalan, lalu:

   ```bash
   docker compose up -d
   docker compose ps
   ```

   - MySQL berjalan di `localhost:3307`.
   - File `db/inventory_management.sql` otomatis di-import ke database `inventory_management` saat container pertama kali dibuat.

3. **Tes koneksi (opsional)**

   ```bash
   mysql -h127.0.0.1 -P3307 -uroot -pinventory_root -e "SHOW DATABASES;"
   ```

---

### 3.2. Opsi B – MySQL Lokal (Homebrew / instal manual)

1. Pastikan MySQL berjalan di `localhost:3306`.

2. Buat database dan import SQL:

   ```bash
   mysql -uroot -p -e "CREATE DATABASE IF NOT EXISTS inventory_management;"
   mysql -uroot -p inventory_management < db/inventory_management.sql
   ```

3. Sesuaikan konfigurasi koneksi:

- **Dengan environment variable** (disarankan):

  Set environment (di sistem atau NetBeans Run Configuration):

  ```text
  DB_HOST=localhost
  DB_PORT=3306
  DB_NAME=inventory_management
  DB_USER=root
  DB_PASSWORD=PASSWORD_MYSQL_ANDA
  ```

- **Dengan file config**:

  Ubah nilai berikut di `src/com/inventory/config/config.properties`:

  ```properties
  db.host=localhost
  db.port=3306
  db.name=inventory_management
  db.user=root
  db.password=PASSWORD_MYSQL_ANDA
  ```

---

## 4. Konfigurasi Aplikasi

Aplikasi membaca konfigurasi dari:

1. **Environment variables** (prioritas tertinggi): `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`
2. **File config di classpath**:
   - `com/inventory/config/config.properties`
   - atau `config.properties` di root classpath

### 4.1. Menyiapkan file config di `src`

Setelah clone:

```bash
cp src/com/inventory/config/config.properties.example src/com/inventory/config/config.properties
```

Contoh isi (disarankan untuk Docker):

```properties
app.name = Inventory Management
app.version = v1.0.0
app.description = Aplikasi Manajemen Gudang & Persediaan Barang
app.perusahaan = PT. EUREKA BOOKHOUSE

# Override via env: DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD
# db.port=3306 = MySQL lokal, db.port=3307 = Docker
db.host=localhost
db.port=3307
db.name=inventory_management
db.user=root
db.password=inventory_root
```

Jika memakai environment variables, password di file ini bisa dikosongkan / diabaikan.

---

## 5. Import Project ke NetBeans

1. Buka **NetBeans**.
2. Menu **File → Open Project…**
3. Arahkan ke folder `inventory_management` (yang berisi `nbproject`, `build.xml`).
4. Pilih project **InventoryManagement** → **Open**.

NetBeans akan otomatis:

- Membaca `nbproject/project.properties` (classpath lib di `lib/` sudah diatur).
- Menambahkan `resources/` dan `src/` sebagai source root.

---

## 6. Konfigurasi Run di NetBeans

1. Klik kanan project **InventoryManagement** → **Properties**.
2. Tab **Run**:
   - **Main Class**: `com.inventory.main.Main`
3. Tab **Run → Environment**:

   Tambahkan (sesuaikan dengan setup DB Anda):

   ```text
   DB_HOST = localhost
   DB_PORT = 3307   # atau 3306 jika MySQL lokal
   DB_NAME = inventory_management
   DB_USER = root
   DB_PASSWORD = inventory_root   # atau password MySQL Anda
   ```

4. Klik **OK**.

---

## 7. Menjalankan Aplikasi

1. Pastikan MySQL sudah jalan:
   - Docker: `docker compose ps`
   - Brew / lokal: `brew services list` (atau cek service lain)
2. Di NetBeans:
   - Klik kanan project → **Clean and Build**
   - Klik kanan → **Run** (atau tekan `F6`)

### 7.1. Akun Login Default

Data user awal ada di `db/inventory_management.sql` dan `Test Login.txt`:

- **Admin**
  - Username: `admin`
  - Password: `admin`
- **Gudang**
  - Username: `gudang`
  - Password: `gudang`
- **Staff**
  - Username: `staff`
  - Password: `staff`

---

## 8. Dummy Data Toko Buku

Setelah import SQL:

- **Kategori**: 15 kategori (Novel & Fiksi, Pendidikan, Komik & Manga, Alat Tulis, Majalah, dll.).
- **Merk / Penerbit & Stationery**: ±30 merk (Gramedia, Erlangga, Mizan, Faber-Castell, Pilot, dsb.).
- **Satuan**: 8 satuan (Eks, Buah, Pack, Set, Lembar, Box, Rim, Dus).
- **Barang**: ±46 item (buku best-seller, komik, kamus, ATK, majalah, bundle ATK).
- **Transaksi**:
  - ±40 transaksi **Barang Masuk**.
  - ±55 transaksi **Barang Keluar** (penjualan) dengan detail lengkap.
  - **Stok Gudang** tersebar di 3 gudang & 16 rak.
  - 3 data **Stock Opname** (2 final, 1 draft) untuk uji fitur SO & dashboard.

Semua data dummy ini otomatis tersedia setelah import `db/inventory_management.sql`.

---

## 9. Keamanan & Best Practices

- File berikut **sengaja tidak di-commit** ke Git:
  - `.env`
  - `src/com/inventory/config/config.properties`
  - `resources/config.properties`
- Gunakan file `.example` sebagai template:
  - `.env.example`
  - `src/com/inventory/config/config.properties.example`
- Di server/production:
  - Simpan credential DB hanya di **environment variables**.
  - Backup volume Docker `mysql_data` sebelum update.

---

## 10. Troubleshooting Singkat

- **Gagal konek DB**:
  - Cek `DB_HOST`, `DB_PORT`, `DB_USER`, `DB_PASSWORD`.
  - Coba koneksi manual dengan CLI: `mysql -h127.0.0.1 -PPORT -uUSER -p`.
- **Aplikasi jalan tapi data kosong**:
  - Pastikan `db/inventory_management.sql` sudah ter-import ke DB yang benar.
- **NetBeans banyak warning kuning**:
  - Sebagian berasal dari kode auto-generated Swing (JForm); secara fungsional sudah dibersihkan dan dapat diabaikan.

