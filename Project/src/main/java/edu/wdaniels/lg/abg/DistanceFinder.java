package edu.wdaniels.lg.abg;

/**
 *
 * @author William
 */
public class DistanceFinder {

    public int find2DChessDistance(Piece piece1, Piece piece2) {
        int v1, v2, x1, x2, y1, y2;
        x1 = piece1.getLocation().getSecond();
        x2 = piece1.getLocation().getFirst();
        y1 = piece2.getLocation().getSecond();
        y2 = piece2.getLocation().getFirst();
        v1 = 7 - x1 + y1;
        v2 = 7 - x2 + y2;
        return (piece1.getReachabilityTwoDMap()[v1][v2]) - 1;
    }

    public int find2DDistance(Piece piece1, Piece piece2) {
        return piece1.getReachabilityTwoDMap()[piece2.getLocation().getSecond()][piece2.getLocation().getFirst()];
    }

    public int find3DDistance(Piece piece1, Piece piece2) {
        return piece1.getReachabilityThreeDMap()[piece2.getLocation().getSecond()][piece2.getLocation().getFirst()][piece2.getLocation().getThird()];
    }
}
