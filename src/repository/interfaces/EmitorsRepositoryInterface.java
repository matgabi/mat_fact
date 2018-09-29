package repository.interfaces;


import javafx.collections.ObservableList;
import model.interfaces.EmitorInterface;

public interface EmitorsRepositoryInterface {
    public boolean addNewEmitor(EmitorInterface emitor);
    public boolean updateEmitor(EmitorInterface emitor);
    public boolean deleteEmitor(EmitorInterface emitor);
    public ObservableList<EmitorInterface> getAllEmitors();
}
