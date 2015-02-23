package edu.wdaniels.lg.gui;

import edu.wdaniels.lg.structures.Triple;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import org.fxyz.tools.CubeViewer;

/**
 *
 * @author William
 */
public class GridBuilder extends CubeViewer {

    private int[][][] map;

    public GridBuilder(double axesSize, double spacing, boolean selfLight, double scatterRadius, double axesThickness, double gridSize) {
        super(axesSize, spacing, selfLight, scatterRadius, axesThickness, gridSize);
    }

    public void setMap(int[][][] map) {
        this.map = map;
    }

    private void buildTrajectory() {
        for (List<Triple<Integer, Integer, Integer>> list : PrimaryController.getController().trajectoryList) {
            for (Triple<Integer, Integer, Integer> item : list) {
                final Cylinder newCyl = new Cylinder();
                newCyl.setRadius(5);
            }
        }
    }

    @Override
    public void setzAxisData(ArrayList<Double> data) {
        super.setzAxisData(data);
        scatterDataGroup.getChildren().clear();
        int x, y = 0, z = 0;
        for (int i = 0; i < super.getzAxisData().size(); i++) {
            x = i % (map.length);
            if (x == 0 && i != 0) {
                if (y + 1 < map.length) {
                    y++;
                } else {
                    y = 0;
                    z++;
                }
            }
            Sphere dataSphere = new Sphere(scatterRadius);
//            final Box dataSphere = new Box(getScatterRadius(), getScatterRadius(), getScatterRadius());

            double translateX = 0.0;
            double translateY = 0.0;
            if (!super.getxAxisData().isEmpty() && super.getxAxisData().size() > i) {
                translateX = super.getxAxisData().get(i);
            }
            if (!super.getyAxisData().isEmpty() && super.getyAxisData().size() > i) {
                translateY = super.getyAxisData().get(i);
            }
            dataSphere.setTranslateX(translateX);
            dataSphere.setTranslateY(translateY);
            dataSphere.setTranslateZ(super.getzAxisData().get(i));
            switch (map[z][y][x]) {

                case 1: {
                    PhongMaterial blueMaterial = new PhongMaterial();
                    blueMaterial.setDiffuseColor(Color.DARKBLUE);
                    dataSphere.setMaterial(blueMaterial);
                    break;
                }
                case 2: {
                    PhongMaterial beigeMaterial = new PhongMaterial();
                    beigeMaterial.setDiffuseColor(Color.ORANGE);
                    dataSphere.setMaterial(beigeMaterial);
                    break;
                }
                case 3: {
                    PhongMaterial chartreuseMaterial = new PhongMaterial();
                    chartreuseMaterial.setDiffuseColor(Color.RED);
                    dataSphere.setMaterial(chartreuseMaterial);
                    break;
                }
                case 4: {
                    PhongMaterial cornsilkMaterial = new PhongMaterial();
                    cornsilkMaterial.setDiffuseColor(Color.PURPLE);
                    dataSphere.setMaterial(cornsilkMaterial);
                    break;
                }
                case 5: {
                    PhongMaterial darkGreenMaterial = new PhongMaterial();
                    darkGreenMaterial.setDiffuseColor(Color.GREEN);
                    dataSphere.setMaterial(darkGreenMaterial);
                    break;
                }
                case 6: {
                    PhongMaterial yellowGreenMaterial = new PhongMaterial();
                    yellowGreenMaterial.setDiffuseColor(Color.WHITE);
                    dataSphere.setMaterial(yellowGreenMaterial);
                    break;
                }
                case 7: {
                    PhongMaterial redmaterial = new PhongMaterial();
                    redmaterial.setDiffuseColor(Color.AQUA);
                    dataSphere.setMaterial(redmaterial);
                    break;
                }
                case 8: {
                    PhongMaterial purpleMaterial = new PhongMaterial();
                    purpleMaterial.setDiffuseColor(Color.PINK);
                    dataSphere.setMaterial(purpleMaterial);
                    break;
                }
                default: {
                    PhongMaterial blackMaterial = new PhongMaterial();
                    blackMaterial.setDiffuseColor(Color.BLACK);
                    dataSphere.setMaterial(blackMaterial);
                    break;
                }

            }

            scatterDataGroup.getChildren().add(dataSphere);
        }
    }

