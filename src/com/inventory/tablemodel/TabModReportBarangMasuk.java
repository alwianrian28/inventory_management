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
public class TabModReportBarangMasuk extends AbstractTableModel {
    private static final long serialVersionUID = 1L;


    private final List<BarangMasukDetail> list = new ArrayList<>();
    private final DecimalFormat decimalFormat;
    
    public TabModReportBarangMasuk() {
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
    
    private final String[] columnNames = {"No","No Transaksi","Tanggal","No Nota","Nama Barang","Jumlah","Harga Beli","Subtotal","Rak","Gudang","Supplier"};
    
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
                return model.getBarangMasuk().getNoTransaksi();
            case 2:
                return model.getBarangMasuk().getTanggalMasuk();
            case 3:
                return model.getBarangMasuk().getNoNota();
            case 4:
                return model.getStokGudang().getBarang().getBarangName();
            case 5:
                return model.getJumlah();
            case 6:
                return decimalFormat.format(model.getHargaBeli());
            case 7:
                return decimalFormat.format(model.getSubtotal());
            case 8:
                return model.getRak().getRakName();
            case 9:
                return model.getGudang().getGudangName();
            case 10:
                return model.getBarangMasuk().getSupplier().getSupplierName();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    
}
