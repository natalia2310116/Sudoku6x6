package org.example.sudoku6x6.Model;

/**
 * Representa una sugerencia de ayuda.
 * Contiene la posición de la celda (fila, columna) y un valor candidato válido
 * según el estado actual del tablero.
 */
public class Sugerencia {
    /** Fila destino (0..5). */
    public final int fila;
    /** Columna destino (0..5). */
    public final int columna;
    /** Valor sugerido (1..6). */
    public final int valor;

    /**
     * Crea una nueva sugerencia.
     * @param fila Fila de la celda
     * @param columna Columna de la celda
     * @param valor Valor candidato sugerido
     */
    public Sugerencia(int fila, int columna, int valor) {
        this.fila = fila;
        this.columna = columna;
        this.valor = valor;
    }
}