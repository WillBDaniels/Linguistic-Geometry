package edu.wdaniels.lg.abg;

import edu.wdaniels.lg.structures.Triple;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is the instantiation of the grammar of zones. This grammar takes
 * in a starting trajectory, a Starting piece, and a target piece, and then
 * computes the bundle of trajectories which compromise the enter zone.
 *
 *
 * @author William
 */
public class GrammarGz {

    private final Piece startingPiece;
    private final Piece targetPiece;
    private Piece currentPiece;
    private Piece currentTargetPiece;
    private final List<Triple<Integer, Integer, Integer>> primaryTraj;
    private final int[][] w;
    private final int[][] v;
    private final int[][] time;
    private final int[][] nexttime;
    private int x = 0, y = 0;
    private final int x0, y0;
    private final int n;
    int l = 0;
    private final List<List<Triple<Integer, Integer, Integer>>> trajectoryList = new ArrayList<>();
    private final ControlledGrammarFunctions cgf;
    private final boolean[][][] markerMap;

    private Step currentStep = Step.Q0;

    private enum Step {

        Q0, Q1, Q2, Q3, Q4, Q5, Q6
    };

    public GrammarGz(Piece startingPiece, Piece targetPiece, List<Triple<Integer, Integer, Integer>> primaryTraj, int boardSize) {
        this.startingPiece = startingPiece;
        this.targetPiece = targetPiece;
        this.currentPiece = startingPiece;
        this.currentTargetPiece = targetPiece;
        this.primaryTraj = primaryTraj;
        n = boardSize;
        w = new int[boardSize][boardSize];
        v = new int[boardSize][boardSize];
        time = new int[boardSize][boardSize];
        nexttime = new int[boardSize][boardSize];
        markerMap = new boolean[boardSize][boardSize][boardSize];
        cgf = new ControlledGrammarFunctions(markerMap);
        x0 = calculateLinearLocation(startingPiece);
        y0 = calculateLinearLocation(targetPiece);
    }

    //TODO make sure the piece locations are zero-based, if they're not, we'll need to subtract one
    // to make this work out correctly.
    private int calculateLinearLocation(Piece piece) {
        return ((piece.getLocation().getFirst() * n) + piece.getLocation().getSecond());
    }

    public List<List<Triple<Integer, Integer, Integer>>> produceZone() {
        return trajectoryList;
    }

    /**
     * This method checks all of our pass/fail conditions for a given step of
     * the grammar, and then returns the next 'step' of the process we should go
     * to. if the step returned is null, we simply stop.
     *
     * @param currentStep this is the current step in the process, ex: Q1, Q2,
     * Q3
     * @return the next step in the sequence, if there is one, null otherwise.
     */
    private GrammarGz.Step checkFailParameter(Step currentStep) {
        int l0 = primaryTraj.size();

        Step outputStep;
        switch (currentStep) {
            case Q0:
                outputStep = Step.Q1;
                break;
            case Q1:
                if (cgf.MAP(startingPiece, targetPiece) <= l && l <= l0 && (cgf.OPPOSE(startingPiece, targetPiece))) {
                    outputStep = Step.Q2;
                } else {
                    outputStep = null;
                }
                break;
            case Q2:
                outputStep = Step.Q3;
                break;
            case Q3:
                if ((x != n) || (y != n)) {
                    outputStep = Step.Q4;
                } else {
                    outputStep = Step.Q5;
                }
                break;
            case Q4:
                if ((l > 0) && (x != x0) && (y != y0)
                        && (((!cgf.OPPOSE(currentPiece, currentTargetPiece)) && cgf.MAP(currentPiece, currentTargetPiece) == 1)
                        || ((cgf.OPPOSE(currentPiece, currentTargetPiece)) && (cgf.MAP(currentPiece, currentTargetPiece) <= l)))) {
                    outputStep = Step.Q4;
                } else {
                    outputStep = Step.Q4;
                }
                break;
            case Q5:
                if (!checkIfEmptyArray(w)) {
                    outputStep = Step.Q3;
                } else {
                    outputStep = Step.Q6;
                }
                break;
            case Q6:
                outputStep = null;
                break;

            default:
                outputStep = null;
        }
        return outputStep;
    }

    private boolean checkIfEmptyArray(int[][] array) {
        for (int[] array1 : array) {
            for (int innery = 0; y < array.length; y++) {
                if (array1[innery] != 0) {
                    return false;
                }
            }
        }
        return true;
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
