package edu.wdaniels.lg.abg;

import edu.wdaniels.lg.gui.BoardGenerator;
import edu.wdaniels.lg.gui.PrimaryController;
import edu.wdaniels.lg.structures.Pair;
import edu.wdaniels.lg.structures.TreeNode;
import edu.wdaniels.lg.structures.Triple;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.mvel2.MVEL;

/**
 * This class is simply a collection of methods (functions) which are used
 * across nearly all grammars that we will be dealing with. Suffice to say, this
 * class will get large.
 *
 * @author William
 */
public class ControlledGrammarFunctions {

    //private Piece temp = null;
    private final boolean[][][] markerMap;
    public static TreeNode<Pair<Triple<Integer, Integer, Integer>, Boolean>> trajectoryTree = new TreeNode<>(new Pair(null, true));
    public static TreeNode<Pair<Triple<Integer, Integer, Integer>, Boolean>> currentNode = null;
    private BoardGenerator bg = new BoardGenerator();

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
        Triple pieceLocation = move(originalLocation, initialPiece, targetPiece, l, l0, i);
        Piece nextPiece = new Piece(originalLocation.getPieceType(), pieceLocation, originalLocation.getReachablityEquation(), originalLocation.getPieceName());
        if (pieceLocation == null) {
            return null;
        }

