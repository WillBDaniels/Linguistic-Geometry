package edu.wdaniels.lg.gui;

import edu.wdaniels.lg.structures.Triple;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

/**
 *
 * @author William
 */
public class Display3DTrajController {

    @FXML
    private TextArea lbl_3dTraj_main;

    @FXML
    private void initialize() {
        int i = 1;
        for (List<Triple<Integer, Integer, Integer>> list : PrimaryController.getController().trajectoryList) {
            lbl_3dTraj_main.setText(lbl_3dTraj_main.getText() + " Trajectory " + i + ": ");
            list.remove(list.size() - 1);
            for (Triple<Integer, Integer, Integer> item : list) {
                lbl_3dTraj_main.setText(lbl_3dTraj_main.getText() + ("( " + item.getThird() + " , " + item.getSecond() + " , " + item.getFirst() + " )") + " , ");
            }
            lbl_3dTraj_main.setText(lbl_3dTraj_main.getText() + "\n");
            i++;
        }
    }
}
