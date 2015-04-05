package edu.wdaniels.lg.gui;

import edu.wdaniels.lg.FieldValidator;
import edu.wdaniels.lg.Main;
import edu.wdaniels.lg.abg.DistanceFinder;
import edu.wdaniels.lg.abg.GrammarGt1;
import edu.wdaniels.lg.abg.GrammarGz;
import edu.wdaniels.lg.abg.Obstacle;
import edu.wdaniels.lg.abg.Piece;
import edu.wdaniels.lg.structures.Pair;
import edu.wdaniels.lg.structures.TableData;
import edu.wdaniels.lg.structures.Triple;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

/**
 *
 * @author William
 */
public class PrimaryController {

    private static PrimaryController controller;

    @FXML
    private TableColumn tc_distance_column, tc_piece_column;

    @FXML
    private Button btn_display_table, btn_generate, btn_generate_test_board, btn_traj_display, btn_generate_zone;

    @FXML
    private TabPane tp_main_display;
    @FXML
    private TableView tv_distances;

    @FXML
    public ListView lv_distance_piece_list, lv_pieces, lv_obstacles, lv_traj_starting, lv_traj_target;

    @FXML
    private TextArea ta_error_pane;
    @FXML
    public TextField tf_board_size, tf_traj_length;
    @FXML
    private ProgressIndicator pi_indicator;

    @FXML
    private RadioButton rb_2d, rb_3d;

    public boolean is2D = true;
    public List<List<Triple<Integer, Integer, Integer>>> trajectoryList = new ArrayList<>();
    public List<Triple<Integer, Integer, Integer>> trajectory;
    public final ArrayList<Piece> pieceList = new ArrayList<>();
    public final ArrayList<Obstacle> obstacleList = new ArrayList<>();
    private Stage addPieceStage, addObstacleStage, display2DStage, displayTraj2D;

    /**
     * This is the primary entry point into the program. Handled all of the
     * initialization.
     *
     */
    @FXML
    public void initialize() {
        controller = this;
        setInitialListeners();
    }

