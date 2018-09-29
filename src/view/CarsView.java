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
import model.Car;
import model.interfaces.CarInterface;
import repository.interfaces.RepositoryInterface;
import view.alerts.Alerts;

import java.io.IOException;

public class CarsView extends BorderPane{

    //-----------------non fxml
    private RepositoryInterface repository;

    //-----------------fxml
    @FXML
    private TableView<CarInterface> table;
    @FXML
    private TextField nameTextField;


    public CarsView(RepositoryInterface repository) throws IOException {
        this.repository = repository;

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("carsview.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }

    @FXML
    private void initialize() {
        TableColumn id = table.getColumns().get(0);
        TableColumn description = table.getColumns().get(1);

        id.setCellValueFactory(new PropertyValueFactory<CarInterface, Integer>("id"));
        description.setCellValueFactory(new PropertyValueFactory<CarInterface, String>("description"));

        description.setCellFactory(TextFieldTableCell.forTableColumn());
        description.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<CarInterface,String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<CarInterface,String> event) {
                CarInterface car= (CarInterface) event.getTableView().getItems().get(event.getTablePosition().getRow());
                car.setDescription(event.getNewValue());
                repository.updateCar(car);
                showTable();
            }
        });

        ContextMenu rightClickMenu = new ContextMenu();
        MenuItem delete = new MenuItem("Sterge masina");

        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CarInterface car = table.getSelectionModel().getSelectedItem();
                repository.deleteCar(car);
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
    private void addNewCar(){
        String name = nameTextField.getText();

        if(name == null || name.equals("")){
            Alerts.showWarningAlert("Introdu datele de identificare ale vehiculului!");
            return;
        }

        CarInterface car = new Car();
        car.setDescription(name);

        repository.addNewCar(car);
        showTable();

    }

    private void showWarningAlert(String string)
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Informatii");
        alert.setContentText(string);
        alert.showAndWait();
    }

    private void showTable() {
        table.setItems(repository.getAllCars());
    }
}
