package com.example.sudoku;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.net.URL;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {

    private MediaPlayer player;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/example/sudoku/sudoku.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);

        stage.setTitle("Sudoku 6x6");
        stage.setScene(scene);

        URL iconUrl = getClass().getResource("/com/example/sudoku/icono.png");
        if (iconUrl != null) {
            stage.getIcons().add(new Image(iconUrl.toExternalForm()));
        }

        stage.setMinWidth(360);
        stage.setMinHeight(460);
        stage.setWidth(460);
        stage.setHeight(600);
        stage.show();

        reproducirMusica();
    }

    private void reproducirMusica() {
        try {
            URL resource = getClass().getResource("/com/example/sudoku/musica.mp3");
            if (resource != null) {
                Media media = new Media(resource.toString());
                player = new MediaPlayer(media);
                player.setCycleCount(MediaPlayer.INDEFINITE);
                player.setVolume(0.2);
                player.play();
            } else {
                Logger.getLogger(Main.class.getName()).log(Level.WARNING, "🎵 Archivo de música no encontrado.");
            }
        } catch (Exception e) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Error al reproducir música", e);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}





