/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diendv9.quicktrans;

import com.melloware.jintellitype.JIntellitype;
import static diendv9.quicktrans.Main.main;
import static diendv9.quicktrans.Main.stage;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.input.Clipboard;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 *
 * @author diendv9
 */
public class Controller implements Initializable {

    private WebEngine webEngine;
    @FXML
    private WebView webView;
    @FXML
    private Button btClose;
    @FXML
    private Button btExit;
    @FXML
    private ComboBox<String> cbPosition;
    @FXML
    private CheckBox chbOnTop;
    @FXML
    private Slider slOpacity;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        webEngine = webView.getEngine();
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                webEngine.executeScript("document.getElementById('gb').parentElement.removeChild(document.getElementById('gb'));");
                webEngine.executeScript("document.getElementById('gba').parentElement.removeChild(document.getElementById('gba'));");
                webEngine.executeScript("document.getElementById('gt-ft-res').parentElement.removeChild(document.getElementById('gt-ft-res'));");
                webEngine.executeScript("document.getElementById('gt-ft').parentElement.removeChild(document.getElementById('gt-ft'));");
                webEngine.executeScript("document.getElementById('gt-appbar').parentElement.removeChild(document.getElementById('gt-appbar'));");
                webEngine.executeScript("document.getElementById('gt-res-share').parentElement.removeChild(document.getElementById('gt-res-share'));");
                webEngine.executeScript("document.getElementById('gt-pb-star').parentElement.removeChild(document.getElementById('gt-pb-star'));");
                stage.show();
            }
        }); // addListener()
        webEngine.load("https://translate.google.com/#en/vi/Hello%20You!");
        webView.getEngine().setUserStyleSheetLocation(getClass().getResource("browser-style.css").toString());
        Clipboard clipboard = Clipboard.getSystemClipboard();
        JIntellitype.getInstance().registerHotKey(1, JIntellitype.MOD_CONTROL, (int) 'D');
        JIntellitype.getInstance().addHotKeyListener((int i) -> {
            if (i == 1) {
                Platform.runLater(() -> {
                    if (clipboard.hasString()) {
                        try {
                            webEngine.load("https://translate.google.com/#en/vi/" + URLEncoder.encode(clipboard.getString(), "UTF-8"));
//                            webEngine.executeScript("document.getElementById('source').value = \" " + clipboard.getString() + " \";");
                        } catch (UnsupportedEncodingException ex) {
                            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
            }
        });

        cbPosition.getSelectionModel().select(Setting.getInstance().getPosition());
        cbPosition.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            Platform.runLater(() -> {
                switch (newValue) {
                    case "Center":
                        Setting.getInstance().setPosition(0);
                        break;
                    case "Top-Left":
                        Setting.getInstance().setPosition(1);
                        break;
                    case "Top-Right":
                        Setting.getInstance().setPosition(2);
                        break;
                    case "Bottom-Right":
                        Setting.getInstance().setPosition(3);
                        break;
                    case "Bottom-Left":
                        Setting.getInstance().setPosition(4);
                        break;
                }
                Main.setPosition();
            });
        });

        chbOnTop.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            Setting.getInstance().setAlwaysOnTop(newValue);
            Main.stage.setAlwaysOnTop(newValue);
        });
        slOpacity.setMax(1);
        slOpacity.setMin(0.6);
        slOpacity.setValue(Setting.getInstance().getOpacity());
        slOpacity.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            Setting.getInstance().setOpacity((double) newValue);
            Main.stage.setOpacity((double) newValue);
        });
    }

    @FXML
    private void exit() {
        JIntellitype.getInstance().cleanUp();
        Setting.getInstance().updateSetting();
        Platform.exit();
    }

    @FXML
    private void close() {
        stage.hide();
        Main.main.addToTray();
    }
}
