package model;

import model.interfaces.BatchInterface;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "batch")
public class Batch implements BatchInterface, Serializable{

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private int id;

    @Column
    private int currentBatchNumber;

    @Column
    private int lowerBoundary;

    @Column
    private int upperBoundary;

    @Column
    private int batchSize;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCurrentBatchNumber() {
        return currentBatchNumber;
    }

    public void setCurrentBatchNumber(int currentBatchNumber) {
        this.currentBatchNumber = currentBatchNumber;
    }

    public int getLowerBoundary() {
        return lowerBoundary;
    }

    public void setLowerBoundary(int lowerBoundary) {
        this.lowerBoundary = lowerBoundary;
    }

    public int getUpperBoundary() {
        return upperBoundary;
    }

    public void setUpperBoundary(int upperBoundary) {
        this.upperBoundary = upperBoundary;
    }

    public int getBatchSize(){
        return batchSize;
    }

    public void setBatchSize(int batchSize){
        this.batchSize = batchSize;
    }

    @Override
    public String toString() {
        return "Batch{" +
                "id=" + id +
                ", currentBatchNumber=" + currentBatchNumber +
                ", lowerBoundary=" + lowerBoundary +
                ", upperBoundary=" + upperBoundary +
                '}';
    }
}
