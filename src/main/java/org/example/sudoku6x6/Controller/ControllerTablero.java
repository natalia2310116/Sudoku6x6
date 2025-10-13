package org.example.sudoku6x6.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import org.example.sudoku6x6.Model.ModelTablero;

public class ControllerTablero {

    @FXML
    private GridPane gridTablero;

    @FXML
    private Button btnIniciar, btnReiniciar, btnSalir;

    private final ModelTablero model = new ModelTablero();

    @FXML
    public void initialize() {
        // No generamos tablero inmediatamente, solo al hacer clic en "Iniciar"
    }

    @FXML
    private void iniciarJuego() {
        model.generarTablero();
        crearTablero();
    }

    @FXML
    private void reiniciarJuego() {
        model.limpiarTablero();
        crearTablero();
    }

    @FXML
    private void salirJuego() {
        Stage stage = (Stage) gridTablero.getScene().getWindow();
        stage.close();
    }

    private void crearTablero() {
        gridTablero.getChildren().clear();

        int[][] tablero = model.getTablero();

        for (int fila = 0; fila < 6; fila++) {
            for (int col = 0; col < 6; col++) {
                TextField celda = new TextField();
                celda.setPrefSize(50, 50);
                celda.setStyle("-fx-font-size: 18px; -fx-alignment: center; -fx-border-color: black; -fx-border-width: 0.5;");

                // Si hay valor generado
                if (tablero[fila][col] != 0) {
                    celda.setText(String.valueOf(tablero[fila][col]));
                    celda.setEditable(false);
                    celda.setStyle("-fx-font-size: 18px; -fx-alignment: center; -fx-font-weight: bold; -fx-text-fill: blue;");
                }

                // Bloques alternos
                int bloqueFila = fila / 2;
                int bloqueCol = col / 3;

                if ((bloqueFila + bloqueCol) % 2 == 0) {
                    celda.setBackground(new Background(new BackgroundFill(
                            Color.rgb(215, 225, 255), new CornerRadii(5), Insets.EMPTY))); // Azul claro
                } else {
                    celda.setBackground(new Background(new BackgroundFill(
                            Color.WHITE, new CornerRadii(5), Insets.EMPTY)));
                }

                gridTablero.add(celda, col, fila);
            }
        }

        gridTablero.setStyle("-fx-border-color: black; -fx-border-width: 2;");
    }
}
