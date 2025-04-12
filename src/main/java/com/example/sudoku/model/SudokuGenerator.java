/**
 * Generador de tableros válidos de Sudoku 6x6.
 * Permite crear tableros completos, válidos y con celdas ocultas para jugar.
 *
 * Desarrollado por:
 * Miguel Ángel Descance - Código de estudiante: 2416788
 * Erick Andrey Obando Narvaez - Código de estudiante: 2415751
 */

package com.example.sudoku.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SudokuGenerator {

    /** Tamaño total del tablero (6x6). */
    private static final int SIZE = 6;

    /** Cantidad de filas por bloque (2x3). */
    private static final int BLOCK_ROWS = 2;

    /** Cantidad de columnas por bloque (2x3). */
    private static final int BLOCK_COLS = 3;

    /** Objeto para generación aleatoria de números. */
    private static final Random random = new Random();

    /**
     * Genera un tablero completo y válido de Sudoku, ocultando ciertas celdas.
     *
     * @param cantidadOculta Número de celdas que se deben ocultar.
     * @return Un arreglo de 6x6 con celdas llenas y otras vacías (0), o null si falla.
     */
    public static int[][] generarTablero(int cantidadOculta) {
        int[][] tablero = new int[SIZE][SIZE];
        if (llenarTablero(tablero, 0, 0)) {
            ocultarCeldas(tablero, cantidadOculta);
            return tablero;
        }
        return null;
    }

    /**
     * Intenta llenar todo el tablero recursivamente respetando las reglas del Sudoku.
     *
     * @param board Tablero a llenar.
     * @param row   Fila actual.
     * @param col   Columna actual.
     * @return true si se completó el tablero exitosamente.
     */
    private static boolean llenarTablero(int[][] board, int row, int col) {
        if (row == SIZE) return true;

        int nextRow = (col == SIZE - 1) ? row + 1 : row;
        int nextCol = (col + 1) % SIZE;

        List<Integer> numeros = new ArrayList<>();
        for (int i = 1; i <= SIZE; i++) numeros.add(i);
        Collections.shuffle(numeros, random);

        for (int num : numeros) {
            if (esSeguro(board, row, col, num)) {
                board[row][col] = num;
                if (llenarTablero(board, nextRow, nextCol)) return true;
                board[row][col] = 0; // backtracking
            }
        }
        return false;
    }

    /**
     * Verifica si se puede colocar un número en una celda sin romper las reglas.
     *
     * @param board Tablero actual.
     * @param row   Fila a verificar.
     * @param col   Columna a verificar.
     * @param num   Número que se quiere colocar.
     * @return true si el número puede colocarse sin repetir en fila, columna ni bloque.
     */
    private static boolean esSeguro(int[][] board, int row, int col, int num) {
        for (int i = 0; i < SIZE; i++) {
            if (board[row][i] == num || board[i][col] == num) return false;
        }

        int startRow = (row / BLOCK_ROWS) * BLOCK_ROWS;
        int startCol = (col / BLOCK_COLS) * BLOCK_COLS;

        for (int i = startRow; i < startRow + BLOCK_ROWS; i++) {
            for (int j = startCol; j < startCol + BLOCK_COLS; j++) {
                if (board[i][j] == num) return false;
            }
        }

        return true;
    }

    /**
     * Oculta una cantidad de celdas aleatorias del tablero (las convierte en ceros).
     *
     * @param board    Tablero generado previamente.
     * @param cantidad Número de celdas a ocultar.
     */
    private static void ocultarCeldas(int[][] board, int cantidad) {
        int ocultadas = 0;
        while (ocultadas < cantidad) {
            int row = random.nextInt(SIZE);
            int col = random.nextInt(SIZE);
            if (board[row][col] != 0) {
                board[row][col] = 0;
                ocultadas++;
            }
        }
    }
}
