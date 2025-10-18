package org.example.sudoku6x6.Model;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

/**
 * Interfaz del tablero de Sudoku 6x6 basada en ArrayList.
 * Define operaciones de generación, validación y ayuda.
 */
public interface Tablero {
    /** Genera un nuevo tablero resoluble y deja 2 pistas por bloque 2x3. */
    void generarTablero();
    /** Limpia el tablero (pone 0 en todas las celdas). */
    void limpiarTablero();
    /** Tamaño del tablero (6). */
    int getSize();
    /** Obtiene el valor en una celda (0 si está vacía). */
    int getValor(int fila, int columna);
    /**
     * Intenta asignar un valor en una celda si no es fija.
     * @return true si se pudo asignar
     */
    boolean setValorSiEditable(int fila, int columna, int valor);
    /** Verifica si un valor respeta fila, columna y bloque. */
    boolean esMovimientoValido(int fila, int columna, int valor);
    /** Devuelve índices lineales (r*6+c) que están en conflicto. */
    Set<Integer> indicesInvalidos();
    /** Sugerencia para la primera celda vacía disponible. */
    Optional<org.example.sudoku6x6.Model.Sugerencia> sugerir();
    /** Sugerencia para una celda específica. */
    Optional<org.example.sudoku6x6.Model.Sugerencia> sugerirPara(int fila, int columna);
    /** Indica si la celda fue una pista inicial (no editable). */
    boolean esFija(int fila, int columna);
    /** Copia de la lista lineal del tablero. */
    ArrayList<Integer> obtenerLista();
}