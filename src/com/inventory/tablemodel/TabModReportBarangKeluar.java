package com.inventory.tablemodel;

import com.inventory.model.BarangKeluarDetail;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Dearclaudia
 */
public class TabModReportBarangKeluar extends AbstractTableModel {
    private static final long serialVersionUID = 1L;


    private final List<BarangKeluarDetail> list = new ArrayList<>();
    private final DecimalFormat decimalFormat;
    
    public TabModReportBarangKeluar() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');

        decimalFormat = new DecimalFormat("#,##0.00", symbols);
    }
    
    public BarangKeluarDetail getData(int index){
        return list.get(index);
    }
    
    public void clear(){
        list.clear();
        fireTableDataChanged();
    }
    
    public void setData(List<BarangKeluarDetail> list){
        clear();
        this.list.addAll(list);
        fireTableDataChanged();
    }
    
    private final String[] columnNames = {"No","No Transaksi","Tanggal","Jenis Keluar","Tujuan","Nama Barang","Jumlah","Harga Jual","Subtotal"};
    
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
        BarangKeluarDetail model = list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return rowOffset + rowIndex + 1;
            case 1:
                return model.getBarangKeluar().getNoTransaksi();
            case 2:
                return model.getBarangKeluar().getTanggalKeluar();
            case 3:
                return model.getBarangKeluar().getJenisKeluar();
            case 4:
                return model.getBarangKeluar().getTujuan();
            case 5:
                return model.getBarang().getBarangName();
            case 6:
                return model.getJumlah();
            case 7:
                return decimalFormat.format(model.getHargaJual());
            case 8:
                return decimalFormat.format(model.getSubtotal());
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    
}
