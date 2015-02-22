package edu.wdaniels.lg.abg;

import edu.wdaniels.lg.gui.BoardGenerator;
import edu.wdaniels.lg.structures.Triple;
import java.util.ArrayList;

/**
 * This class is simply a collection of methods (functions) which are used
 * across nearly all grammars that we will be dealing with. Suffice to say, this
 * class will get large.
 *
 * @author William
 */
public class ControlledGrammarFunctions {

    private final boolean[][][] markerMap;

    public ControlledGrammarFunctions(boolean[][][] markerMap) {
        this.markerMap = markerMap;
    }

    /**
     * Thank goodness, a simple function finally. Decrements the length. Only in
     * here for completeness sake, so everything is properly contained in this
     * class.
     *
     * @param l the length we are decrementing
     * @return the value l -1;
     */
    public int f(int l) {
        return l - 1;
    }

    /**
     * This is the function 'next_i(x,l)' which takes in the starting point, the
     * length of the shortest trajectory, and outputs the 'next' location that
     * should be visited in the trajectory. More info about this function can be
     * found in Boris Stilman's Linguistic Geometry book.
     *
     * @param i this is a randomly guessed index
     * @param l This is the length of the shortest trajectory.
     * @param l0
     * @param originalLocation
     * @param initialPiece
     * @param targetPiece
     * @param obsList
     * @return a 'Point' location which corresponds to the next point in the
     * trajectory. Aptly named.
     */
    public Piece next(int i,
            int l, int l0, Piece originalLocation, Piece initialPiece, Piece targetPiece,
            ArrayList<Obstacle> obsList) {
        markerMap[originalLocation.getLocation().getSecond()][originalLocation.getLocation().getFirst()][originalLocation.getLocation().getThird()] = true;
//        System.out.println("originalLocation map: ");
//        originalLocation.print3DBoard();
//        System.out.println("initialPiece map: ");
//        initialPiece.getReachabilityThreeDMap();
//        initialPiece.print3DBoard();
//        System.out.println("targetPiece map: ");
//        targetPiece.getReachabilityThreeDMap();
//        targetPiece.print3DBoard();
        Triple pieceLocation = move(originalLocation, initialPiece, targetPiece, l, l0, i);
        Piece nextPiece = new Piece(pieceLocation, originalLocation.getReachablityEquation(), originalLocation.getPieceName());
        BoardGenerator bg = new BoardGenerator();
        if (pieceLocation == null) {
            return null;
        }
        nextPiece.setReachabilityTwoDMap(bg.generate2DBoard(nextPiece, obsList, originalLocation.getReachabilityTwoDMap().length, false));
//        System.out.println("next pieces board: ");
//        nextPiece.getReachabilityThreeDMap();
//        nextPiece.print3DBoard();
        return nextPiece;
    }

    public Triple<Integer, Integer, Integer> move(Piece originalLocation, Piece startLocation,
            Piece targetLocation, int l, int l0, int i) {
        int[][][] st1, st2, sum;
        sum = sum(originalLocation, targetLocation, l0);
        st1 = st(1, startLocation);
        st2 = st(l0 - l + 1, originalLocation);
        int length = st2.length;
        Triple returnTriple = null;
        int choice = 0;
        System.out.println("st1: ");
        printSummedMap(st1);
        System.out.println("st2:");
        printSummedMap(st2);
        System.out.println("Thi is the current marker map: ");
        printSummedMap(markerMap);

        for (int x = 0; x < length; x++) {
            for (int y = 0; y < length; y++) {
                for (int z = 0; z < length; z++) {
                    if ((sum[x][y][z] > 0) && (st1[x][y][z] > 0) && (st2[x][y][z] > 0)) {
                        if (choice == i) {
                            if (markerMap[x][y][z]) {
                                i++;
                            } else {
                                markerMap[x][y][z] = true;

                                returnTriple = new Triple(y, x, z);
                            }
                            choice++;
                        } else {
                            choice++;
                        }
                    }
                }
            }
        }
        return returnTriple;
    }

    public int[][][] sum(Piece start, Piece target, int length) {
        //We're really assuming the pieces come from the same board, and thus have
        //the same dimension. If they don't... well it'll break. tough luck.

        int[][][] startRp = start.getReachabilityThreeDMap();
        int[][][] targetRp = target.getReachabilityThreeDMap();
//        System.out.println("starting map: ");
//        printSummedMap(startRp);
//        System.out.println("Target map: ");
//        printSummedMap(targetRp);
        int[][][] summedMap = new int[startRp.length][startRp.length][startRp.length];
        for (int x = 0; x < startRp.length; x++) {
            for (int y = 0; y < startRp[x].length; y++) {
                for (int z = 0; z < startRp[x][y].length; z++) {
                    summedMap[x][y][z] = (startRp[x][y][z] - 1) + (targetRp[x][y][z] - 1);

                    if (summedMap[x][y][z] != (length - 1)) {
                        summedMap[x][y][z] = 0;
                    }
                }
            }
        }
        //System.out.println("Summed map: ");
        //printSummedMap(summedMap);
        return summedMap;
    }

    private void printSummedMap(int[][][] inputMap) {
        for (int x = 0; x < inputMap.length; x++) {
            for (int y = 0; y < inputMap.length; y++) {
                System.out.print(inputMap[x][y][0] + " ");
            }
            System.out.println("\n");
        }
    }

    private void printSummedMap(boolean[][][] inputMap) {
        for (int x = 0; x < inputMap.length; x++) {
            for (int y = 0; y < inputMap.length; y++) {
                System.out.print((inputMap[x][y][0] ? "T" : "F") + " ");
            }
            System.out.println("\n");
        }
    }

    /**
     * This is the st method... yea. Not much else to say.
     *
     * @param radius
     * @param startingLocation
     * @return
     */
    public int[][][] st(int radius, Piece startingLocation) {
        int[][][] pieceRp = startingLocation.getReachabilityThreeDMap();
        int[][][] matrix = new int[pieceRp.length][pieceRp.length][pieceRp.length];
        for (int x = 0; x < pieceRp.length; x++) {
            for (int y = 0; y < pieceRp.length; y++) {
                for (int z = 0; z < pieceRp.length; z++) {
                    if (pieceRp[x][y][z] - 1 == radius && pieceRp[x][y][z] - 1 > 0) {
                        matrix[x][y][z] = 1;
                    } else {
                        matrix[x][y][z] = 0;
                    }
                }
            }
        }
        return matrix;
    }
}
