package com.inventory.tablemodel;

import com.inventory.model.BarangKeluar;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Dearclaudia
 */
public class TabModBarangKeluar extends AbstractTableModel {
    private static final long serialVersionUID = 1L;


    private final List<BarangKeluar> list = new ArrayList<>();
    private final DecimalFormat decimalFormat;
    
    public TabModBarangKeluar() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');

        decimalFormat = new DecimalFormat("#,##0.00", symbols);
    }
    
    public BarangKeluar getData(int index){
        return list.get(index);
    }
    
    public void clear(){
        list.clear();
        fireTableDataChanged();
    }
    
    public void setData(List<BarangKeluar> list){
        clear();
        this.list.addAll(list);
        fireTableDataChanged();
    }
    
    private final String[] columnNames = {"No","No Transaksi","Tanggal","Jenis Keluar","Tujuan","Total Jumlah","Total Keluar","User"};
    
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
        BarangKeluar model = list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return rowOffset + rowIndex + 1;
            case 1:
                return model.getNoTransaksi();
            case 2:
                return model.getTanggalKeluar();
            case 3:
                return model.getJenisKeluar();
            case 4:
                return model.getTujuan();
            case 5:
                return model.getTotalJumlah();
            case 6:
                return decimalFormat.format(model.getTotalKeluar());
            case 7:
                return model.getUser().getName();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    
}
