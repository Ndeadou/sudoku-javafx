/**
 * Clase principal del juego Sudoku 6x6.
 * Inicia la aplicación JavaFX, carga la interfaz desde FXML,
 * configura la ventana principal e inicia la música de fondo.
 *
 * Desarrollado por:
 * Miguel Ángel Descance - Código: 2416788
 * Erick Andrey Obando Narvaez - Código: 2415751
 */

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

    /**
     * Reproductor de música para el fondo del juego.
     */
    private MediaPlayer player;

    /**
     * Método principal de inicio de JavaFX.
     * Carga la vista desde FXML, configura la escena y muestra la ventana.
     *
     * @param stage La ventana principal de la aplicación.
     * @throws IOException si ocurre un error al cargar el archivo FXML.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/example/sudoku/sudoku.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);

        stage.setTitle("Sudoku 6x6");
        stage.setScene(scene);

        // Establecer icono personalizado si existe
        URL iconUrl = getClass().getResource("/com/example/sudoku/icono.png");
        if (iconUrl != null) {
            stage.getIcons().add(new Image(iconUrl.toExternalForm()));
        }

        // Configuración de dimensiones de la ventana
        stage.setMinWidth(360);
        stage.setMinHeight(460);
        stage.setWidth(460);
        stage.setHeight(600);
        stage.show();

        reproducirMusica();
    }

    /**
     * Reproduce música de fondo en bucle si el archivo de audio está disponible.
     * Muestra advertencia o error si no se encuentra o no se puede reproducir el archivo.
     */
    private void reproducirMusica() {
        try {
            URL resource = getClass().getResource("/com/example/sudoku/musica.mp3");
            if (resource != null) {
                Media media = new Media(resource.toString());
                player = new MediaPlayer(media);
                player.setCycleCount(MediaPlayer.INDEFINITE); // Repetir indefinidamente
                player.setVolume(0.1); // Volumen bajo
                player.play();
            } else {
                Logger.getLogger(Main.class.getName()).log(Level.WARNING, "🎵 Archivo de música no encontrado.");
            }
        } catch (Exception e) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Error al reproducir música", e);
        }
    }

    /**
     * Método main de la aplicación.
     *
     * @param args Argumentos desde la línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        launch();
    }
}






