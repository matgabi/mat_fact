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
import model.Client;
import model.interfaces.ClientInterface;
import repository.interfaces.RepositoryInterface;
import view.alerts.Alerts;

import javax.swing.*;
import java.io.IOException;

public class ClientsView extends BorderPane {

    //----------------------------non fxml
    private RepositoryInterface repository;

    //----------------------------fxml
    @FXML
    private TableView<ClientInterface> table;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField cuiTextField;
    @FXML
    private TextField nrTextField;
    @FXML
    private TextField headquarterTextField;
    @FXML
    private TextField countyTextField;
    @FXML
    private TextField accountTextField;
    @FXML
    private TextField bankTextField;

    public ClientsView(RepositoryInterface repository) throws IOException {
        this.repository = repository;

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("clientsview.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }

    @FXML
    private void initialize() {
        TableColumn name = table.getColumns().get(0);
        TableColumn cui = table.getColumns().get(1);
        TableColumn nr = table.getColumns().get(2);
        TableColumn headquarters = table.getColumns().get(3);
        TableColumn county = table.getColumns().get(4);
        TableColumn account = table.getColumns().get(5);
        TableColumn bank = table.getColumns().get(6);

        name.setCellValueFactory(new PropertyValueFactory<ClientInterface, String>("name"));
        cui.setCellValueFactory(new PropertyValueFactory<ClientInterface, String>("cui"));
        nr.setCellValueFactory(new PropertyValueFactory<ClientInterface, String>("nr"));
        headquarters.setCellValueFactory(new PropertyValueFactory<ClientInterface, String>("headquarters"));
        county.setCellValueFactory(new PropertyValueFactory<ClientInterface, String>("county"));
        account.setCellValueFactory(new PropertyValueFactory<ClientInterface, String>("account"));
        bank.setCellValueFactory(new PropertyValueFactory<ClientInterface, String>("bank"));


        name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ClientInterface,String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ClientInterface,String> event) {
                ClientInterface client= (ClientInterface) event.getTableView().getItems().get(event.getTablePosition().getRow());
                client.setName(event.getNewValue());
                repository.updateClient(client);
                showTable();
            }
        });

        cui.setCellFactory(TextFieldTableCell.forTableColumn());
        cui.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ClientInterface,String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ClientInterface,String> event) {
                ClientInterface client= (ClientInterface)event.getTableView().getItems().get(event.getTablePosition().getRow());
                client.setCui(event.getNewValue());
                repository.updateClient(client);
                showTable();
            }
        });

        nr.setCellFactory(TextFieldTableCell.forTableColumn());
        nr.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ClientInterface,String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ClientInterface,String> event) {
                ClientInterface client= (ClientInterface)event.getTableView().getItems().get(event.getTablePosition().getRow());
                client.setNr(event.getNewValue());
                repository.updateClient(client);
                showTable();
            }
        });

        headquarters.setCellFactory(TextFieldTableCell.forTableColumn());
        headquarters.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ClientInterface,String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ClientInterface,String> event) {
                ClientInterface client= (ClientInterface)event.getTableView().getItems().get(event.getTablePosition().getRow());
                client.setHeadquarters(event.getNewValue());
                repository.updateClient(client);
                showTable();
            }
        });

        county.setCellFactory(TextFieldTableCell.forTableColumn());
        county.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ClientInterface,String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ClientInterface,String> event) {
                ClientInterface client= (ClientInterface)event.getTableView().getItems().get(event.getTablePosition().getRow());
                client.setCounty(event.getNewValue());
                repository.updateClient(client);
                showTable();
            }
        });

        account.setCellFactory(TextFieldTableCell.forTableColumn());
        account.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ClientInterface,String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ClientInterface,String> event) {
                ClientInterface client= (ClientInterface)event.getTableView().getItems().get(event.getTablePosition().getRow());
                client.setAccount(event.getNewValue());
                repository.updateClient(client);
                showTable();
            }
        });

        bank.setCellFactory(TextFieldTableCell.forTableColumn());
        bank.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ClientInterface,String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ClientInterface,String> event) {
                ClientInterface client= (ClientInterface)event.getTableView().getItems().get(event.getTablePosition().getRow());
                client.setBank(event.getNewValue());
                repository.updateClient(client);
                showTable();
            }
        });

        ContextMenu rightClickMenu = new ContextMenu();
        MenuItem delete = new MenuItem("Sterge client");

        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ClientInterface client = table.getSelectionModel().getSelectedItem();
                repository.deleteClient(client);
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


        TableColumn<ClientInterface, Number> indexColumn = new TableColumn<ClientInterface, Number>("#");
        indexColumn.setSortable(false);
        indexColumn.setCellValueFactory(column-> new ReadOnlyObjectWrapper<Number>(table.getItems().indexOf(column.getValue()) + 1));
        indexColumn.setMaxWidth(900);
        table.getColumns().add(0, indexColumn);

        showTable();
    }

    @FXML
    private void addNewClient(){
        String name = nameTextField.getText();
        String cui = cuiTextField.getText();
        String nr = nrTextField.getText();
        String headquarter = headquarterTextField.getText();
        String county = countyTextField.getText();
        String account = accountTextField.getText();
        String bank = bankTextField.getText();

        if(name == null || name.equals("")){
            Alerts.showWarningAlert("Introdu numele!");
            return;
        }
        if(cui == null || cui.equals("")){
            Alerts.showWarningAlert("Introdu cui!");
            return;
        }
        if(nr == null || nr.equals("")){
            Alerts.showWarningAlert("Introdu nr!");
            return;
        }
        if(headquarter == null || headquarter.equals("")){
            Alerts.showWarningAlert("Introdu sediu!");
            return;
        }
        if(county == null || county.equals("")){
            Alerts.showWarningAlert("Introdu judet!");
            return;
        }
        if(account == null || account.equals("")){
            Alerts.showWarningAlert("Introdu cont!");
            return;
        }
        if(bank == null || bank.equals("")){
            Alerts.showWarningAlert("Introdu banca!");
            return;
        }

        ClientInterface client = new Client();
        client.setName(name);
        client.setCui(cui);
        client.setNr(nr);
        client.setHeadquarters(headquarter);
        client.setCounty(county);
        client.setAccount(account);
        client.setBank(bank);

        repository.addNewClient(client);
        showTable();

    }

    private void showTable() {
        table.setItems(repository.getAllClients());
    }


}
