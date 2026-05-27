package com.inventory.tablemodel;

import com.inventory.model.StokOpname;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Dearclaudia
 */
public class TabModOpname extends AbstractTableModel {
    private static final long serialVersionUID = 1L;


    private final List<StokOpname> list = new ArrayList<>();
    
    public StokOpname getData(int index){
        return list.get(index);
    }
    
    public void clear(){
        list.clear();
        fireTableDataChanged();
    }
    
    public void setData(List<StokOpname> list){
        clear();
        this.list.addAll(list);
        fireTableDataChanged();
    }
    
    private final String[] columnNames = {"No","Tanggal SO","Keterangan","Status","User"};
    
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
        StokOpname model = list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return rowOffset + rowIndex + 1;
            case 1:
                return model.getTanggalOpname();
            case 2:
                return model.getKeterangan();
            case 3:
                return model.getStatus();
            case 4:
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
