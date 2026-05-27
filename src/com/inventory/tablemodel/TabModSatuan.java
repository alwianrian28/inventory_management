package com.inventory.tablemodel;

import com.inventory.model.Satuan;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Dearclaudia
 */
public class TabModSatuan extends AbstractTableModel {
    private static final long serialVersionUID = 1L;


    private final List<Satuan> list = new ArrayList<>();
    
    public Satuan getData(int index){
        return list.get(index);
    }
    
    public void clear(){
        list.clear();
        fireTableDataChanged();
    }
    
    public void setData(List<Satuan> list){
        clear();
        this.list.addAll(list);
        fireTableDataChanged();
    }
    
    private final String[] columnNames = {"No","Nama Satuan","Keterangan"};
    
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
        Satuan model = list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return rowOffset + rowIndex + 1;
            case 1:
                return model.getSatuanName();
            case 2:
                return model.getKeterangan();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    
}
