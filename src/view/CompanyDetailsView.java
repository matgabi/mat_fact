package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Seller;
import model.interfaces.SellerInterface;
import repository.interfaces.RepositoryInterface;

import java.io.IOException;

public class CompanyDetailsView extends VBox {

    //-------------non fxml
    private RepositoryInterface repository;
    private SellerInterface seller;

    //-------------fxml
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField cuiTextField;
    @FXML
    private TextField nrTextField;
    @FXML
    private TextField headquartersTextField;
    @FXML
    private TextField capitalTextField;
    @FXML
    private TextField countyTextField;
    @FXML
    private TextField accountTextField;
    @FXML
    private TextField bankTextField;

    public CompanyDetailsView(RepositoryInterface repository) throws IOException {

        this.repository = repository;

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("companydetailsview.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }

    @FXML
    private void initialize() {
        seller = repository.getAllSellers().isEmpty() ? null : repository.getAllSellers().get(0);
        if(seller == null) {
            seller = new Seller();
            seller.setName("introdu");
            seller.setCui("introdu");
            seller.setNr("introdu");
            seller.setHeadquarters("introdu");
            seller.setCapital("introdu");
            seller.setCounty("introdu");
            seller.setAccount("introdu");
            seller.setBank("introdu");

            repository.addNewSeller(seller);
            seller = repository.getAllSellers().get(0);
        }

        nameTextField.setText(seller.getName());
        cuiTextField.setText(seller.getCui());
        nrTextField.setText(seller.getNr());
        headquartersTextField.setText(seller.getHeadquarters());
        capitalTextField.setText(seller.getCapital());
        countyTextField.setText(seller.getCounty());
        accountTextField.setText(seller.getAccount());
        bankTextField.setText(seller.getBank());
    }

    @FXML
    private void saveChanges() {
        seller.setName(nameTextField.getText());
        seller.setCui(cuiTextField.getText());
        seller.setNr(nrTextField.getText());
        seller.setHeadquarters(headquartersTextField.getText());
        seller.setCapital(capitalTextField.getText());
        seller.setCounty(countyTextField.getText());
        seller.setAccount(accountTextField.getText());
        seller.setBank(bankTextField.getText());

        repository.updateSeller(seller);
        ((Stage)this.getScene().getWindow()).close();
    }

}
