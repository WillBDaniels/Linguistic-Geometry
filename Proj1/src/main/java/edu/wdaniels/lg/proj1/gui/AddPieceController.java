package edu.wdaniels.lg.proj1.gui;

import edu.wdaniels.lg.proj1.abg.Piece;
import edu.wdaniels.lg.proj1.structures.Triple;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 *
 * @author William
 */
public class AddPieceController {
    
    @FXML
    private TextField tf_piece_name, tf_piece_x_location, tf_piece_y_location, tf_piece_z_location; 
    @FXML
    private TextArea ta_reachability_equation;
    
    @FXML
    private void initialize(){}
    
    @FXML
    private void addNewPiece(){
        Piece newPiece = new Piece(new Triple(Integer.valueOf(tf_piece_x_location.getText()),
                Integer.valueOf(tf_piece_y_location.getText()), Integer.valueOf(tf_piece_z_location.getText())),
                ta_reachability_equation.getText(), tf_piece_name.getText());
        PrimaryController.getController().addNewPiece(newPiece);
        
    }
}
