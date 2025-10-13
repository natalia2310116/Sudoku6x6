package org.example.sudoku6x6.Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Clase principal que carga la vista del tablero de Sudoku 6x6.
 * @author Natalia
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    getClass().getResource("/org/example/sudoku6x6/tablero-view.fxml")
            );
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Sudoku 6x6");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
