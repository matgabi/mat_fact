package repository.interfaces;


import javafx.collections.ObservableList;
import model.interfaces.UMInterface;


import java.util.List;

public interface UMRepositoryInterface {
    public boolean addNewUm(UMInterface um);
    public boolean updateUm(UMInterface um);
    public boolean deleteUm(UMInterface um);
    public ObservableList<UMInterface> getAllUmValues();
}
