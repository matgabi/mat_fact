package model.interfaces;

public interface BatchInterface {

    public int getId();
    public void setId(int id);
    public int getCurrentBatchNumber();
    public void setCurrentBatchNumber(int currentBatchNumber);
    public int getLowerBoundary();
    public void setLowerBoundary(int lowerBoundary);
    public int getUpperBoundary();
    public void setUpperBoundary(int upperBoundary);
    public int getBatchSize();
    public void setBatchSize(int batchSize);
}
