package repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.InvoiceBlob;
import model.interfaces.InvoiceInterface;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import repository.interfaces.InvoiceRepositoryInterface;
import view.alerts.Alerts;

import java.util.List;

public class InvoiceRepository implements InvoiceRepositoryInterface{

    private SessionFactory factory;

    public InvoiceRepository(SessionFactory factory) {
        this.factory = factory;
    }

    public boolean addNewInvoice(InvoiceBlob invoice) {
        try (Session session = factory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(invoice);
            transaction.commit();
        } catch (Exception e) {
            Alerts.showExceptionAlert("Eroare la adaugarea in baza de date", e);
            return false;
        }
        return true;
    }

    public boolean updateInvoice(InvoiceBlob invoice) {
        try (Session session = factory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.update(invoice);
            transaction.commit();
        } catch (Exception e) {
            Alerts.showExceptionAlert("Eroare la updatare in baza de date", e);
            return false;
        }
        return true;
    }

    public boolean deleteInvoice(InvoiceBlob invoice) {
        try (Session session = factory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.delete(invoice);
            transaction.commit();
        } catch (Exception e) {
            Alerts.showExceptionAlert("Eroare la adaugarea in baza de date", e);
            return false;
        }
        return true;
    }

    public ObservableList<InvoiceBlob> getAllInvoices() {
        List<InvoiceBlob> invoices = null;
        Session session = factory.openSession();
        invoices = session.createQuery("from model.InvoiceBlob", InvoiceBlob.class).list();
        return FXCollections.observableArrayList(invoices);
    }

    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.configure("hib_config/hibernate.cfg.xml");

        SessionFactory factory = configuration.buildSessionFactory();

    }

}
