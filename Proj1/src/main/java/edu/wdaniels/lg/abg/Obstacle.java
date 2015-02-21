package edu.wdaniels.lg.abg;

import edu.wdaniels.lg.structures.Triple;

/**
 *
 * @author William
 */
public class Obstacle {
    private String obstacleName = "";
    private Triple<Integer, Integer, Integer> obstacleLocation = null;
    
    public Obstacle(String obstacleName, Triple<Integer, Integer, Integer> obstacleLocation){
        this.obstacleName = obstacleName;
        this.obstacleLocation = obstacleLocation;
    }

    public String getObstacleName() {
        return obstacleName;
    }

    public void setObstacleName(String obstacleName) {
        this.obstacleName = obstacleName;
    }

    public Triple<Integer, Integer, Integer> getObstacleLocation() {
        return obstacleLocation;
    }

    public void setObstacleLocation(Triple<Integer, Integer, Integer> obstacleLocation) {
        this.obstacleLocation = obstacleLocation;
    }
}
