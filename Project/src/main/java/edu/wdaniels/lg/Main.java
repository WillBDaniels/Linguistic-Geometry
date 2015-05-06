package edu.wdaniels.lg;

import edu.wdaniels.lg.logging.Log4jLogger;
import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

/**
 * This is going to be the primary Linguistic Geometry Project1 entry point.
 *
 * @author William
 */
public class Main extends Application {

    private final Logger rootLog = Log4jLogger.rootLogger();
    private static Stage mainStage;

    /**
     * This is going to be the main entry point into the program. EVERYTHING
     * must happen here due to the nature of the javafx / gradle relationship.
     *
     * @param primaryStage this is unused, it's essentially the default stage
     * that javafx provides us with. I find it's easier to simply create our own
     * and roll with it.
     * @throws IOException If there is a problem reading the fxml this gets
     * thrown, and quietly ignored.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        mainStage = new Stage();
        Parent root;
        root = FXMLLoader.load(getClass().getResource("gui/fxml/PrimaryWindow.fxml"));
        Scene scene = new Scene(root);

        mainStage.setScene(scene);
        //mainStage.getIcons().add(new Image(getClass().getResourceAsStream("images/programLogo128.png")));
        mainStage.setTitle("AI Project Extra Credit - William Daniels");
        //mainStage.setResizable(true);
        mainStage.show();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Simply returns the stage for our use.
     *
     * @return
     */
    public static Stage get_stage() {
        return mainStage;
    }

}
