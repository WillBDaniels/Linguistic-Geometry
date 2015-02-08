package edu.wdaniels.lg.proj1.gui;

import edu.wdaniels.lg.proj1.abg.Obstacle;
import edu.wdaniels.lg.proj1.structures.Triple;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 *
 * @author William
 */
public class ObstacleController {
    @FXML
    private TextField tf_obstacle_name, tf_x_location_obstacle, tf_y_location_obstacle, tf_z_location_obstacle;
    
    
    
    @FXML
    private void initialize(){}
    
    @FXML
    private void addNewObstacle(){
        Triple triple = new Triple(Integer.valueOf(tf_x_location_obstacle.getText()),
                Integer.valueOf(tf_y_location_obstacle.getText()), Integer.valueOf(tf_z_location_obstacle.getText()));
        Obstacle obs = new Obstacle(tf_obstacle_name.getText(), triple);
        PrimaryController.getController().addNewObstacle(obs);
    }
}
