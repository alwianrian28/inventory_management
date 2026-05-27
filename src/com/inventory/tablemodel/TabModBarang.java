package com.inventory.tablemodel;

import com.inventory.model.Barang;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Dearclaudia
 */
public class TabModBarang extends AbstractTableModel {
    private static final long serialVersionUID = 1L;


    private final List<Barang> list = new ArrayList<>();
    private final DecimalFormat decimalFormat;
    
    public TabModBarang() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');

        decimalFormat = new DecimalFormat("#,##0.00", symbols);
    }
    
    public Barang getData(int index){
        return list.get(index);
    }
    
    public void clear(){
        list.clear();
        fireTableDataChanged();
    }
    
    public void setData(List<Barang> list){
        clear();
        this.list.addAll(list);
        fireTableDataChanged();
    }
    
    private final String[] columnNames = {"No","Kode Barang","Barcode","Nama Barang","Merk","Kategori","Satuan","Harga Jual","Stok Minimum","Total Stok","Supplier","Photo"};
    
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
        Barang model = list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return rowOffset + rowIndex + 1;
            case 1:
                return model.getBarangCode();
            case 2:
                return model.getBarcode();
            case 3:
                return model.getBarangName();
            case 4:
                return model.getMerk().getMerkName();
            case 5:
                return model.getKategori().getKategoriName();
            case 6:
                return model.getSatuan().getSatuanName();
            case 7:
                return decimalFormat.format(model.getHargaJual());
            case 8:
                return model.getStokMinimum();
            case 9:
                return model.getTotalStok();
            case 10:
                return model.getSupplier().getSupplierName();
            case 11:
                return model.getPhotoThumbnail();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    
}
