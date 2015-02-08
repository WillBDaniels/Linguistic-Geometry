/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wdaniels.lg.proj1.structures;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author William
 */
public class TableData {
    private final SimpleStringProperty pieceName;
    private final SimpleIntegerProperty pieceDistance;
 
    public TableData(String pieceName, int pieceDistance) {
        this.pieceName = new SimpleStringProperty(pieceName);
        this.pieceDistance = new SimpleIntegerProperty(pieceDistance);
    }
 
    public String getPieceName() {
        return pieceName.get();
    }
    public void setPieceName(String pieceName) {
        this.pieceName.set(pieceName);
    }
        
    public Integer getPieceDistance() {
        return pieceDistance.get();
    }
    public void setPieceDistance(Integer pieceDistance) {
        this.pieceDistance.set(pieceDistance);
    }
}