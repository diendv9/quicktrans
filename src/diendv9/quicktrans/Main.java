/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diendv9.quicktrans;

import com.melloware.jintellitype.JIntellitype;
import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import static javafx.stage.StageStyle.UNDECORATED;
import javafx.stage.WindowEvent;
import javax.imageio.ImageIO;

/**
 *
 * @author diendv9
 */
public class Main extends Application {

    private Setting setting;
    static Stage stage;
    private TrayIcon trayIcon;
    private SystemTray tray;
    static Main main;

    @Override
    public void start(Stage stage) throws Exception {
        Main.stage = stage;
        main = this;
        setting = Setting.getInstance();
        tray = SystemTray.getSystemTray();

        BorderPane root = FXMLLoader.load(getClass().getResource("screen.fxml"));
        Scene scene = new Scene(root);
        stage.initStyle(UNDECORATED);
        Platform.setImplicitExit(false);
        stage.setResizable(false);

        stage.setOnCloseRequest((WindowEvent event) -> {
            event.consume();
        });
        createTrayIcon();
        setPosition();
        stage.setOpacity(setting.getOpacity());
        stage.setAlwaysOnTop(setting.isAlwaysOnTop());
        stage.setScene(scene);
        stage.getIcons().add(new Image(this.getClass().getResourceAsStream("m.png")));
        stage.setTitle("Quick Trans 1.0");

    }

    private void createTrayIcon() {
        if (SystemTray.isSupported()) {
            java.awt.Image image = null;
            try {
                image = ImageIO.read(getClass().getResource("icon.png"));
            } catch (IOException ex) {
                System.out.println(ex);
            }
            PopupMenu popup = new PopupMenu();
            MenuItem showItem = new MenuItem("Show");
            showItem.addActionListener((ActionEvent e) -> {
                Platform.runLater(() -> {
                    stage.show();
                    hideFromTray();
                });
            });
            popup.add(showItem);
            MenuItem exitItem = new MenuItem("Exit");
            exitItem.addActionListener((ActionEvent e) -> {
                Platform.runLater(() -> {
                    JIntellitype.getInstance().cleanUp();
                    Setting.getInstance().updateSetting();
                    System.exit(0);
                });
            });
            popup.add(exitItem);
            trayIcon = new TrayIcon(image, "QuickTrans", popup);
        }
    }

    void addToTray() {
        try {
            tray.add(trayIcon);
        } catch (AWTException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void hideFromTray() {
        tray.remove(trayIcon);
    }

    public static void setPosition() {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        switch (Setting.getInstance().getPosition()) {
            case 0:
                stage.setX(screenBounds.getWidth() / 2 - 400);
                stage.setY(screenBounds.getHeight() / 2 - 300);
                break;
            case 1:
                stage.setX(0);
                stage.setY(0);
                break;
            case 2:
                stage.setX(screenBounds.getWidth() - 800);
                stage.setY(0);
                break;
            case 3:
                stage.setX(screenBounds.getWidth() - 800);
                stage.setY(screenBounds.getHeight() - 600);
                break;
            case 4:
                stage.setX(0);
                stage.setY(screenBounds.getHeight() - 600);
                break;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