    @Override
    /**
     * @param data the xAxisData to set
     */
    public void setxAxisData(ArrayList<Double> data) {
        super.setxAxisData(data);
        scatterDataGroup.getChildren().clear();
        for (int i = 0; i < super.getxAxisData().size(); i++) {
            final Sphere dataSphere = new Sphere(scatterRadius);
//            final Box dataSphere = new Box(getScatterRadius(), getScatterRadius(), getScatterRadius());
            double translateY = 0.0;
            double translateZ = 0.0;
            dataSphere.setOpacity(.1);
            if (!super.getyAxisData().isEmpty() && super.getyAxisData().size() > i) {
                translateY = super.getyAxisData().get(i);
            }
            if (!super.getzAxisData().isEmpty() && super.getzAxisData().size() > i) {
                translateZ = super.getzAxisData().get(i);
            }
            dataSphere.setTranslateX(super.getxAxisData().get(i));
            dataSphere.setTranslateY(translateY);
            dataSphere.setTranslateZ(translateZ);
            scatterDataGroup.getChildren().add(dataSphere);
        }
    }

    @Override
    /**
     * @param data the yAxisData to set
     */
    public void setyAxisData(ArrayList<Double> data) {
        super.setyAxisData(data);
        scatterDataGroup.getChildren().clear();
        for (int i = 0; i < super.getyAxisData().size(); i++) {
            final Sphere dataSphere = new Sphere(scatterRadius);
//            final Box dataSphere = new Box(getScatterRadius(), getScatterRadius(), getScatterRadius());
            double translateX = 0.0;
            double translateZ = 0.0;
            if (!super.getxAxisData().isEmpty() && super.getxAxisData().size() > i) {
                translateX = super.getxAxisData().get(i);
            }
            if (!super.getzAxisData().isEmpty() && super.getzAxisData().size() > i) {
                translateZ = super.getzAxisData().get(i);
            }
            dataSphere.setTranslateX(translateX);
            dataSphere.setTranslateY(super.getyAxisData().get(i));
            dataSphere.setTranslateZ(translateZ);
            if (i < map.length) {
//                switch (map[i][i][i]) {
//                    case 1: {
//                        final PhongMaterial blueMaterial = new PhongMaterial();
//                        blueMaterial.setDiffuseColor(Color.DARKBLUE);
//                        dataSphere.setMaterial(blueMaterial);
//                    }
//                    case 2: {
//                        final PhongMaterial blueMaterial = new PhongMaterial();
//                        blueMaterial.setDiffuseColor(Color.BEIGE);
//                        dataSphere.setMaterial(blueMaterial);
//                    }
//                    case 3: {
//                        final PhongMaterial blueMaterial = new PhongMaterial();
//                        blueMaterial.setDiffuseColor(Color.CHARTREUSE);
//                        dataSphere.setMaterial(blueMaterial);
//                    }
//                    case 4: {
//                        final PhongMaterial blueMaterial = new PhongMaterial();
//                        blueMaterial.setDiffuseColor(Color.CORNSILK);
//                        dataSphere.setMaterial(blueMaterial);
//                    }
//                    case 5: {
//                        final PhongMaterial blueMaterial = new PhongMaterial();
//                        blueMaterial.setDiffuseColor(Color.DARKGREEN);
//                        dataSphere.setMaterial(blueMaterial);
//                    }
//                    case 6: {
//                        final PhongMaterial blueMaterial = new PhongMaterial();
//                        blueMaterial.setDiffuseColor(Color.YELLOWGREEN);
//                        dataSphere.setMaterial(blueMaterial);
//                    }
//                    case 7: {
//                        final PhongMaterial blueMaterial = new PhongMaterial();
//                        blueMaterial.setDiffuseColor(Color.RED);
//                        dataSphere.setMaterial(blueMaterial);
//                    }
//                    case 8: {
//                        final PhongMaterial blueMaterial = new PhongMaterial();
//                        blueMaterial.setDiffuseColor(Color.PURPLE);
//                        dataSphere.setMaterial(blueMaterial);
//                    }
//
//                }
            }
            scatterDataGroup.getChildren().add(dataSphere);
        }
    }
}
