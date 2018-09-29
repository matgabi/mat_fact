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
import model.UM;
import model.interfaces.UMInterface;
import repository.interfaces.RepositoryInterface;
import view.alerts.Alerts;

import java.io.IOException;

public class UMView extends BorderPane {

    //-----------------non fxml
    private RepositoryInterface repository;

    //-----------------fxml
    @FXML
    private TableView<UMInterface> table;
    @FXML
    private TextField nameTextField;


    public UMView(RepositoryInterface repository) throws IOException {
        this.repository = repository;

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("umview.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }

    @FXML
    private void initialize() {
        TableColumn id = table.getColumns().get(0);
        TableColumn description = table.getColumns().get(1);

        id.setCellValueFactory(new PropertyValueFactory<UMInterface, Integer>("id"));
        description.setCellValueFactory(new PropertyValueFactory<UMInterface, String>("description"));

        description.setCellFactory(TextFieldTableCell.forTableColumn());
        description.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<UMInterface,String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<UMInterface,String> event) {
                UMInterface um= (UMInterface) event.getTableView().getItems().get(event.getTablePosition().getRow());
                um.setDescription(event.getNewValue());
                repository.updateUm(um);
                showTable();
            }
        });

        ContextMenu rightClickMenu = new ContextMenu();
        MenuItem delete = new MenuItem("Sterge client");

        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                UMInterface um = table.getSelectionModel().getSelectedItem();
                repository.deleteUm(um);
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
    private void addNewUM(){
        String name = nameTextField.getText();

        if(name == null || name.equals("")){
            Alerts.showWarningAlert("Introdu numele!");
            return;
        }

        UMInterface um = new UM();
        um.setDescription(name);

        repository.addNewUm(um);
        showTable();

    }

    private void showTable() {
        table.setItems(repository.getAllUmValues());
    }
}
