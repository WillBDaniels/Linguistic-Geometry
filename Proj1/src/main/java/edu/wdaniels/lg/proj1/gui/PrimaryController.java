package edu.wdaniels.lg.proj1.gui;

import edu.wdaniels.lg.proj1.Project1;
import edu.wdaniels.lg.proj1.abg.DistanceFinder;
import edu.wdaniels.lg.proj1.abg.Obstacle;
import edu.wdaniels.lg.proj1.abg.Piece;
import edu.wdaniels.lg.proj1.structures.TableData;
import edu.wdaniels.lg.proj1.structures.Triple;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
    private Button btn_display_table, btn_generate, btn_generate_test_board;

    @FXML
    private TabPane tp_main_display;
    @FXML
    private TableView tv_distances;

    @FXML
    private ListView lv_distance_piece_list, lv_pieces, lv_obstacles;

    @FXML
    private TextArea ta_error_pane;
    @FXML
    private TextField tf_board_size;
    @FXML
    private ProgressIndicator pi_indicator;

    @FXML
    private RadioButton rb_2d, rb_3d;

    private boolean is2D = true;
    private final ArrayList<Piece> pieceList = new ArrayList<>();
    private final ArrayList<Obstacle> obstacleList = new ArrayList<>();
    private Stage addPieceStage, addObstacleStage;

    /**
     * This is the primary entry point into the program. Handled all of the
     * initialization.
     *
     */
    @FXML
    @SuppressWarnings("Convert2Lambda")
    public void initialize() {
        controller = this;
        rb_2d.selectedProperty().addListener((ChangeListener) -> {
            is2D = rb_2d.isSelected();
            btn_display_table.setVisible(false);
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
                                        dataList.add(new TableData(piece.getPieceName(), df.find2DChessDistance((Piece) newValue, piece)));
                                    }else{
                                        if (is2D){
                                            dataList.add(new TableData(piece.getPieceName(), df.find2DDistance((Piece) newValue, piece)));
                                        }else{
                                            dataList.add(new TableData(piece.getPieceName(), df.find3DDistance((Piece) newValue, piece)));
                                        }
                                    }
                                } else {
                                    if (is2D){
                                        dataList.add(new TableData(piece.getPieceName(), df.find2DDistance((Piece) newValue, piece)));
                                    }else{
                                        dataList.add(new TableData(piece.getPieceName(), df.find3DDistance((Piece) newValue, piece)));
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
        Project1.get_stage().setOnCloseRequest((EventHandler) -> {
            if (addPieceStage != null && addPieceStage.isShowing()) {
                addPieceStage.close();
            }
            if (addObstacleStage != null && addObstacleStage.isShowing()) {
                addObstacleStage.close();
            }

        });
        Project1.get_stage().heightProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
                if (newValue.doubleValue() < 600) {
                    Project1.get_stage().setHeight(oldValue.doubleValue());
                }
            }

        });
        Project1.get_stage().widthProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
                if (newValue.doubleValue() < 800) {
                    Project1.get_stage().setWidth(oldValue.doubleValue());
                }
            }

        });
        String expression = "(x <= y && x > 2)";
        Boolean result;
        Map<String, Integer> vars = new HashMap<>();
        vars.put("x", 7);
        vars.put("y", 6);

        Project1.get_stage().setOnShown((WindowEvent) -> {
            Project1.get_stage().widthProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue ov, Number oldValue, Number newValue) {
                    if (lv_distance_piece_list.getWidth() < 300) {
                        lv_distance_piece_list.setPrefWidth(tv_distances.getPrefWidth() + (newValue.doubleValue() - oldValue.doubleValue()));
                    }
                    tv_distances.setPrefWidth(tv_distances.getPrefWidth() + (newValue.doubleValue() - oldValue.doubleValue()));
                }
            });
            Project1.get_stage().heightProperty().addListener(new ChangeListener<Number>() {
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
            if (!newTab.textProperty().get().equalsIgnoreCase("distance table")) {
                ta_error_pane.setText("I'm sorry, this feature isn't implemented yet. Please check again in "
                        + "future versions of this program. ");
                tp_main_display.getSelectionModel().select(oldTab);
            }
        });
    }

    @FXML
    private void generateDistance() throws Exception {
        pi_indicator.setVisible(true);
        btn_display_table.setDisable(true);
        btn_generate.setDisable(true);
        btn_generate_test_board.setDisable(true);
        rb_2d.setDisable(true);
        rb_3d.setDisable(true);
        Thread t = new Thread(() -> {
            BoardGenerator gen = new BoardGenerator();
            for (Piece piece : pieceList) {
                
                int boardSize = Integer.valueOf(tf_board_size.getText());
                if (boardSize == 8 && is2D && obstacleList.isEmpty()){
                    boardSize = 15;
                }
                if (is2D) {
                    piece.setReachabilityTwoDMap(gen.generate2DBoard(piece, obstacleList, boardSize));
                } else {
                    piece.setReachabilityThreeDMap(gen.generate3DBoard(piece, obstacleList, boardSize));
                }
                
//                ThreeDBoardMaker temp = new ThreeDBoardMaker(boardSize);
//
//                temp.setMap(gen.generate3DBoard((Piece)lv_pieces.getItems().get(0), obstacleList, 8));
//
//                try {
//                    temp.start();
//                } catch (Exception ex) {
//                    Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
//                }
            }
            refreshDistancePieceList();
            finished();
        });
        t.start();

    }

    public void finished() {
        pi_indicator.setVisible(false);
        btn_display_table.setDisable(false);
        btn_generate.setDisable(false);
        btn_generate_test_board.setDisable(false);
        rb_2d.setDisable(false);
        rb_3d.setDisable(false);
        
    }

    /**
     * This method is for handling the 'close' action of the program. Simply
     * closes the primary stage.
     */
    @FXML
    private void menu_item_close() {
        Project1.get_stage().close();
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
        root = FXMLLoader.load(getClass().getResource("../fxml/AddObstacle.fxml"));
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
        root = FXMLLoader.load(getClass().getResource("../fxml/AddPiece.fxml"));
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
    
    
    @FXML
    private void generateTestBoard(){
        pieceList.clear();
        obstacleList.clear();
        String whitePawnRelation = "((y1-x1 == 0) ) && ((y2-x2 == 1))";
        String blackPawnRelation = "((y1-x1 == 0) ) && ((y2-x2 == -1))";
        String kingRelation = "((y1-x1 <=1) && (y1-x1 >=-1)) && ((y2-x2 <=1) && (y2-x2 >=-1))";
        
        
        Piece wPawn0, wPawn1, wPawn2, wPawn3, wPawn4, wPawn5, wPawn6, wPawn7,
                bPawn0, bPawn1, bPawn2, bPawn3, bPawn4, bPawn5, bPawn6, bPawn7,
                wKing, wBish0, wBish1, wRook0, wRook1, wKnight0, wKnight1, wQueen,
                bKing, bBish0, bBish1, bRook0, bRook1, bKnight0, bKnight1, bQueen;
        //White Pawns
        wPawn0 = new Piece(new Triple(0,1,0),whitePawnRelation,"White Pawn 0");
        wPawn1 = new Piece(new Triple(1,1,0),whitePawnRelation,"White Pawn 1");
        wPawn2 = new Piece(new Triple(2,1,0),whitePawnRelation,"White Pawn 2");
        wPawn3 = new Piece(new Triple(3,1,0),whitePawnRelation,"White Pawn 3");
        wPawn4 = new Piece(new Triple(4,1,0),whitePawnRelation,"White Pawn 4");
        wPawn5 = new Piece(new Triple(5,1,0),whitePawnRelation,"White Pawn 5");
        wPawn6 = new Piece(new Triple(6,1,0),whitePawnRelation,"White Pawn 6");
        wPawn7 = new Piece(new Triple(7,1,0),whitePawnRelation,"White Pawn 7");
        
        //White Royalty
        wKing = new Piece(new Triple(4, 0, 0), kingRelation, "White King");
        
        //Black Pawns
        bPawn0 = new Piece(new Triple(0,6,0),blackPawnRelation,"Black Pawn 0");
        bPawn1 = new Piece(new Triple(1,6,0),blackPawnRelation,"Black Pawn 1");
        bPawn2 = new Piece(new Triple(2,6,0),blackPawnRelation,"Black Pawn 2");
        bPawn3 = new Piece(new Triple(3,6,0),blackPawnRelation,"Black Pawn 3");
        bPawn4 = new Piece(new Triple(4,6,0),blackPawnRelation,"Black Pawn 4");
        bPawn5 = new Piece(new Triple(5,6,0),blackPawnRelation,"Black Pawn 5");
        bPawn6 = new Piece(new Triple(6,6,0),blackPawnRelation,"Black Pawn 6");
        bPawn7 = new Piece(new Triple(7,6,0),blackPawnRelation,"Black Pawn 7");
        
        //Black Royalty
        bKing = new Piece(new Triple(3, 7, 0), kingRelation, "Black King");

        
        tf_board_size.setText("8");
        pieceList.addAll(Arrays.asList(wKing, bKing, wPawn0, wPawn1, wPawn2, wPawn3, wPawn4, wPawn5, wPawn6, wPawn7
        , bPawn0, bPawn1, bPawn2, bPawn3, bPawn4, bPawn5, bPawn6, bPawn7));
        refreshPieceList();
                
    }
    
    @FXML
    private void generatePieceTable(){
        
        Piece piece = (Piece)lv_distance_piece_list.getSelectionModel().getSelectedItem();
        if (piece != null){
            if (is2D){
                piece.printBoard();
            }else{
                ThreeDBoardMaker coolBoard = new ThreeDBoardMaker(Integer.valueOf(tf_board_size.getText()));
                coolBoard.setMap(piece.getReachabilityThreeDMap());
                try {
                    coolBoard.start();
                } catch (Exception ex) {
                    Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
            
    }
}
