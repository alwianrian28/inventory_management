#!/usr/bin/env python3
"""Generate bookstore seed data for inventory_management.sql (PT. Eureka Bookhouse)."""

from __future__ import annotations

import random
from datetime import date, timedelta
from pathlib import Path

random.seed(42)
TS = "2026-01-15 10:00:00"
BY = 1

KATEGORI = [
    ("Novel & Fiksi", "Novel dewasa, thriller, roman"),
    ("Non-Fiksi", "Biografi, sejarah, ensiklopedia ringkas"),
    ("Pendidikan", "Buku pelajaran SD-SMA, modul, LKS"),
    ("Anak & Remaja", "Cerita anak, activity book, young adult"),
    ("Komik & Manga", "Komik lokal dan manga import"),
    ("Agama & Spiritual", "Al-Quran, doa, motivasi islami"),
    ("Bisnis & Motivasi", "Self-help, entrepreneurship, leadership"),
    ("Sains & Teknologi", "IT, sains populer, coding"),
    ("Seni & Hobi", "Menggambar, musik, fotografi"),
    ("Bahasa", "Inggris, Mandarin, Jepang, dictionary"),
    ("Sastra Indonesia", "Sastra klasik dan kontemporer"),
    ("Alat Tulis", "Pulpen, buku tulis, penghapus"),
    ("Stationery", "Planner, sticky notes, map"),
    ("Majalah", "Majalah mingguan dan bulanan"),
    ("Referensi", "Kamus, atlas, buku panduan"),
]

MERK = [
    ("Gramedia Pustaka", "Penerbit novel & umum"),
    ("Erlangga", "Penerbit pendidikan"),
    ("Mizan", "Penerbit sastra & religi"),
    ("Bentang Pustaka", "Sastra Indonesia"),
    ("Grasindo", "Pendidikan & komik"),
    ("Elex Media Komputindo", "Komputer & hobi"),
    ("Kompas", "Jurnalistik & non-fiksi"),
    ("Republika", "Agama & inspirasi"),
    ("Bhuana Ilmu Populer", "Sains populer"),
    ("GagasMedia", "Novel muda"),
    ("Penerbit Buku Kompas", "Referensi & sejarah"),
    ("Pustaka Jaya", "Pelajaran & modul"),
    ("Shogakukan", "Manga Jepang"),
    ("Shueisha", "Manga Jump"),
    ("Marvel Comics", "Komik superhero"),
    ("Disney Publishing", "Buku anak Disney"),
    ("National Geographic", "Edukasi & alam"),
    ("Oxford University Press", "Bahasa Inggris"),
    ("Cambridge", "Bahasa & akademik"),
    ("Kawan Pustaka", "Sastra & budaya"),
    ("Ansori", "Agama & pesantren"),
    ("Buku Guru", "Buku pegangan guru"),
    ("Stationery Plus", "Merek alat tulis"),
    ("Paperline", "Kertas & notebook"),
    ("Faber-Castell", "Alat gambar & tulis"),
    ("Pilot", "Pulpen & pena"),
    ("Staedtler", "Pensil & penghapus"),
    ("Sakura", "Spidol & art supply"),
    ("Kiky", "Stationery lokal"),
    ("Eureka House", "Private label toko"),
]

RAK = [
    ("R-A01", "Rak Novel A", "Novel fiksi & thriller"),
    ("R-A02", "Rak Novel B", "Novel romance & drama"),
    ("R-B01", "Rak Pendidikan", "SD, SMP, SMA"),
    ("R-B02", "Rak UTBK & SNBT", "Tryout & modul masuk PTN"),
    ("R-C01", "Rak Anak", "Cerita & activity anak"),
    ("R-C02", "Rak Young Adult", "Remaja & coming of age"),
    ("R-D01", "Rak Komik", "Komik & graphic novel"),
    ("R-D02", "Rak Manga", "Manga serial"),
    ("R-E01", "Rak Agama", "Al-Quran & spiritual"),
    ("R-F01", "Rak Motivasi", "Self-help & bisnis"),
    ("R-G01", "Rak IT & Sains", "Teknologi & sains"),
    ("R-H01", "Rak Bahasa", "Inggris & Mandarin"),
    ("R-I01", "Rak ATK Meja", "Pulpen & pensil"),
    ("R-I02", "Rak Notebook", "Buku tulis & planner"),
    ("R-J01", "Rak Majalah", "Display majalah"),
    ("R-K01", "Rak Promo", "Buku diskon & bundle"),
]

