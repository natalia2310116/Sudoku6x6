package org.example.sudoku6x6.Controller;

import javafx.geometry.Insets;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.example.sudoku6x6.Model.ModelTablero;
import org.example.sudoku6x6.Model.Sugerencia;
import org.example.sudoku6x6.Model.Tablero;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Controlador de la vista del Sudoku 6x6.
 * Maneja eventos de UI, entrada por teclado, validación y ayudas.
 */
public class ControllerTablero {

    @FXML
    private GridPane gridTablero;

    @FXML
    private Button btnIniciar, btnReiniciar, btnSalir, btnAyuda;

    private final Tablero model = new ModelTablero();
    private final Map<String, TextField> cellMap = new HashMap<>(); // key: r,c
    private int ayudasRestantes = 3;

    @FXML
    /** Inicializa la vista creando el tablero vacío y el texto de ayudas. */
    public void initialize() {
        // Se crea una grilla vacía inicialmente
        crearTablero();
        actualizarTextoAyuda();
    }

    @FXML
    /** Inicia un nuevo juego previa confirmación y reinicia contador de ayudas. */
    private void iniciarJuego() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Nuevo juego");
        confirm.setHeaderText("¿Deseas iniciar un nuevo juego?");
        confirm.setContentText("Se generarán 2 números por bloque 2x3 válidos.");
        Optional<ButtonType> res = confirm.showAndWait();
        if (res.isPresent() && res.get() == ButtonType.OK) {
            model.generarTablero();
            crearTablero();
            ayudasRestantes = 3;
            actualizarTextoAyuda();
        }
    }

    @FXML
    /** Limpia el tablero y restablece las ayudas. */
    private void reiniciarJuego() {
        model.limpiarTablero();
        crearTablero();
        ayudasRestantes = 3;
        actualizarTextoAyuda();
    }

    @FXML
    /** Cierra la ventana actual. */
    private void salirJuego() {
        Stage stage = (Stage) gridTablero.getScene().getWindow();
        stage.close();
    }

    @FXML
    /**
     * Realiza una sugerencia aleatoria en una celda vacía.
     * Reduce contador de ayudas y resalta visualmente la sugerencia.
     */
    private void sugerir() {
        if (ayudasRestantes <= 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ayuda agotada");
            alert.setHeaderText("No tienes más ayudas disponibles");
            alert.setContentText("Has usado todas las 3 ayudas permitidas.");
            alert.showAndWait();
            return;
        }

        // Obtener sugerencia aleatoria del modelo
        Optional<Sugerencia> sug = model.sugerir();
        if (sug.isPresent()) {
            Sugerencia s = sug.get();
            String key = key(s.fila, s.columna);
            TextField tf = cellMap.get(key);
            if (tf != null && tf.isEditable()) {
                // Limpiar sugerencias anteriores
                limpiarSugerenciasAnteriores();

                // Aplicar nueva sugerencia
                tf.setPromptText(String.valueOf(s.valor));
                tf.setStyle(tf.getStyle() + "; -fx-border-color: #00aa88; -fx-border-width: 3; -fx-background-color: #e8f5e8;");
                tf.requestFocus();

                ayudasRestantes--;
                actualizarTextoAyuda();

                // Mostrar mensaje informativo
                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setTitle("Sugerencia");
                info.setHeaderText("Ayuda aplicada");
                info.setContentText("Se ha sugerido el número " + s.valor + " en la posición (" + (s.fila + 1) + "," + (s.columna + 1) + ").\nAyudas restantes: " + ayudasRestantes);
                info.showAndWait();
            }
        } else {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Sin sugerencias");
            a.setHeaderText("No hay sugerencias disponibles");
            a.setContentText("No se encontraron celdas vacías con valores válidos.");
            a.showAndWait();
        }
    }

    /**
     * Limpia todas las sugerencias visuales anteriores.
     */
    private void limpiarSugerenciasAnteriores() {
        for (TextField tf : cellMap.values()) {
            if (tf.isEditable()) {
                tf.setPromptText("");
                // Restaurar estilo base sin sugerencias
                String baseStyle = "-fx-font-size: 18px; -fx-alignment: center; -fx-border-color: black; -fx-border-width: 0.5;";
                tf.setStyle(baseStyle);
            }
        }
    }

    /** Crea o recrea los TextField del GridPane en base al modelo. */
    private void crearTablero() {
        gridTablero.getChildren().clear();
        cellMap.clear();

        int size = model.getSize();
        for (int fila = 0; fila < size; fila++) {
            for (int col = 0; col < size; col++) {
                TextField celda = new TextField();
                celda.setPrefSize(50, 50);
                celda.setFocusTraversable(true);
                celda.setText( model.getValor(fila, col) == 0 ? "" : String.valueOf(model.getValor(fila, col)) );

                if (model.esFija(fila, col)) {
                    celda.setEditable(false);
                    celda.setStyle("-fx-font-size: 18px; -fx-alignment: center; -fx-font-weight: bold; -fx-text-fill: blue;");
                } else {
                    celda.setEditable(true);
                    celda.setStyle("-fx-font-size: 18px; -fx-alignment: center; -fx-border-color: black; -fx-border-width: 0.5;");
                    configurarManejoTeclado(celda, fila, col);
                    configurarEventosMouse(celda, fila, col);
                }

                int bloqueFila = fila / 2;
                int bloqueCol = col / 3;
                if ((bloqueFila + bloqueCol) % 2 == 0) {
                    celda.setBackground(new Background(new BackgroundFill(
                            Color.rgb(215, 225, 255), new CornerRadii(5), Insets.EMPTY)));
                } else {
                    celda.setBackground(new Background(new BackgroundFill(
                            Color.WHITE, new CornerRadii(5), Insets.EMPTY)));
                }

                gridTablero.add(celda, col, fila);
                cellMap.put(key(fila, col), celda);
            }
        }

        gridTablero.setStyle("-fx-border-color: black; -fx-border-width: 2;");
        actualizarValidaciones();
    }

    /** Configura eventos de mouse para selección y hover. */
    private void configurarEventosMouse(TextField celda, int fila, int col) {
        // Evento de clic para seleccionar la celda
        celda.setOnMouseClicked(e -> {
            celda.requestFocus();
            celda.selectAll();
        });

        // Evento de hover para resaltar
        celda.setOnMouseEntered(e -> {
            if (celda.isEditable() && !celda.isFocused()) {
                celda.setStyle(celda.getStyle() + "; -fx-border-color: #3b82f6; -fx-border-width: 2;");
            }
        });

        celda.setOnMouseExited(e -> {
            if (celda.isEditable() && !celda.isFocused()) {
                // Restaurar estilo normal si no tiene sugerencia
                if (celda.getPromptText().isEmpty()) {
                    celda.setStyle("-fx-font-size: 18px; -fx-alignment: center; -fx-border-color: #374151; -fx-border-width: 1; -fx-background-color: white;");
                }
            }
        });
    }

    /** Configura manejo de BACK_SPACE/DELETE y entrada numérica 1..6. */
    private void configurarManejoTeclado(TextField celda, int fila, int col) {
        celda.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.BACK_SPACE || e.getCode() == KeyCode.DELETE) {
                // Eliminar número
                if (model.setValorSiEditable(fila, col, 0)) {
                    celda.clear();
                    celda.setPromptText("");
                    actualizarValidaciones();
                }
                e.consume();
            }
        });

        celda.addEventFilter(KeyEvent.KEY_TYPED, e -> {
            String ch = e.getCharacter();
            if (ch == null || ch.isEmpty()) return;

            if (ch.matches("[1-6]")) {
                int valor = Integer.parseInt(ch);
                if (model.esMovimientoValido(fila, col, valor)) {
                    if (model.setValorSiEditable(fila, col, valor)) {
                        celda.setText(ch);
                        celda.positionCaret(1);
                        celda.setPromptText("");
                        actualizarValidaciones();
                        verificarVictoria();
                    }
                } else {
                    // Mostrar borde rojo temporal y alerta breve
                    celda.setStyle(celda.getStyle() + "; -fx-border-color: red; -fx-border-width: 2;");
                }
                e.consume();
            } else if (!ch.equals("\t") && !ch.equals("\r") && !ch.equals("\n")) {
                // Bloquear cualquier otra entrada distinta 1-6
                e.consume();
            }
        });
    }

    /** Aplica estilos de error a celdas en conflicto según el modelo. */
    private void actualizarValidaciones() {
        Set<Integer> invalid = model.indicesInvalidos();
        int size = model.getSize();
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                TextField tf = cellMap.get(key(r, c));
                if (tf == null) continue;
                boolean isInvalid = invalid.contains(r * size + c);

                if (model.esFija(r, c)) {
                    // Estilo para celdas fijas (azul como antes)
                    String fixedStyle = "-fx-font-size: 18px; -fx-alignment: center; -fx-font-weight: bold; -fx-text-fill: blue;";
                    tf.setStyle(fixedStyle);
                } else {
                    // Estilo base para celdas editables
                    String baseStyle = "-fx-font-size: 18px; -fx-alignment: center; -fx-border-color: black; -fx-border-width: 0.5;";

                    if (isInvalid) {
                        // Resaltar errores con fondo rojizo
                        tf.setStyle(baseStyle + "; -fx-background-color: #ffd6d6;");
                    } else if (!tf.getPromptText().isEmpty()) {
                        // Mantener estilo de sugerencia
                        tf.setStyle(baseStyle + "; -fx-border-color: #00aa88; -fx-border-width: 2; -fx-background-color: #e8f5e8;");
                    } else {
                        // Estilo normal
                        tf.setStyle(baseStyle);
                    }
                }
            }
        }
    }

    private String key(int r, int c) { return r + "," + c; }

    /** Actualiza el texto/estado del botón de ayuda con el contador restante. */
    private void actualizarTextoAyuda() {
        if (btnAyuda != null) {
            btnAyuda.setText("Ayuda (" + ayudasRestantes + ")");
            btnAyuda.setDisable(ayudasRestantes <= 0);
        }
    }

    /** Indica si no quedan celdas vacías. */
    private boolean tableroCompleto() {
        int size = model.getSize();
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (model.getValor(r, c) == 0) return false;
            }
        }
        return true;
    }

    /** Muestra alerta de victoria si el tablero está completo y sin conflictos. */
    private void verificarVictoria() {
        if (tableroCompleto() && model.indicesInvalidos().isEmpty()) {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("¡Ganaste!");
            a.setHeaderText("¡Ganaste!");
            a.setContentText("Has completado correctamente el Sudoku 6x6.");
            a.showAndWait();
        }
    }
}
