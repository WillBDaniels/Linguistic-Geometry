package edu.wdaniels.lg.abg;

import edu.wdaniels.lg.gui.PrimaryController;
import edu.wdaniels.lg.structures.Triple;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is the instantiation of the grammar of shortest trajectories. This
 * grammar takes in a starting location, and then produces a 'bundle' of
 * trajectories from which someone could then display the trajectories. In
 * specific, a trajectory bundle is made up of 'trajectories', which in turn are
 * made up of 'points'. Each point should correspond to a discretized point in
 * space.
 *
 *
 * @author William
 */
public class GrammarGt1 {

    private final int n;
    private int l;
    private final int l0;
    private final Piece startPiece;
    private final Piece targetPiece;
    private Step currentStep = Step.Q0;
    private final ControlledGrammarFunctions cgf;
    private final boolean[][][] markerMap;

    public GrammarGt1(int n, int l0, Piece startPiece, Piece targetPiece) {
        this.n = n;
        this.l0 = l0;
        this.l = l0;
        this.startPiece = startPiece;
        this.targetPiece = targetPiece;
        this.markerMap = new boolean[n][n][n];
        cgf = new ControlledGrammarFunctions(markerMap);

    }

    private enum Step {

        Q0, Q1, Q2, Q3
    };

    /**
     * This method checks all of our pass/fail conditions for a given step of
     * the grammar, and then returns the next 'step' of the process we should go
     * to. if the step returned is null, we simply stop.
     *
     * @param currentStep this is the current step in the process, ex: Q1, Q2,
     * Q3
     * @return the next step in the sequence, if there is one, null otherwise.
     */
    private Step checkFailParameter(Step currentStep) {
        Step outputStep;
        switch (currentStep) {
            case Q0:
                outputStep = Step.Q1;
                break;
            case Q1:
                if (l != getMapDistance(startPiece, targetPiece) || !((0 < l) && (l < n))) {
                    outputStep = null;
                } else {
                    outputStep = Step.Q2;
                }
                break;
            case Q2:
                if (l >= 1) {
                    outputStep = Step.Q2;
                } else {
                    outputStep = Step.Q3;
                }
                break;
            case Q3:
                outputStep = null;
                break;
            default:
                outputStep = null;
        }
        return outputStep;
    }

    
    public boolean hasMoreTrajectories(){
        return true;
    }
    
    /**
     * This is the primary public facing method that is used to collect the
     * trajectory points into a single 'trajectory' woo.
     *
     * @return A list of Pieces that compromise the trajectory. if it's empty,
     * then there must not have been any trajectories (unreachable piece)
     */
    public List<Triple<Integer, Integer, Integer>> produceTrajectory() {
        List<Triple<Integer, Integer, Integer>> outputTrajectory = new ArrayList<>();
        Triple<Integer, Integer, Integer> tempLoc;
        S start = new S(startPiece, targetPiece, l0);
        A temp = new A(start.x, start.y, start.l);
        Step nextStep = null;
        while (currentStep != null) {
            currentStep = checkFailParameter(currentStep);
            if (currentStep == null) {
                break;
            }
            switch (currentStep) {
                case Q1:
                    temp = new A(start.x, start.y, start.l);
                    break;
                case Q2:
                    tempLoc = temp.x.getLocation();
                    outputTrajectory.add(new Triple(tempLoc.getThird(), tempLoc.getSecond(),
                            tempLoc.getFirst()));
                    temp = new A(cgf.next(0, temp.l, l0, startPiece, temp.x, targetPiece,
                            PrimaryController.getController().obstacleList), targetPiece, cgf.f(l));
                    l = temp.l;
                    break;
                case Q3:
                    tempLoc = temp.y.getLocation();
                    outputTrajectory.add(new Triple(tempLoc.getThird(), tempLoc.getSecond(),
                            tempLoc.getFirst()));
            }
        }
        return outputTrajectory;
    }

    /**
     * This function computes MAP_xp (y) = l
     *
     * @param startPiece
     * @param targetPiece
     * @return
     */
    private int getMapDistance(Piece startPiece, Piece targetPiece) {
        int x = targetPiece.getLocation().getSecond();
        int y = targetPiece.getLocation().getFirst();
        int z = targetPiece.getLocation().getThird();
//        if (PrimaryController.getController().is2D && PrimaryController.getController().obstacleList.isEmpty()
//                && ((Integer.valueOf(PrimaryController.getController().tf_board_size.getText()) == 8))) {
//            DistanceFinder df = new DistanceFinder();
//            return df.find2DChessDistance(startPiece, targetPiece);
//        }
        System.out.println("We're looking for the piece at: " + x + " , " + y + " , " + z + " which is: " + startPiece.getReachabilityThreeDMap()[x][y][z]);
        startPiece.print3DBoard();
        if (startPiece.getReachabilityThreeDMap()[x][y][z] > 0) {
            return startPiece.getReachabilityThreeDMap()[x][y][z];
        } else {
            return -1;
        }
    }

    private class S {

        public Piece x;

        public Piece getX() {
            return x;
        }

        public void setX(Piece x) {
            this.x = x;
        }

        public Piece getY() {
            return y;
        }

        public void setY(Piece y) {
            this.y = y;
        }

        public int getL() {
            return l;
        }

        public void setL(int l) {
            this.l = l;
        }
        public Piece y;
        public int l;

        public S(Piece x, Piece y, int l) {
            this.x = x;
            this.y = y;
            this.l = l;
        }
    }

    private class A {

        public Piece x;
        public Piece y;
        public int l;

        public A(Piece x, Piece y, int l) {
            this.x = x;
            this.y = y;
            this.l = l;
        }

        public Piece getX() {
            return x;
        }

        public void setX(Piece x) {
            this.x = x;
        }

        public Piece getY() {
            return y;
        }

        public void setY(Piece y) {
            this.y = y;
        }

        public int getL() {
            return l;
        }

        public void setL(int l) {
            this.l = l;
        }

    }
}
