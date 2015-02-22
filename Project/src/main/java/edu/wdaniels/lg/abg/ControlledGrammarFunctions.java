package edu.wdaniels.lg.abg;

import edu.wdaniels.lg.gui.BoardGenerator;
import edu.wdaniels.lg.structures.Triple;
import java.util.ArrayList;

/**
 * This class is simply a collection of static methods (functions) which are
 * used across nearly all grammars that we will be dealing with. Suffice to say,
 * this class will get large.
 *
 * @author William
 */
public class ControlledGrammarFunctions {

    /**
     * Thank goodness, a simple function finally. Decrements the length. Only in
     * here for completeness sake, so everything is properly contained in this
     * class.
     *
     * @param l the length we are decrementing
     * @return the value l -1;
     */
    public static int f(int l) {
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
    public static Piece next(int i,
            int l, int l0, Piece originalLocation, Piece initialPiece, Piece targetPiece,
            ArrayList<Obstacle> obsList) {
        Triple pieceLocation = move(originalLocation, initialPiece, targetPiece, l, l0, i);

        Piece nextPiece = new Piece(pieceLocation, originalLocation.getReachablityEquation(), originalLocation.getPieceName());
        BoardGenerator bg = new BoardGenerator();
        nextPiece.setReachabilityThreeDMap(bg.generate3DBoard(nextPiece, obsList, originalLocation.getReachabilityThreeDMap().length));
        return nextPiece;
    }

    public static Triple<Integer, Integer, Integer> move(Piece originalLocation, Piece startLocation,
            Piece targetLocation, int l, int l0, int i) {
        int[][][] st1, st2, sum;
        sum = sum(startLocation, targetLocation, l0);
        st1 = st(1, startLocation);
        st2 = st(l0 - l + 1, originalLocation);
        int length = st2.length;
        Triple returnTriple = null;
        int choice = 0;
        for (int x = 0; x < length; x++) {
            for (int y = 0; y < length; y++) {
                for (int z = 0; z < length; z++) {
                    if ((sum[x][y][z] > 0) && (st1[x][y][z] > 0) && (st2[x][y][z] > 0)) {
                        if (choice == i) {
                            returnTriple = new Triple(x, y, z);
                        } else {
                            choice++;
                        }
                    }
                }
            }
        }
        return returnTriple;
    }

    public static int[][][] sum(Piece start, Piece target, int length) {
        //We're really assuming the pieces come from the same board, and thus have
        //the same dimension. If they don't... well it'll break. tough luck.
        int[][][] startRp = start.getReachabilityThreeDMap();
        int[][][] targetRp = target.getReachabilityThreeDMap();
        int[][][] summedMap = new int[startRp.length][startRp.length][startRp.length];
        for (int x = 0; x < startRp.length; x++) {
            for (int y = 0; y < startRp[x].length; y++) {
                for (int z = 0; z < startRp[x][y].length; z++) {
                    summedMap[x][y][z] = startRp[x][y][z] + targetRp[x][y][z];
                    if (summedMap[x][y][z] != length) {
                        summedMap[x][y][z] = 0;
                    }
                }
            }
        }
        return summedMap;
    }

    /**
     * This is the st method... yea. Not much else to say.
     *
     * @param radius
     * @param startingLocation
     * @return
     */
    public static int[][][] st(int radius, Piece startingLocation) {
        int[][][] pieceRp = startingLocation.getReachabilityThreeDMap();
        int[][][] matrix = new int[pieceRp.length][pieceRp.length][pieceRp.length];
        for (int x = 0; x < pieceRp.length; x++) {
            for (int y = 0; y < pieceRp.length; y++) {
                for (int z = 0; z < pieceRp.length; z++) {
                    if (pieceRp[x][y][z] <= radius) {
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
