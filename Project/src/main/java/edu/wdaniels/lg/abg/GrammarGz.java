package edu.wdaniels.lg.abg;

import edu.wdaniels.lg.structures.Pair;
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
    private  int[] w;
    private final  int[] v;
    private final  int[] time;
    private final int[] nexttime;
    private int x = 0, y = 0;
    private final int x0, y0;
    private final int n;
    int l = 0;
    private final int l0;
    private A currentA;
    private boolean stepCheckPassed = false;
    private final List<Pair<List<Triple<Integer, Integer, Integer>>, Integer>> trajectoryList = new ArrayList<>();
    private final ControlledGrammarFunctions cgf;
    private final boolean[][][] markerMap;

    
    private Step previousStep = Step.Q0;
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
        w = new int[boardSize * boardSize];
        v = new int[boardSize * boardSize];
        time = new int[boardSize * boardSize];
        nexttime = new int[boardSize * boardSize];
        markerMap = new boolean[boardSize][boardSize][boardSize];
        cgf = new ControlledGrammarFunctions(markerMap);
        x0 = calculateLinearLocation(startingPiece);
        y0 = calculateLinearLocation(targetPiece);
        l0 = primaryTraj.size();
        l = l0;
    }

    //TODO make sure the piece locations are zero-based, if they're not, we'll need to subtract one
    // to make this work out correctly.
    private int calculateLinearLocation(Piece piece) {
        return ((piece.getLocation().getFirst() * n) + piece.getLocation().getSecond());
    }

    public List<Pair<List<Triple<Integer, Integer, Integer>>, Integer>> produceZone() {
        while (currentStep != null){
            previousStep = currentStep;
            currentStep = checkFailParameter(currentStep);
            if (currentStep == null){
                continue;
            }
            if (stepCheckPassed){
                switch(previousStep){
                    case Q0:
                        break;
                    case Q1:
                        currentA = new A(new Triple<>(calculateLinearLocation(currentPiece),
                                calculateLinearLocation(targetPiece), l), v, w);
                        break;
                    case Q2:
                        handleQ2();
                        break;
                    case Q3:
                        handleQ3();
                        break;
                    case Q4:
                        handleQ4();
                        break;
                    case Q5:
                        currentA = new A(new Triple<>(0, 0, 0), w, new int[n]);
                        System.arraycopy(nexttime, 0, time, 0, nexttime.length);
                        break;
                    case Q6:
                        break;
                }
            }
        }
        return trajectoryList;
    }
    
    
    
    private void handleQ2(){
        Pair<List<Triple<Integer, Integer, Integer>>, Integer> traj;
        traj = new Pair<>(cgf.h(0,currentPiece, targetPiece, l), l0+1);
        trajectoryList.add(traj);
        for (int i = 0; i < v.length; i++){
            v[i] = cgf.g(currentPiece, cgf.h(0,currentPiece, targetPiece, l), w, i);
        }
        w = new int[n];
        currentA = new A((new Triple<>(0, 0, 0)),v , w);
        for (int i = 0; i < time.length; i++){
            time[i] = (cgf.DIST(i, currentPiece, cgf.h(0,currentPiece, targetPiece, l)));
        }
    }
    
    private void handleQ3(){
        currentA = new A(cgf.f(currentA.u, time, v, l), v, w);
        for (int i = 0; i < nexttime.length; i++){
            nexttime[i] = cgf.init(currentA.u, nexttime[i], n);
        }
    }
    
    private void handleQ4(){
        Pair<List<Triple<Integer, Integer, Integer>>, Integer> traj;
        traj = new Pair<>(cgf.h(0,currentPiece, new Piece(linearToTriple(currentA.getU().getSecond()), "", ""), l), time[currentA.getU().getSecond()]);
        trajectoryList.add(traj);
         for (int i = 0; i < w.length; i++){
             w[i] = cgf.g(currentPiece, cgf.h(0,currentPiece, new Piece(linearToTriple(currentA.getU().getSecond()), "", ""), l), w, i);
         }
        currentA = new A(currentA.getU(), v, w);
        for (int i = 0; i < nexttime.length; i++){
            nexttime[i] = cgf.ALPHA(nexttime,i,currentPiece, cgf.h(0,currentPiece, new Piece(linearToTriple(currentA.getU().getSecond()), "", ""), l), time[currentA.getU().getSecond()] - l +1);
        }
    }
    
    private Triple<Integer, Integer, Integer> linearToTriple(int linear){
        Triple<Integer, Integer, Integer> output = new Triple<>();
        output.setFirst(linear / n);
        output.setSecond(linear % n);
        output.setThird(0);
        return output;
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
        
        Step outputStep;
        switch (currentStep) {
            case Q0:
                stepCheckPassed = true;
                outputStep = Step.Q1;
                break;
            case Q1:
                if (cgf.MAP(startingPiece, targetPiece) <= l && l <= l0 && (cgf.OPPOSE(startingPiece, targetPiece))) {
                    stepCheckPassed = true;
                    outputStep = Step.Q2;
                } else {
                    stepCheckPassed = false;
                    outputStep = null;
                }
                break;
            case Q2:
                stepCheckPassed = true;
                outputStep = Step.Q3;
                break;
            case Q3:
                if ((x != n) || (y != n)) {
                    stepCheckPassed = true;
                    outputStep = Step.Q4;
                } else {
                    stepCheckPassed = false;
                    outputStep = Step.Q5;
                }
                break;
            case Q4:
                if ((l > 0) && (x != x0) && (y != y0)
                        && (((!cgf.OPPOSE(currentPiece, currentTargetPiece)) && cgf.MAP(currentPiece, currentTargetPiece) == 1)
                        || ((cgf.OPPOSE(currentPiece, currentTargetPiece)) && (cgf.MAP(currentPiece, currentTargetPiece) <= l)))) {
                    stepCheckPassed = true;
                    outputStep = Step.Q4;
                } else {
                    stepCheckPassed = false;
                    outputStep = Step.Q4;
                }
                break;
            case Q5:
                if (!checkIfEmptyArray(w)) {
                    stepCheckPassed = true;
                    outputStep = Step.Q3;
                } else {
                    stepCheckPassed = false;
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
    


    /**
     * This iterates through the given 2D array and checks to see if any of the values are
     * greater than 0. If they are, it returns false. Otherwise it returns true. 
     * 
     * @param array The array that we're going to iterate through to see if it's empty
     * @return True if every element in the integer array is 0, false otherwise. 
     */
    private boolean checkIfEmptyArray(int[] array) {
        for (int i = 0; i < array.length; i++){
            if (array[i] != 0){
                return false;
            }
        }
        return true;
    }

    /**
     * This class just represents the 'A' state of the grammar which is used to hold some state 
     * between steps. 
     */
    private class A {

        public Triple<Integer, Integer, Integer> u;
        public int[] v;
        public int[] w;

        public A(Triple<Integer, Integer, Integer> u, int[] v, int[] w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }

        
        
        public Triple<Integer, Integer, Integer> getU() {
            return u;
        }

        public void setU(Triple<Integer, Integer, Integer> u) {
            l = u.getThird();
            
            this.u = u;
        }

        public int[] getV() {
            return v;
        }

        public void setV(int[] v) {
            this.v = v;
        }

        public int[] getW() {
            return w;
        }

        public void setW(int[] w) {
            this.w = w;
        }

        

    }

}
