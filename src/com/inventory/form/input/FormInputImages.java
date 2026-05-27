package com.inventory.form.input;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.inventory.controller.ImagesController;
import com.inventory.form.FormImages;
import com.inventory.model.Images;
import static com.inventory.util.AlertUtils.getOptionAlert;
import com.inventory.util.AppResources;
import com.inventory.util.ImageCropperDialog;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.Toast;

/**
 *
 * @author Dearclaudia
 */
public class FormInputImages extends JPanel{
    private static final long serialVersionUID = 1L;


    private JTextField txtImageName;
    private JButton btnSave, btnCancel, btnBrowse;
   
    private JLabel lbImages;
    private String imagePath;
    
    private final ImagesController controller = new ImagesController();
    
    private final Images model;
    private final FormImages formImages;
    private int imagesID;
    
    private BufferedImage croppedImagePreview;
    
    public FormInputImages(Images model, FormImages formImages) {
        this.model = model;
        this.formImages = formImages;
        
        init();
        
        if(model != null){
            loadData();
        }
    }

    private void init() {
        setLayout(new MigLayout("fillx, insets 5 30 5 30, wrap 2, gap 20, width 500","[50][fill, grow]"));
        this.putClientProperty(FlatClientProperties.STYLE, "background:rgb(255,255,255)");
        
        txtImageName = new JTextField();
        txtImageName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter Images Name");
        
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
        add(new JLabel("Name"),"align right");
        add(txtImageName);
        add(new JLabel("Image"),"align right, top");
        add(createImagePanel(), "wrap");
        add(createSeparator(),"span, growx, height 2!");
        add(btnSave,"span, split 2, align center, sg btn, hmin 30");
        add(btnCancel,"sg btn, hmin 30");
    }
    
