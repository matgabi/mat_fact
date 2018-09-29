package repository.interfaces;


import javafx.collections.ObservableList;

import model.interfaces.BatchInterface;


public interface BatchRepositoryInterface {
    public boolean addNewBatch(BatchInterface batch);
    public boolean updateBatch(BatchInterface batch);
    public boolean deleteBatch(BatchInterface batch);
    public ObservableList<BatchInterface> getAllBatches();
}
