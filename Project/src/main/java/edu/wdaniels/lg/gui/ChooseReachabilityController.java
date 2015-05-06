/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wdaniels.lg.gui;

import edu.wdaniels.lg.abg.Piece;
import edu.wdaniels.lg.structures.Pair;
import edu.wdaniels.lg.structures.Triple;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

/**
 *
 * @author William
 */
public class ChooseReachabilityController {
    private ChessBoard cb;
    private static ChooseReachabilityController controller;
    private List<Pair<Triple<Integer, Integer, Integer>, Image>> imageList = new ArrayList<>();

    @FXML
    private StackPane pane_main_grid;

    @FXML
    public void initialize() {
        controller = this;
        displayPieceMap((Piece) PrimaryController.getController().lv_traj_starting.getSelectionModel().getSelectedItem());
    }

    public void displayPieceMap(Piece piece) {
        int size = PrimaryController.getController().getBoardSize();

        cb = new ChessBoard(size);
        GridPane gp = cb.getDynamicChessBoard(AddPieceController.getController().getPiece2DLocation(), 1);

        pane_main_grid.getChildren().addAll(gp);


    }

    @FXML
    private void submitReachability(){
        if (cb != null){
            AddPieceController.getController().setReachabilityMap(cb.getNumList());
            AddPieceController.getController().getReachabilityStage().close();
        }
    }

    public static ChooseReachabilityController getController() {
        return controller;
    }
}