    @FXML
    private void displayTrajectories() {
        trajectoryList.clear();

        int size = getBoardSize();
        if (is2D & obstacleList.isEmpty() && size == 8) {
            ta_error_pane.setText("I'm sorry, there is a known bug with this combination of parameters, "
                    + "please change the board size, switch to 3D, or add an obstacle. Thank you.");
            return;
        }
        ta_error_pane.setText("Warning, this operation can take quite some time depending on the board size and speed of the computer running it. "
                + "A new window will open, and the spinner will disappear when it finishes. 3D times will take approximately 5 times as long in the worst case. ");
        Thread t = new Thread(() -> {
            Platform.runLater(() -> {
                btn_traj_display.setDisable(true);
                pi_indicator.setVisible(true);

            });
            int count = 0;
            Piece start = (Piece) lv_traj_starting.getSelectionModel().getSelectedItem();
            Piece target = (Piece) lv_traj_target.getSelectionModel().getSelectedItem();
            while (trajectoryList.size() < size * 5 && count < size * size) {
                GrammarGt1 gt1 = new GrammarGt1(size, Integer.valueOf(tf_traj_length.getText()), start, target);
                List<Triple<Integer, Integer, Integer>> traj = gt1.produceTrajectory();

                if (!traj.isEmpty()) {
                    System.out.println("adding trajectory..");
                    trajectoryList.add(traj);
                }
                count++;
            }
            if (trajectoryList.isEmpty()) {
                ta_error_pane.setText("I'm sorry, no trajectories of that length found, please check the distance pane and try again.");
                Platform.runLater(() -> {
                    btn_traj_display.setDisable(false);
                    pi_indicator.setVisible(false);
                });
                return;
            }
            Platform.runLater(() -> {
                if (is2D) {
                    try {
                        if (displayTraj2D != null && displayTraj2D.isShowing()) {
                            displayTraj2D.requestFocus();
                            return;
                        }
                        displayTraj2D = new Stage(StageStyle.DECORATED);
                        Parent root;
                        root = FXMLLoader.load(getClass().getResource("fxml/DisplayTraj2D.fxml"));
                        Scene scene = new Scene(root);

                        displayTraj2D.setScene(scene);
                        //mainStage.getIcons().add(new Image(getClass().getResourceAsStream("images/programLogo128.png")));
                        displayTraj2D.setTitle("Display 2D Trajectory Table");
                        //mainStage.setResizable(true);
                        displayTraj2D.show();
                    } catch (IOException ex) {
                        Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else {
                    try {
                        Stage displayTraj3D = new Stage();
                        if (displayTraj3D.isShowing()) {
                            displayTraj3D.requestFocus();
                            return;
                        }
                        displayTraj3D = new Stage(StageStyle.DECORATED);
                        Parent root;
                        root = FXMLLoader.load(getClass().getResource("fxml/Display3DTraj.fxml"));
                        Scene scene = new Scene(root);

                        displayTraj3D.setScene(scene);
                        //mainStage.getIcons().add(new Image(getClass().getResourceAsStream("images/programLogo128.png")));
                        displayTraj3D.setTitle("Display 3D Trajectories");
                        //mainStage.setResizable(true);
                        displayTraj3D.show();
                    } catch (IOException ex) {
                        Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
                pi_indicator.setVisible(false);
                btn_traj_display.setDisable(false);
            });

        });
        t.start();

    }

    @FXML
    private void generateDistance() throws Exception {
        if (!showInvalidFields()) {
            return;
        }
        pi_indicator.setVisible(true);
        btn_display_table.setDisable(true);
        btn_generate.setDisable(true);
        btn_generate_test_board.setDisable(true);
        rb_2d.setDisable(true);
        rb_3d.setDisable(true);
        tf_board_size.setDisable(true);
        Thread t = new Thread(() -> {
            BoardGenerator gen = new BoardGenerator();
            for (Piece piece : pieceList) {

                int boardSize = Integer.valueOf(tf_board_size.getText());
                if (boardSize == 8 && is2D && obstacleList.isEmpty()) {
                    boardSize = 15;
                }
                if (is2D) {
                    if (boardSize == 15 && obstacleList.isEmpty()) {
                        piece.setReachabilityTwoDMap(gen.generate2DBoard(piece, obstacleList, boardSize, true));
                    } else {
                        piece.setReachabilityTwoDMap(gen.generate2DBoard(piece, obstacleList, boardSize, false));
                    }
                } else {
                    piece.setReachabilityThreeDMap(gen.generate3DBoard(piece, obstacleList, boardSize));
                }
            }
            refreshDistancePieceList();
            refreshTrajectoryPieceList();
            finished();
        });
        t.start();

    }

    private void refreshTrajectoryPieceList() {
        Platform.runLater(() -> {

            lv_traj_starting.setItems(null);
            ObservableList<Piece> items = FXCollections.observableArrayList(pieceList);
            lv_traj_starting.setItems(items);
            lv_traj_starting.setCellFactory(new Callback<ListView<Piece>, ListCell<Piece>>() {

                @Override
                public ListCell<Piece> call(ListView<Piece> p) {

                    ListCell<Piece> cell = new ListCell<Piece>() {

                        @Override
                        protected void updateItem(Piece t, boolean bln) {
                            super.updateItem(t, bln);
                            if (t != null) {
                                setText(t.getPieceName() + ": Location: " + t.getLocation());

                            }
                        }

                    };

                    return cell;
                }
            });
            lv_traj_target.setItems(null);
            lv_traj_target.setItems(items);
            lv_traj_target.setCellFactory(new Callback<ListView<Piece>, ListCell<Piece>>() {

                @Override
                public ListCell<Piece> call(ListView<Piece> p) {

                    ListCell<Piece> cell = new ListCell<Piece>() {

                        @Override
                        protected void updateItem(Piece t, boolean bln) {
                            super.updateItem(t, bln);
                            if (t != null) {
                                setText(t.getPieceName() + ": Location: " + t.getLocation());

                            }
                        }

                    };

                    return cell;
                }
            });
        });
    }

    public void finished() {
        pi_indicator.setVisible(false);
        btn_display_table.setDisable(false);
        btn_generate.setDisable(false);
        btn_generate_test_board.setDisable(false);
        tf_board_size.setDisable(false);
        rb_2d.setDisable(false);
        rb_3d.setDisable(false);

    }

    /**
     * This method is for handling the 'close' action of the program. Simply
     * closes the primary stage.
     */
    @FXML
    private void menu_item_close() {
        Main.get_stage().close();
    }

    /**
     * This method is for handling the 'about' button in our help menu. This
     * will open a new stage and window which display the current information
     * about the program.
     */
    @FXML
    private void menu_item_about() {
        System.out.println("We need to write some stuff about the program here");
    }

    @FXML
    private void addNewObstacle() throws IOException {
        if (addObstacleStage != null && addObstacleStage.isShowing()) {
            addObstacleStage.requestFocus();
            return;
        }
        addObstacleStage = new Stage(StageStyle.DECORATED);
        Parent root;
        root = FXMLLoader.load(getClass().getResource("fxml/AddNewObstacle.fxml"));
        Scene scene = new Scene(root);

        addObstacleStage.setScene(scene);
        //mainStage.getIcons().add(new Image(getClass().getResourceAsStream("images/programLogo128.png")));
        addObstacleStage.setTitle("Add a new piece!");
        //mainStage.setResizable(true);
        addObstacleStage.show();
    }

    @FXML
    private void addNewPiece() throws IOException {
        if (addPieceStage != null && addPieceStage.isShowing()) {
            addPieceStage.requestFocus();
            return;
        }
        addPieceStage = new Stage(StageStyle.DECORATED);
        Parent root;
        root = FXMLLoader.load(getClass().getResource("fxml/AddNewPiece.fxml"));
        Scene scene = new Scene(root);

        addPieceStage.setScene(scene);
        //mainStage.getIcons().add(new Image(getClass().getResourceAsStream("images/programLogo128.png")));
        addPieceStage.setTitle("Add a new piece!");
        //mainStage.setResizable(true);
        addPieceStage.show();
    }

    @FXML
    private void deletePiece() {
        if (lv_pieces.getSelectionModel().getSelectedItem() != null) {
            removeExistingPiece((Piece) lv_pieces.getSelectionModel().getSelectedItem());
            refreshPieceList();
        }
    }

    @FXML
    private void deleteObstacle() {
        if (lv_obstacles.getSelectionModel().getSelectedItem() != null) {
            removeExistingObstacle((Obstacle) lv_obstacles.getSelectionModel().getSelectedItem());
            refreshObstacleList();
        }
    }

    @FXML
    private void generateZones() {
        Piece start = (Piece) lv_traj_starting.getSelectionModel().getSelectedItem();
        Piece target = (Piece) lv_traj_target.getSelectionModel().getSelectedItem();
        pi_indicator.setVisible(true);
        Thread t = new Thread(() -> {
            GrammarGz gz = new GrammarGz(start, target, trajectoryList.get(0), getBoardSize());
            System.out.println("Generating zones...");
            List<Pair<List<Triple<Integer, Integer, Integer>>, Integer>> zone;
            zone = gz.produceZone();
            trajectoryList.clear();
            zone.stream().forEach((pair) -> {

                trajectoryList.add(pair.getFirst());
            });
            Platform.runLater(() -> {
                try {
                    displayTraj2D = new Stage(StageStyle.DECORATED);
                    Parent root;
                    root = FXMLLoader.load(getClass().getResource("fxml/DisplayTraj2D.fxml"));
                    Scene scene = new Scene(root);

                    displayTraj2D.setScene(scene);
                    //mainStage.getIcons().add(new Image(getClass().getResourceAsStream("images/programLogo128.png")));
                    displayTraj2D.setTitle("Display 2D Trajectory Table");
                    //mainStage.setResizable(true);
                    displayTraj2D.show();
                    pi_indicator.setVisible(false);
                } catch (IOException ex) {
                    ex.printStackTrace(System.err);
                    Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
                }

            });
        });
        t.start();

        System.out.println("Done...?");
    }

    /**
     * Simply adds a piece to our piece list. Very dramatic.
     *
     * @param piece
     */
    public void addNewPiece(Piece piece) {
        for (Piece item : pieceList) {
            if (item.getPieceName().equals(piece.getPieceName())) {
                ta_error_pane.setText("ERROR: There is already a piece with this name defined, please choose a new one.");
                return;
            }
            if (item.getLocation().compareTo(piece.getLocation()) == 1) {
                ta_error_pane.setText("ERROR: There is already a piece at this location, please place it somewhere else.");
                return;
            }
        }
        pieceList.add(piece);
        refreshPieceList();
    }

    /**
     * Removes a piece using object equality.
     *
     * @param piece The piece to be removed.
     */
    public void removeExistingPiece(Piece piece) {
        pieceList.remove(piece);
    }

    /**
     * removes a piece by location.
     *
     * @param location the index of the piece to be removed.
     */
    public void removeExistingPiece(int location) {
        pieceList.remove(location);
    }

    /**
     * Simply adds a obstacle to our obstacle list. Very dramatic.
     *
     * @param obstacle
     */
    public void addNewObstacle(Obstacle obstacle) {
        obstacleList.add(obstacle);
        refreshObstacleList();
    }

    /**
     * Removes a obstacle using object equality.
     *
     * @param obstacle The obstacle to be removed.
     */
    public void removeExistingObstacle(Obstacle obstacle) {
        obstacleList.remove(obstacle);
    }

    /**
     * removes a obstacle by location.
     *
     * @param location the index of the obstacle to be removed.
     */
    public void removeExistingObstacle(int location) {
        obstacleList.remove(location);
    }

    public static PrimaryController getController() {
        return controller;
    }

    /**
     * This simply refreshes our pieces list on the left side of the screen.
     *
     */
    private void refreshPieceList() {
        lv_pieces.setItems(null);
        ObservableList<Piece> items = FXCollections.observableArrayList(pieceList);
        lv_pieces.setItems(items);
        lv_pieces.setCellFactory(new Callback<ListView<Piece>, ListCell<Piece>>() {

            @Override
            public ListCell<Piece> call(ListView<Piece> p) {

                ListCell<Piece> cell = new ListCell<Piece>() {

                    @Override
                    protected void updateItem(Piece t, boolean bln) {
                        super.updateItem(t, bln);
                        if (t != null) {
                            setText(t.getPieceName() + ": Location: " + t.getLocation() + " reachability: " + t.getReachablityEquation());
                        }
                    }

                };

                return cell;
            }
        });
    }

    private void refreshDistancePieceList() {
        Platform.runLater(() -> {

            lv_distance_piece_list.setItems(null);
            ObservableList<Piece> items = FXCollections.observableArrayList(pieceList);
            lv_distance_piece_list.setItems(items);
            lv_distance_piece_list.setCellFactory(new Callback<ListView<Piece>, ListCell<Piece>>() {

                @Override
                public ListCell<Piece> call(ListView<Piece> p) {

                    ListCell<Piece> cell = new ListCell<Piece>() {

                        @Override
                        protected void updateItem(Piece t, boolean bln) {
                            super.updateItem(t, bln);
                            if (t != null) {
                                setText(t.getPieceName() + ": Location: " + t.getLocation() + " reachability: " + t.getReachablityEquation());

                            }
                        }

                    };

                    return cell;
                }
            });
        });

    }

    /**
     * This simply refreshes our pieces list on the left side of the screen.
     *
     */
    private void refreshObstacleList() {
        lv_obstacles.setItems(null);
        ObservableList<Obstacle> items = FXCollections.observableArrayList(obstacleList);
        lv_obstacles.setItems(items);
        lv_obstacles.setCellFactory(new Callback<ListView<Obstacle>, ListCell<Obstacle>>() {

            @Override
            public ListCell<Obstacle> call(ListView<Obstacle> p) {

                ListCell<Obstacle> cell = new ListCell<Obstacle>() {

                    @Override
                    protected void updateItem(Obstacle t, boolean bln) {
                        super.updateItem(t, bln);
                        if (t != null) {
                            setText(t.getObstacleName() + ": Location: " + t.getObstacleLocation());
                        }
                    }

                };

                return cell;
            }
        });
    }

    public int getBoardSize() {
        return Integer.valueOf(tf_board_size.getText());
    }

    private void generateMidtermBoard() {
        pieceList.clear();
        obstacleList.clear();
        String kingRelation = "((y1-x1 <= 1) && (y1-x1 >= -1)) && ((y2-x2 <= 1) && (y2-x2 >= -1))";
        String towerRelation = "((y1-x1 == 1) && (y1-x1 >= -1)) && ((y2-x2 == 1) && (y2-x2 >= -1))";
        String knightRelation = "(((y1-x1 == 1) || (x1-y1) == 1) && (((y2-x2 == 2) || (x2-y2 == 2)))) || (((y1-x1 == 2) || (x1 -y1 == 2))  && ((y2-x2 == 1) || (x2-y2 ==1)))";

        Piece king, wKnight, btower1, btower2, btower3, btower4, wtower1, target;

        Obstacle block1, block2, block3, block4, block5, block6, block7, block8, block9, block10, block11;

        king = new Piece("black", new Triple(7, 0, 0), kingRelation, "Black King");
        wKnight = new Piece("white", new Triple(6, 5, 0), knightRelation, "White Knight");
        btower1 = new Piece("black", new Triple(3, 7, 0), towerRelation, "Black Tower 1");
        btower2 = new Piece("black", new Triple(7, 5, 0), towerRelation, "Black Tower 2");
        btower3 = new Piece("black", new Triple(6, 2, 0), towerRelation, "Black Tower 3");
        btower4 = new Piece("black", new Triple(4, 1, 0), towerRelation, "Black Tower 4");
        wtower1 = new Piece("white", new Triple(7, 2, 0), towerRelation, "White Tower 1");
        target = new Piece("white", new Triple(2, 1, 0), kingRelation, "Target");
        tf_board_size.setText("8");
        pieceList.addAll(Arrays.asList(king, target, wKnight, btower1, btower2, btower3, btower4, wtower1));
        refreshPieceList();
    }

    @FXML
    private void generateTestBoard() {
        //generateMidtermBoard();

        pieceList.clear();
        obstacleList.clear();
        String whitePawnRelation = "((y1-x1 == 0) ) && ((y2-x2 == 1))";
        String blackPawnRelation = "((y1-x1 == 0) ) && ((y2-x2 == -1))";
        String kingRelation = "((y1-x1 <=1) && (y1-x1 >=-1)) && ((y2-x2 <=1) && (y2-x2 >=-1))";
        String knightRelation = "(((y1-x1 == 1) || (x1-y1) == 1) && (((y2-x2 == 2) || (x2-y2 == 2)))) || (((y1-x1 == 2) || (x1 -y1 == 2))  && ((y2-x2 == 1) || (x2-y2 ==1)))";
        String bishopRelation = "((((y2-x2) == (y1-x1)) || ((x2 - y2) == (x1 - y1)) || ((x2-y2) == (y1-x1) || (x1-y1) == (y2-x2))))";
        String rookRelation = "((x1 == y1) || (y2 == x2))";
        String queenRelation = rookRelation + " || " + bishopRelation;

        Piece wPawn0, wPawn1, wPawn2, wPawn3, wPawn4, wPawn5, wPawn6, wPawn7,
                bPawn0, bPawn1, bPawn2, bPawn3, bPawn4, bPawn5, bPawn6, bPawn7,
                wKing, wBish0, wBish1, wRook0, wRook1, wKnight0, wKnight1, wQueen,
                bKing, bBish0, bBish1, bRook0, bRook1, bKnight0, bKnight1, bQueen;
        //White Pawns
        wPawn0 = new Piece("white", new Triple(0, 1, 0), whitePawnRelation, "White Pawn 0");
        wPawn0.setPieceImage(new Image(getClass().getResourceAsStream("../images/whitePawn.png")));
        wPawn1 = new Piece("white", new Triple(1, 1, 0), whitePawnRelation, "White Pawn 1");
        wPawn1.setPieceImage(new Image(getClass().getResourceAsStream("../images/whitePawn.png")));
        wPawn2 = new Piece("white", new Triple(2, 1, 0), whitePawnRelation, "White Pawn 2");
        wPawn2.setPieceImage(new Image(getClass().getResourceAsStream("../images/whitePawn.png")));
        wPawn3 = new Piece("white", new Triple(3, 1, 0), whitePawnRelation, "White Pawn 3");
        wPawn3.setPieceImage(new Image(getClass().getResourceAsStream("../images/whitePawn.png")));
        wPawn4 = new Piece("white", new Triple(4, 1, 0), whitePawnRelation, "White Pawn 4");
        wPawn4.setPieceImage(new Image(getClass().getResourceAsStream("../images/whitePawn.png")));
        wPawn5 = new Piece("white", new Triple(5, 1, 0), whitePawnRelation, "White Pawn 5");
        wPawn5.setPieceImage(new Image(getClass().getResourceAsStream("../images/whitePawn.png")));
        wPawn6 = new Piece("white", new Triple(6, 1, 0), whitePawnRelation, "White Pawn 6");
        wPawn6.setPieceImage(new Image(getClass().getResourceAsStream("../images/whitePawn.png")));
        wPawn7 = new Piece("white", new Triple(7, 1, 0), whitePawnRelation, "White Pawn 7");
        wPawn7.setPieceImage(new Image(getClass().getResourceAsStream("../images/whitePawn.png")));

        //White Royalty
        wKing = new Piece("white", new Triple(4, 0, 0), kingRelation, "White King");
        wKing.setPieceImage(new Image(getClass().getResourceAsStream("../images/whiteKing.png")));
        wQueen = new Piece("white", new Triple(3, 0, 0), queenRelation, "White Queen");
        wQueen.setPieceImage(new Image(getClass().getResourceAsStream("../images/whiteQueen.png")));
        wBish0 = new Piece("white", new Triple(5, 0, 0), bishopRelation, "White Bishop 1");
        wBish0.setPieceImage(new Image(getClass().getResourceAsStream("../images/whiteBishop.png")));
        wBish1 = new Piece("white", new Triple(2, 0, 0), bishopRelation, "White Bishop 2");
        wBish1.setPieceImage(new Image(getClass().getResourceAsStream("../images/whiteBishop.png")));
        wKnight0 = new Piece("white", new Triple(1, 0, 0), knightRelation, "White Knight 1");
        wKnight0.setPieceImage(new Image(getClass().getResourceAsStream("../images/whiteKnight.png")));
        wKnight1 = new Piece("white", new Triple(6, 0, 0), knightRelation, "White Knight 2");
        wKnight1.setPieceImage(new Image(getClass().getResourceAsStream("../images/whiteKnight.png")));
        wRook0 = new Piece("white", new Triple(0, 0, 0), rookRelation, "White Rook 1");
        wRook0.setPieceImage(new Image(getClass().getResourceAsStream("../images/whiteRook.png")));
        wRook1 = new Piece("white", new Triple(7, 0, 0), rookRelation, "White Rook 2");
        wRook1.setPieceImage(new Image(getClass().getResourceAsStream("../images/whiteRook.png")));

        //Black Pawns
        bPawn0 = new Piece("black", new Triple(0, 6, 0), blackPawnRelation, "Black Pawn 0");
        bPawn0.setPieceImage(new Image(getClass().getResourceAsStream("../images/blackPawn.png")));
        bPawn1 = new Piece("black", new Triple(1, 6, 0), blackPawnRelation, "Black Pawn 1");
        bPawn1.setPieceImage(new Image(getClass().getResourceAsStream("../images/blackPawn.png")));
        bPawn2 = new Piece("black", new Triple(2, 6, 0), blackPawnRelation, "Black Pawn 2");
        bPawn2.setPieceImage(new Image(getClass().getResourceAsStream("../images/blackPawn.png")));
        bPawn3 = new Piece("black", new Triple(3, 6, 0), blackPawnRelation, "Black Pawn 3");
        bPawn3.setPieceImage(new Image(getClass().getResourceAsStream("../images/blackPawn.png")));
        bPawn4 = new Piece("black", new Triple(4, 6, 0), blackPawnRelation, "Black Pawn 4");
        bPawn4.setPieceImage(new Image(getClass().getResourceAsStream("../images/blackPawn.png")));
        bPawn5 = new Piece("black", new Triple(5, 6, 0), blackPawnRelation, "Black Pawn 5");
        bPawn5.setPieceImage(new Image(getClass().getResourceAsStream("../images/blackPawn.png")));
        bPawn6 = new Piece("black", new Triple(6, 6, 0), blackPawnRelation, "Black Pawn 6");
        bPawn6.setPieceImage(new Image(getClass().getResourceAsStream("../images/blackPawn.png")));
        bPawn7 = new Piece("black", new Triple(7, 6, 0), blackPawnRelation, "Black Pawn 7");
        bPawn7.setPieceImage(new Image(getClass().getResourceAsStream("../images/blackPawn.png")));

        //Black Royalty
        bQueen = new Piece("black", new Triple(4, 7, 0), queenRelation, "Black Queen");
        bQueen.setPieceImage(new Image(getClass().getResourceAsStream("../images/blackQueen.png")));
        bKing = new Piece("black", new Triple(3, 7, 0), kingRelation, "Black King");
        bKing.setPieceImage(new Image(getClass().getResourceAsStream("../images/blackKing.png")));
        bBish0 = new Piece("black", new Triple(5, 7, 0), bishopRelation, "Black Bishop 1");
        bBish0.setPieceImage(new Image(getClass().getResourceAsStream("../images/blackBishop.png")));
        bBish1 = new Piece("black", new Triple(2, 7, 0), bishopRelation, "Black Bishop 2");
        bBish1.setPieceImage(new Image(getClass().getResourceAsStream("../images/blackBishop.png")));
        bKnight0 = new Piece("black", new Triple(1, 7, 0), knightRelation, "Black Knight 1");
        bKnight0.setPieceImage(new Image(getClass().getResourceAsStream("../images/blackKnight.png")));
        bKnight1 = new Piece("black", new Triple(6, 7, 0), knightRelation, "Black Knight 2");
        bKnight1.setPieceImage(new Image(getClass().getResourceAsStream("../images/blackKnight.png")));
        bRook0 = new Piece("black", new Triple(0, 7, 0), rookRelation, "Black Rook 0");
        bRook0.setPieceImage(new Image(getClass().getResourceAsStream("../images/blackRook.png")));
        bRook1 = new Piece("black", new Triple(7, 7, 0), rookRelation, "Black Rook 1");
        bRook1.setPieceImage(new Image(getClass().getResourceAsStream("../images/blackRook.png")));

        tf_board_size.setText("9");
        pieceList.addAll(Arrays.asList(wKing, bKing, wQueen, bQueen, wPawn0, wPawn1, wPawn2, wPawn3, wPawn4, wPawn5, wPawn6, wPawn7, bPawn0, bPawn1, bPawn2, bPawn3, bPawn4, bPawn5, bPawn6, bPawn7,
                wBish0, wBish1, wKnight0, wKnight1, wRook0, wRook1, bBish0, bBish1, bKnight0, bKnight1, bRook0, bRook1));
        refreshPieceList();
    }

    @FXML
    private void generatePieceTable() throws IOException {

        Piece piece = (Piece) lv_distance_piece_list.getSelectionModel().getSelectedItem();
        if (piece != null) {
            if (is2D) {
                piece.printBoard();
                if (display2DStage != null && display2DStage.isShowing()) {
                    display2DStage.requestFocus();
                    return;
                }
                display2DStage = new Stage(StageStyle.DECORATED);
                Parent root;
                root = FXMLLoader.load(getClass().getResource("fxml/Display2D.fxml"));
                Scene scene = new Scene(root);

                display2DStage.setScene(scene);
                //mainStage.getIcons().add(new Image(getClass().getResourceAsStream("images/programLogo128.png")));
                display2DStage.setTitle("Display 2D Table");
                //mainStage.setResizable(true);
                display2DStage.show();

            } else {
                ThreeDBoardMaker coolBoard = new ThreeDBoardMaker(Integer.valueOf(tf_board_size.getText()));
                coolBoard.setMap(piece.getReachabilityThreeDMap());
                try {
                    coolBoard.start(false);
                } catch (Exception ex) {
                    Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    /**
     * This method sets all of our listeners for the various text fields.
     */
    @SuppressWarnings("Convert2Lambda")
    private void setInitialListeners() {

        final FieldValidator fv = new FieldValidator();
        Main.get_stage().setOnShown((WindowEvent) -> {
            Main.get_stage().widthProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue ov, Number oldValue, Number newValue) {
                    if (lv_distance_piece_list.getWidth() < 300) {
                        lv_distance_piece_list.setPrefWidth(tv_distances.getPrefWidth() + (newValue.doubleValue() - oldValue.doubleValue()));
                    }
                    tv_distances.setPrefWidth(tv_distances.getPrefWidth() + (newValue.doubleValue() - oldValue.doubleValue()));
                }
            });
            Main.get_stage().heightProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue ov, Number oldValue, Number newValue) {
                    lv_pieces.setPrefHeight(lv_pieces.getHeight() + (newValue.doubleValue() - oldValue.doubleValue()));
                    lv_obstacles.setPrefHeight(lv_obstacles.getHeight() + (newValue.doubleValue() - oldValue.doubleValue()));
                    tp_main_display.setPrefHeight(tp_main_display.getHeight() + (newValue.doubleValue() - oldValue.doubleValue()));
                    tv_distances.setPrefHeight(tv_distances.getHeight() + (newValue.doubleValue() - oldValue.doubleValue()));
                }
            });
        });
        tp_main_display.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Tab> ov, Tab oldTab, Tab newTab) -> {
            if (!newTab.textProperty().get().equalsIgnoreCase("distance table") && !newTab.textProperty().get().equalsIgnoreCase("trajectories")) {
                ta_error_pane.setText("I'm sorry, this feature isn't implemented yet. Please check again in "
                        + "future versions of this program. ");
                tp_main_display.getSelectionModel().select(oldTab);
            }
        });
        tf_board_size.textProperty().addListener((ChangeListener) -> {
            if (fv.integerValidator(tf_board_size.getText())) {
                displayErrorAndStyle(tf_board_size, "Invalid Board Size", tf_board_size.textProperty());
            } else {
                tf_board_size.getStyleClass().removeAll("bad", "good");
                tf_board_size.getStyleClass().add("good");
            }
        });
        tf_traj_length.textProperty().addListener((ChangeListener) -> {
            ta_error_pane.setText("");
            if (fv.integerValidator(tf_traj_length.getText())) {
                displayErrorAndStyle(tf_traj_length, "Invalid Trajectory Length", tf_traj_length.textProperty());
            } else {
                tf_traj_length.getStyleClass().removeAll("bad", "good");
                tf_traj_length.getStyleClass().add("good");
            }
            allTrajectoryItemsSelected();
        });
        rb_2d.selectedProperty().addListener((ChangeListener) -> {
            is2D = rb_2d.isSelected();
            btn_display_table.setVisible(false);
        });

        lv_traj_starting.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                ta_error_pane.setText("");
                allTrajectoryItemsSelected();
            }

        });
        lv_traj_target.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                ta_error_pane.setText("");
                allTrajectoryItemsSelected();
            }

        });

        lv_distance_piece_list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue ov, Object oldValue, Object newValue) {
                if (newValue != null) {
                    DistanceFinder df = new DistanceFinder();
                    tc_piece_column.setCellValueFactory(
                            new PropertyValueFactory<>("pieceName")
                    );
                    tc_distance_column.setCellValueFactory(
                            new PropertyValueFactory<>("pieceDistance")
                    );
                    ArrayList<TableData> dataList = new ArrayList<>();
                    if (!pieceList.isEmpty()) {
                        tv_distances.getItems().clear();
                        for (Piece piece : pieceList) {
                            if (((Piece) newValue) != piece) {
                                if (Integer.parseInt(tf_board_size.getText()) == 8) {
                                    if (obstacleList.isEmpty() && is2D) {
                                        dataList.add(new TableData(piece.getPieceName(), df.find2DChessDistance((Piece) newValue, piece) - 1));
                                    } else {
                                        if (is2D) {
                                            dataList.add(new TableData(piece.getPieceName(), df.find2DDistance((Piece) newValue, piece) - 1));
                                        } else {
                                            dataList.add(new TableData(piece.getPieceName(), df.find3DDistance((Piece) newValue, piece) - 1));
                                        }
                                    }
                                } else {
                                    if (is2D) {
                                        dataList.add(new TableData(piece.getPieceName(), df.find2DDistance((Piece) newValue, piece) - 1));
                                    } else {
                                        dataList.add(new TableData(piece.getPieceName(), df.find3DDistance((Piece) newValue, piece) - 1));
                                    }
                                }
                            }
                        }
                    }
                    tv_distances.setItems(FXCollections.observableArrayList(dataList));
                    btn_display_table.setVisible(true);

                } else {
                    btn_display_table.setVisible(false);
                }
            }
        });
        Main.get_stage().setOnCloseRequest((EventHandler) -> {
            if (addPieceStage != null && addPieceStage.isShowing()) {
                addPieceStage.close();
            }
            if (addObstacleStage != null && addObstacleStage.isShowing()) {
                addObstacleStage.close();
            }

        });
        Main.get_stage().heightProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
                if (newValue.doubleValue() < 600) {
                    Main.get_stage().setHeight(oldValue.doubleValue());
                }
            }

        });
        Main.get_stage().widthProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
                if (newValue.doubleValue() < 800) {
                    Main.get_stage().setWidth(oldValue.doubleValue());
                }
            }

        });
    }

    private void allTrajectoryItemsSelected() {
        if (!tf_traj_length.getText().isEmpty() && (lv_traj_starting.getSelectionModel().getSelectedItem() != null)
                && (lv_traj_target.getSelectionModel().getSelectedItem() != null)) {
            btn_traj_display.setVisible(true);
        } else {
            btn_traj_display.setVisible(false);
        }

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
        if (tf_board_size.getText().isEmpty()) {
            displayErrorAndStyle(tf_board_size, "Board Size Required", tf_board_size.textProperty());
            wasValid = false;
        } else if (tf_board_size.getStyleClass().contains("bad")) {
            wasValid = false;
        }
        return wasValid;
    }

    /**
     * This method prevents us from having to have a bunch of repetitive code in
     * the showInvalidFields() method. sets an error label and gives the red
     * highlighted styling to an individual node.
     *
     * @param node the node that we are styling and anchoring the error popup
     * to.
     * @param errorMessage the error message we are displaying in the popup.
     */
    private void displayErrorAndStyle(Node node, String errorMessage, final StringProperty textProperty) {
        PopErrorWindow error = new PopErrorWindow();
        PopErrorWindow.setCustomErrorMessage(errorMessage);
        try {
            error.PopErrorWindowShower(node.localToScene(node.getBoundsInLocal()).getMaxX() + 5, node.localToScene(node.getBoundsInLocal()).getMaxY());
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

    public Stage getAddPieceStage() {
        return addPieceStage;
    }

    public Stage getAddObstacleStage() {
        return addObstacleStage;
    }
}
