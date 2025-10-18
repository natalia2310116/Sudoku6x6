package org.example.sudoku6x6.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

/**
 * Modelo del Sudoku 6x6 basado en ArrayList (no matriz).
 *
 * Responsabilidades:
 * - Generar una solución completa por backtracking y tallar el rompecabezas con 2 pistas por bloque 2x3.
 * - Mantener celdas fijas (no editables) y estado actual del tablero.
 * - Validar reglas (fila/columna/bloque) y proporcionar sugerencias en base al estado actual.
 */
public class ModelTablero implements Tablero {

    private static final int SIZE = 6; // 6x6
    private static final int BLOCK_ROWS = 2;
    private static final int BLOCK_COLS = 3;

    // Representación lineal del tablero 6x6 (36 posiciones). 0 representa vacío
    private final ArrayList<Integer> board;
    // Índices fijos generados al iniciar el juego
    private final Set<Integer> fixedIndices;
    // Guarda una solución completa para poder asistir en ayudas dirigidas
    private ArrayList<Integer> solution;

    public ModelTablero() {
        this.board = new ArrayList<>(SIZE * SIZE);
        for (int i = 0; i < SIZE * SIZE; i++) board.add(0);
        this.fixedIndices = new HashSet<>();
        this.solution = new ArrayList<>(Collections.nCopies(SIZE * SIZE, 0));
    }

    @Override
    public void generarTablero() {
        limpiarTablero();
        fixedIndices.clear();
        // 1) Generar una solución completa válida por backtracking
        generarSolucionCompleta();
        // Copiar como solución de referencia
        this.solution = new ArrayList<>(board);

        // 2) "Tallado" del rompecabezas: mantener exactamente 2 celdas por bloque 2x3
        //    Esto garantiza que el rompecabezas es resoluble (al menos por la solución generada).
        Random rnd = new Random();
        Set<Integer> mantener = new HashSet<>();
        for (int br = 0; br < SIZE / BLOCK_ROWS; br++) {
            for (int bc = 0; bc < SIZE / BLOCK_COLS; bc++) {
                List<Integer> posiciones = new ArrayList<>();
                int startRow = br * BLOCK_ROWS;
                int startCol = bc * BLOCK_COLS;
                for (int r = startRow; r < startRow + BLOCK_ROWS; r++) {
                    for (int c = startCol; c < startCol + BLOCK_COLS; c++) {
                        posiciones.add(toIndex(r, c));
                    }
                }
                Collections.shuffle(posiciones, rnd);
                // Mantener las dos primeras
                mantener.add(posiciones.get(0));
                mantener.add(posiciones.get(1));
            }
        }

        // Vaciar el resto de celdas
        for (int i = 0; i < board.size(); i++) {
            if (!mantener.contains(i)) {
                board.set(i, 0);
            }
        }
        fixedIndices.addAll(mantener);
    }

