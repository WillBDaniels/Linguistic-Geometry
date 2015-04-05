package edu.wdaniels.lg.gui;

import edu.wdaniels.lg.abg.Piece;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

/**
 *
 * @author William
 */
public class Display2DController {

    private static Display2DController controller;

    @FXML
    private StackPane pane_main_grid;

    @FXML
    public void initialize() {
        controller = this;
        displayPieceMap((Piece) PrimaryController.getController().lv_distance_piece_list.getSelectionModel().getSelectedItem());
    }

    public void displayPieceMap(Piece piece) {
        int size = PrimaryController.getController().getBoardSize();
        ChessBoard cb2 = null;
        if (size == 8 && PrimaryController.getController().is2D && PrimaryController.getController().obstacleList.isEmpty()) {
            size = 15;
            cb2 = new ChessBoard(8);
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
        AnchorPane pane2 = new AnchorPane();
        ImageView temp = new ImageView(piece.getPieceImage());
        temp.setX((piece.getLocation().getFirst() * 40));
        temp.setY((piece.getLocation().getSecond()) * 40);
        temp.setPreserveRatio(false);
        temp.setFitWidth(40);
        temp.setFitHeight(40);
        pane2.getChildren().add(temp);
        pane_main_grid.getChildren().addAll(gp);
        if (gp2 != null) {
            pane_main_grid.getChildren().add(pane1);
        }
        pane_main_grid.getChildren().add(pane2);

    }

    public static Display2DController getController() {
        return controller;
    }
}
