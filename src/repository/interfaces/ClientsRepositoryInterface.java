package repository.interfaces;

import javafx.collections.ObservableList;
import model.interfaces.ClientInterface;

public interface ClientsRepositoryInterface {
    public boolean addNewClient(ClientInterface client);
    public boolean updateClient(ClientInterface client);
    public boolean deleteClient(ClientInterface client);
    public ObservableList<ClientInterface> getAllClients();
}
