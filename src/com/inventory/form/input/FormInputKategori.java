package com.inventory.form.input;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.inventory.controller.KategoriController;
import com.inventory.form.FormKategori;
import com.inventory.main.FormManager;
import com.inventory.model.Kategori;
import com.inventory.model.User;
import static com.inventory.util.AlertUtils.getOptionAlert;
import com.inventory.util.AppResources;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.Toast;

/**
 *
 * @author Dearclaudia
 */
public class FormInputKategori extends JPanel{
    private static final long serialVersionUID = 1L;


    private JTextField txtKategoriName, txtKeterangan;
    private JButton btnSave, btnCancel;
   
    private final KategoriController controller = new KategoriController();
    private final Kategori model;
    private final FormKategori formKategori;
    private int kategoriID;
    private final User loggedInUser;
    
    public FormInputKategori(Kategori model, FormKategori formKategori) {
        this.loggedInUser = FormManager.getLoggedInUser();
        this.model = model;
        this.formKategori = formKategori;
        
        init();
        
        if(model != null){
            loadData();
        }
    }

    private void init() {
        setLayout(new MigLayout("fillx, insets 5 30 5 30, wrap 2, gap 20, width 400","[50][fill, grow]"));
        this.putClientProperty(FlatClientProperties.STYLE, "background:rgb(255,255,255)");
        
        txtKategoriName = new JTextField();
        txtKategoriName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan nama kategori");
        
        txtKeterangan = new JTextField();
        txtKeterangan.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan keterangan");
        
        btnSave = new JButton("Save");
        btnSave.putClientProperty(FlatClientProperties.STYLE, "background:@accentColor;foreground:rgb(255,255,255)");
        btnSave.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "save_white.svg",0.8f));
        btnSave.setIconTextGap(5);
        btnSave.addActionListener((e) -> {
            if(model == null){
                insertData();
            }else{
                updateData();
            }
        });
        
        btnCancel = new JButton("Cancel");
        btnCancel.setIcon(new FlatSVGIcon(AppResources.ICON_BASE + "cancel.svg",0.4f));
        btnCancel.setIconTextGap(5);
        btnCancel.addActionListener((e) -> {
            if(model == null){
                ModalDialog.closeModal("form input");
            }else{
                ModalDialog.closeModal("form update");
            }
        });
        
        add(createSeparator(),"span, growx, height 2!");
        add(new JLabel("Nama Kategori"),"align right");
        add(txtKategoriName);
        add(new JLabel("Keterangan"),"align right");
        add(txtKeterangan);
        add(createSeparator(),"span, growx, height 2!");
        add(btnSave,"span, split 2, align center, sg btn, hmin 30");
        add(btnCancel,"sg btn, hmin 30");
    }
    
    private JSeparator createSeparator(){
        JSeparator separator = new JSeparator();
        separator.putClientProperty(FlatClientProperties.STYLE, "foreground:rgb(206,206,206)");
        return separator;
    }

    private boolean inputValidation() {

        if (txtKategoriName.getText().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO,
                    "Nama kategori tidak boleh kosong", getOptionAlert());
            return false;
        }

        if (txtKeterangan.getText().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO,
                    "Keterangan tidak boleh kosong", getOptionAlert());
            return false;
        }

        return true;
    }

    
    private void insertData() {

        if (!inputValidation()) return;

        Kategori kategori = new Kategori();
        kategori.setKategoriName(txtKategoriName.getText().trim());
        kategori.setKeterangan(txtKeterangan.getText().trim());
        kategori.setInsertBy(loggedInUser.getUserID());

        boolean success = controller.insertData(kategori);

        if (success) {
            Toast.show(this, Toast.Type.SUCCESS,
                    "Data berhasil ditambahkan", getOptionAlert());
            formKategori.refreshTable();
            resetForm();
        } else {
            Toast.show(this, Toast.Type.WARNING,
                    "Gagal menambahkan data. Periksa data yang duplikat atau data yang belum lengkap.", getOptionAlert());
        }
    }


    private void loadData() {
        btnSave.setText("Update");
        
        kategoriID = model.getKategoriID();
        txtKategoriName.setText(model.getKategoriName());
        txtKeterangan.setText(model.getKeterangan());
    }
    
    private void updateData() {

        if (!inputValidation()) return;

        Kategori kategori = new Kategori();
        kategori.setKategoriID(kategoriID);
        kategori.setKategoriName(txtKategoriName.getText().trim());
        kategori.setKeterangan(txtKeterangan.getText().trim());
        kategori.setUpdateBy(loggedInUser.getUserID());

        boolean success = controller.updateData(kategori);

        if (success) {
            Toast.show(this, Toast.Type.SUCCESS,
                    "Data berhasil diperbarui", getOptionAlert());
            formKategori.refreshTable();
            resetForm();
            ModalDialog.closeModal("form update");
        } else {
            Toast.show(this, Toast.Type.WARNING,
                    "Gagal memperbarui data. Periksa data yang duplikat atau data yang belum lengkap.", getOptionAlert());
        }
    }


    private void resetForm() {
        txtKategoriName.setText("");
        txtKeterangan.setText("");
    }
    
}
