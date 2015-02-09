package edu.wdaniels.lg.proj1.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * (c) Magnetic Variation Services, LLC. 2014.
 *
 * @author rchirumamilla
 */
public class PopErrorController implements Initializable {

    @FXML
    private Label top_error_label;
    @FXML
    private Label bottom_error_label;
    @FXML
    private AnchorPane primary_anchor;

    /**
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (PopErrorWindow.getCustomErrorMessage().equals("")) {
            if (top_error_label != null) {
                top_error_label.setText("Selected input file is open");
            }
        } else {
            if (top_error_label != null) {
                top_error_label.setPrefWidth(PopErrorWindow.getCustomErrorMessage().length() * 8);
                primary_anchor.setPrefWidth(top_error_label.getPrefWidth());
                
                top_error_label.setText(PopErrorWindow.getCustomErrorMessage());
                if (bottom_error_label != null){
                    bottom_error_label.setText(PopErrorWindow.getCustomBottomErrorMessage());
                }
            }
            PopErrorWindow.setCustomErrorMessage("");
        }
    }
}
