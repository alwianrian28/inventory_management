package com.inventory.tablemodel;

import com.inventory.model.StokGudang;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Dearclaudia
 */
public class TabModStokGudang extends AbstractTableModel {
    private static final long serialVersionUID = 1L;


    private final List<StokGudang> list = new ArrayList<>();
    private final DecimalFormat decimalFormat;
    
    public TabModStokGudang() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');

        decimalFormat = new DecimalFormat("#,##0.00", symbols);
    }
    
    public StokGudang getData(int index){
        return list.get(index);
    }
    
    public void clear(){
        list.clear();
        fireTableDataChanged();
    }
    
    public void setData(List<StokGudang> list){
        clear();
        this.list.addAll(list);
        fireTableDataChanged();
    }
    
    private final String[] columnNames = {"No","Nama Barang","Jumlah","Harga Beli","Subtotal","Rak","Gudang"};
    
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
        StokGudang model = list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return rowOffset + rowIndex + 1;
            case 1:
                return model.getBarang().getBarangName();
            case 2:
                return model.getJumlah();
            case 3:
                return decimalFormat.format(model.getHargaBeli());
            case 4:
                return decimalFormat.format(model.getSubtotal());
            case 5:
                return model.getRak().getRakName();
            case 6:
                return model.getGudang().getGudangName();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    
}
