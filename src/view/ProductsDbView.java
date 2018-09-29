package view;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;

import model.Product;
import model.interfaces.ProductInterface;
import repository.interfaces.RepositoryInterface;
import view.alerts.Alerts;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class ProductsDbView extends BorderPane {

    //-----------------------non fxml members
    private RepositoryInterface repository;

    //-----------------------fxml members
    @FXML
    private TableView<ProductInterface> table;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField priceTextField;
    @FXML
    private TextField umTextField;


    public ProductsDbView(RepositoryInterface repository) throws IOException {
        this.repository = repository;

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("productsdbview.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }

    @FXML
    private void initialize(){
        TableColumn name = table.getColumns().get(0);
        TableColumn price = table.getColumns().get(2);
        TableColumn um = table.getColumns().get(1);

        name.setCellValueFactory(new PropertyValueFactory<ProductInterface, String>("productName"));
        price.setCellValueFactory(new PropertyValueFactory<ProductInterface, BigDecimal>("price"));
        um.setCellValueFactory(new PropertyValueFactory<ProductInterface, String>("um"));

        name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ProductInterface,String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ProductInterface,String> event) {
                ProductInterface product = (ProductInterface)event.getTableView().getItems().get(event.getTablePosition().getRow());
                product.setProductName(event.getNewValue());
                repository.updateProduct(product);
            }
        });


        price.setCellFactory(TextFieldTableCell.<ProductInterface,BigDecimal>forTableColumn(new StringConverter<BigDecimal>() {
            @Override
            public String toString(BigDecimal object) {
                if(object == null){
                    return null;
                }
                return object.setScale(2, RoundingMode.HALF_UP).toString();
            }

            @Override
            public BigDecimal fromString(String string) {
                return new BigDecimal(string);
            }
        }));
        price.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ProductInterface,BigDecimal>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ProductInterface,BigDecimal> event) {
                ProductInterface product = (ProductInterface)event.getTableView().getItems().get(event.getTablePosition().getRow());
                product.setPrice(event.getNewValue());
                repository.updateProduct(product);
                showTable();
            }
        });

        um.setCellFactory(TextFieldTableCell.forTableColumn());
        um.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ProductInterface,String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ProductInterface,String> event) {
                ProductInterface product = (ProductInterface)event.getTableView().getItems().get(event.getTablePosition().getRow());
                product.setUm(event.getNewValue());
                repository.updateProduct(product);
            }
        });

        ContextMenu rightClickMenu = new ContextMenu();
        MenuItem delete = new MenuItem("Sterge produs");

        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ProductInterface product = table.getSelectionModel().getSelectedItem();
                repository.deleteProduct(product);
                showTable();
            }
        });
        rightClickMenu.getItems().add(delete);

        table.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton() == MouseButton.SECONDARY) {
                    rightClickMenu.show(table, event.getScreenX(), event.getScreenY());
                }else {
                    rightClickMenu.hide();
                }
            }
        });

        TableColumn<ProductInterface, Number> indexColumn = new TableColumn<ProductInterface, Number>("#");
        indexColumn.setSortable(false);
        indexColumn.setCellValueFactory(column-> new ReadOnlyObjectWrapper<Number>(table.getItems().indexOf(column.getValue()) + 1));
        indexColumn.setMaxWidth(900);
        table.getColumns().add(0, indexColumn);

        showTable();
    }

    @FXML
    private void addNewProduct(){
        String name = nameTextField.getText();
        String price = priceTextField.getText();
        String um = umTextField.getText();

        if(name == null || name.equals("")){
            Alerts.showWarningAlert("Introdu numele produsului");
            return;
        }

        if(um == null || um.equals("")){
            Alerts.showWarningAlert("Introdu um produsului");
            return;
        }

        try {
            Double priceParsed = Double.parseDouble(price);
            Product product = new Product();
            product.setProductName(name);
            product.setPrice(BigDecimal.valueOf(priceParsed));
            product.setUm(um);

            repository.addNewProduct(product);
        }catch (Exception e){
            Alerts.showExceptionAlert("Introdu pretul corect!", e);
        }
        showTable();
    }

    private void showTable() {
        table.setItems(repository.getAllProducts());
    }

}