        if (PrimaryController.getController().is2D) {
            if (originalLocation.isGraphicalReachability()){
                nextPiece.setReachabilityTwoDMap(decrementMap(initialPiece.getReachabilityTwoDMap()));
            }else{
                nextPiece.setReachabilityTwoDMap(bg.generate2DBoard(nextPiece, obsList, originalLocation.getReachabilityTwoDMap().length, false));
            }
        } else {
            nextPiece.setReachabilityThreeDMap(bg.generate3DBoard(nextPiece, obsList, originalLocation.getReachabilityTwoDMap().length));
        }
        return nextPiece;
    }

    private int[][] decrementMap(int[][] inputMap){
        int[][] outputMap = new int[inputMap.length][inputMap.length];
        for (int i = 0; i < inputMap.length; i++){
            for (int y = 0; y < inputMap.length; y++){
                outputMap[i][y] = inputMap[i][y] -1;
            }
        }
        return outputMap;
    }

    public Triple<Integer, Integer, Integer> move(Piece originalLocation, Piece startLocation,
            Piece targetLocation, int l, int l0, int i) {
        int[][][] st1, st2, sum;
        sum = sum(originalLocation, targetLocation, l0);
        st1 = st(1, startLocation);
        st2 = st(l0 - l + 1, originalLocation);

        int length = st2.length;
        int choice;
        List<Triple<Integer, Integer, Integer>> availableOptions = new ArrayList<>();

        for (int x = 0; x < length; x++) {
            for (int y = 0; y < length; y++) {
                for (int z = 0; z < length; z++) {
                    if ((sum[x][y][z] > 0) && (st1[x][y][z] > 0) && (st2[x][y][z] > 0)) {
                        availableOptions.add(new Triple(y, x, z));
                    }
                }
            }
        }
        if (availableOptions.isEmpty()) {
            //We're ona leaf that has no more moves, so we set the 'visited' to true, since well.. it's true!
            if (currentNode != null) {
                currentNode.data.setSecond(true);
            }
            return null;
        }
        if (currentNode == null) {
            currentNode = trajectoryTree;
        }
        boolean foundNewTriple = false;
        TreeNode<Pair<Triple<Integer, Integer, Integer>, Boolean>> temp = currentNode;
        if (currentNode.isRoot()) {
            if (currentNode.children.isEmpty()) {

                foundNewTriple = true;
                for (Triple<Integer, Integer, Integer> option : availableOptions) {
                    //add all the starting options to the root
                    temp.addChild(new Pair(option, false));

                }

                currentNode = temp.children.get(0);
            } else {
                for (TreeNode<Pair<Triple<Integer, Integer, Integer>, Boolean>> child : currentNode.children) {
                    if (!child.data.getSecond()) {
                        foundNewTriple = true;
                        currentNode = child;
                        break;
                    }
                }
            }
        } else {
            if (currentNode.children.isEmpty() && !currentNode.data.getSecond()) {
                //currentNode.data.setSecond(true);
                foundNewTriple = true;
                for (Triple<Integer, Integer, Integer> option : availableOptions) {
                    //add all the starting options to the root
                    temp.addChild(new Pair(option, false));
                }
                currentNode = temp.children.get(0);
            } else {
                for (TreeNode<Pair<Triple<Integer, Integer, Integer>, Boolean>> child : currentNode.children) {
                    if (!child.data.getSecond()) {
                        foundNewTriple = true;
                        currentNode = child;
                    }
                }
            }
        }
        if (!foundNewTriple) {
            //all of the children have been visited.. so we leave this node alone from now on!
            temp.data.setSecond(true);
            return null;
        }

        //markerMap[choiceTriple.getSecond()][choiceTriple.getFirst()][choiceTriple.getThird()] = true;
        return (currentNode.data.getFirst());

    }

    public int[][][] sum(Piece start, Piece target, int length) {
        String kingRelation = "((y1-x1 <=1) && (y1-x1 >=-1)) && ((y2-x2 <=1) && (y2-x2 >=-1))";

        //We're really assuming the pieces come from the same board, and thus have
        //the same dimension. If they don't... well it'll break. tough luck.
        int[][][] startRp = start.getReachabilityThreeDMap();
        BoardGenerator bgInner = new BoardGenerator();
        BoardGenerator.multipleRunFlag = false;
        BoardGenerator.longTermCompile = null;
        Piece temp = new Piece("", target.getLocation(), kingRelation, "");
        temp.setReachablityEquation(kingRelation);
        temp.setReachabilityTwoDMap(bgInner.generate2DBoard(temp, new ArrayList<>(), PrimaryController.getController().getBoardSize(), false));
        int[][][] targetRp = temp.getReachabilityThreeDMap();

        int[][][] summedMap = new int[startRp.length][startRp.length][startRp.length];
        for (int x = 0; x < startRp.length; x++) {
            for (int y = 0; y < startRp[x].length; y++) {
                for (int z = 0; z < startRp[x][y].length; z++) {
                    summedMap[x][y][z] = (startRp[x][y][z]) + (targetRp[x][y][z]);

                    if (summedMap[x][y][z] != (length)) {
                        summedMap[x][y][z] = 0;
                    }else{
                        //System.out.println("sum isn't 0, it is: " + summedMap[x][y][z] + "  at : " + x + " , " + y  + " , " + z);
                    }
                }
            }
        }
        return summedMap;
    }

    /**
     * Returns a pseudo-random number between min and max, inclusive. The
     * difference between min and max can be at most
     * <code>Integer.MAX_VALUE - 1</code>.
     *
     * @param min Minimum value
     * @param max Maximum value. Must be greater than min.
     * @return Integer between min and max, inclusive.
     * @see java.util.Random#nextInt(int)
     */
    public int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    private void printSummedMap(boolean[][][] inputMap) {
        for (boolean[][] inputMap1 : inputMap) {
            for (int y = 0; y < inputMap.length; y++) {
                System.out.print((inputMap1[y][0] ? "T" : "F") + " ");
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
            for (int y = 0; y < pieceRp[x].length; y++) {
                for (int z = 0; z < pieceRp[x][y].length; z++) {
                    if (pieceRp[x][y][z] == radius) {
                        //System.out.println("Radius matched!: " + matrix[x][y][z] + " at: " + x + " , " + y + " , " + z);
                        matrix[x][y][z] = 1;
                    } else {
                        matrix[x][y][z] = 0;
                    }
                }
            }
        }
        return matrix;
    }

    ///////////////////////////////////////////////////////////////////////////////////
    //Methods for the grammar of zones
    /**
     *
     * @param piece
     * @return
     */
    //TODO make sure the piece locations are zero-based, if they're not, we'll need to subtract one
    // to make this work out correctly.
    private int calculateLinearLocation(Piece piece) {
        int n = PrimaryController.getController().getBoardSize();
        return ((piece.getLocation().getFirst()) + piece.getLocation().getSecond() * n);
    }

    //TODO make sure the piece locations are zero-based, if they're not, we'll need to subtract one
    // to make this work out correctly.
    private int calculateLinearLocation(Triple<Integer, Integer, Integer> location) {
        int n = PrimaryController.getController().getBoardSize();
        return ((location.getFirst()) + location.getSecond() * n);
    }

    public int ALPHA(int[] nexttime, int x, Piece p0, List<Triple<Integer, Integer, Integer>> t0, int k) {
        int n = PrimaryController.getController().getBoardSize();
        if (DIST(x, p0, t0) != (2 * (n * n)) && (nexttime[x] != (2 * (n * n)))) {
            return (max(nexttime[x], k));
        }
        if ((DIST(x, p0, t0) != (2 * (n * n))) && (nexttime[x] == (2 * (n * n)))) {
            return k;
        } else {
            return nexttime[x];
        }
    }

    private int max(int first, int second) {
        return (first > second ? first : second);
    }

    /**
     * This method calculates the 'distance' function for the grammar of zones.
     * Essentially, it allows for the function to determine the 'distance' a
     * given point along the trajectory is from the certain piece.
     *
     * @param x
     * @param p0
     * @param t0
     * @return
     */
    public int DIST(int x, Piece p0, List<Triple<Integer, Integer, Integer>> t0) {
        int i = 0;
        for (Triple<Integer, Integer, Integer> location : t0) {
            int first = location.getThird();
            int y = location.getSecond();
            int z = location.getFirst();
            Triple newLoc = new Triple(first, y, z);
            if (i == 0) {
                i++;
                continue;
            }
            if (i == t0.size() - 1) {
                continue;
            }
            if (x == calculateLinearLocation(newLoc)) {
                return i + 1;
            }
            i++;
        }
        return (PrimaryController.getController().getBoardSize() * PrimaryController.getController().getBoardSize()) * 2;
    }

    /**
     *
     * @param p0
     * @param t0
     * @param w
     * @param r
     * @return
     */
    public int g(Piece p0, List<Triple<Integer, Integer, Integer>> t0, int[] w, int r) {
        int n = PrimaryController.getController().getBoardSize();
        if (DIST(r, p0, t0) < (2 * (n * n))) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * This function (f) is the primary 'search' mechanism of the grammar of
     * zones. This goes through the table both first sear
     *
     *
     * @param u
     * @param TIME
     * @param v
     * @param l
     * @return
     */
    public Triple<Integer, Integer, Integer> f(Triple<Integer, Integer, Integer> u,
            int[] TIME, int[] v) {
        int n = PrimaryController.getController().getBoardSize();
        n = n * n;
        if (((u.getFirst() != n) && (u.getThird() > 0)) || ((u.getSecond() == n) && (u.getThird() <= 0))) {
            //System.out.println("Incrementing x, u is: " + u);
            return new Triple(u.getFirst() + 1, u.getSecond(), u.getThird());
        } else {
            //System.out.println("Here, incrementing y (and possibly setting l), u is: " + u);
            //due to the array indexing, normally, it's time(y+1), but that's 1-based, so mine is y+1 - 1 for each
            if (u.getSecond() + 1 > (n - 1)) {
                return new Triple(1, n, 0);
            }

            return (new Triple(1, u.getSecond() + 1, (TIME[u.getSecond() + 1] * v[u.getSecond() + 1])));
        }
    }

    /**
     * This is the function that initializes the NEXTTIME map, but really it
     * just returns 2n (n is the size of the board) unless you're on the piece,
     * in which case it returns r.
     *
     * @param u The starting piece location
     * @param r The value of 'nexttime' at r
     * @param n the size of the board.
     * @return 2n (n is the size of the board) unless you're on the piece, in
     * which case it returns r.
     */
    public int init(Triple<Integer, Integer, Integer> u, int r, int n) {
        if ((u.getFirst() == 0) && (u.getSecond() == 0)
                && (u.getThird() == 0)) {
            return 2 * (n * n);
        } else {
            return r;
        }
    }

    /**
     * This simply compares two pieces and makes sure they are opposing. Pretty
     * basic.
     *
     * @param p1 the first piece
     * @param p2 the second piece.
     * @return true if they are opposing, false otherwise.
     */
    public boolean OPPOSE(Piece p1, Piece p2) {
        return !p1.getPieceType().equalsIgnoreCase(p2.getPieceType());
    }

    /**
     * This function computes MAP_xp (y) = l
     *
     * @param startPiece
     * @param targetPiece
     * @return
     */
    public int MAP(Piece startPiece, Piece targetPiece) {
        int x = targetPiece.getLocation().getFirst();
        int y = targetPiece.getLocation().getSecond();
        int z = targetPiece.getLocation().getThird();
//        if (PrimaryController.getController().is2D && PrimaryController.getController().obstacleList.isEmpty()
//                && ((Integer.valueOf(PrimaryController.getController().tf_board_size.getText()) == 8))) {
//            DistanceFinder df = new DistanceFinder();
//            return df.find2DChessDistance(startPiece, targetPiece);
//        }
        //System.out.println("We're looking for the piece at: " + x + " , " + y + " , " + z + " which is: " + startPiece.getReachabilityThreeDMap()[x][y][z]);
        //startPiece.print3DBoard();
        if (startPiece.getReachabilityThreeDMap()[y][x][z] > 0) {
            return startPiece.getReachabilityThreeDMap()[y][x][z] - 1;
        } else {
            return -1;
        }
    }

    public List<Triple<Integer, Integer, Integer>> h(int choice, Piece startingLocation, Piece targetLocation, int l) {
        List<List<Triple<Integer, Integer, Integer>>> outputTraj = new ArrayList<>();
        //System.out.println("This is my startingLocation: " + startingLocation.getLocation() + " and my target: " + targetLocation.getLocation());

        int i = 0;
        int innerl = l;
        boolean goingDown = true;
        while (outputTraj.isEmpty()) {
            BoardGenerator.multipleRunFlag = true;
            BoardGenerator.longTermCompile = MVEL.compileExpression(startingLocation.getReachablityEquation());
            GrammarGt1 gt1 = new GrammarGt1(PrimaryController.getController().getBoardSize(), innerl, startingLocation, targetLocation);
            List<Triple<Integer, Integer, Integer>> traj = gt1.produceTrajectory();
            if (traj.size() > 0) {
                outputTraj.add(traj);
            } else {
                if (innerl > 0 && goingDown) {
                    innerl -= 1;
                } else {
                    goingDown = false;
                    innerl += 1;
                    if (innerl > PrimaryController.getController().getBoardSize()) {
                        goingDown = true;
                    }
                }
            }
            i++;
        }
        ControlledGrammarFunctions.currentNode = null;
        ControlledGrammarFunctions.trajectoryTree = new TreeNode<>(new Pair(null, true));
        BoardGenerator.multipleRunFlag = false;
        BoardGenerator.longTermCompile = null;
        if (choice < outputTraj.size()) {
            //outputTraj.get(choice).remove(0);
            return outputTraj.get(choice);
        } else {
            if (outputTraj.isEmpty()) {
                return null;
            }
            return outputTraj.get(outputTraj.size() - 1);
        }
    }

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    //********************************************************************************
    public class u {

        private int x;
        private int y;
        private int l;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getL() {
            return l;
        }

        public void setL(int l) {
            this.l = l;
        }

        @Override
        public String toString() {
            return "( " + x + " ," + y + " ," + l + " )";
        }
    }

}
