package com.inventory.model;

import java.awt.Image;
import java.io.File;
import javax.swing.ImageIcon;

/**
 *
 * @author Dearclaudia
 */
public class Images {

    private int imageID;
    private String imageName;
    private String imagePath;
    
    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Image getImageRaw() {
        if (imagePath != null) {
            File file = new File(imagePath);
            if (file.exists()) {
                return new ImageIcon(imagePath).getImage();
            }
        }
        return null;
    }

}
