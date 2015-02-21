package edu.wdaniels.lg.proj1.gui;

import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;

/**
 * This class generates a 'chess board' using a grid pane with javafx
 *
 * @author William
 */
public class ChessBoard {

    private final int size;

    public ChessBoard(int size) {
        this.size = size;
    }

    /**
     * This actually generates the chess board itself.
     *
     * @param gridFill This is an optional parameter, it allows you to 'fill'
     * the grid with ints
     * @param opacity THis allows us to set the opacity of the squres, useful
     * for overlay
     *
     * @return The grid pane 'chess board' object.
     */
    public GridPane getChessBoard(int[][] gridFill, double opacity) {
        GridPane root = new GridPane();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                StackPane square = new StackPane();
                square.setOpacity(opacity);
                Label label = new Label();
                if (gridFill != null) {
                    label.setStyle("-fx-text-fill: green;");
                    label.setText(String.valueOf(gridFill[col][row] - 1));
                    square.getChildren().add(label);
                }
                String color;
                if ((row + col) % 2 == 0) {
                    if (opacity < 1) {
                        color = "blue";
                    } else {
                        color = "white";
                    }
                } else {
                    if (opacity < 1) {
                        color = "gray";
                    } else {
                        color = "black";
                    }
                }
                square.setStyle("-fx-background-color: " + color + ";");

                root.add(square, col, row);
            }
        }
        for (int i = 0; i < size; i++) {
            root.getColumnConstraints().add(new ColumnConstraints(40));
            root.getRowConstraints().add(new RowConstraints(40));
        }
        return root;
    }

}
