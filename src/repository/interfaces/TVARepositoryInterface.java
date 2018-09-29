package repository.interfaces;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.interfaces.TvaInterface;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public interface TVARepositoryInterface {

    public boolean addNewTva(TvaInterface tva);
    public boolean updateTva(TvaInterface tva);
    public boolean deleteTva(TvaInterface tva);
    public ObservableList<TvaInterface> getAllTvaValues();

}
