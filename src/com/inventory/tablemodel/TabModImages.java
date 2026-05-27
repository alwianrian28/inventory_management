package com.inventory.tablemodel;

import com.inventory.model.Images;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Dearclaudia
 */
public class TabModImages extends AbstractTableModel {
    private static final long serialVersionUID = 1L;


    private final List<Images> list = new ArrayList<>();
    
    public Images getData(int index){
        return list.get(index);
    }
    
    public void clear(){
        list.clear();
        fireTableDataChanged();
    }
    
    public void setData(List<Images> list){
        clear();
        this.list.addAll(list);
        fireTableDataChanged();
    }
    
    private final String[] columnNames = {"No","Nama","Image"};
    
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
        Images model = list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return rowOffset + rowIndex + 1;
            case 1:
                return model.getImageName();
            case 2:
                return model.getImageRaw();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    
}
