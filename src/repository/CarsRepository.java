package repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import model.Car;
import model.interfaces.CarInterface;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import repository.interfaces.CarsRepositoryInterface;
import view.alerts.Alerts;

import java.util.List;

public class CarsRepository implements CarsRepositoryInterface {

    private SessionFactory factory;


    public CarsRepository(SessionFactory factory) {
        this.factory = factory;
    }

    public boolean addNewCar(CarInterface car)
    {
        try(Session session = factory.openSession())
        {
            Transaction transaction = session.beginTransaction();
            session.persist(car);
            transaction.commit();
        }
        catch (Exception e)
        {
            Alerts.showExceptionAlert("Eroare la adaugarea in baza de date", e);
            return false;
        }
        return true;
    }

    public boolean updateCar(CarInterface car)
    {
        try(Session session = factory.openSession())
        {
            Transaction transaction = session.beginTransaction();
            session.update(car);
            transaction.commit();
        }
        catch (Exception e)
        {
            Alerts.showExceptionAlert("Eroare la updatare in baza de date", e);
            return false;
        }
        return true;
    }

    public boolean deleteCar(CarInterface car)
    {
        try(Session session = factory.openSession())
        {
            Transaction transaction = session.beginTransaction();
            session.delete(car);
            transaction.commit();
        }
        catch (Exception e)
        {
            Alerts.showExceptionAlert("Eroare la stergere in baza de date", e);
            return false;
        }
        return true;
    }

    public ObservableList<CarInterface> getAllCars()
    {
        List<CarInterface> cars = null;
        Session session = factory.openSession();
        cars = session.createQuery("from model.interfaces.CarInterface", CarInterface.class).list();
        return FXCollections.observableArrayList(cars);
    }

    public static void main(String[] args)
    {
        Configuration configuration = new Configuration();
        configuration.configure("hib_config/hibernate.cfg.xml");
        SessionFactory factory = configuration.buildSessionFactory();

        CarsRepository repository = new CarsRepository(factory);

        Car car = new Car("NT-66-MAT");
        repository.addNewCar(car);

        for(CarInterface p : repository.getAllCars())
        {
            System.out.println(p);
        }

        factory.close();
    }

}
