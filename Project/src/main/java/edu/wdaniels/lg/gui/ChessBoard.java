package edu.wdaniels.lg.gui;

import edu.wdaniels.lg.structures.Pair;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
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
    private int[][] numList;

    public ChessBoard(int size) {
        this.size = size;
        numList = new int[size][size];
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
        System.out.println("GridFille.length: " + gridFill.length);
        GridPane root = new GridPane();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                StackPane square = new StackPane();
                square.setOpacity(opacity);
                Label label = new Label();
                if (gridFill != null) {
                    label.setStyle("-fx-text-fill: green;");
                    label.setText(String.valueOf(gridFill[row][col]));
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

    public int[][] getNumList(){
        return numList;
    }

    public GridPane getDynamicChessBoard(Pair<Integer, Integer> initialLocation, double opacity){
        GridPane root = new GridPane();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                StackPane square = new StackPane();
                square.setOpacity(opacity);
                Label label = new Label();
                label.setStyle("-fx-text-fill: green;");
                if (col == initialLocation.getFirst() && row == initialLocation.getSecond()){
                    label.setText(String.valueOf(0));
                }else{
                    label.setText(String.valueOf(-1));
                }
                label.setId(row + "," + col);
                label.setOnMouseClicked((MouseEvent event) -> {
                    System.out.println("event type: " + event.getButton());
                    if (event.getButton().toString().toLowerCase().contains("primary")){
                        label.setText(String.valueOf(Integer.valueOf(label.getText()) + 1));
                    }else if (event.getButton().toString().toLowerCase().contains("secondary")){
                        label.setText(String.valueOf(Integer.valueOf(label.getText()) - 1));
                    }else{
                         label.setText(String.valueOf(Integer.valueOf(label.getText()) + 1));
                    }
                    String[] items = label.getId().split(",");
                    numList[Integer.valueOf(items[0])][Integer.valueOf(items[1])] = Integer.valueOf(label.getText());

                });
                square.getChildren().add(label);

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
