package com.inventory.tablemodel;

import com.inventory.model.StokOpnameTmp;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Dearclaudia
 */
public class TabModOpnameTmp extends AbstractTableModel {
    private static final long serialVersionUID = 1L;


    private final List<StokOpnameTmp> list = new ArrayList<>();
    
    public StokOpnameTmp getData(int index){
        return list.get(index);
    }
    
    public void clear(){
        list.clear();
        fireTableDataChanged();
    }
    
    public void setData(List<StokOpnameTmp> list){
        clear();
        this.list.addAll(list);
        fireTableDataChanged();
    }
    
    public void removeRow(int row) {
        list.remove(row);
        fireTableRowsDeleted(row, row);
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 8;
    }
    
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
    
    private final String[] columnNames = {"No","Kode Barang","Barcode","Nama Barang","Stok Sistem","Stok Fisik","Selisih","Catatan","Aksi"};
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        StokOpnameTmp model = list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return rowOffset + rowIndex + 1;
            case 1:
                return model.getBarang().getBarangCode();
            case 2:
                return model.getBarang().getBarcode();
            case 3:
                return model.getBarang().getBarangName();
            case 4:
                return model.getStokSistem();
            case 5:
                return model.getStokFisik();
            case 6:
                return model.getSelisih();
            case 7:
                return model.getCatatan();
            case 8:
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
