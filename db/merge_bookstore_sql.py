#!/usr/bin/env python3
"""Merge bookstore seed data into inventory_management.sql."""

from pathlib import Path
import re

ROOT = Path(__file__).parent
ORIGINAL = ROOT / "inventory_management.sql"
SEED = ROOT / "_seed_data_bookstore.sql"
OUTPUT = ROOT / "inventory_management.sql"

TABLES_WITH_DATA = [
    "kategori",
    "merk",
    "rak",
    "gudang",
    "supplier",
    "satuan",
    "barang",
    "stok_gudang",
    "barang_keluar",
    "barang_keluar_detail",
    "barang_masuk",
    "barang_masuk_detail",
    "stok_opname",
    "stok_opname_detail",
    "images",
]

EMPTY_TABLES = ["barang_keluar_tmp", "barang_masuk_tmp", "stok_opname_tmp"]


def extract_create_blocks(text: str) -> dict[str, str]:
    pattern = re.compile(
        r"/\*Table structure for table `(\w+)` \*/\n(.*?)(?=/\*Data for the table `|\Z)",
        re.DOTALL,
    )
    return {
        m.group(1): f"/*Table structure for table `{m.group(1)}` */\n{m.group(2).rstrip()}\n\n"
        for m in pattern.finditer(text)
    }


def extract_seed_blocks(text: str) -> dict[str, str]:
    pattern = re.compile(
        r"(/\*Data for the table `(\w+)` \*/\n.*?)(?=/\*Data for the table|\Z)",
        re.DOTALL,
    )
    return {m.group(2): m.group(1).rstrip() + "\n\n" for m in pattern.finditer(text)}


def main() -> None:
    original = ORIGINAL.read_text(encoding="utf-8")
    seed = SEED.read_text(encoding="utf-8")

    user_end = original.index("/*Table structure for table `kategori` */")
    header = original[:user_end]

    creates = extract_create_blocks(original)
    seeds = extract_seed_blocks(seed)

    parts = [header]
    order = [
        "kategori",
        "merk",
        "rak",
        "gudang",
        "supplier",
        "satuan",
        "barang",
        "stok_gudang",
        "barang_keluar",
        "barang_keluar_detail",
        "barang_keluar_tmp",
        "barang_masuk",
        "barang_masuk_detail",
        "barang_masuk_tmp",
        "stok_opname",
        "stok_opname_detail",
        "stok_opname_tmp",
        "images",
    ]

    for table in order:
        if table not in creates:
            raise KeyError(f"Missing CREATE for {table}")
        parts.append(creates[table])
        if table in TABLES_WITH_DATA:
            parts.append(seeds[table])
        elif table in EMPTY_TABLES:
            parts.append(f"/*Data for the table `{table}` */\n\n")

    OUTPUT.write_text("".join(parts), encoding="utf-8")
    print(f"Updated {OUTPUT} ({OUTPUT.stat().st_size // 1024} KB)")


if __name__ == "__main__":
    main()