GUDANG = [
    ("GD-01", "Gudang Utama", "Jl. Pendidikan No. 88, Jakarta Selatan", "Penyimpanan stok utama"),
    ("GD-02", "Etalase Toko", "Lantai 1 Eureka Bookhouse", "Display penjualan retail"),
    ("GD-03", "Gudang Online", "Jl. Kurir No. 12, Jakarta Timur", "Pemenuhan pesanan e-commerce"),
]

SUPPLIER = [
    ("PT Gramedia Distribusi", "021-7894561", "distribusi@gramedia.com", "Jakarta Pusat"),
    ("PT Erlangga Massindo", "021-8790123", "sales@erlangga.co.id", "Jakarta Selatan"),
    ("PT Mizan Pustaka", "022-6123456", "order@mizan.com", "Bandung"),
    ("PT Kompas Gramedia", "021-2567890", "supply@kompas.id", "Jakarta Barat"),
    ("CV Buku Anak Nusantara", "0812-3344556", "anak@bukuanak.id", "Depok"),
    ("PT Elex Media Komputindo", "021-5301234", "halo@elexmedia.co.id", "Jakarta Pusat"),
    ("PT Republika Media", "021-8754321", "redaksi@republika.co.id", "Jakarta Selatan"),
    ("PT Bhuana Ilmu Populer", "021-3912345", "info@bip.co.id", "Jakarta Timur"),
    ("PT Shogakukan Asia", "021-5798765", "import@manga.id", "Jakarta Pusat"),
    ("CV Stationery Jaya", "0813-9988776", "atk@stationeryjaya.com", "Tangerang"),
    ("PT Oxford Indonesia", "021-5678901", "school@oup.com", "Jakarta Selatan"),
    ("PT Eureka Book Supply", "021-5566778", "procurement@eurekabook.id", "Jakarta Selatan"),
]

SATUAN = [
    ("Eks", "Satuan per buku"),
    ("Buah", "Satuan per item fisik"),
    ("Pack", "Paket isi banyak"),
    ("Set", "Set lengkap seri"),
    ("Lembar", "Kertas & lembaran"),
    ("Box", "Kemasan box"),
    ("Rim", "Kertas per rim"),
    ("Dus", "Kemasan distributor"),
]

