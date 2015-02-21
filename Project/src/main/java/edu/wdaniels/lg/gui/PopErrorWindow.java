package edu.wdaniels.lg.gui;

import edu.wdaniels.lg.proj1.Main;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Popup;
import javafx.stage.Stage;

/**
 * (c) Magnetic Variation Services, LLC. 2014.
 *
 * @author rchirumamilla
 */
public class PopErrorWindow {

    private static String customError = "";
    private static String customBottomError = "";
    private final Popup pop;
    /**
     * This is the default public constructor for PopErrorWindow.
     */
    public PopErrorWindow() {
        pop = new Popup();
    }

    /**
     * Shows the error message on the X and Y coordinate received from arguments
     *
     * @param x
     * @param y
     * @throws IOException
     */
    public void PopErrorWindowShower(double x, double y) throws IOException {

        Parent root3 = FXMLLoader.load(getClass().getResource("../fxml/ErrorPopInvalidEntry.fxml"));
        root3.getStylesheets().add(getClass().getResource("../css/Primary.css").toExternalForm());
        
        pop.setAutoHide(true);
        pop.setAutoFix(true);
        pop.setHideOnEscape(true);
        pop.getContent().add(root3);
        pop.centerOnScreen();
        pop.setX(Main.get_stage().getX() + x);
        pop.setY(Main.get_stage().getY() + y);
        pop.show(Main.get_stage());
    }

    /**
     * this method is for setting any sort of custom error message when dealing
     * with anything. Ideally any user of this will clear out the error message
     * upon consumption, unless it is to be immediately reused, otherwise
     * problems could persist with it being static.
     *
     * @param newError the new error message you want to set.
     */
    public static void setCustomErrorMessage(String newError) {
        customError = newError;
    }

    /**
     * this method is for setting any sort of custom error message when dealing
     * with anything. Ideally any user of this will clear out the error message
     * upon consumption, unless it is to be immediately reused, otherwise
     * problems could persist with it being static.
     *
     * @param newError the new error message you want to set.
     * @param bottomError
     */
    public static void setCustomErrorMessage(String newError, String bottomError) {
        customError = newError;
        customBottomError = bottomError;
        
    }

    /**
     * This method returns whatever custom error message is present at the time.
     * The default mesage is an EMPTY string, not a NULL string, so careful with
     * that.
     *
     * @return The current custom error message being stored in our static
 variable customError.
     */
    public static String getCustomErrorMessage() {
        return customError;
        
    }

    /**
     * This method returns whatever custom error message is present at the time.
     * The default mesage is an EMPTY string, not a NULL string, so careful with
     * that.
     *
     * @return The current custom error message being stored in our static
 variable customError.
     */
    public static String getCustomBottomErrorMessage() {
        return customBottomError;
    }

    /**
     * This method displays the FXML sent based on the X and Y coordinate With
     * this method one can display any FXMl at any coordinates. This method is
     * used to display error messages at a specific text field
     *
     * @param x
     * @param y
     * @param scene_fxml
     * @throws IOException
     */
    public void PopErrorWindowShower(double x, double y, String scene_fxml) throws IOException {

        Parent root3 = FXMLLoader.load(getClass().getResource(scene_fxml));
        root3.getStylesheets().add(getClass().getResource("../css/Primary.css").toExternalForm());

        pop.setAutoHide(true);
        pop.setAutoFix(true);
        pop.setHideOnEscape(true);
        pop.getContent().add(root3);
        pop.centerOnScreen();
        pop.setX(Main.get_stage().getX() + x);
        pop.setY(Main.get_stage().getY() + y);
        pop.show(Main.get_stage());

    }

    /**
     * This method is for creating an arbitrary pop window with a given stage
     * and a given location. This is nice becasue it doesn't require fxml to be
     * defined, it simply uses the default defined for our error popups.
     *
     * @param x The x location within the given stage you want the popup at.
     * @param y The y location with the given stage you want the popup at.
     * @param stage The stage inside of which the popup will be nested.
     * @throws IOException If there was a problem finding the default css or
     * fxml.
     */
    public void PopErrorWindowShower(double x, double y, Stage stage) throws IOException {
        Parent root3 = FXMLLoader.load(getClass().getResource("../fxml/ErrorPopInvalidEntry.fxml"));
        root3.getStylesheets().add(getClass().getResource("../css/Primary.css").toExternalForm());

        pop.setAutoHide(true);
        pop.setAutoFix(true);
        pop.setHideOnEscape(true);
        pop.getContent().add(root3);
        pop.centerOnScreen();
        pop.setX(stage.getX() + x);
        pop.setY(stage.getY() + y);
        pop.show(stage);
    }

    /**
     * This method displays the FXML sent based on the X and Y coordinate With
     * this method one can display any FXMl at any coordinates. This method is
     * used to display error messages at a specific text field
     *
     * @param x The x location of the popup.
     * @param y The y location of the popup.
     * @param scene_fxml the scene that we are using for the popup.
     * @param stage The input stage upon which we build the popup.
     * @throws IOException
     */
    public void PopErrorWindowShower(double x, double y, String scene_fxml, Stage stage) throws IOException {

        Parent root3 = FXMLLoader.load(getClass().getResource(scene_fxml));
        root3.getStylesheets().add(getClass().getResource("../css/Primary.css").toExternalForm());

        pop.setAutoHide(true);
        pop.setAutoFix(true);
        pop.setHideOnEscape(true);
        pop.getContent().add(root3);
        pop.centerOnScreen();
        pop.setX(stage.getX() + x);
        pop.setY(stage.getY() + y);
        pop.show(stage);

    }
    
    public Popup get_popup(){
        return pop;
    }
}
