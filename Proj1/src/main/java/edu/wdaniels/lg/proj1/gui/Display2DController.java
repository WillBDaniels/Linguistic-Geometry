package edu.wdaniels.lg.proj1.gui;

import edu.wdaniels.lg.proj1.abg.Piece;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

/**
 *
 * @author William
 */
public class Display2DController {
    private static Display2DController controller;
    
    
    @FXML
    private TextArea ta_display_2d;
    
    @FXML
    public void initialize(){
        controller = this;
        displayPieceMap((Piece)PrimaryController.getController().lv_distance_piece_list.getSelectionModel().getSelectedItem());
    }
    
    
    public void displayPieceMap(Piece piece){
        ta_display_2d.setText(piece.printBoard());
        System.out.println("Should have set the value...");
    }
    
    public static Display2DController getController(){
        return controller;
    }
}