BOOKS = [
    ("Laskar Pelangi", 1, 11, 1, 1, 89000, 15),
    ("Bumi Manusia", 1, 11, 1, 3, 125000, 10),
    ("Filosofi Teras", 7, 8, 1, 4, 78000, 20),
    ("Atomic Habits (ID)", 7, 6, 1, 9, 95000, 25),
    ("Rich Dad Poor Dad", 7, 6, 1, 9, 85000, 15),
    ("Matematika SMP Kelas 8", 3, 2, 1, 2, 72000, 30),
    ("IPA SMP Kelas 9", 3, 2, 1, 2, 75000, 30),
    ("Bahasa Indonesia SMA", 3, 2, 1, 2, 68000, 25),
    ("Modul UTBK SNBT 2026", 3, 4, 1, 2, 145000, 20),
    ("Si Anak Pintar", 4, 5, 1, 5, 45000, 20),
    ("Petualangan Sherina", 4, 5, 1, 5, 52000, 15),
    ("Negeri 5 Menara", 4, 16, 1, 3, 65000, 20),
    ("One Piece Vol. 108", 5, 13, 1, 9, 42000, 40),
    ("Detektif Conan Vol. 102", 5, 14, 1, 9, 38000, 35),
    ("Marvel Avengers Collection", 5, 15, 1, 9, 175000, 10),
    ("Al-Quran Terjemah Mukhtashar", 6, 8, 1, 7, 185000, 15),
    ("30 Hari Bahasa Inggris", 10, 18, 1, 11, 69000, 25),
    ("Cambridge English A2", 10, 19, 1, 11, 210000, 12),
    ("Sapiens (Edisi Indonesia)", 2, 9, 1, 8, 135000, 15),
    ("Brief History of Time", 8, 9, 1, 8, 98000, 10),
    ("Clean Code Bahasa Indonesia", 8, 6, 1, 6, 165000, 12),
    ("Python untuk Pemula", 8, 6, 1, 6, 89000, 20),
    ("Menggambar Karakter Anime", 9, 25, 1, 6, 75000, 15),
    ("National Geographic Atlas", 2, 17, 1, 8, 245000, 8),
    ("Kamus Besar Bahasa Indonesia", 15, 11, 1, 4, 320000, 10),
    ("Planner 2026 Hardcover", 13, 24, 2, 10, 85000, 30),
    ("Sticky Notes Neon Pack", 13, 29, 3, 10, 25000, 50),
    ("Notebook A5 70gsm 58L", 13, 24, 1, 10, 18000, 60),
    ("Pulpen Pilot G2 Hitam", 12, 26, 2, 10, 22000, 80),
    ("Pensil 2B Faber Castell", 12, 25, 2, 10, 15000, 100),
    ("Penghapus Staedtler", 12, 27, 2, 10, 8000, 100),
    ("Spidol Sakura Pigma", 12, 28, 2, 10, 35000, 40),
    ("Buku Tulis Big Boss 38L", 12, 24, 1, 10, 12000, 80),
    ("Majalah National Geographic", 14, 17, 1, 4, 55000, 25),
    ("Majalah Tempo Edisi Khusus", 14, 7, 1, 4, 48000, 20),
    ("Nanti Kita Cerita Tentang Hari Ini", 1, 10, 1, 1, 72000, 20),
    ("Pulang (Tere Liye)", 1, 10, 1, 1, 68000, 25),
    ("Bumi (Tere Liye)", 1, 10, 1, 1, 65000, 25),
    ("Komik Doraemon Vol. 50", 5, 13, 1, 9, 28000, 45),
    ("Harry Potter 1 (New Cover)", 4, 16, 1, 1, 135000, 18),
    ("The Little Prince (ID)", 4, 20, 1, 1, 55000, 20),
    ("Steins;Gate Light Novel", 5, 13, 1, 9, 95000, 12),
    ("Buku Tabungan Anak", 4, 30, 1, 12, 35000, 30),
    ("Bundle ATK Pelajar", 12, 30, 4, 12, 45000, 25),
    ("Seri Matematika Dasar SD", 3, 2, 4, 2, 195000, 15),
    ("Ensiklopedia Anak Eureka", 4, 30, 1, 12, 275000, 10),
]

TUJUAN_KELUAR = [
    "Pelanggan Walk-in",
    "Pesanan Online",
    "Sekolah Mitra",
    "Kantor Korporat",
    "Reseller",
    "Promo Hari Raya",
    "Event Bazar Buku",
]


def esc(s: str) -> str:
    return s.replace("'", "''")


def row_vals(cols: list) -> str:
    parts = []
    for v in cols:
        if v is None:
            parts.append("NULL")
        elif isinstance(v, str):
            parts.append(f"'{esc(v)}'")
        else:
            parts.append(str(v))
    return "(" + ",".join(parts) + ")"


def insert_block(table: str, columns: str, rows: list[list]) -> str:
    if not rows:
        return f"/*Data for the table `{table}` */\n\n"
    lines = [
        f"/*Data for the table `{table}` */",
        "",
        f"INSERT  INTO `{table}`({columns}) VALUES ",
    ]
    for i, r in enumerate(rows):
        suffix = "," if i < len(rows) - 1 else ";"
        lines.append(row_vals(r) + suffix)
    lines.append("")
    return "\n".join(lines)


