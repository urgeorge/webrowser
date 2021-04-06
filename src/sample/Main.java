package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;


public class Main extends Application {

    final JFXPanel jfxPanel = new JFXPanel();
    final JFXPanel jfxPanel2 = new JFXPanel();

    private WebEngine webEngine;

    private final JPanel jpanel = new JPanel(new BorderLayout());
    private final JButton jbIdz = new JButton("Go");
    private final JTextField jtxtURL = new JTextField();
    final JFrame frame = new JFrame("Webrowser");


    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                initGUI(); //Inicjalizacja interfejsu
            }
        });
    }
    private void initGUI() {
        // Uruchomienie w wątku EDT

        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadPage(jtxtURL.getText());
            }
        };
        jbIdz.addActionListener(listener);
        jtxtURL.addActionListener(listener);

        jpanel.add(jtxtURL, BorderLayout.CENTER);
        jpanel.add(jbIdz, BorderLayout.EAST);
        frame.add(jpanel,BorderLayout.NORTH);


        frame.add(jfxPanel, BorderLayout.SOUTH);
        frame.add(jfxPanel2, BorderLayout.CENTER);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); //Fullscreen
        frame.setLocationRelativeTo(null); //Wyswietlanie okna na srodku

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);

        //Uruchomienie w wątku JavyFX
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                initJFX(jfxPanel);
            }
        });
    }

    private void initJFX(JFXPanel jfxPanel) {
        createScene();
    }

    public void loadPage(final String url) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    String tmp = new URL("https://"+url).toExternalForm();
                    webEngine.load(tmp);
                }
                catch (MalformedURLException ex) {
                    try {
                        String tmp2 = new URL("http://"+url).toExternalForm();

                        webEngine.load(tmp2);
                    } catch (MalformedURLException ex2) {

                        System.out.println("Błędny adres URL");
                    }
                }
            }
        });
    }

    private void createScene() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                WebView view = new WebView();
                webEngine = view.getEngine();
                webEngine.titleProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable,
                                        String oldValue, final String newValue) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                frame.setTitle(newValue);
                            }
                        });
                    }
                });
                jfxPanel2.setScene(new Scene(view));
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
