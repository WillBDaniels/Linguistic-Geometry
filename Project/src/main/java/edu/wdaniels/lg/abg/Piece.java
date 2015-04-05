package edu.wdaniels.lg.abg;

import edu.wdaniels.lg.structures.Triple;
import javafx.scene.image.Image;

/**
 * This class is for defining a 'piece' object on the board. The piece is
 * independent of any board it might be used on, and is simply for
 *
 * @author William
 */
public class Piece {

    private Triple<Integer, Integer, Integer> location = new Triple<>(0, 0, 0);
    private String reachablityEquation = "";
    private String pieceName = "";
    private int[][][] reachabilityThreeDMap;
    private int[][] reachabilityTwoDMap;
    private String pieceType = "";
    private Image pieceImage;

    public int[][][] getReachabilityThreeDMap() {
        if (reachabilityThreeDMap == null && reachabilityTwoDMap != null) {
            //System.out.println("Generating 3d map");
            reachabilityThreeDMap = getThreeDMapFromTwoD(reachabilityTwoDMap);
            //print3DBoard();
        }
        return reachabilityThreeDMap;
    }

    private int[][] getTwoDMapFromThreeD(int[][][] threeDMap) {
        if (threeDMap == null) {
            return null;
        }
        int[][] outputMap = new int[threeDMap.length][threeDMap.length];
        for (int x = 0; x < threeDMap.length; x++) {
            for (int y = 0; y < threeDMap.length; y++) {
                outputMap[x][y] = threeDMap[x][y][0];
            }
        }
        return outputMap;
    }

    private int[][][] getThreeDMapFromTwoD(int[][] twoDMap) {
        if (twoDMap == null) {
            return null;
        }
        int[][][] outputMap = new int[twoDMap.length][twoDMap.length][twoDMap.length];
        for (int x = 0; x < twoDMap.length; x++) {
            for (int y = 0; y < twoDMap.length; y++) {
                for (int z = 0; z < twoDMap.length; z++) {
                    if (z == 0) {
                        outputMap[x][y][z] = twoDMap[x][y];
                    } else {
                        outputMap[x][y][z] = 0;
                    }
                }
            }
        }
        return outputMap;
    }

    public void setReachabilityThreeDMap(int[][][] reachabilityThreeDMap) {
        this.reachabilityThreeDMap = reachabilityThreeDMap;
    }

    public int[][] getReachabilityTwoDMap() {
        if (reachabilityTwoDMap == null && reachabilityThreeDMap != null) {
            //System.out.println("Generating 2d from 3d");
            reachabilityTwoDMap = getTwoDMapFromThreeD(reachabilityThreeDMap);
            printBoard();
        }
        return reachabilityTwoDMap;
    }

    public void setReachabilityTwoDMap(int[][] reachabilityTwoDMap) {
        this.reachabilityTwoDMap = reachabilityTwoDMap;
    }

    /**
     * This is our public constructor which constructs the entire piece. The
     * 'piece' class doesn't do a whole lot, really. mostly just holds the state
     * of a few different values for us.
     *
     * @param pieceType This is the actual type of the piece (aka: white piece,
     * black piece)
     * @param location the location of the piece on a given board. The piece
     * doesn't check to see if the board it's being placed on actually has the
     * specified location, that's for the board to decide!
     * @param reachabilityEquation This is the String-values reachability
     * equation. Should be defined like normal.
     * @param pieceName This is simply the name of the piece being defined.
     */
    public Piece(String pieceType, Triple<Integer, Integer, Integer> location, String reachabilityEquation, String pieceName) {
        this.pieceType = pieceType;
        this.location = location;
        this.reachablityEquation = reachabilityEquation;
        this.pieceName = pieceName;
    }

    public Triple<Integer, Integer, Integer> getLocation() {
        return location;
    }

    public void setLocation(Triple<Integer, Integer, Integer> location) {
        this.location = location;
    }

    public void setPieceImage(Image img) {
        this.pieceImage = img;
    }

    public Image getPieceImage() {
        return pieceImage;
    }

    public String getReachablityEquation() {
        return reachablityEquation;
    }

    public void setReachablityEquation(String reachablityEquation) {
        this.reachablityEquation = reachablityEquation;
    }

    public String getPieceName() {
        return pieceName;
    }

    public void setPieceName(String pieceName) {
        this.pieceName = pieceName;
    }

    public void print3DBoard() {
        String outputBoard = "";
        for (int x = 0; x < reachabilityThreeDMap.length; x++) {
            for (int y = 0; y < reachabilityThreeDMap.length; y++) {
                outputBoard += " " + reachabilityThreeDMap[x][y][0];
            }
            outputBoard += "\n";
        }
        System.out.println("board: \n" + outputBoard);
    }

    public String printBoard() {
        String outputBoard = "";
        for (int x = 0; x < reachabilityTwoDMap.length; x++) {
            for (int y = 0; y < reachabilityTwoDMap.length; y++) {
                outputBoard += " " + reachabilityTwoDMap[x][y];
            }
            outputBoard += "\n";
        }
        System.out.println("board: \n" + outputBoard);
        return outputBoard;
    }

    public void setPieceType(String type) {
        pieceType = type;
    }

    public String getPieceType() {
        return pieceType;
    }
}
