package com.inventory.tablemodel;

import com.inventory.model.StokOpnameDetail;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Dearclaudia
 */
public class TabModOpnameDetail extends AbstractTableModel {
    private static final long serialVersionUID = 1L;


    private final List<StokOpnameDetail> list = new ArrayList<>();
    
    public StokOpnameDetail getData(int index){
        return list.get(index);
    }
    
    public void clear(){
        list.clear();
        fireTableDataChanged();
    }
    
    public void setData(List<StokOpnameDetail> list){
        clear();
        this.list.addAll(list);
        fireTableDataChanged();
    }
    
    private final String[] columnNames = {"No","Kode Barang","Barcode","Nama Barang","Tanggal SO","Stok Sistem","Stok Fisik","Selisih","Catatan"};
    
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
        StokOpnameDetail model = list.get(rowIndex);
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
                return model.getStokOpname().getTanggalOpname();
            case 5:
                return model.getStokSistem();
            case 6:
                return model.getStokFisik();
            case 7:
                return model.getSelisih();
            case 8:
                return model.getCatatan();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    
}