    private boolean generarSolucionCompleta() {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < SIZE * SIZE; i++) indices.add(i);
        // Un poco de aleatoriedad para variedad
        Collections.shuffle(indices, new Random());
        return backtrack(0, indices);
    }

    private boolean backtrack(int k, List<Integer> order) {
        if (k == order.size()) return true;
        int idx = order.get(k);
        int fila = idx / SIZE;
        int col = idx % SIZE;
        if (board.get(idx) != 0) return backtrack(k + 1, order);

        List<Integer> candidatos = new ArrayList<>();
        for (int v = 1; v <= SIZE; v++) candidatos.add(v);
        Collections.shuffle(candidatos, new Random());
        for (int v : candidatos) {
            if (esMovimientoValido(fila, col, v)) {
                board.set(idx, v);
                if (backtrack(k + 1, order)) return true;
                board.set(idx, 0);
            }
        }
        return false;
    }

    @Override
    public void limpiarTablero() {
        for (int i = 0; i < board.size(); i++) board.set(i, 0);
    }

    @Override
    public int getSize() {
        return SIZE;
    }

    @Override
    /** Obtiene el valor almacenado en (fila, columna). */
    public int getValor(int fila, int columna) {
        return board.get(toIndex(fila, columna));
    }

    @Override
    /**
     * Intenta escribir un valor en una celda si no es fija.
     * @return true si se pudo escribir
     */
    public boolean setValorSiEditable(int fila, int columna, int valor) {
        int idx = toIndex(fila, columna);
        if (fixedIndices.contains(idx)) return false;
        board.set(idx, valor);
        return true;
    }

    @Override
    /**
     * Verifica que valor no viole fila/columna/bloque para la posición indicada.
     */
    public boolean esMovimientoValido(int fila, int columna, int valor) {
        if (valor < 1 || valor > SIZE) return false;
        // Verificar fila y columna
        for (int i = 0; i < SIZE; i++) {
            if (i != columna && getValor(fila, i) == valor) return false;
            if (i != fila && getValor(i, columna) == valor) return false;
        }
        // Verificar bloque 2x3
        int startRow = (fila / BLOCK_ROWS) * BLOCK_ROWS;
        int startCol = (columna / BLOCK_COLS) * BLOCK_COLS;
        for (int r = startRow; r < startRow + BLOCK_ROWS; r++) {
            for (int c = startCol; c < startCol + BLOCK_COLS; c++) {
                if ((r != fila || c != columna) && getValor(r, c) == valor) return false;
            }
        }
        return true;
    }

    @Override
    /** Devuelve los índices lineales en conflicto para resaltar en la UI. */
    public Set<Integer> indicesInvalidos() {
        Set<Integer> invalid = new HashSet<>();
        // Revisar filas
        for (int r = 0; r < SIZE; r++) {
            marcarRepetidosEnColeccion(invalid, r, true);
        }
        // Revisar columnas
        for (int c = 0; c < SIZE; c++) {
            marcarRepetidosEnColeccion(invalid, c, false);
        }
        // Revisar bloques
        for (int br = 0; br < SIZE / BLOCK_ROWS; br++) {
            for (int bc = 0; bc < SIZE / BLOCK_COLS; bc++) {
                marcarRepetidosEnBloque(invalid, br, bc);
            }
        }
        return invalid;
    }

    @Override
    public Optional<Sugerencia> sugerir() {
        // Recopilar todas las celdas vacías disponibles
        List<int[]> celdasVacias = new ArrayList<>();
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (board.get(toIndex(r, c)) == 0) {
                    celdasVacias.add(new int[]{r, c});
                }
            }
        }

        if (celdasVacias.isEmpty()) {
            return Optional.empty();
        }

        // Seleccionar aleatoriamente una celda vacía
        Random random = new Random();
        int[] celdaSeleccionada = celdasVacias.get(random.nextInt(celdasVacias.size()));
        int fila = celdaSeleccionada[0];
        int columna = celdaSeleccionada[1];

        // Buscar un valor válido para esa celda
        List<Integer> valoresValidos = new ArrayList<>();
        for (int v = 1; v <= SIZE; v++) {
            if (esMovimientoValido(fila, columna, v)) {
                valoresValidos.add(v);
            }
        }

        if (valoresValidos.isEmpty()) {
            return Optional.empty();
        }

        // Seleccionar aleatoriamente un valor válido
        int valorSugerido = valoresValidos.get(random.nextInt(valoresValidos.size()));
        return Optional.of(new Sugerencia(fila, columna, valorSugerido));
    }

    @Override
    public Optional<Sugerencia> sugerirPara(int fila, int columna) {
        int idx = toIndex(fila, columna);
        if (board.get(idx) != 0) return Optional.empty();
        // Calcular sugerencia válida a partir del estado actual
        for (int v = 1; v <= SIZE; v++) {
            if (esMovimientoValido(fila, columna, v)) {
                return Optional.of(new Sugerencia(fila, columna, v));
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean esFija(int fila, int columna) {
        return fixedIndices.contains(toIndex(fila, columna));
    }

    @Override
    public ArrayList<Integer> obtenerLista() {
        return new ArrayList<>(board);
    }

    private int toIndex(int fila, int columna) {
        return fila * SIZE + columna;
    }

    private void marcarRepetidosEnColeccion(Set<Integer> invalid, int fixed, boolean isRow) {
        int[] counts = new int[SIZE + 1];
        for (int i = 0; i < SIZE; i++) {
            int value = isRow ? getValor(fixed, i) : getValor(i, fixed);
            if (value != 0) counts[value]++;
        }
        for (int i = 0; i < SIZE; i++) {
            int r = isRow ? fixed : i;
            int c = isRow ? i : fixed;
            int value = getValor(r, c);
            if (value != 0 && counts[value] > 1) invalid.add(toIndex(r, c));
        }
    }

    private void marcarRepetidosEnBloque(Set<Integer> invalid, int blockRow, int blockCol) {
        int[] counts = new int[SIZE + 1];
        int startRow = blockRow * BLOCK_ROWS;
        int startCol = blockCol * BLOCK_COLS;
        for (int r = startRow; r < startRow + BLOCK_ROWS; r++) {
            for (int c = startCol; c < startCol + BLOCK_COLS; c++) {
                int val = getValor(r, c);
                if (val != 0) counts[val]++;
            }
        }
        for (int r = startRow; r < startRow + BLOCK_ROWS; r++) {
            for (int c = startCol; c < startCol + BLOCK_COLS; c++) {
                int val = getValor(r, c);
                if (val != 0 && counts[val] > 1) invalid.add(toIndex(r, c));
            }
        }
    }
}

