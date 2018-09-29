package repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Batch;
import model.interfaces.BatchInterface;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import repository.interfaces.BatchRepositoryInterface;
import view.alerts.Alerts;

import java.util.List;

public class BatchesRepository implements BatchRepositoryInterface{

    private SessionFactory factory;


    public BatchesRepository(SessionFactory factory) {
        this.factory = factory;
    }

    public boolean addNewBatch(BatchInterface batch)
    {
        try(Session session = factory.openSession())
        {
            Transaction transaction = session.beginTransaction();
            session.persist(batch);
            transaction.commit();
        }
        catch (Exception e)
        {
            Alerts.showExceptionAlert("Eroare la adaugarea in baza de date", e);
            return false;
        }
        return true;
    }

    public boolean updateBatch(BatchInterface batch)
    {
        batch.setCurrentBatchNumber(batch.getCurrentBatchNumber() + 1);
        if(batch.getCurrentBatchNumber() == batch.getUpperBoundary() + 1){
            batch.setLowerBoundary(batch.getLowerBoundary() + batch.getBatchSize());
            batch.setUpperBoundary(batch.getUpperBoundary() + batch.getBatchSize());
        }

        try(Session session = factory.openSession())
        {
            Transaction transaction = session.beginTransaction();
            session.update(batch);
            transaction.commit();
        }
        catch (Exception e)
        {
            Alerts.showExceptionAlert("Eroare la updatarea topului in baza de date", e);
            return false;
        }
        return true;
    }

    public boolean deleteBatch(BatchInterface batch)
    {
        try(Session session = factory.openSession())
        {
            Transaction transaction = session.beginTransaction();
            session.delete(batch);
            transaction.commit();
        }
        catch (Exception e)
        {
            Alerts.showExceptionAlert("Eroare la stergerea topului din baza de date", e);
            return false;
        }
        return true;
    }

    public ObservableList<BatchInterface> getAllBatches()
    {
        List<BatchInterface> batches = null;
        Session session = factory.openSession();
        batches = session.createQuery("from model.interfaces.BatchInterface", BatchInterface.class).list();
        return FXCollections.observableArrayList(batches);
    }

    public static void main(String[] args)
    {
        Configuration configuration = new Configuration();
        configuration.configure("hib_config/hibernate.cfg.xml");
        SessionFactory factory = configuration.buildSessionFactory();

        BatchRepositoryInterface repostory = new BatchesRepository(factory);

        Batch batch = new Batch();
        batch.setLowerBoundary(1);
        batch.setUpperBoundary(50);
        batch.setCurrentBatchNumber(1);
        batch.setBatchSize(50);

        repostory.addNewBatch(batch);

        for(BatchInterface b : repostory.getAllBatches()){
            System.out.println(b);
        }

        factory.close();
    }

}
