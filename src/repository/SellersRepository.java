package repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Seller;
import model.interfaces.SellerInterface;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import org.hibernate.cfg.Configuration;
import repository.interfaces.SellersRepositoryInterface;
import view.alerts.Alerts;

import java.util.List;

public class SellersRepository implements SellersRepositoryInterface {

    private SessionFactory factory;

    public SellersRepository(SessionFactory factory) {

        this.factory = factory;
    }

    public boolean addNewSeller(SellerInterface seller) {
        try (Session session = factory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(seller);
            transaction.commit();
        } catch (Exception e) {
            Alerts.showExceptionAlert("Eroare la adaugarea in baza de date", e);
            return false;
        }
        return true;
    }

    public boolean updateSeller(SellerInterface seller) {
        try (Session session = factory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.update(seller);
            transaction.commit();
        } catch (Exception e) {
            Alerts.showExceptionAlert("Eroare la updatare in baza de date", e);
            return false;
        }
        return true;
    }

    public boolean deleteSeller(SellerInterface seller) {
        try (Session session = factory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.delete(seller);
            transaction.commit();
        } catch (Exception e) {
            Alerts.showExceptionAlert("Eroare la stergere in baza de date", e);
            return false;
        }
        return true;
    }

    public ObservableList<SellerInterface> getAllSellers() {
        List<SellerInterface> sellers = null;
        Session session = factory.openSession();
        sellers = session.createQuery("from model.interfaces.SellerInterface", SellerInterface.class).list();
        return FXCollections.observableArrayList(sellers);
    }

    public static void main(String[] args){
        Configuration configuration = new Configuration();
        configuration.configure("hib_config/hibernate.cfg.xml");

        SessionFactory factory = configuration.buildSessionFactory();

        SellersRepositoryInterface sellersRepository = new SellersRepository(factory);

        Seller seller = new Seller();
        seller.setName("SC Mat SRL");
        seller.setCui("J22/111/2004");
        seller.setNr("RO13272111");
        seller.setHeadquarters("Iasi");
        seller.setCapital("200 RON");
        seller.setCounty("Iasi");
        seller.setAccount("RO89BRDL52112565851");
        seller.setBank("Banca BRD");

        sellersRepository.addNewSeller(seller);

        factory.close();
    }

}
