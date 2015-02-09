package edu.wdaniels.lg.proj1.gui;

import edu.wdaniels.lg.proj1.abg.Piece;
import edu.wdaniels.lg.proj1.structures.Triple;
import java.io.IOException;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
        if (!showInvalidFields()){
            return;
        }
        Piece newPiece = new Piece(new Triple(Integer.valueOf(tf_piece_x_location.getText()),
                Integer.valueOf(tf_piece_y_location.getText()), Integer.valueOf(tf_piece_z_location.getText())),
                ta_reachability_equation.getText(), tf_piece_name.getText());
        PrimaryController.getController().addNewPiece(newPiece);
        
    }
    
        private void displayErrorAndStyle(Node node, String errorMessage, final StringProperty textProperty) {
        PopErrorWindow error = new PopErrorWindow();
        PopErrorWindow.setCustomErrorMessage(errorMessage);
        try {
            error.PopErrorWindowShower(node.localToScene(node.getBoundsInLocal()).getMaxX() + 5, node.localToScene(node.getBoundsInLocal()).getMaxY(), PrimaryController.getController().getAddPieceStage());
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
        node.getStyleClass().removeAll("good", "bad");
        node.getStyleClass().add("bad");
        final ChangeListener change = (ChangeListener) (ObservableValue observable, Object oldValue, Object newValue) -> {
            error.get_popup().hide();
        };
        textProperty.addListener((ChangeListener) -> {
            error.get_popup().hide();
        });
        error.get_popup().setOnHidden((EventHandler) -> {
            textProperty.removeListener(change);
        });

    }
    /**
     * This method goes through the entire pane and finds which fields are
     * invalid and displays a reasonable message accordingly to the user, in the
     * form of an error popup. Unfortunately, there is no perfectly generic way
     * to handle this method. I pretty much just have to go through every field
     * and see if any of them are empty. We only have to do empty checks since
     * the fields themselves handle the actual validation of proper input, and
     * once they have text ever they can check for emptiness, but if they never
     * get any input it won't work.
     *
     * @return false if any fields were invalid, true otherwise.
     */
    public boolean showInvalidFields() {
        boolean wasValid = true;
        if (tf_piece_name.getText().isEmpty()) {
            displayErrorAndStyle(tf_piece_name, "Piece Name Required", tf_piece_name.textProperty());
            wasValid = false;
        } else if (tf_piece_name.getStyleClass().contains("bad")) {
            wasValid = false;
        }
        if (tf_piece_x_location.getText().isEmpty()) {
            displayErrorAndStyle(tf_piece_x_location, "X Location Required", tf_piece_x_location.textProperty());
            wasValid = false;
        } else if (tf_piece_x_location.getStyleClass().contains("bad")) {
            wasValid = false;
        }
        if (tf_piece_y_location.getText().isEmpty()) {
            displayErrorAndStyle(tf_piece_y_location, "Y Location Required", tf_piece_y_location.textProperty());
            wasValid = false;
        } else if (tf_piece_y_location.getStyleClass().contains("bad")) {
            wasValid = false;
        }
        if (tf_piece_z_location.getText().isEmpty()) {
            displayErrorAndStyle(tf_piece_z_location, "Z Location Required", tf_piece_z_location.textProperty());
            wasValid = false;
        } else if (tf_piece_z_location.getStyleClass().contains("bad")) {
            wasValid = false;
        }
        return wasValid;
    }
}
