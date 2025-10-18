package org.example.sudoku6x6.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase adaptadora que convierte el tablero ArrayList a formato de matriz
 * para facilitar la integración con componentes que esperan matrices.
 * Implementa el patrón Adapter para adaptar la interfaz ArrayList a matriz.
 */
public class AdaptadorTablero {

    private final Tablero tablero;

    /**
     * Constructor que recibe un tablero para adaptar.
     * @param tablero El tablero a adaptar
     */
    public AdaptadorTablero(Tablero tablero) {
        this.tablero = tablero;
    }

    /**
     * Convierte el tablero ArrayList a una matriz 6x6.
     * @return Matriz bidimensional representando el tablero
     */
    public int[][] toMatrix() {
        int[][] matrix = new int[6][6];
        for (int fila = 0; fila < 6; fila++) {
            for (int columna = 0; columna < 6; columna++) {
                matrix[fila][columna] = tablero.getValor(fila, columna);
            }
        }
        return matrix;
    }

    /**
     * Convierte una matriz 6x6 al formato ArrayList del tablero.
     * @param matrix Matriz bidimensional a convertir
     */
    public void fromMatrix(int[][] matrix) {
        for (int fila = 0; fila < 6; fila++) {
            for (int columna = 0; columna < 6; columna++) {
                tablero.setValorSiEditable(fila, columna, matrix[fila][columna]);
            }
        }
    }

    /**
     * Obtiene una fila específica del tablero como lista.
     * @param fila Índice de la fila (0-5)
     * @return Lista con los valores de la fila
     */
    public List<Integer> getFila(int fila) {
        List<Integer> filaList = new ArrayList<>();
        for (int columna = 0; columna < 6; columna++) {
            filaList.add(tablero.getValor(fila, columna));
        }
        return filaList;
    }

    /**
     * Obtiene una columna específica del tablero como lista.
     * @param columna Índice de la columna (0-5)
     * @return Lista con los valores de la columna
     */
    public List<Integer> getColumna(int columna) {
        List<Integer> columnaList = new ArrayList<>();
        for (int fila = 0; fila < 6; fila++) {
            columnaList.add(tablero.getValor(fila, columna));
        }
        return columnaList;
    }

    /**
     * Obtiene un bloque 2x3 específico del tablero como lista.
     * @param bloqueFila Índice del bloque en filas (0-2)
     * @param bloqueCol Índice del bloque en columnas (0-1)
     * @return Lista con los valores del bloque
     */
    public List<Integer> getBloque(int bloqueFila, int bloqueCol) {
        List<Integer> bloqueList = new ArrayList<>();
        int startFila = bloqueFila * 2;
        int startCol = bloqueCol * 3;

        for (int fila = startFila; fila < startFila + 2; fila++) {
            for (int col = startCol; col < startCol + 3; col++) {
                bloqueList.add(tablero.getValor(fila, col));
            }
        }
        return bloqueList;
    }

    /**
     * Verifica si el tablero está completo (sin celdas vacías).
     * @return true si está completo, false en caso contrario
     */
    public boolean estaCompleto() {
        for (int fila = 0; fila < 6; fila++) {
            for (int columna = 0; columna < 6; columna++) {
                if (tablero.getValor(fila, columna) == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Cuenta el número de celdas vacías en el tablero.
     * @return Número de celdas vacías
     */
    public int contarCeldasVacias() {
        int vacias = 0;
        for (int fila = 0; fila < 6; fila++) {
            for (int columna = 0; columna < 6; columna++) {
                if (tablero.getValor(fila, columna) == 0) {
                    vacias++;
                }
            }
        }
        return vacias;
    }
}
