package org.example.sudoku6x6.Model;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Clase que representa el modelo lógico del tablero de Sudoku 6x6.
 * Se encarga de generar y validar el tablero.
 * @author Natalia
 */
public class ModelTablero {

    private final int[][] tablero;
    private static final int TAM = 6;

    public ModelTablero() {
        tablero = new int[TAM][TAM];
    }

    /**
     * Genera un tablero inicial colocando 2 números aleatorios
     * en cada bloque 2x3, asegurando que no se repitan en filas, columnas o bloques.
     */
    public void generarTablero() {
        limpiarTablero();
        Random random = new Random();

        for (int bloqueFila = 0; bloqueFila < 3; bloqueFila++) {
            for (int bloqueCol = 0; bloqueCol < 2; bloqueCol++) {
                Set<Integer> usados = new HashSet<>();
                int celdasLlenas = 0;

                while (celdasLlenas < 2) {
                    int fila = bloqueFila * 2 + random.nextInt(2);
                    int col = bloqueCol * 3 + random.nextInt(3);
                    int valor = random.nextInt(6) + 1;

                    if (tablero[fila][col] == 0 && validarMovimiento(fila, col, valor, usados)) {
                        tablero[fila][col] = valor;
                        usados.add(valor);
                        celdasLlenas++;
                    }
                }
            }
        }
    }

    /** Valida que un valor no se repita en fila, columna o bloque */
    private boolean validarMovimiento(int fila, int col, int valor, Set<Integer> usados) {
        if (usados.contains(valor)) return false;

        for (int i = 0; i < TAM; i++) {
            if (tablero[fila][i] == valor || tablero[i][col] == valor) return false;
        }

        int bloqueFila = (fila / 2) * 2;
        int bloqueCol = (col / 3) * 3;
        for (int i = bloqueFila; i < bloqueFila + 2; i++) {
            for (int j = bloqueCol; j < bloqueCol + 3; j++) {
                if (tablero[i][j] == valor) return false;
            }
        }

        return true;
    }

    /** Limpia el tablero */
    public void limpiarTablero() {
        for (int i = 0; i < TAM; i++) {
            for (int j = 0; j < TAM; j++) {
                tablero[i][j] = 0;
            }
        }
    }

    public int[][] getTablero() {
        return tablero;
    }
}

