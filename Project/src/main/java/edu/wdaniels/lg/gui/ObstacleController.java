package edu.wdaniels.lg.gui;

import edu.wdaniels.lg.abg.Obstacle;
import edu.wdaniels.lg.structures.Triple;
import java.io.IOException;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;

/**
 *
 * @author William
 */
public class ObstacleController {

    @FXML
    private TextField tf_obstacle_name, tf_x_location_obstacle, tf_y_location_obstacle, tf_z_location_obstacle;

    @FXML
    private void initialize() {
    }

    @FXML
    private void addNewObstacle() {
        Triple triple = new Triple(Integer.valueOf(tf_x_location_obstacle.getText()),
                Integer.valueOf(tf_y_location_obstacle.getText()), Integer.valueOf(tf_z_location_obstacle.getText()));
        Obstacle obs = new Obstacle(tf_obstacle_name.getText(), triple);
        PrimaryController.getController().addNewObstacle(obs);
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
        if (tf_obstacle_name.getText().isEmpty()) {
            displayErrorAndStyle(tf_obstacle_name, "Obstacle Name Required", tf_obstacle_name.textProperty());
            wasValid = false;
        } else if (tf_obstacle_name.getStyleClass().contains("bad")) {
            wasValid = false;
        }
        if (tf_x_location_obstacle.getText().isEmpty()) {
            displayErrorAndStyle(tf_x_location_obstacle, "X Location Required", tf_x_location_obstacle.textProperty());
            wasValid = false;
        } else if (tf_x_location_obstacle.getStyleClass().contains("bad")) {
            wasValid = false;
        }
        if (tf_y_location_obstacle.getText().isEmpty()) {
            displayErrorAndStyle(tf_y_location_obstacle, "Y Location Required", tf_y_location_obstacle.textProperty());
            wasValid = false;
        } else if (tf_y_location_obstacle.getStyleClass().contains("bad")) {
            wasValid = false;
        }
        if (tf_z_location_obstacle.getText().isEmpty()) {
            displayErrorAndStyle(tf_z_location_obstacle, "Z Location Required", tf_z_location_obstacle.textProperty());
            wasValid = false;
        } else if (tf_z_location_obstacle.getStyleClass().contains("bad")) {
            wasValid = false;
        }
        return wasValid;
    }
}
