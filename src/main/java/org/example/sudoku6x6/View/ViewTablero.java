package org.example.sudoku6x6.View;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Clase encargada de mostrar la vista del tablero Sudoku 6x6.
 * Carga el archivo FXML y lo presenta en una ventana JavaFX.
 */
public class ViewTablero {

    private final Stage stage;

    public ViewTablero() {
        this.stage = new Stage();
    }

    public void mostrar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/sudoku6x6/tablero-view.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setTitle("Sudoku 6x6 - Tablero");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

