package edu.wdaniels.lg.abg;

import edu.wdaniels.lg.structures.Pair;
import edu.wdaniels.lg.structures.TreeNode;
import edu.wdaniels.lg.structures.Triple;

/**
 * This class is specifically for the Grammar of reduced searches.
 *
 * @author William
 */
public class GrammarGrs {
    Triple<Triple<Integer, Integer, Integer>, Triple<Integer, Integer, Integer>, String> test = new Triple<>();
    private TreeNode<Triple<Pair<Integer, Integer>, Pair<Integer, Integer>, String>> root;
    private Step currentStep = Step.Q0;
    private Step previousStep;
    private boolean stepCheckPassed = true;

    private enum Step {

        Q0, Q1, Q2, Q3;
    };

    public GrammarGrs() {
        root = new TreeNode<>(null);
        //This is the 'top' branch of the tree as seen on the pdf
        TreeNode white2 = root.addChild(new Triple(new Pair(3, 6), new Pair(3, 7), "white"));
        TreeNode black1 = white2.addChild(new Triple(new Pair(1,6), new Pair(2, 7), "black"));
        TreeNode white3 = black1.addChild(new Triple(new Pair(8, 8), new Pair(7, 7), "white"));
        TreeNode white4 = black1.addChild(new Triple(new Pair(3, 7), new Pair(3, 8), "white"));

        //first termincation
        white3.addChild(new Triple(new Pair(2, 7), new Pair(3, 7), "black"));

        //second termination
        white4.addChild(new Triple(new Pair(2, 7), new Pair(3, 8), "black"));

        //THis is the primary bottom branch. Ugh.
        TreeNode white1 = root.addChild(new Triple(new Pair(8, 8), new Pair(7, 7), "white"));

        TreeNode black2 = white1.addChild(new Triple(new Pair(1, 6), new Pair(2, 5), "black"));
        TreeNode black3 = white1.addChild(new Triple(new Pair(8, 5), new Pair(8, 4), "black"));
        TreeNode black4 = white1.addChild(new Triple(new Pair(1, 6), new Pair(2, 6), "black"));
        TreeNode white5 = black4.addChild(new Triple(new Pair(3, 6), new Pair(3, 7), "white"));
        TreeNode white6 = black4.addChild(new Triple(new Pair(7, 7), new Pair(6, 6), "white"));
        TreeNode white7 = black3.addChild(new Triple(new Pair(3, 6), new Pair(3, 7), "white"));
        TreeNode white8 = black3.addChild(new Triple(new Pair(7, 7), new Pair(6, 6), "white"));
        TreeNode white = black2.addChild(new Triple(new Pair(3, 6), new Pair(3, 7), "white"));

        //Third termination, top to bottom
        white5.addChild(new Triple(new Pair(2, 6), new Pair(3, 7), "black"));

        //fourth termination
        white6.addChild(new Triple(new Pair(2, 6), new Pair(3, 6), "black"));
        TreeNode black7 = white6.addChild(new Triple(new Pair(2, 6), new Pair(2, 7), "black"));
        TreeNode black8 = white6.addChild(new Triple(new Pair(8, 5), new Pair(8, 4), "black"));

        TreeNode black9 = white7.addChild(new Triple(new Pair(1, 6), new Pair(2, 7), "black"));

        TreeNode black10 = white8.addChild(new Triple(new Pair(1, 6), new Pair(2, 7), "black"));
        TreeNode black11 = white8.addChild(new Triple(new Pair(1, 6), new Pair(2, 6), "black"));
        TreeNode black12 = white8.addChild(new Triple(new Pair(8, 4), new Pair(8, 3), "black"));

        TreeNode black13 = white.addChild(new Triple(new Pair(8, 5), new Pair(8, 4), "black"));


        TreeNode white9 = black8.addChild(new Triple(new Pair(3, 6), new Pair(3, 7), "white"));
        TreeNode white10 = black8.addChild(new Triple(new Pair(6, 6), new Pair(5, 5), "white"));

        //termination 5 (in order of what happens first, top to bottom)
        black7.addChild(new Triple(new Pair(3, 6), new Pair(2, 7), "white"));

        TreeNode white12 = black9.addChild(new Triple(new Pair(3, 7), new Pair(3, 8), "white"));
        TreeNode white13 = black9.addChild(new Triple(new Pair(7, 7), new Pair(6, 6), "white"));

        //Termination 6
        black10.addChild(new Triple(new Pair(3, 6), new Pair(2, 7), "white"));

        TreeNode white15 = black11.addChild(new Triple(new Pair(3, 6), new Pair(2, 7), "white"));
        TreeNode white16 = black11.addChild(new Triple(new Pair(6, 6), new Pair(5, 5), "white"));

        //termination 7
        black12.addChild(new Triple(new Pair(6, 6), new Pair(5, 7), "white"));

        //termination 8
        black13.addChild(new Triple(new Pair(3, 7), new Pair(3, 8), "white"));

        //termination 9
        white9.addChild(new Triple(new Pair(2, 6), new Pair(3, 7), "black"));

        //termination 10
        white10.addChild(new Triple(new Pair(2, 6), new Pair(3, 6), "black"));
        TreeNode black16 = white10.addChild(new Triple(new Pair(8, 4), new Pair(8, 3), "black"));

        //termination 11
        white12.addChild(new Triple(new Pair(2, 7), new Pair(3, 8), "black"));

        //termination 12
        white13.addChild(new Triple(new Pair(2, 7), new Pair(3, 7), "black"));

        //termination 13
        white15.addChild(new Triple(new Pair(2, 6), new Pair(3, 7), "black"));

        //termination 14
        white16.addChild(new Triple(new Pair(2, 6), new Pair(3, 6), "black"));
        TreeNode black21 = white16.addChild(new Triple(new Pair(8, 4), new Pair(8, 3), "black"));
        TreeNode black22 = white16.addChild(new Triple(new Pair(2, 6), new Pair(2, 7), "black"));

        //termination 15
        black16.addChild(new Triple(new Pair(5, 5), new Pair(4, 6), "white"));

        //termination 16
        black21.addChild(new Triple(new Pair(5, 5), new Pair(4, 6), "white"));

        //termination 17
        black22.addChild(new Triple(new Pair(3, 6), new Pair(2, 7), "white"));
    }

