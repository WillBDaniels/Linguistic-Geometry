package edu.wdaniels.lg.proj1.abg;

import edu.wdaniels.lg.proj1.structures.Triple;

/**
 * This class is for defining a 'piece' object on the board. The piece is independent 
 * of any board it might be used on, and is simply for 
 * @author William
 */
public class Piece {
    private Triple<Integer, Integer, Integer> location = new Triple<>(0,0, 0);
    private String reachablityEquation = "";
    private String pieceName = "";
    private int[][][] reachabilityThreeDMap;
    private int[][] reachabilityTwoDMap;

    public int[][][] getReachabilityThreeDMap() {
        return reachabilityThreeDMap;
    }

    public void setReachabilityThreeDMap(int[][][] reachabilityThreeDMap) {
        this.reachabilityThreeDMap = reachabilityThreeDMap;
    }

    public int[][] getReachabilityTwoDMap() {
        return reachabilityTwoDMap;
    }

    public void setReachabilityTwoDMap(int[][] reachabilityTwoDMap) {
        this.reachabilityTwoDMap = reachabilityTwoDMap;
    }
    
    /**
     * This is our public constructor which constructs the entire piece. The 'piece' class doesn't do a whole lot, really. 
     * mostly just holds the state of a few different values for us. 
     * 
     * @param location the location of the piece on a given board. The piece doesn't check to see if the board it's being placed on
     * actually has the specified location, that's for the board to decide!
     * @param reachabilityEquation This is the String-values reachability equation. Should be defined like normal. 
     * @param pieceName This is simply the name of the piece being defined. 
     */
    public Piece(Triple<Integer, Integer, Integer> location, String reachabilityEquation, String pieceName){
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
    public String printBoard(){
        String outputBoard = "";
        for (int x = 0; x < reachabilityTwoDMap.length; x++) {
            for (int y = 0; y < reachabilityTwoDMap.length; y++) {
                outputBoard += " " + reachabilityTwoDMap[y][x];
            }
            outputBoard += "\n";
        }
        System.out.println("board: \n" + outputBoard);
        return outputBoard;

    }
}
