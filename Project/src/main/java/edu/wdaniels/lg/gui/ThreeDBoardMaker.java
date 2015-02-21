package edu.wdaniels.lg.gui;

import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.fxyz.cameras.CameraTransformer;

/**
 *
 * @author wdaniels
 */
public class ThreeDBoardMaker {

    private PerspectiveCamera camera;
    private final double sceneWidth = 600;
    private final double sceneHeight = 600;
    private final double cameraDistance = 5000;
    private final int cubeSize;
    private GridBuilder cubeViewer;
    private final CameraTransformer cameraTransform = new CameraTransformer();

    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;
    private double mouseDeltaX;
    private double mouseDeltaY;

    //Create and add some data to the Cube
    private final ArrayList<Double> dataX;
    private final ArrayList<Double> dataY;
    private final ArrayList<Double> dataZ;
    private int[][][] map;
    
    public ThreeDBoardMaker(int cubeSize){
        this.cubeSize = cubeSize;
        dataX = new ArrayList<>();
        dataY = new ArrayList<>();
        dataZ = new ArrayList<>();
    }
    public ThreeDBoardMaker(int cubeSize, ArrayList<Double> dataX, ArrayList<Double> dataY, ArrayList<Double> dataZ){
        this.dataX = dataX;
        this.dataY = dataY;
        this.dataZ = dataZ;
        this.cubeSize = cubeSize;
    }
    
    public void setMap(int[][][] map){
        this.map = map;
    }
    
    public void start() throws Exception {
        Stage primaryStage = new Stage();
        Group sceneRoot = new Group();
        Scene scene = new Scene(sceneRoot, sceneWidth, sceneHeight, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.BLACK);
        
        //Setup camera and scatterplot cubeviewer
        camera = new PerspectiveCamera(true);
        cubeViewer = new GridBuilder(cubeSize * 100, 100, true, 5, 1, 1);
        cubeViewer.setMap(map);
        sceneRoot.getChildren().addAll(cubeViewer);
        //setup camera transform for rotational support
        cubeViewer.getChildren().add(cameraTransform);
        cameraTransform.setTranslate(0, 0, 0);
        cameraTransform.getChildren().add(camera);
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        camera.setTranslateZ(-5000);
        cameraTransform.ry.setAngle(-45.0);
        cameraTransform.rx.setAngle(-10.0);
        //add a Point Light for better viewing of the grid coordinate system
        PointLight light = new PointLight(Color.WHITE);
        cameraTransform.getChildren().add(light);
        light.setTranslateX(camera.getTranslateX());
        light.setTranslateY(camera.getTranslateY());
        light.setTranslateZ(camera.getTranslateZ());
        scene.setCamera(camera);
        for (int i = new Double(-(Math.floor(cubeSize / 2))).intValue(); i < new Double((Math.ceil(cubeSize / 2))).intValue(); i++) {
            for (int y = new Double(-(Math.floor(cubeSize / 2))).intValue(); y < new Double((Math.ceil(cubeSize / 2))).intValue(); y++) {
                for (int z = new Double(-(Math.floor(cubeSize / 2))).intValue(); z < new Double((Math.ceil(cubeSize / 2))).intValue(); z++) {
                    dataX.add(new Double(i * 100));
                    dataY.add(new Double(y * 100));
                    dataZ.add(new Double(z * 100));
                }
            }

        }
        //The cube viewer will add data nodes as cubes here but you can add
        //your own scatter plot to the same space as the cube if you want.
        cubeViewer.setxAxisData(dataX);
        cubeViewer.setyAxisData(dataY);
        cubeViewer.setzAxisData(dataZ);
        //First person shooter keyboard movement 
        scene.setOnKeyPressed(event -> {
            double change = 10.0;
            //Add shift modifier to simulate "Running Speed"
            if (event.isShiftDown()) {
                change = 100.0;
            }
            //What key did the user press?
            KeyCode keycode = event.getCode();
            //Step 2c: Add Zoom controls
            if (keycode == KeyCode.W) {
                camera.setTranslateZ(camera.getTranslateZ() + change);
            }
            if (keycode == KeyCode.S) {
                camera.setTranslateZ(camera.getTranslateZ() - change);
            }
            //Step 2d:  Add Strafe controls
            if (keycode == KeyCode.A) {
                camera.setTranslateX(camera.getTranslateX() - change);
            }
            if (keycode == KeyCode.D) {
                camera.setTranslateX(camera.getTranslateX() + change);
            }
        });

        scene.setOnMousePressed((MouseEvent me) -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
        });
        scene.setOnMouseDragged((MouseEvent me) -> {
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseDeltaX = (mousePosX - mouseOldX);
            mouseDeltaY = (mousePosY - mouseOldY);

            double modifier = 10.0;
            double modifierFactor = 0.1;

            if (me.isControlDown()) {
                modifier = 0.1;
            }
            if (me.isShiftDown()) {
                modifier = 50.0;
            }
            if (me.isPrimaryButtonDown()) {
                cameraTransform.ry.setAngle(((cameraTransform.ry.getAngle() + mouseDeltaX * modifierFactor * modifier * 2.0) % 360 + 540) % 360 - 180);  // +
                cameraTransform.rx.setAngle(((cameraTransform.rx.getAngle() - mouseDeltaY * modifierFactor * modifier * 2.0) % 360 + 540) % 360 - 180);  // -
                cubeViewer.adjustPanelsByPos(cameraTransform.rx.getAngle(), cameraTransform.ry.getAngle(), cameraTransform.rz.getAngle());
            } else if (me.isSecondaryButtonDown()) {
                double z = camera.getTranslateZ();
                double newZ = z + mouseDeltaX * modifierFactor * modifier;
                camera.setTranslateZ(newZ);
            } else if (me.isMiddleButtonDown()) {
                cameraTransform.t.setX(cameraTransform.t.getX() + mouseDeltaX * modifierFactor * modifier * 0.3);  // -
                cameraTransform.t.setY(cameraTransform.t.getY() + mouseDeltaY * modifierFactor * modifier * 0.3);  // -
            }
        });

        primaryStage.setTitle("3D Board Simulation");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
