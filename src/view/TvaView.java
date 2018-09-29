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
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import model.TVA;
import model.interfaces.TvaInterface;
import repository.interfaces.RepositoryInterface;
import view.alerts.Alerts;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class TvaView extends VBox {

    //---------non fxml
    private RepositoryInterface repository;

    //---------fxml
    @FXML
    private TableView<TvaInterface> table;
    @FXML
    private TextField valueTextField;
    @FXML
    private TextField descriptionTextField;

    public TvaView(RepositoryInterface repository) throws IOException {
        this.repository = repository;

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("tvaview.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }

    @FXML
    private void initialize() {
        TableColumn id = table.getColumns().get(0);
        TableColumn value = table.getColumns().get(1);
        TableColumn description = table.getColumns().get(2);

        id.setCellValueFactory(new PropertyValueFactory<TvaInterface, Integer>("id"));
        value.setCellValueFactory(new PropertyValueFactory<TvaInterface, BigInteger>("value"));
        description.setCellValueFactory(new PropertyValueFactory<TvaInterface, String>("description"));


        value.setCellFactory(TextFieldTableCell.<TvaInterface,BigDecimal>forTableColumn(new StringConverter<BigDecimal>() {
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
        value.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TvaInterface,BigDecimal>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TvaInterface,BigDecimal> event) {
                TvaInterface value = (TvaInterface)event.getTableView().getItems().get(event.getTablePosition().getRow());
                value.setValue(event.getNewValue());
                repository.updateTva(value);
                showTable();
            }
        });

        description.setCellFactory(TextFieldTableCell.forTableColumn());
        description.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TvaInterface,String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TvaInterface,String> event) {
                TvaInterface tva= (TvaInterface) event.getTableView().getItems().get(event.getTablePosition().getRow());
                tva.setDescription(event.getNewValue());
                repository.updateTva(tva);
                showTable();
            }
        });

        ContextMenu rightClickMenu = new ContextMenu();
        MenuItem delete = new MenuItem("Sterge tva");

        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TvaInterface tva = table.getSelectionModel().getSelectedItem();
                repository.deleteTva(tva);
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
    private void addNewTva(){
        String valueText = valueTextField.getText();
        String description = descriptionTextField.getText();
        Double value = null;
        try {
            value = Double.parseDouble(valueText);
        } catch (Exception e) {
            Alerts.showExceptionAlert( "Introdu o valoare corecta!", e);
            return;
        }

        if (description == null || description.equals("")) {
            Alerts.showWarningAlert("Introdu o descriere!");
            return;
        }

        TvaInterface tva = new TVA();
        tva.setValue(new BigDecimal(value));
        tva.setDescription(description);

        repository.addNewTva(tva);
        showTable();
    }

    private void showTable() {
        table.setItems(repository.getAllTvaValues());
    }
}