def main() -> None:
    kategori_rows = [
        [i, n, k, TS, BY, None, None, None, None, 0]
        for i, (n, k) in enumerate(KATEGORI, 1)
    ]
    merk_rows = [
        [i, n, k, TS, BY, None, None, None, None, 0]
        for i, (n, k) in enumerate(MERK, 1)
    ]
    rak_rows = [
        [i, c, n, k, TS, BY, None, None, None, None, 0]
        for i, (c, n, k) in enumerate(RAK, 1)
    ]
    gudang_rows = [
        [i, c, n, a, k, TS, BY, None, None, None, None, 0]
        for i, (c, n, a, k) in enumerate(GUDANG, 1)
    ]
    supplier_rows = [
        [i, n, t, e, a, TS, BY, None, None, None, None, 0]
        for i, (n, t, e, a) in enumerate(SUPPLIER, 1)
    ]
    satuan_rows = [
        [i, n, k, TS, BY, None, None, None, None, 0]
        for i, (n, k) in enumerate(SATUAN, 1)
    ]

    barang_rows = []
    for i, b in enumerate(BOOKS, 1):
        name, kat, merk, sat, sup, harga, stok_min = b
        code = f"BK-{i:04d}"
        barcode = f"978602{random.randint(1000000, 9999999)}"
        barang_rows.append(
            [
                i,
                code,
                barcode,
                name,
                merk,
                kat,
                sat,
                float(harga),
                stok_min,
                sup,
                None,
                TS,
                BY,
                None,
                None,
                None,
                None,
                0,
            ]
        )

    num_barang = len(barang_rows)
    stok_rows = []
    stok_id = 1
    barang_stok_map: dict[int, list[int]] = {i: [] for i in range(1, num_barang + 1)}

    for bid in range(1, num_barang + 1):
        harga_jual = barang_rows[bid - 1][7]
        harga_beli = round(float(harga_jual) * 0.72, 2)
        batches = random.randint(1, 2)
        for _ in range(batches):
            qty = random.randint(15, 80)
            rak_id = random.randint(1, len(RAK))
            gudang_id = random.choices([1, 2, 3], weights=[50, 30, 20])[0]
            subtotal = round(harga_beli * qty, 2)
            stok_rows.append(
                [
                    stok_id,
                    bid,
                    qty,
                    harga_beli,
                    subtotal,
                    rak_id,
                    gudang_id,
                    TS,
                    None,
                    None,
                    None,
                    None,
                    None,
                    0,
                ]
            )
            barang_stok_map[bid].append(stok_id)
            stok_id += 1

    masuk_rows = []
    masuk_detail_rows = []
    mid = 1
    mdid = 1
    start_masuk = date(2025, 10, 1)
    for _ in range(40):
        d = start_masuk + timedelta(days=random.randint(0, 200))
        sup = random.randint(1, len(SUPPLIER))
        ym = d.strftime("%Y%m")
        masuk_rows.append(
            [
                mid,
                f"BM-{ym}-{mid:03d}",
                f"INV-{mid:04d}",
                d.isoformat(),
                0,
                0.0,
                sup,
                1,
                TS,
                BY,
                None,
                None,
                None,
                None,
                0,
            ]
        )
        detail_count = random.randint(1, 3)
        total_qty = 0
        total_val = 0.0
        for _ in range(detail_count):
            bid = random.randint(1, num_barang)
            if not barang_stok_map[bid]:
                continue
            sid = random.choice(barang_stok_map[bid])
            st = next(s for s in stok_rows if s[0] == sid)
            qty = random.randint(5, 25)
            hb = float(st[3])
            sub = round(hb * qty, 2)
            masuk_detail_rows.append(
                [mdid, mid, sid, qty, hb, sub, st[5], st[6]]
            )
            total_qty += qty
            total_val += sub
            mdid += 1
        masuk_rows[-1][4] = total_qty
        masuk_rows[-1][5] = round(total_val, 2)
        mid += 1

    keluar_rows = []
    keluar_detail_rows = []
    kid = 1
    kdid = 1
    start_keluar = date(2025, 11, 1)
    for _ in range(55):
        d = start_keluar + timedelta(days=random.randint(0, 180))
        ym = d.strftime("%Y%m")
        tujuan = random.choice(TUJUAN_KELUAR)
        keluar_rows.append(
            [
                kid,
                f"BK-{ym}-{kid:03d}",
                d.isoformat(),
                "PENJUALAN",
                tujuan,
                0,
                0.0,
                1,
                TS,
                None,
                None,
                None,
                None,
                None,
                0,
            ]
        )
        lines = random.randint(1, 4)
        total_qty = 0
        total_val = 0.0
        for _ in range(lines):
            bid = random.randint(1, num_barang)
            harga = float(barang_rows[bid - 1][7])
            qty = random.randint(1, 5)
            sub = round(harga * qty, 2)
            keluar_detail_rows.append([kdid, kid, bid, qty, harga, sub])
            total_qty += qty
            total_val += sub
            kdid += 1
        keluar_rows[-1][5] = total_qty
        keluar_rows[-1][6] = round(total_val, 2)
        kid += 1

    opname_rows = [
        [1, "2026-01-10", "Stock opname bulanan toko", "Final", 1, TS, BY, TS, BY, None, None, 0],
        [2, "2026-02-05", "Opname rak novel & komik", "Final", 1, TS, BY, TS, BY, None, None, 0],
        [3, "2026-03-12", "Opname alat tulis", "Draft", 3, TS, 3, None, None, None, None, 0],
    ]
    opname_detail_rows = []
    odid = 1
    for oid, bids in [(1, [1, 2, 5, 12, 20]), (2, [13, 14, 15, 36, 38]), (3, [27, 28, 29, 30, 31])]:
        for bid in bids:
            sis = random.randint(20, 120)
            fis = sis + random.randint(-3, 5)
            opname_detail_rows.append(
                [
                    odid,
                    oid,
                    bid,
                    sis,
                    fis,
                    fis - sis,
                    "Pengecekan fisik rak",
                ]
            )
            odid += 1

    images_rows = [
        [1, "Banner Promo Tahun Baru", "files/banner_promo_tahun_baru.png", "2026-01-01 08:00:00"],
        [2, "Banner Diskon Novel", "files/banner_diskon_novel.png", "2026-01-01 08:05:00"],
        [3, "Banner Back to School", "files/banner_back_to_school.png", "2026-01-05 09:00:00"],
        [4, "Banner ATK Pelajar", "files/banner_atk_pelajar.png", "2026-01-10 10:00:00"],
        [5, "Banner Komik & Manga", "files/banner_komik_manga.png", "2026-01-15 11:00:00"],
        [6, "Banner Buku Agama", "files/banner_buku_agama.png", "2026-01-20 12:00:00"],
    ]

    parts = [
        insert_block(
            "kategori",
            "`kategori_id`,`kategori_name`,`keterangan`,`insert_at`,`insert_by`,`update_at`,`update_by`,`delete_at`,`delete_by`,`is_delete`",
            kategori_rows,
        ),
        insert_block(
            "merk",
            "`merk_id`,`merk_name`,`keterangan`,`insert_at`,`insert_by`,`update_at`,`update_by`,`delete_at`,`delete_by`,`is_delete`",
            merk_rows,
        ),
        insert_block(
            "rak",
            "`rak_id`,`rak_code`,`rak_name`,`keterangan`,`insert_at`,`insert_by`,`update_at`,`update_by`,`delete_at`,`delete_by`,`is_delete`",
            rak_rows,
        ),
        insert_block(
            "gudang",
            "`gudang_id`,`gudang_code`,`gudang_name`,`alamat`,`keterangan`,`insert_at`,`insert_by`,`update_at`,`update_by`,`delete_at`,`delete_by`,`is_delete`",
            gudang_rows,
        ),
        insert_block(
            "supplier",
            "`supplier_id`,`supplier_name`,`telepon`,`email`,`alamat`,`insert_at`,`insert_by`,`update_at`,`update_by`,`delete_at`,`delete_by`,`is_delete`",
            supplier_rows,
        ),
        insert_block(
            "satuan",
            "`satuan_id`,`satuan_name`,`keterangan`,`insert_at`,`insert_by`,`update_at`,`update_by`,`delete_at`,`delete_by`,`is_delete`",
            satuan_rows,
        ),
        insert_block(
            "barang",
            "`barang_id`,`barang_code`,`barcode`,`barang_name`,`merk_id`,`kategori_id`,`satuan_id`,`harga_jual`,`stok_minimum`,`supplier_id`,`photo`,`insert_at`,`insert_by`,`update_at`,`update_by`,`delete_at`,`delete_by`,`is_delete`",
            barang_rows,
        ),
        insert_block(
            "stok_gudang",
            "`stok_id`,`barang_id`,`jumlah`,`harga_beli`,`subtotal`,`rak_id`,`gudang_id`,`insert_at`,`insert_by`,`update_at`,`update_by`,`delete_at`,`delete_by`,`is_delete`",
            stok_rows,
        ),
        insert_block(
            "barang_keluar",
            "`barang_keluar_id`,`no_transaksi`,`tanggal_keluar`,`jenis_keluar`,`tujuan`,`total_jumlah`,`total_keluar`,`user_id`,`insert_at`,`insert_by`,`update_at`,`update_by`,`delete_at`,`delete_by`,`is_delete`",
            keluar_rows,
        ),
        insert_block(
            "barang_keluar_detail",
            "`barang_keluar_detail_id`,`barang_keluar_id`,`barang_id`,`jumlah`,`harga_jual`,`subtotal`",
            keluar_detail_rows,
        ),
        insert_block(
            "barang_masuk",
            "`barang_masuk_id`,`no_transaksi`,`no_nota`,`tanggal_masuk`,`total_jumlah`,`total_masuk`,`supplier_id`,`user_id`,`insert_at`,`insert_by`,`update_at`,`update_by`,`delete_at`,`delete_by`,`is_delete`",
            masuk_rows,
        ),
        insert_block(
            "barang_masuk_detail",
            "`barang_masuk_detail_id`,`barang_masuk_id`,`stok_id`,`jumlah`,`harga_beli`,`subtotal`,`rak_id`,`gudang_id`",
            masuk_detail_rows,
        ),
        insert_block(
            "stok_opname",
            "`opname_id`,`tanggal_opname`,`keterangan`,`status`,`user_id`,`insert_at`,`insert_by`,`update_at`,`update_by`,`delete_at`,`delete_by`,`is_delete`",
            opname_rows,
        ),
        insert_block(
            "stok_opname_detail",
            "`opname_detail_id`,`opname_id`,`barang_id`,`stok_sistem`,`stok_fisik`,`selisih`,`catatan`",
            opname_detail_rows,
        ),
        insert_block(
            "images",
            "`id`,`image_name`,`image_path`,`created_at`",
            images_rows,
        ),
    ]

    out = Path(__file__).parent / "_seed_data_bookstore.sql"
    out.write_text("\n".join(parts), encoding="utf-8")
    print(f"Wrote {out}")
    print(
        f"Counts: kategori={len(kategori_rows)}, merk={len(merk_rows)}, rak={len(rak_rows)}, "
        f"gudang={len(gudang_rows)}, supplier={len(supplier_rows)}, satuan={len(satuan_rows)}, "
        f"barang={len(barang_rows)}, stok={len(stok_rows)}, masuk={len(masuk_rows)}, "
        f"keluar={len(keluar_rows)}, opname={len(opname_rows)}"
    )


if __name__ == "__main__":
    main()
