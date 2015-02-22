package edu.wdaniels.lg.gui;

import edu.wdaniels.lg.abg.Piece;
import edu.wdaniels.lg.structures.Triple;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 *
 * @author William
 */
public class DisplayTrajBoard {

    private static DisplayTrajBoard controller;

    @FXML
    private StackPane pane_main_grid;

    @FXML
    public void initialize() {
        controller = this;
        displayPieceMap((Piece) PrimaryController.getController().lv_traj_starting.getSelectionModel().getSelectedItem());
    }

    public void displayPieceMap(Piece piece) {
        int size = PrimaryController.getController().getBoardSize();
        ChessBoard cb2 = null;
        if (size == 8 && PrimaryController.getController().is2D && PrimaryController.getController().obstacleList.isEmpty()) {
            size = 15;
            cb2 = new ChessBoard(8);
        }
        List<Circle> circleList = new ArrayList<>();
        Circle temp;
        for (int j = 0; j < PrimaryController.getController().trajectoryList.size(); j++) {
            for (Triple location : PrimaryController.getController().trajectoryList.get(j)) {
                temp = new Circle(3);
                temp.setLayoutX(((Integer) location.getThird() * 40) + 20);
                temp.setLayoutY(((Integer) location.getSecond() * 40) + 20);
                temp.setFill(Color.RED);
                circleList.add(temp);
            }
        }
        circleList.remove(circleList.size() - 1);
        List<Line> lineList = new ArrayList<>();
        for (int i = 0; i < circleList.size() - 1; i++) {
            Line tempLine = new Line();
            tempLine.setStrokeWidth(2);
            tempLine.setStroke(Color.GREEN);
            tempLine.setStartX(circleList.get(i).getLayoutX());
            tempLine.setStartY(circleList.get(i).getLayoutY());
            tempLine.setEndX(circleList.get(i + 1).getLayoutX());
            tempLine.setEndY(circleList.get(i + 1).getLayoutY());
            lineList.add(tempLine);
        }
        ChessBoard cb = new ChessBoard(size);
        GridPane gp = cb.getChessBoard(piece.getReachabilityTwoDMap(), 1);
        GridPane gp2 = null;
        AnchorPane pane1 = new AnchorPane();
        if (cb2 != null) {
            gp2 = cb2.getChessBoard(null, .3);
            gp2.setLayoutY((7 - piece.getLocation().getSecond()) * 40);
            gp2.setLayoutX(piece.getLocation().getFirst() * 40);
            pane1.getChildren().add(gp2);

        }

        pane_main_grid.getChildren().addAll(gp);
        if (gp2 != null) {
            pane_main_grid.getChildren().add(pane1);
        }
        AnchorPane ap = new AnchorPane();
        for (Circle circle : circleList) {
            ap.getChildren().add(circle);
        }
        for (Line line : lineList) {
            System.out.println("Adding a line...");
            ap.getChildren().add(line);
        }
        pane_main_grid.getChildren().add(ap);

    }

    public static DisplayTrajBoard getController() {
        return controller;
    }
}
