package com.inventory.tablemodel;

import com.inventory.model.BarangMasukDetail;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Dearclaudia
 */
public class TabModReportStok extends AbstractTableModel {
    private static final long serialVersionUID = 1L;


    private final List<BarangMasukDetail> list = new ArrayList<>();
    private final DecimalFormat decimalFormat;
    
    public TabModReportStok() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');

        decimalFormat = new DecimalFormat("#,##0.00", symbols);
    }
    
    public BarangMasukDetail getData(int index){
        return list.get(index);
    }
    
    public void clear(){
        list.clear();
        fireTableDataChanged();
    }
    
    public void setData(List<BarangMasukDetail> list){
        clear();
        this.list.addAll(list);
        fireTableDataChanged();
    }
    
    private final String[] columnNames = {"No","Kode","No Nota","Nama Barang","Merk","Kategori","Satuan","Harga Beli","Harga Jual","Stok Minimum","Stok","Rak","Gudang"};
    
    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    private int rowOffset = 0;

    public void setRowOffset(int offset) {
        this.rowOffset = offset;
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        BarangMasukDetail model = list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return rowOffset + rowIndex + 1;
            case 1:
                return model.getStokGudang().getBarang().getBarangCode();
            case 2:
                return model.getBarangMasuk().getNoNota();
            case 3:
                return model.getStokGudang().getBarang().getBarangName();
            case 4:
                return model.getStokGudang().getBarang().getMerk().getMerkName();
            case 5:
                return model.getStokGudang().getBarang().getKategori().getKategoriName();
            case 6:
                return model.getStokGudang().getBarang().getSatuan().getSatuanName();
            case 7:
                return decimalFormat.format(model.getStokGudang().getHargaBeli());
            case 8:
                return decimalFormat.format(model.getStokGudang().getBarang().getHargaJual());
            case 9:
                return model.getStokGudang().getBarang().getStokMinimum();
            case 10:
                return model.getStokGudang().getJumlah();
            case 11:
                return model.getStokGudang().getRak().getRakName();
            case 12:
                return model.getStokGudang().getGudang().getGudangName();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    
}
