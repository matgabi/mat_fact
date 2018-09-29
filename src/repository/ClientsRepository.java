package repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Client;
import model.interfaces.ClientInterface;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import repository.interfaces.ClientsRepositoryInterface;
import view.alerts.Alerts;

import java.util.List;

public class ClientsRepository implements ClientsRepositoryInterface {

    private SessionFactory factory;

    public ClientsRepository(SessionFactory factory) {
        this.factory = factory;
    }

    public boolean addNewClient(ClientInterface client)
    {
        try(Session session = factory.openSession())
        {
            Transaction transaction = session.beginTransaction();
            session.persist(client);
            transaction.commit();
        }
        catch (Exception e)
        {
            Alerts.showExceptionAlert("Eroare la adaugarea in baza de date", e);
            return false;
        }
        return true;
    }

    public boolean updateClient(ClientInterface client)
    {
        try(Session session = factory.openSession())
        {
            Transaction transaction = session.beginTransaction();
            session.update(client);
            transaction.commit();
        }
        catch (Exception e)
        {
            Alerts.showExceptionAlert("Eroare la updatare in baza de date", e);
            return false;
        }
        return true;
    }

    public boolean deleteClient(ClientInterface client)
    {
        try(Session session = factory.openSession())
        {
            Transaction transaction = session.beginTransaction();
            session.delete(client);
            transaction.commit();
        }
        catch (Exception e)
        {
            Alerts.showExceptionAlert("Eroare la stergere in baza de date", e);
            return false;
        }
        return true;
    }

    public ObservableList<ClientInterface> getAllClients()
    {
        List<ClientInterface> clients = null;
        Session session = factory.openSession();
        clients = session.createQuery("from model.interfaces.ClientInterface order by name",ClientInterface.class).list();
        return FXCollections.observableArrayList(clients);
    }

    public static void main(String[] args)
    {
        Configuration configuration = new Configuration();
        configuration.configure("hib_config/hibernate.cfg.xml");

        SessionFactory factory = configuration.buildSessionFactory();

        ClientsRepository repository = new ClientsRepository(factory);

        Client client = new Client();
        client.setName("SC FMM SRL");
        client.setCui("J22/234/2007");
        client.setNr("RO10872342");
        client.setHeadquarters("Cluj-Napoca");
        client.setCounty("Cluj");
        client.setAccount("RO89BRDL25136136251");
        client.setBank("Banca BRD");


        client.setName("SC WTF SRL");

        //repository.addNewClient(client);
        //repository.updateClient(client);
        //repository.deleteClient(client);
        for(ClientInterface c  : repository.getAllClients())
        {
            System.out.println(c);
        }
        factory.close();
    }
}
