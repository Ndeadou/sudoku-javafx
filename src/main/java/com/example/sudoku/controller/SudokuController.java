package com.example.sudoku.controller;

import com.example.sudoku.model.SudokuGenerator;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class SudokuController {

    @FXML
    private GridPane gameGrid;

    @FXML
    private ComboBox<String> difficultyBox;

    private final TextField[][] cells = new TextField[6][6];

    @FXML
    public void initialize() {
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                TextField cell = new TextField();
                cell.setPrefWidth(40);
                cell.setPrefHeight(40);
                aplicarEstiloCelda(cell, row, col, false, false);
                final int r = row;
                final int c = col;

                cell.setOnKeyReleased(event -> {
                    validarEntrada(cell);
                    validarTablero();
                });

                cells[row][col] = cell;
                gameGrid.add(cell, col, row);
            }
        }

        if (difficultyBox != null) {
            difficultyBox.getItems().addAll("Fácil", "Medio", "Difícil");
            difficultyBox.setValue("Medio"); // valor por defecto
        }
    }

    private void aplicarEstiloCelda(TextField cell, int row, int col, boolean error, boolean esInicial) {
        String top = (row % 2 == 0) ? "2" : "1";
        String left = (col % 3 == 0) ? "2" : "1";
        String bottom = ((row + 1) % 2 == 0) ? "2" : "1";
        String right = ((col + 1) % 3 == 0) ? "2" : "1";

        String border = "-fx-border-color: " + (error ? "red" : "lightgray") + ";" +
                "-fx-border-width: " + top + " " + right + " " + bottom + " " + left + ";";

        String background = "";
        if (error) {
            background = "-fx-background-color: #ffe5e5;";
        } else if (esInicial) {
            background = "-fx-background-color: #eeeeee; -fx-font-weight: bold;";
        }

        cell.setStyle(border + background + "-fx-alignment: center; -fx-font-size: 14px;");
    }

    private void validarEntrada(TextField cell) {
        String text = cell.getText();
        if (!text.matches("[1-6]?")) {
            cell.setText("");
        }
    }

    private void validarTablero() {
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                boolean esInicial = cells[row][col].isDisabled();
                aplicarEstiloCelda(cells[row][col], row, col, false, esInicial);
            }
        }

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                String valor = cells[row][col].getText();
                if (!valor.matches("[1-6]")) continue;

                if (!esValidoEnFila(row, col, valor) ||
                        !esValidoEnColumna(row, col, valor) ||
                        !esValidoEnBloque(row, col, valor)) {
                    aplicarEstiloCelda(cells[row][col], row, col, true, false);
                }
            }
        }

        if (tableroCompletoYValido()) {
            mostrarMensajeVictoria();
        }
    }

    private boolean tableroCompletoYValido() {
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                String valor = cells[row][col].getText();
                if (!valor.matches("[1-6]")) return false;

                if (!esValidoEnFila(row, col, valor) ||
                        !esValidoEnColumna(row, col, valor) ||
                        !esValidoEnBloque(row, col, valor)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void mostrarMensajeVictoria() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("¡Felicidades!");
        alert.setHeaderText(null);
        alert.setContentText("🎉 ¡Has completado correctamente el Sudoku 6x6!");
        alert.showAndWait();
    }

    private boolean esValidoEnFila(int fila, int colActual, String valor) {
        for (int col = 0; col < 6; col++) {
            if (col != colActual && cells[fila][col].getText().equals(valor)) return false;
        }
        return true;
    }

    private boolean esValidoEnColumna(int filaActual, int col, String valor) {
        for (int fila = 0; fila < 6; fila++) {
            if (fila != filaActual && cells[fila][col].getText().equals(valor)) return false;
        }
        return true;
    }

    private boolean esValidoEnBloque(int fila, int col, String valor) {
        int bloqueInicioFila = (fila / 2) * 2;
        int bloqueInicioCol = (col / 3) * 3;
        for (int r = bloqueInicioFila; r < bloqueInicioFila + 2; r++) {
            for (int c = bloqueInicioCol; c < bloqueInicioCol + 3; c++) {
                if ((r != fila || c != col) && cells[r][c].getText().equals(valor)) return false;
            }
        }
        return true;
    }

    @FXML
    private void onHelpClick() {
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                if (cells[row][col].getText().isEmpty()) {
                    for (int num = 1; num <= 6; num++) {
                        String valor = String.valueOf(num);
                        if (esValidoEnFila(row, col, valor) &&
                                esValidoEnColumna(row, col, valor) &&
                                esValidoEnBloque(row, col, valor)) {

                            cells[row][col].setText(valor);
                            aplicarEstiloCelda(cells[row][col], row, col, false, false);
                            cells[row][col].setStyle(cells[row][col].getStyle() +
                                    "-fx-background-color: #d1ffd1;");
                            return;
                        }
                    }
                }
            }
        }
    }

    @FXML
    private void onNewGameClick() {
        limpiarTablero();

        String dificultad = (difficultyBox != null && difficultyBox.getValue() != null)
                ? difficultyBox.getValue()
                : "Medio";

        int celdasOcultas = switch (dificultad) {
            case "Fácil" -> 10;
            case "Medio" -> 14;
            case "Difícil" -> 18;
            default -> 14;
        };

        int[][] generado = SudokuGenerator.generarTablero(celdasOcultas);

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                int valor = generado[row][col];
                TextField cell = cells[row][col];
                if (valor != 0) {
                    cell.setText(String.valueOf(valor));
                    cell.setDisable(true);
                    aplicarEstiloCelda(cell, row, col, false, true);
                } else {
                    cell.setText("");
                    cell.setDisable(false);
                    aplicarEstiloCelda(cell, row, col, false, false);
                }
            }
        }

        validarTablero();
    }

    @FXML
    private void onInstructionsClick() {
        String mensaje = """
        🎯 Objetivo:
        Completa el tablero 6x6 con números del 1 al 6 de manera que:
        - Cada fila contenga los números del 1 al 6 sin repetir.
        - Cada columna contenga los números del 1 al 6 sin repetir.
        - Cada bloque de 2x3 también contenga los números del 1 al 6 sin repetir.

        🎮 Cómo jugar:
        - Haz clic en una celda vacía e ingresa un número entre 1 y 6.
        - El sistema marcará en rojo los errores de forma automática.
        - Puedes eliminar números con la tecla retroceso o supr.

        🧠 Botón de ayuda:
        - Sugiere un posible número válido para una celda vacía.
        - Solo se muestra si existe una sugerencia que no viole las reglas.

        ¡Buena suerte y diviértete! 🔢
        """;

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Instrucciones");
        alert.setHeaderText("Cómo jugar al Sudoku 6x6");
        alert.setContentText(mensaje);
        alert.getDialogPane().setPrefWidth(480);
        alert.showAndWait();
    }

    private void limpiarTablero() {
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                cells[row][col].setText("");
                cells[row][col].setDisable(false);
                aplicarEstiloCelda(cells[row][col], row, col, false, false);
            }
        }
    }
}
