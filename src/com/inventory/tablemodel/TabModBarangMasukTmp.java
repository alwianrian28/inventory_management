package com.inventory.tablemodel;

import com.inventory.model.BarangMasukTmp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Dearclaudia
 */
public class TabModBarangMasukTmp extends AbstractTableModel {
    private static final long serialVersionUID = 1L;


    private final List<BarangMasukTmp> list = new ArrayList<>();
    private final DecimalFormat decimalFormat;
    
    public TabModBarangMasukTmp() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');

        decimalFormat = new DecimalFormat("#,##0.00", symbols);
    }
    
    public BarangMasukTmp getData(int index){
        return list.get(index);
    }
    
    public void clear(){
        list.clear();
        fireTableDataChanged();
    }
    
    public void setData(List<BarangMasukTmp> list){
        clear();
        this.list.addAll(list);
        fireTableDataChanged();
    }
    
    public void removeRow(int row) {
        list.remove(row);
        fireTableRowsDeleted(row, row);
    }
    
    private final String[] columnNames = {"No","Barcode","Nama Barang","Satuan","Jumlah","Harga Beli","Subtotal","Rak","Gudang","Aksi"};
    
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
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 9;
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        BarangMasukTmp model = list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return rowOffset + rowIndex + 1;
            case 1:
                return model.getBarang().getBarcode();
            case 2:
                return model.getBarang().getBarangName();
            case 3:
                return model.getBarang().getSatuan().getSatuanName();
            case 4:
                return model.getJumlah();
            case 5:
                return decimalFormat.format(model.getHargaBeli());
            case 6:
                return decimalFormat.format(model.getSubtotal());
            case 7:
                return model.getRak().getRakName();
            case 8:
                return model.getGudang().getGudangName();
            case 9:
                return "Hapus";
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    
}
