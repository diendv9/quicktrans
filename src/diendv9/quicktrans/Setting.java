/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diendv9.quicktrans;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author diendv9
 */
public class Setting implements Serializable {

    private static Setting setting;

    private double opacity;
    private String primaryLang;
    private String secondLang;
    private boolean alwaysOnTop;
    private int position; //0: center   1: top-left     2: top-right    3: bottom-right     4: bottom-left

    private Setting() {
        opacity = 0.9;
        primaryLang = "vi";
        secondLang = "en";
        alwaysOnTop = false;
        position = 2;
    }

    public static Setting getInstance() {
        if (setting == null) {
            loadSetting();
            setting.updateSetting();
        }
        return setting;
    }

    public double getOpacity() {
        return opacity;
    }

    public void setOpacity(double opacity) {
        this.opacity = opacity;
    }

    public String getPrimaryLang() {
        return primaryLang;
    }

    public void setPrimaryLang(String primaryLang) {
        this.primaryLang = primaryLang;
    }

    public String getSecondLang() {
        return secondLang;
    }

    public void setSecondLang(String secondLang) {
        this.secondLang = secondLang;
    }

    public boolean isAlwaysOnTop() {
        return alwaysOnTop;
    }

    public void setAlwaysOnTop(boolean alwaysOnTop) {
        this.alwaysOnTop = alwaysOnTop;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    private static void loadSetting() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("conf.ini"))) {
            setting = (Setting) ois.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            setting = new Setting();
        }
    }

    public void updateSetting() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("conf.ini"))) {
            oos.writeObject(setting);
        } catch (IOException ex) {
            Logger.getLogger(Setting.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