    private JPanel createImagePanel() {
        JPanel panel = new JPanel(new MigLayout("insets 0, gap 5"));
        panel.setOpaque(false);

        lbImages = new JLabel("Recommended size: 1600 × 360 px");
        lbImages.setOpaque(true);
        lbImages.setBackground(Color.WHITE);
        lbImages.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lbImages.setHorizontalAlignment(JLabel.CENTER);
        lbImages.setVerticalAlignment(JLabel.CENTER);

        btnBrowse = new JButton("Browse");
        btnBrowse.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(
                new FileNameExtensionFilter("Images", "jpg", "jpeg", "png")
            );

            if (fileChooser.showOpenDialog(
                    SwingUtilities.getWindowAncestor(this)
            ) == JFileChooser.APPROVE_OPTION) {

                File file = fileChooser.getSelectedFile();
                ImageCropperDialog cropper =
                    new ImageCropperDialog(
                        (Frame) SwingUtilities.getWindowAncestor(this),
                        file.getAbsolutePath()
                    );
                cropper.setVisible(true);

                BufferedImage cropped = cropper.getCroppedImageResult();
                if (cropped != null) {
                    croppedImagePreview = cropped;
                    imagePath = null;

                    Image scaled = cropped.getScaledInstance(
                            400, 100, Image.SCALE_SMOOTH
                    );
                    lbImages.setText(null);
                    lbImages.setIcon(new ImageIcon(scaled));
                }
            }
        });

        panel.add(lbImages, "w 400!, h 100!");
        panel.add(btnBrowse, "align left, top");

        return panel;
    }

    
    private void setImages(String path) {
        if (path != null && !path.isEmpty()) {
            ImageIcon icon = new ImageIcon(path);
            Image scaled = icon.getImage().getScaledInstance(
                    400, 100, Image.SCALE_SMOOTH
            );
            lbImages.setText(null);
            lbImages.setIcon(new ImageIcon(scaled));
        } else {
            lbImages.setIcon(null);
            lbImages.setText("Recommended size: 1600 × 360 px");
        }
    }

    
    private JSeparator createSeparator(){
        JSeparator separator = new JSeparator();
        separator.putClientProperty(FlatClientProperties.STYLE, "foreground:rgb(206,206,206)");
        return separator;
    }

    private boolean inputValidation(){
        boolean valid = false;
        
        if(txtImageName.getText().trim().isEmpty()){
            Toast.show(this, Toast.Type.INFO, "Images Name cannot be empty", getOptionAlert());
        }else if(imagePath == null && croppedImagePreview == null){
            Toast.show(this, Toast.Type.INFO, "Please choose a image", getOptionAlert());
        }else{
            valid = true;
        }
        
        return valid;
    }
    
    private void insertData(){
        if(inputValidation()){
            String imageName = txtImageName.getText();
            String newImagePath;

            try {
                if (croppedImagePreview != null) {
                    // Simpan hasil crop
                    newImagePath = saveBufferedImageToFile(croppedImagePreview);
                } else {
                    // Copy file lama
                    newImagePath = saveFileToProject(new File(imagePath));
                }

                Images modelImages = new Images();
                modelImages.setImageName(imageName);
                modelImages.setImagePath(newImagePath);

                controller.insertData(modelImages);
                Toast.show(this, Toast.Type.SUCCESS, "Data has been successfully added", getOptionAlert());
                formImages.refreshTable();
                resetForm();
            } catch (IOException e) {
                Toast.show(this, Toast.Type.ERROR, "Failed to save image", getOptionAlert());
            }
        }
    }

    
    private String saveFileToProject(File file){
        String projectDir = System.getProperty("user.dir");
        String folderName = "files";
        File destFolder = new File(projectDir, folderName);

        if (!destFolder.exists()) {
            destFolder.mkdirs();
        }

        String appPrefix = "Images_";
        String originalFileName = file.getName();

        String extension = originalFileName.substring(originalFileName.lastIndexOf(".")); 
        String timestamp = String.valueOf(System.currentTimeMillis()); 
        String newFileName = appPrefix + timestamp + extension;

        File destFile = new File(destFolder, newFileName);
        try {
            Files.copy(file.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return folderName + "/" + newFileName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    
    private String saveBufferedImageToFile(BufferedImage img) throws IOException {
        String projectDir = System.getProperty("user.dir");
        String folderName = "files";
        File destFolder = new File(projectDir, folderName);
        if (!destFolder.exists()) {
            destFolder.mkdirs();
        }

        String fileName = "Images_" + System.currentTimeMillis() + ".png";
        File destFile = new File(destFolder, fileName);
        ImageIO.write(img, "png", destFile);

        return folderName + "/" + fileName;
    }


    private void loadData() {
        btnSave.setText("Update");
        
        imagesID = model.getImageID();
        txtImageName.setText(model.getImageName());
        
        imagePath = model.getImagePath();
        setImages(imagePath);
    }
    
    private void updateData() {
        if (inputValidation()) {
            String imageName = txtImageName.getText();
            String newImagePath = null;
            String oldImagePath = model.getImagePath();   // path lama dari DB

            try {
                if (croppedImagePreview != null) {
                    // Simpan hasil crop ke file baru
                    newImagePath = saveBufferedImageToFile(croppedImagePreview);
                } else if (imagePath != null) {
                    // Copy file lama (kalau pilih gambar baru tapi tanpa crop)
                    newImagePath = saveFileToProject(new File(imagePath));
                } else {
                    // Tidak ada perubahan gambar → tetap pakai path lama
                    newImagePath = oldImagePath;
                }

                if (newImagePath == null || newImagePath.isEmpty()) {
                    Toast.show(this, Toast.Type.INFO, "Image path is empty", getOptionAlert());
                    return;
                }

                Images modelImages = new Images();
                modelImages.setImageName(imageName);
                modelImages.setImagePath(newImagePath);
                modelImages.setImageID(imagesID);

                controller.updateData(modelImages);

                // 🔥 Hapus file lama kalau ada file baru
                if (oldImagePath != null && !oldImagePath.equals(newImagePath)) {
                    File oldFile = new File(System.getProperty("user.dir"), oldImagePath);
                    if (oldFile.exists()) {
                        oldFile.delete();
                    }
                }

                Toast.show(this, Toast.Type.SUCCESS, "Data has been successfully updated", getOptionAlert());

                formImages.refreshTable();
                resetForm();
                ModalDialog.closeModal("form update");
            } catch (IOException e) {
                Toast.show(this, Toast.Type.ERROR, "Failed to update image", getOptionAlert());
            }
        }
    }



    private void resetForm() {
        txtImageName.setText("");
        lbImages.setIcon(null);
        lbImages.setText("Recommended size: 1600 × 360 px");
    }

}
