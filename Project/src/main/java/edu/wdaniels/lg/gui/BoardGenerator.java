package edu.wdaniels.lg.gui;

import edu.wdaniels.lg.abg.Obstacle;
import edu.wdaniels.lg.abg.Piece;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.mvel2.MVEL;

/**
 *
 * @author William
 */
public class BoardGenerator {

    public static boolean multipleRunFlag = false;
    public static Serializable longTermCompile = null;
    private Serializable compiled = null;

    public int[][] generate2DBoard(Piece piece, ArrayList<Obstacle> obstacles, int boardSize, boolean center) {
        int[][] board = new int[boardSize][boardSize];
        boolean[][] markerBoard = new boolean[boardSize][boardSize];
//        for (int a = 0; a < boardSize; a++) {
//            for (int b = 0; b < boardSize; b++) {
//                markerBoard[a][b] = false;
//            }
//        }
        if (center) {
            board[new Double(Math.ceil(boardSize / 2)).intValue()][new Double(Math.ceil(boardSize / 2)).intValue()] = 1;
            markerBoard[new Double(Math.ceil(boardSize / 2)).intValue()][new Double(Math.ceil(boardSize / 2)).intValue()] = true;
        } else {
            board[piece.getLocation().getSecond()][piece.getLocation().getFirst()] = 1;
            markerBoard[piece.getLocation().getSecond()][piece.getLocation().getFirst()] = true;
        }
        boolean foundNewMove = true;
        int i = 1;
        //compiled = MVEL.compileExpression(piece.getReachablityEquation());
        if (!multipleRunFlag) {
            compiled = MVEL.compileExpression(piece.getReachablityEquation());
        } else {
            if (longTermCompile != null) {
                //System.out.println("Successfully using long term compile..");
                compiled = longTermCompile;
            } else {
                compiled = MVEL.compileExpression(piece.getReachablityEquation());
            }
        }
        while (foundNewMove) {
            foundNewMove = false;
            for (int x = 0; x < boardSize; x++) {
                for (int y = 0; y < boardSize; y++) {
                    if (board[x][y] == i) {
                        Map<String, Integer> vars = new HashMap<>();
                        vars.put("x1", y);
                        vars.put("x2", x);
                        Boolean result;
                        for (int xInner = 0; xInner < boardSize; xInner++) {
                            for (int yInner = 0; yInner < boardSize; yInner++) {
                                vars.put("y1", yInner);
                                vars.put("y2", xInner);
                                try {

                                    result = (Boolean) MVEL.executeExpression(compiled, vars);
                                    if (result) {
                                        if (!markerBoard[xInner][yInner]) {
                                            markerBoard[xInner][yInner] = true;
                                            foundNewMove = true;
                                            board[xInner][yInner] = i + 1;
                                        }

                                    }

                                } catch (Exception e) {
                                    e.printStackTrace(System.err);
                                }
                            }
                        }
                    }
                }
            }

            i++;
        }
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board.length; y++) {
                board[x][y] = board[x][y] - 1;
            }
        }

        obstacles.stream()
                .forEach((obs) -> {
                    board[obs.getObstacleLocation().getFirst()][obs.getObstacleLocation().getSecond()] = -1;
                }
                );
        //printBoard(board);
        return board;
    }
//
//    public int[][] produceBasicDistance2D(int startX, int startY, int size) {
//        int[][] outputMap = new int[size][size];
//        for (int x = 0; x < size; x++) {
//            for (int y = 0; y < size; y++) {
//                outputMap
//            }
//        }
//    }

    public void printBoard(int[][] input) {
        String outputBoard = "";
        for (int x = 0; x < input.length; x++) {
            for (int y = 0; y < input.length; y++) {
                System.out.printf("%5d ", input[x][y]);
            }
            System.out.println();
        }
        System.out.println("board: \n" + outputBoard);
    }

    public int[][][] generate3DBoard(Piece piece, ArrayList<Obstacle> obstacles, int boardSize) {
        int[][][] board = new int[boardSize][boardSize][boardSize];
        boolean[][][] markerBoard = new boolean[boardSize][boardSize][boardSize];
        for (int a = 0; a < boardSize; a++) {
            for (int b = 0; b < boardSize; b++) {
                for (int c = 0; c < boardSize; c++) {
                    markerBoard[a][b][c] = false;
                }
            }
        }
        board[piece.getLocation().getSecond()][piece.getLocation().getFirst()][piece.getLocation().getThird()] = 1;
        markerBoard[piece.getLocation().getSecond()][piece.getLocation().getFirst()][piece.getLocation().getThird()] = true;
        //printBoard(board, markerBoard);
        boolean foundNewMove = true;
        int i = 1;
        if (!multipleRunFlag) {
            compiled = MVEL.compileExpression(piece.getReachablityEquation());
        } else {
            if (longTermCompile != null) {
                //System.out.println("Successfully using long term compile..");
                compiled = longTermCompile;
            } else {
                compiled = MVEL.compileExpression(piece.getReachablityEquation());
            }
        }
        while (foundNewMove) {
            //System.out.println("i is: " + i);
            foundNewMove = false;
            for (int x = 0; x < boardSize; x++) {
                for (int y = 0; y < boardSize; y++) {
                    for (int z = 0; z < boardSize; z++) {
                        if (board[x][y][z] == i) {
                            Map<String, Integer> vars = new HashMap<>();
                            vars.put("x1", y);
                            vars.put("x2", x);
                            vars.put("x3", z);
                            Boolean result;
                            for (int xInner = 0; xInner < boardSize; xInner++) {
                                for (int yInner = 0; yInner < boardSize; yInner++) {
                                    for (int zInner = 0; zInner < boardSize; zInner++) {
                                        vars.put("y1", yInner);
                                        vars.put("y2", xInner);
                                        vars.put("y3", zInner);
                                        try {
                                            result = (Boolean) MVEL.executeExpression(compiled, vars);
                                            if (result) {
                                                if (!markerBoard[xInner][yInner][zInner]) {
                                                    markerBoard[xInner][yInner][zInner] = true;
                                                    foundNewMove = true;
                                                    //System.out.println("it's true! i is: " + i + " xInner: " + xInner + " yInner: " + yInner);
                                                    board[xInner][yInner][zInner] = i + 1;
                                                }

                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace(System.err);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            //printBoard(board, markerBoard);
            i++;
        }
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board.length; y++) {
                for (int z = 0; z < board.length; z++) {
                    board[x][y][z] = board[x][y][z] - 1;
                }
            }
        }
        //printBoard(board, markerBoard);
        obstacles.stream().forEach((obs) -> {
            board[obs.getObstacleLocation().getFirst()][obs.getObstacleLocation().getSecond()][obs.getObstacleLocation().getThird()] = -1;
        });
        return board;
    }

}