    private void constructFakeTree(){

    }

    //TODO make sure the piece locations are zero-based, if they're not, we'll need to subtract one
    // to make this work out correctly.
    private int calculateLinearLocation(Piece piece, int boardSize) {
        return ((piece.getLocation().getFirst()) + piece.getLocation().getSecond() * boardSize);
    }
//
//    public List<Pair<List<Triple<Integer, Integer, Integer>>, Integer>> produceZone() {
//        while (currentStep != null) {
//
//            previousStep = currentStep;
//            currentStep = checkFailParameter(currentStep);
//            // System.out.println("Doing step: " + currentStep + " will it execute? : " + stepCheckPassed);
//            if (currentStep == null) {
//                continue;
//            }
//            if (stepCheckPassed) {
//                switch (previousStep) {
//                    case Q0:
//                        break;
//                    case Q1:
//                        currentA = new A(new Triple<>(calculateLinearLocation(currentPiece),
//                                calculateLinearLocation(targetPiece), l), v, w);
//                        break;
//                    case Q2:
//                        handleQ2();
//                        break;
//                    case Q3:
//                        handleQ3();
//                        break;
//                    case Q4:
//                        handleQ4();
//                        break;
//                    case Q5:
//                        currentType = (currentType.equals(startingPiece.getPieceType()) ? targetPiece.getPieceType() : startingPiece.getPieceType());
//                        currentA = new A(new Triple<>(0, 0, 0), w, new int[n * n]);
//                        System.arraycopy(nexttime, 0, time, 0, nexttime.length);
//                        System.arraycopy(w, 0, v, 0, w.length);
//                        w = new int[n * n];
//                        break;
//                    case Q6:
//                        break;
//                }
//            }
//        }
//        return trajectoryList;
//    }
//
//    private void handleQ2() {
//        Pair<List<Triple<Integer, Integer, Integer>>, Integer> traj;
//        traj = new Pair<>(cgf.h(0, currentPiece, targetPiece, currentA.getU().getThird()), l0 + 1);
//        trajectoryList.add(traj);
//        List<Triple<Integer, Integer, Integer>> trajnew;
//        trajnew = cgf.h(0, currentPiece, targetPiece, currentA.getU().getThird());
//        for (int i = 0; i < v.length; i++) {
//            v[i] = cgf.g(currentPiece, trajnew, w, i);
//            time[i] = (cgf.DIST(i, currentPiece, trajnew));
//        }
//        currentA = new A((new Triple<>(0, 0, 0)), v, w);
//    }
//
//    private void handleQ3() {
//        for (int i = 0; i < nexttime.length; i++) {
//            nexttime[i] = cgf.init(currentA.u, nexttime[i], n);
//        }
//        currentA = new A(cgf.f(currentA.u, time, v), v, w);
//        if (currentA.getU().getThird() == (2 * (n * n))) {
//            currentA.getU().setThird(0);
//        }
//    }
//
//    private void handleQ4() {
//        Pair<List<Triple<Integer, Integer, Integer>>, Integer> traj;
//
//        List<Triple<Integer, Integer, Integer>> trajectoryNew = cgf.h(0, currentPiece, currentTargetPiece, currentA.getU().getThird());
//        if (isTrajectoryInList(trajectoryList, trajectoryNew)) {
//            return;
//        }
//        traj = new Pair<>(trajectoryNew, time[currentA.getU().getSecond()]);
//
//        trajectoryList.add(traj);
//        for (int i = 0; i < w.length; i++) {
//            w[i] = cgf.g(currentPiece, trajectoryNew, w, i);
//            nexttime[i] = cgf.ALPHA(nexttime, i, currentPiece, trajectoryNew, time[currentA.getU().getSecond()] - currentA.getU().getThird() + 1);
//        }
//        for (int i = 0; i < nexttime.length; i++) {
//
//        }
//        currentA = new A(currentA.getU(), v, w);
//    }
//
//    private Triple<Integer, Integer, Integer> linearToTriple(int linear) {
//        Triple<Integer, Integer, Integer> output = new Triple<>();
//        output.setFirst((linear % n));
//        output.setSecond((linear / n));
//        if (output.getFirst() == n) {
//            output.setFirst(output.getFirst() - 1);
//        }
//        if (output.getSecond() == n) {
//            output.setSecond(output.getSecond() - 1);
//        }
//        output.setThird(0);
//        return output;
//    }
//
//    /**
//     * This method checks all of our pass/fail conditions for a given step of
//     * the grammar, and then returns the next 'step' of the process we should go
//     * to. if the step returned is null, we simply stop.
//     *
//     * @param currentStep this is the current step in the process, ex: Q1, Q2,
//     * Q3
//     * @return the next step in the sequence, if there is one, null otherwise.
//     */
//    private GrammarGz.Step checkFailParameter(Step currentStep) {
//
//        Step outputStep;
//        switch (currentStep) {
//            case Q0:
//                stepCheckPassed = true;
//                outputStep = Step.Q1;
//                break;
//            case Q1:
//                if (cgf.MAP(startingPiece, targetPiece) <= l && l <= l0 && (cgf.OPPOSE(currentPiece, targetPiece))) {
//                    stepCheckPassed = true;
//                    outputStep = Step.Q2;
//                } else {
//                    stepCheckPassed = false;
//                    outputStep = null;
//                }
//                break;
//            case Q2:
//                stepCheckPassed = true;
//                outputStep = Step.Q3;
//                break;
//            case Q3:
//                if ((currentA.u.getFirst() != (n * n)) || (currentA.u.getSecond() != (n * n))) {
//                    stepCheckPassed = true;
//                    outputStep = Step.Q4;
//                } else {
//                    stepCheckPassed = false;
//                    outputStep = Step.Q5;
//                }
//                break;
//            case Q4:
//                boolean hasFoundPiece = false;
//                for (Piece piece : PrimaryController.getController().pieceList) {
//                    Triple<Integer, Integer, Integer> locationFirst = linearToTriple(currentA.u.getFirst());
//                    if (piece.getLocation().compareTo(locationFirst) == 1) {
//                        currentPiece = piece;
//                        Triple<Integer, Integer, Integer> location = linearToTriple(currentA.u.getSecond());
//
//                        Piece temp = new Piece(currentType, location, currentPiece.getReachablityEquation(), startingPiece.getPieceName());
//                        BoardGenerator bg = new BoardGenerator();
//                        temp.setReachabilityTwoDMap(bg.generate2DBoard(temp, PrimaryController.getController().obstacleList, n, false));
//                        currentTargetPiece = temp;
//                        hasFoundPiece = true;
//                        //currentPiece.printBoard();
//
//                    }
//                }
//                if (!hasFoundPiece) {
//
//                    stepCheckPassed = false;
//                    outputStep = Step.Q3;
//                    break;
//                }
//                if (((currentA.getU().getThird() > 0) && (currentA.u.getFirst() != x0) && (currentA.u.getFirst() != y0))
//                        && ((!cgf.OPPOSE(currentPiece, currentTargetPiece) && cgf.MAP(currentPiece, currentTargetPiece) == 1)
//                        || (cgf.OPPOSE(currentPiece, currentTargetPiece) && cgf.MAP(currentPiece, currentTargetPiece) <= currentA.getU().getThird()
//                        && cgf.MAP(currentPiece, currentTargetPiece) > 0))) {
//                    stepCheckPassed = true;
//                    outputStep = Step.Q3;
//                } else {
//                    stepCheckPassed = false;
//                    outputStep = Step.Q3;
//                }
//                break;
//            case Q5:
//                if (!checkIfEmptyArray(w)) {
//                    stepCheckPassed = true;
//                    outputStep = Step.Q3;
//                } else {
//                    stepCheckPassed = false;
//                    outputStep = Step.Q6;
//                }
//                break;
//            case Q6:
//                outputStep = null;
//                break;
//
//            default:
//                outputStep = null;
//        }
//        return outputStep;
//    }
}
