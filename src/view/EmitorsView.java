package view;

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
import model.Emitor;
import model.interfaces.ClientInterface;
import model.interfaces.EmitorInterface;
import repository.interfaces.RepositoryInterface;
import view.alerts.Alerts;

import java.io.IOException;

public class EmitorsView extends BorderPane {

    //----------------------non fxml
    private RepositoryInterface repository;

    //----------------------fxml
    @FXML
    private TableView<EmitorInterface> table;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lasttNameTextField;
    @FXML
    private TextField seriaTextField;
    @FXML
    private TextField nrTextField;
    @FXML
    private TextField ciEmitorTextField;

    public EmitorsView(RepositoryInterface repository) throws IOException {
        this.repository = repository;

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("emitorsview.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }

    @FXML
    private void initialize(){
        TableColumn id = table.getColumns().get(0);
        TableColumn lastName = table.getColumns().get(1);
        TableColumn firstName = table.getColumns().get(2);
        TableColumn seria = table.getColumns().get(3);
        TableColumn nr = table.getColumns().get(4);
        TableColumn ciEmitor = table.getColumns().get(5);

        id.setCellValueFactory(new PropertyValueFactory<EmitorInterface, Integer>("id"));
        firstName.setCellValueFactory(new PropertyValueFactory<EmitorInterface, String>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<EmitorInterface, String>("lastName"));
        seria.setCellValueFactory(new PropertyValueFactory<EmitorInterface, String>("seria"));
        nr.setCellValueFactory(new PropertyValueFactory<EmitorInterface, String>("nr"));
        ciEmitor.setCellValueFactory(new PropertyValueFactory<EmitorInterface, String>("ciEmitor"));

        firstName.setCellFactory(TextFieldTableCell.forTableColumn());
        firstName.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ClientInterface,String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ClientInterface,String> event) {
                EmitorInterface emitor = (EmitorInterface) event.getTableView().getItems().get(event.getTablePosition().getRow());
                emitor.setFirstName(event.getNewValue());
                repository.updateEmitor(emitor);
                showTable();
            }
        });

        lastName.setCellFactory(TextFieldTableCell.forTableColumn());
        lastName.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ClientInterface,String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ClientInterface,String> event) {
                EmitorInterface emitor = (EmitorInterface) event.getTableView().getItems().get(event.getTablePosition().getRow());
                emitor.setLastName(event.getNewValue());
                repository.updateEmitor(emitor);
                showTable();
            }
        });

        seria.setCellFactory(TextFieldTableCell.forTableColumn());
        seria.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ClientInterface,String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ClientInterface,String> event) {
                EmitorInterface emitor = (EmitorInterface) event.getTableView().getItems().get(event.getTablePosition().getRow());
                emitor.setSeria(event.getNewValue());
                repository.updateEmitor(emitor);
                showTable();
            }
        });

        nr.setCellFactory(TextFieldTableCell.forTableColumn());
        nr.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ClientInterface,String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ClientInterface,String> event) {
                EmitorInterface emitor = (EmitorInterface) event.getTableView().getItems().get(event.getTablePosition().getRow());
                emitor.setNr(event.getNewValue());
                repository.updateEmitor(emitor);
                showTable();
            }
        });

        ciEmitor.setCellFactory(TextFieldTableCell.forTableColumn());
        ciEmitor.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ClientInterface,String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ClientInterface,String> event) {
                EmitorInterface emitor= (EmitorInterface)event.getTableView().getItems().get(event.getTablePosition().getRow());
                emitor.setCiEmitor(event.getNewValue());
                repository.updateEmitor(emitor);
                showTable();
            }
        });

        ContextMenu rightClickMenu = new ContextMenu();
        MenuItem delete = new MenuItem("Sterge angajat");

        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                EmitorInterface emitor = table.getSelectionModel().getSelectedItem();
                repository.deleteEmitor(emitor);
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

        showTable();
    }

    @FXML
    private void addNewEmitor(){
        String firstName = firstNameTextField.getText();
        String lastName = lasttNameTextField.getText();
        String seria = seriaTextField.getText();
        String nr = nrTextField.getText();
        String ciEmitor = ciEmitorTextField.getText();

        if(firstName == null || firstName.equals("")){
            Alerts.showWarningAlert("Introdu prenumele!");
            return;
        }
        if(lastName == null || lastName.equals("")){
            Alerts.showWarningAlert("Introdu numele!");
            return;
        }
        if(seria == null || seria.equals("")){
            Alerts.showWarningAlert("Introdu seria!");
            return;
        }
        if(nr == null || nr.equals("")){
            Alerts.showWarningAlert("Introdu nr!");
            return;
        }
        if(ciEmitor == null || ciEmitor.equals("")){
            Alerts.showWarningAlert("Introdu emitor!");
            return;
        }

        EmitorInterface emitor = new Emitor();
        emitor.setFirstName(firstName);
        emitor.setLastName(lastName);
        emitor.setSeria(seria);
        emitor.setNr(nr);
        emitor.setCiEmitor(ciEmitor);

        repository.addNewEmitor(emitor);
        showTable();
    }

    private void showTable() {
        table.setItems(repository.getAllEmitors());
    }
}
