package view;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import model.InvoiceBlob;
import model.NewInvoiceState;
import model.ProductItem;

import model.UM;
import model.combo.SearchableComboBox;
import model.export.ClientExcelExporter;
import model.export.ExcelExporter;
import model.export.ProductExcelExporter;
import model.interfaces.*;
import repository.interfaces.RepositoryInterface;

import java.awt.*;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import java.util.*;
import model.combo.SearchableComboBox.*;

import view.alerts.Alerts;

public class InvoiceView extends BorderPane {

    //----------------------non fxml members
    private NewInvoiceState invoiceState;
    private RepositoryInterface repository;

    //fxml members
    @FXML
    MenuBar menuBar;
    @FXML
    DatePicker datePicker;

    //-------------------------------insert product handlers
    ObservableList<HideableItem<ProductInterface>> filteredProductItems;
    @FXML
    private ComboBox<HideableItem<ProductInterface>> productComboBox;
    @FXML
    private TextField quantityTextField;
    @FXML
    private TextField priceTextField;
    @FXML
    private ComboBox umComboBox;
    @FXML
    private TextField discountTextField;
    @FXML
    private Button addProductButton;
    //------------------------------------------------------

    //-------------------------------select client handlers
    ObservableList<HideableItem<ClientInterface>> filteredClientItems;
    @FXML
    private ComboBox<HideableItem<ClientInterface>> clientComboBox;
    @FXML
    private TextField cuiTextField;
    @FXML
    private TextField nrTextField;
    @FXML
    private TextField headquartersTextField;
    @FXML
    private TextField countyTextField;
    @FXML
    private TextField bankTextField;
    @FXML
    private TextField accountTextField;
    //-----------------------------------------------------

    //------------------------------tva handlers
    @FXML
    private ComboBox tvaComboBox;
    //------------------------------------------


    //------------------------------batch handlers
    @FXML
    private TextField batchTextField;
    @FXML
    private TextField nrBatchTextField;
    //------------------------------batch

    //------------------------------table handlers
    @FXML
    private TableView<TableItem> productsTable;
    @FXML
    private TextField valueTextField;
    @FXML
    private TextField tvaTextField;
    @FXML
    private TextField totalTextField;
    @FXML
    private TableView<InvoiceInterface> invoiceTable;
    //--------------------------------------------


    //--------------------------------------edit items handlers
    @FXML
    private Button saveEditChangesButton;
    @FXML
    private Button cancelEditChangesButton;
    @FXML
    private VBox editInvoiceVBox;
    @FXML
    private ComboBox editEmitorComboBox;
    @FXML
    private ComboBox editCarComboBox;
    @FXML
    private RadioButton editA4Radio;
    @FXML
    private RadioButton editA5Radio;
    @FXML
    private ToggleGroup invoiceFormat;
    @FXML
    private TextField invoiceSeriaTextField;
    @FXML
    private TextField invoiceNrTextField;
    @FXML
    private TextField nrAvizTextField;
    @FXML
    private TextArea scadentaTextArea;
    @FXML
    private TextField hourTextField;

    //---------------------------------------------------------

    //------------------------------------------search invoice handlers
    @FXML
    private TextField searchInvoiceTextField;
    @FXML
    private ComboBox searchInvoiceByYear;
    @FXML
    private ComboBox searchInvoiceByBatch;
    private String searchInvoiceString;
    private String searchInvoiceBatch;
    private int searchInvoiceYear;
    //-----------------------------------------------------------------

    public InvoiceView(NewInvoiceState invoiceState, RepositoryInterface repository) throws IOException {
        this.invoiceState = invoiceState;
        this.repository = repository;

        searchInvoiceString = "";
        searchInvoiceBatch = "";
        searchInvoiceYear = LocalDate.now().getYear();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("invoiceview.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();


    }

    @FXML
    private void initialize(){
        addProductButton.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
            if (ev.getCode() == KeyCode.ENTER) {
                addProductButton.fire();
                ev.consume();
            }});
        //init table
        productsTable.setItems(invoiceState.getProducts());

        //initialize date
        datePicker.setValue(LocalDate.now());

        //initialize client combo
        filteredClientItems = SearchableComboBox.createComboBoxWithAutoCompletionSupport(repository.getAllClients(), clientComboBox);

        //initialize product combo box
        filteredProductItems = SearchableComboBox.createComboBoxWithAutoCompletionSupport(repository.getAllProducts(), productComboBox);

        //init um combo box
        umComboBox.setItems(repository.getAllUmValues() != null ? repository.getAllUmValues() : FXCollections.observableArrayList());
        umComboBox.setConverter(new StringConverter() {
            @Override
            public String toString(Object object) {
                return ((UMInterface)object).getDescription();
            }

            @Override
            public Object fromString(String string) {
                return null;
            }
        });
        umComboBox.getSelectionModel().selectFirst();

        //init tva combo box
        tvaComboBox.setItems(repository.getAllTvaValues() != null ? repository.getAllTvaValues() : FXCollections.observableArrayList());
        tvaComboBox.setConverter(new StringConverter() {
            @Override
            public String toString(Object object) {
                return ((TvaInterface)object).getDescription();
            }

            @Override
            public Object fromString(String string) {
                return null;
            }
        });
        tvaComboBox.getSelectionModel().selectFirst();
        invoiceState.setTva((TvaInterface)tvaComboBox.getItems().get(0));

        //init batch fields
        batchTextField.setText(repository.getAllBatches().get(0).getLowerBoundary() + " - " + repository.getAllBatches().get(0).getUpperBoundary());
        nrBatchTextField.setText(repository.getAllBatches().get(0).getCurrentBatchNumber() + "");

        //init edit fields
        editEmitorComboBox.setItems(repository.getAllEmitors());
        editEmitorComboBox.setConverter(new StringConverter() {
            @Override
            public String toString(Object object) {
                return ((EmitorInterface)object).getFirstName() + " " + ((EmitorInterface)object).getLastName();
            }

            @Override
            public Object fromString(String string) {
                return null;
            }
        });
        editCarComboBox.setItems(repository.getAllCars());
        editCarComboBox.setConverter(new StringConverter() {
            @Override
            public String toString(Object object) {
                return ((CarInterface)object).getDescription();
            }

            @Override
            public Object fromString(String string) {
                return null;
            }
        });

        //init display methods for invoices
        initDisplayInvoiceMethods();

        //init tables
        initProductsTable();
        initInvoiceTable();
    }

    private void initDisplayInvoiceMethods() {
        ObservableList<InvoiceBlob> invoiceBlobs = repository.getAllInvoices();
        SortedSet<String> batchSearchList = new TreeSet<>();
        SortedSet<Integer> yearSearchList = new TreeSet<>(Collections.reverseOrder());

        for(InvoiceBlob blob : invoiceBlobs){
            if(!blob.isDeleted()){
                try {
                    ByteArrayInputStream instream = new ByteArrayInputStream(blob.getInvoiceBytes().getBytes(1, (int) blob.getInvoiceBytes().length()));
                    ObjectInputStream objectInputStream = new ObjectInputStream(instream);

                    InvoiceInterface invoice = (InvoiceInterface) objectInputStream.readObject();
                    BatchInterface batch = invoice.getBatch();
                    String batchText = batch.getLowerBoundary() + "-" + batch.getUpperBoundary();
                    batchSearchList.add(batchText);

                    LocalDate date = invoice.getDate();
                    yearSearchList.add(date.getYear());
                    }
                catch (Exception e){
                    Alerts.showExceptionAlert("Eroare in 'initDisplayInvoiceMethods'", e);
                }
            }
        }
        ObservableList<String> searchBatchObsLst = FXCollections.observableArrayList(batchSearchList);
        searchBatchObsLst.add(0, "Toate");
        searchInvoiceByBatch.setItems(searchBatchObsLst);
        searchInvoiceByBatch.getSelectionModel().selectFirst();


        ObservableList<Integer> searchYearObsLst = FXCollections.observableArrayList(yearSearchList);
        searchInvoiceByYear.setItems(searchYearObsLst);
        searchInvoiceByYear.getSelectionModel().selectFirst();
    }

    private void initInvoiceTable() {
        TableColumn name = invoiceTable.getColumns().get(0);
        TableColumn date = invoiceTable.getColumns().get(1);

        name.setCellValueFactory(new PropertyValueFactory<InvoiceInterface,String>("name"));
        date.setCellValueFactory(new PropertyValueFactory<InvoiceInterface,LocalDate>("date"));

        ContextMenu rightClickMenu = new ContextMenu();
        MenuItem delete = new MenuItem("Sterge factura");
        MenuItem openPdf = new MenuItem("Deschide pdf");
        MenuItem editInvoice = new MenuItem("Editeaza factura");

        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                InvoiceInterface invoice = invoiceTable.getSelectionModel().getSelectedItem();
                if(invoice != null){
                    ByteArrayOutputStream dbout = new ByteArrayOutputStream();
                    ObjectOutputStream dboutstraeam;

                    try {
                        dboutstraeam = new ObjectOutputStream(dbout);
                        dboutstraeam.writeObject(invoice);
                        dboutstraeam.flush();
                    }catch (Exception e){
                        Alerts.showExceptionAlert("Eroare la deserializarea facturii", e);
                    }

                    InvoiceBlob invoiceBlob = new InvoiceBlob(dbout.toByteArray());
                    invoiceBlob.setDeleted(true);
                    invoiceBlob.setId(invoice.getId());

                    repository.updateInvoice(invoiceBlob);
                    refreshInvoiceTable();

                }
            }
        });
        openPdf.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                InvoiceInterface invoice = invoiceTable.getSelectionModel().getSelectedItem();
                if(invoice != null)
                {
                    try {
                        Desktop.getDesktop().open(invoice.getPdf());
                    } catch (IOException e) {
                        Alerts.showExceptionAlert("E posibil sa nu existe program default de deschis un PDF", e);
                    }
                }
            }
        });
        editInvoice.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                InvoiceInterface invoice = invoiceTable.getSelectionModel().getSelectedItem();

                if(invoice != null){
                    invoiceState.loadEditableInvoice(invoice);

                    refreshInterfaceOnEditAction();

                    invoiceTable.setDisable(true);
                    batchTextField.setDisable(false);
                    nrBatchTextField.setDisable(false);
                    editInvoiceVBox.setVisible(true);
                    searchInvoiceTextField.setDisable(true);
                    searchInvoiceByYear.setDisable(true);
                    searchInvoiceByBatch.setDisable(true);
                }
            }
        });

        rightClickMenu.getItems().add(openPdf);
        rightClickMenu.getItems().add(editInvoice);
        rightClickMenu.getItems().add(delete);


        invoiceTable.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton() == MouseButton.SECONDARY) {
                    rightClickMenu.show(productsTable, event.getScreenX(), event.getScreenY());
                }else {
                    rightClickMenu.hide();
                }
            }
        });

        invoiceTable.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    InvoiceInterface invoice = invoiceTable.getSelectionModel().getSelectedItem();
                    if(invoice != null){
                        invoiceState.setProducts(FXCollections.observableArrayList(invoice.getProducts()));
                        invoiceState.setClient(invoice.getClient());
                        invoiceState.setTva(invoice.getTva());

                        clientComboBox.getSelectionModel().select(new HideableItem<>(invoiceState.getClient()));
                        tvaComboBox.getSelectionModel().select(invoiceState.getTva());

                        setTotalPriceInformation();
                    }

                }
            }
        });

        refreshInvoiceTable();
    }

    public void refreshInvoiceTable() {
        ObservableList<InvoiceBlob> invoiceBlobs = repository.getAllInvoices();
        ObservableList<InvoiceInterface> invoices = FXCollections.observableArrayList();

        for(InvoiceBlob blob : invoiceBlobs){
            if(!blob.isDeleted()){
                try {
                    ByteArrayInputStream instream = new ByteArrayInputStream(blob.getInvoiceBytes().getBytes(1, (int) blob.getInvoiceBytes().length()));
                    ObjectInputStream objectInputStream = new ObjectInputStream(instream);

                    InvoiceInterface invoice = (InvoiceInterface) objectInputStream.readObject();
                    String currentBatch = invoice.getBatch().getLowerBoundary() + "-" + invoice.getBatch().getUpperBoundary();
                    if(     invoice.getName().toLowerCase().replaceAll("_"," ").contains(searchInvoiceString) &&
                            (currentBatch.contains(searchInvoiceBatch)) &&
                            invoice.getDate().getYear() == searchInvoiceYear
                            ){
                        invoice.setId(blob.getId());
                        invoices.add(0, invoice);
                    }

                }catch (Exception e){
                    Alerts.showExceptionAlert("Eroare la deserializarea facturilor", e);
                }
            }
        }

        invoiceTable.setItems(invoices);
    }




    private void initProductsTable(){
        TableColumn name = productsTable.getColumns().get(0);
        TableColumn um = productsTable.getColumns().get(1);
        TableColumn quantity = productsTable.getColumns().get(2);
        TableColumn pretUnitar = productsTable.getColumns().get(3);
        TableColumn value = productsTable.getColumns().get(4);
        TableColumn tva = productsTable.getColumns().get(5);
        TableColumn discount = productsTable.getColumns().get(6);
        TableColumn discountValue = productsTable.getColumns().get(7);
        TableColumn discountTvaValue = productsTable.getColumns().get(8);

        name.setCellValueFactory(new PropertyValueFactory<TableItem,String>("productName"));
        um.setCellValueFactory(new PropertyValueFactory<TableItem,String>("umValue"));
        quantity.setCellValueFactory(new PropertyValueFactory<TableItem,Integer>("quantity"));
        pretUnitar.setCellValueFactory(new PropertyValueFactory<TableItem,BigDecimal>("productPrice"));
        value.setCellValueFactory(new PropertyValueFactory<TableItem,BigDecimal>("productValue"));
        tva.setCellValueFactory(new PropertyValueFactory<TableItem,BigDecimal>("productTvaValue"));
        discount.setCellValueFactory(new PropertyValueFactory<TableItem,BigDecimal>("discount"));
        discountValue.setCellValueFactory(new PropertyValueFactory<TableItem,BigDecimal>("productDiscountValue"));
        discountTvaValue.setCellValueFactory(new PropertyValueFactory<TableItem,BigDecimal>("productDiscountTvaValue"));

        name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableItem,String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableItem,String> event) {
                TableItem product = (TableItem)event.getTableView().getItems().get(event.getTablePosition().getRow());
                product.setProductName(event.getNewValue());
            }
        });

        um.setCellFactory(TextFieldTableCell.forTableColumn());
        um.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableItem,String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableItem,String> event) {
                TableItem product = (TableItem)event.getTableView().getItems().get(event.getTablePosition().getRow());
                product.setUmValue(event.getNewValue());
            }
        });

        quantity.setCellFactory(TextFieldTableCell.<TableItem,Double>forTableColumn(new StringConverter<Double>() {
            @Override
            public String toString(Double object) {
                if(object == null){
                    return null;
                }
                return String.valueOf(BigDecimal.valueOf(object).setScale(2,RoundingMode.HALF_UP).doubleValue());
            }

            @Override
            public Double fromString(String string) {
                return new Double(string);
            }
        }));
        quantity.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableItem,Double>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableItem,Double> event) {
                TableItem product = (TableItem)event.getTableView().getItems().get(event.getTablePosition().getRow());
                product.setQuantity(event.getNewValue());

                invoiceState.recomputeDiscountOnEdit();
                productsTable.refresh();
                setTotalPriceInformation();
            }
        });

        pretUnitar.setCellFactory(TextFieldTableCell.<TableItem,BigDecimal>forTableColumn(new StringConverter<BigDecimal>() {
            @Override
            public String toString(BigDecimal object) {
                if(object == null){
                    return null;
                }
                return object.setScale(2,RoundingMode.HALF_UP).toString();
            }

            @Override
            public BigDecimal fromString(String string) {
                return new BigDecimal(string).setScale(2, RoundingMode.HALF_UP);
            }
        }));
        pretUnitar.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableItem,BigDecimal>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableItem,BigDecimal> event) {
                TableItem product = (TableItem)event.getTableView().getItems().get(event.getTablePosition().getRow());
                product.setProductPrice(event.getNewValue());

                invoiceState.recomputeDiscountOnEdit();

                productsTable.refresh();
                setTotalPriceInformation();
            }
        });

        value.setCellFactory(TextFieldTableCell.<TableItem,BigDecimal>forTableColumn(new StringConverter<BigDecimal>() {
            @Override
            public String toString(BigDecimal object) {
                if(object == null){
                    return null;
                }
                return object.setScale(2,RoundingMode.HALF_UP).toString();
            }

            @Override
            public BigDecimal fromString(String string) {
                return new BigDecimal(string).setScale(2, RoundingMode.HALF_UP);
            }
        }));
        value.setEditable(false);

        tva.setCellFactory(TextFieldTableCell.<TableItem,BigDecimal>forTableColumn(new StringConverter<BigDecimal>() {
            @Override
            public String toString(BigDecimal object) {
                if(object == null){
                    return null;
                }
                return object.setScale(2,RoundingMode.HALF_UP).toString();
            }

            @Override
            public BigDecimal fromString(String string) {
                return new BigDecimal(string).setScale(2, RoundingMode.HALF_UP);
            }
        }));
        tva.setEditable(false);

        discount.setCellFactory(TextFieldTableCell.<TableItem,BigDecimal>forTableColumn(new StringConverter<BigDecimal>() {
            @Override
            public String toString(BigDecimal object) {
                if(object == null){
                    return null;
                }
                return object.setScale(2,RoundingMode.HALF_UP).toString();
            }

            @Override
            public BigDecimal fromString(String string) {
                return new BigDecimal(string).setScale(2, RoundingMode.HALF_UP);
            }
        }));
        discount.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableItem,BigDecimal>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableItem,BigDecimal> event) {
                TableItem product = (TableItem)event.getTableView().getItems().get(event.getTablePosition().getRow());
                product.setProductDiscountValue(event.getNewValue());

                invoiceState.recomputeDiscountOnEdit();

                productsTable.refresh();
                setTotalPriceInformation();
            }
        });

        discountValue.setCellFactory(TextFieldTableCell.<TableItem,BigDecimal>forTableColumn(new StringConverter<BigDecimal>() {
            @Override
            public String toString(BigDecimal object) {
                if(object == null){
                    return null;
                }
                return object.setScale(2,RoundingMode.HALF_UP).toString();
            }

            @Override
            public BigDecimal fromString(String string) {
                return new BigDecimal(string).setScale(2, RoundingMode.HALF_UP);
            }
        }));
        discountValue.setEditable(false);

        discountTvaValue.setCellFactory(TextFieldTableCell.<TableItem,BigDecimal>forTableColumn(new StringConverter<BigDecimal>() {
            @Override
            public String toString(BigDecimal object) {
                if(object == null){
                    return null;
                }
                return object.setScale(2,RoundingMode.HALF_UP).toString();
            }

            @Override
            public BigDecimal fromString(String string) {
                return new BigDecimal(string).setScale(2, RoundingMode.HALF_UP);
            }
        }));
        discountTvaValue.setEditable(false);

        ContextMenu rightClickMenu = new ContextMenu();
        MenuItem delete = new MenuItem("Sterge produs");

        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TableItem product = productsTable.getSelectionModel().getSelectedItem();
                if(product != null){
                    invoiceState.deleteProduct(product);
                    productsTable.refresh();
                    setTotalPriceInformation();
                }
            }
        });
        rightClickMenu.getItems().add(delete);

        productsTable.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton() == MouseButton.SECONDARY) {
                    rightClickMenu.show(productsTable, event.getScreenX(), event.getScreenY());
                }else {
                    rightClickMenu.hide();
                }
            }
        });

        TableColumn<TableItem, Number> indexColumn = new TableColumn<TableItem, Number>("#");
        indexColumn.setSortable(false);
        indexColumn.setCellValueFactory(column-> new ReadOnlyObjectWrapper<Number>(productsTable.getItems().indexOf(column.getValue()) + 1));
        indexColumn.setMaxWidth(600);
        productsTable.getColumns().add(0, indexColumn);
    }

    @FXML
    private void openProductsDataBase(){
        BorderPane productsDbView = null;
        try {
            productsDbView = new ProductsDbView(repository);
        } catch (IOException e) {
            Alerts.showExceptionAlert("Nu s-a putut deschide baza de date", e);
            return;
        }
        Stage dbStage = new Stage();
        dbStage.setTitle("Produse");
        dbStage.initModality(Modality.WINDOW_MODAL);

        dbStage.initOwner(this.getScene().getWindow());

        Scene dbScene = new Scene(productsDbView);
        dbStage.setScene(dbScene);
        dbStage.setResizable(false);
        dbStage.setOnCloseRequest(event -> {
            filteredProductItems.clear();
            (repository.getAllProducts()).forEach(e->{
                filteredProductItems.add(new HideableItem<>(e));
            });
        });
        dbStage.showAndWait();
    }

    @FXML
    private void openClientsDataBase(){
        BorderPane clientView = null;

        try {
            clientView = new ClientsView(repository);
        } catch (IOException e) {
            Alerts.showExceptionAlert("Nu s-a putut deschide baza de date", e);
            return;
        }
        Stage dbStage = new Stage();
        dbStage.setTitle("Clienti");
        dbStage.initModality(Modality.WINDOW_MODAL);

        dbStage.initOwner(this.getScene().getWindow());

        Scene dbScene = new Scene(clientView);
        dbStage.setScene(dbScene);
        dbStage.setResizable(false);
        dbStage.setOnCloseRequest(event -> {
            filteredClientItems.clear();
            repository.getAllClients().forEach(e -> {
                filteredClientItems.add(new HideableItem<>(e));
            });
            ClientInterface client = invoiceState.getClient();
            if(client != null){
                clientComboBox.getSelectionModel().select(new HideableItem<>(client));
            }
        });
        dbStage.showAndWait();
    }

    @FXML
    private void openEmitorsDataBase(){
        BorderPane emitorsView = null;

        try {
            emitorsView = new EmitorsView(repository);
        } catch (IOException e) {
            Alerts.showExceptionAlert("Nu s-a putut deschide baza de date", e);
            return;
        }
        Stage dbStage = new Stage();
        dbStage.setTitle("Angajati");
        dbStage.initModality(Modality.WINDOW_MODAL);

        dbStage.initOwner(this.getScene().getWindow());

        Scene dbScene = new Scene(emitorsView);
        dbStage.setScene(dbScene);
        dbStage.setResizable(false);
        dbStage.setOnCloseRequest(event -> {

        });
        dbStage.showAndWait();
    }

    @FXML
    private void openUmsDataBase(){
        BorderPane umsView = null;

        try {
            umsView = new UMView(repository);
        } catch (IOException e) {
            Alerts.showExceptionAlert("Nu s-a putut deschide baza de date", e);
            return;
        }
        Stage dbStage = new Stage();
        dbStage.setTitle("Unitati de masura");
        dbStage.initModality(Modality.WINDOW_MODAL);

        dbStage.initOwner(this.getScene().getWindow());

        Scene dbScene = new Scene(umsView);
        dbStage.setScene(dbScene);
        dbStage.setResizable(false);
        dbStage.setOnCloseRequest(event -> {
            umComboBox.setItems(repository.getAllUmValues());
            umComboBox.getSelectionModel().selectFirst();
        });
        dbStage.showAndWait();
    }

    @FXML
    private void openCarsDataBase(){
        BorderPane carsView = null;

        try {
            carsView = new CarsView(repository);
        } catch (IOException e) {
            Alerts.showExceptionAlert("Nu s-a putut deschide baza de date", e);
            return;
        }
        Stage dbStage = new Stage();
        dbStage.setTitle("Autovehicule");
        dbStage.initModality(Modality.WINDOW_MODAL);

        dbStage.initOwner(this.getScene().getWindow());

        Scene dbScene = new Scene(carsView);
        dbStage.setScene(dbScene);
        dbStage.setResizable(false);
        dbStage.setOnCloseRequest(event -> {

        });
        dbStage.showAndWait();
    }

    @FXML
    private void openCompanyDetailsView() {
        VBox companyDetailsView = null;

        try {
            companyDetailsView = new CompanyDetailsView(repository);
        } catch (IOException e) {
            Alerts.showExceptionAlert("Nu s-a putut deschide baza de date", e);
            return;
        }
        Stage dbStage = new Stage();
        dbStage.setTitle("Detalii firma");
        dbStage.initModality(Modality.WINDOW_MODAL);

        dbStage.initOwner(this.getScene().getWindow());

        Scene dbScene = new Scene(companyDetailsView);
        dbStage.setScene(dbScene);
        dbStage.setResizable(false);
        dbStage.setOnCloseRequest(event -> {

        });
        dbStage.showAndWait();
    }

    @FXML
    private void openBatchDetailsView() {
        VBox batchDetailsView = null;


        try {
            batchDetailsView = new BatchView(repository, batchTextField, nrBatchTextField, invoiceState);
        } catch (IOException e) {
            Alerts.showExceptionAlert("Nu s-a putut deschide baza de date", e);
            return;
        }
        Stage dbStage = new Stage();
        dbStage.setTitle("Detalii toc facturi");
        dbStage.initModality(Modality.WINDOW_MODAL);

        dbStage.initOwner(this.getScene().getWindow());

        Scene dbScene = new Scene(batchDetailsView);
        dbStage.setScene(dbScene);
        dbStage.setResizable(false);
        dbStage.setOnCloseRequest(event -> {
            batchTextField.setText(invoiceState.getBatch().getLowerBoundary() + " - " + invoiceState.getBatch().getUpperBoundary());
            nrBatchTextField.setText(invoiceState.getBatch().getCurrentBatchNumber() + "");
        });
        dbStage.showAndWait();
    }

    @FXML
    private void openTvaDetailsView() {
        VBox tvaDetailsView = null;

        try {
            tvaDetailsView = new TvaView(repository);
        } catch (IOException e) {
            Alerts.showExceptionAlert("Nu s-a putut deschide baza de date", e);
            return;
        }
        Stage dbStage = new Stage();
        dbStage.setTitle("Detalii tva");
        dbStage.initModality(Modality.WINDOW_MODAL);

        dbStage.initOwner(this.getScene().getWindow());

        Scene dbScene = new Scene(tvaDetailsView);
        dbStage.setScene(dbScene);
        dbStage.setResizable(false);
        dbStage.setOnCloseRequest(event -> {
            tvaComboBox.setItems(repository.getAllTvaValues());
            tvaComboBox.getSelectionModel().select(invoiceState.getTva());
        });
        dbStage.showAndWait();
    }

    @FXML
    private void openCreateInvoiceView() {
      if(!invoiceState.isEditingState()){

          HideableItem<ClientInterface> clientItem = clientComboBox.getSelectionModel().getSelectedItem();
          if(clientItem == null || clientItem.getObject() == null){
              Alerts.showWarningAlert("Selecteaza un client!");
              return;
          }
          ClientInterface client = clientItem.getObject();

          TvaInterface tva = (TvaInterface)tvaComboBox.getSelectionModel().getSelectedItem();
          if(tva == null){
              Alerts.showWarningAlert("Selecteaza tva!");
              return;
          }

          LocalDate date = datePicker.getValue();
          if(date == null){
              Alerts.showWarningAlert( "Selecteaza data!");
              return;
          }

          client.setCui(cuiTextField.getText());
          client.setNr(nrTextField.getText());
          client.setHeadquarters(headquartersTextField.getText());
          client.setCounty(countyTextField.getText());
          client.setAccount(accountTextField.getText());
          client.setBank(bankTextField.getText());

          invoiceState.setClient(client);
          invoiceState.setTva(tva);
          invoiceState.setDate(date);

          SellerInterface seller = repository.getAllSellers().get(0);
          if(seller == null){
              Alerts.showWarningAlert("Introdu datele companiei!");
              return;
          }

          invoiceState.setSeller(seller);

          BorderPane createInvoiceView = null;

          try {
              createInvoiceView = new CreateInvoiceView(invoiceState, repository, this);
          } catch (IOException e) {
              Alerts.showExceptionAlert("Nu s-a putut deschide baza de date", e);
              return;
          }
          Stage dbStage = new Stage();
          dbStage.setTitle("Genereaza factura");
          dbStage.initModality(Modality.WINDOW_MODAL);

          dbStage.initOwner(this.getScene().getWindow());

          Scene dbScene = new Scene(createInvoiceView);
          dbStage.setScene(dbScene);
          dbStage.setResizable(false);
          dbStage.setOnCloseRequest(event -> {
          });
          dbStage.showAndWait();
      }else{
          Alerts.showWarningAlert("Salveaza sau anuleaza modificarile");
      }
    }

    @FXML
    private void openDeletedInvoicesView() {
        TableView deletedInvoicesView = null;

        try {
            deletedInvoicesView = new DeletedInvoicesView(this, repository);
        } catch (IOException e) {
            Alerts.showExceptionAlert("Nu s-a putut deschide baza de date", e);
            return;
        }
        Stage dbStage = new Stage();
        dbStage.setTitle("Facturi sterse");
        dbStage.initModality(Modality.WINDOW_MODAL);

        dbStage.initOwner(this.getScene().getWindow());

        Scene dbScene = new Scene(deletedInvoicesView);
        dbStage.setScene(dbScene);
        dbStage.setResizable(false);
        dbStage.setOnCloseRequest(event -> {
            refreshInvoiceTable();
        });
        dbStage.showAndWait();
    }

    @FXML
    private void onClientComboBoxAction()
    {
        HideableItem<ClientInterface> clientItem = clientComboBox.getSelectionModel().getSelectedItem();
        if(clientItem != null) {
            ClientInterface client = clientItem.getObject();
            if (client != null) {
                cuiTextField.setText(client.getCui());
                nrTextField.setText(client.getNr());
                headquartersTextField.setText(client.getHeadquarters());
                countyTextField.setText(client.getCounty());
                bankTextField.setText(client.getBank());
                accountTextField.setText(client.getAccount());

                invoiceState.setClient(client);
            }
        }
    }

    @FXML
    private void onProductComboBoxAction(){
        HideableItem<ProductInterface> productItem = productComboBox.getSelectionModel().getSelectedItem();
        if(productItem != null){
            ProductInterface product = productItem.getObject();
            priceTextField.setText(product != null ? product.getPrice().toString() : "");

            UMInterface um = new UM(product.getUm());
            umComboBox.getSelectionModel().select(um);
        }
    }

    @FXML
    private void onTvaComboBoxAction(){
        TvaInterface tva = (TvaInterface)tvaComboBox.getSelectionModel().getSelectedItem();
        if(tva != null){
            invoiceState.setTva(tva);
            invoiceState.changeTva(tva);
            productsTable.refresh();
            setTotalPriceInformation();
        }
    }

    @FXML
    private void addNewProduct(){
        HideableItem<ProductInterface> productItem = productComboBox.getSelectionModel().getSelectedItem();
        if(productItem == null){
            Alerts.showWarningAlert("Alege un produs!");
            return;
        }
        ProductInterface product = productItem.getObject();

        String quantityText = quantityTextField.getText();
        Double quantity = null;
        try{
            quantity = Double.parseDouble(quantityText);
        }catch (Exception e){
            Alerts.showExceptionAlert("Introdu cantitatea corect!", e);
            return;
        }

        String price = priceTextField.getText();
        Double doublePrice = null;
        try{
            doublePrice = Double.parseDouble(price);
        }catch (Exception e){
            Alerts.showExceptionAlert("Introdu pretul corect!", e);
            return;
        }
        product.setPrice(BigDecimal.valueOf(doublePrice));

        UMInterface um = ((UMInterface)umComboBox.getSelectionModel().getSelectedItem());

        String discountText = discountTextField.getText();
        Double discount = null;
        try{
            discount = Double.parseDouble(discountText);
        }catch (Exception e){
            Alerts.showExceptionAlert("Introdu discountul corect!", e);
            return;
        }

        ProductItem tableItem = new ProductItem(product, quantity, um, BigDecimal.valueOf(discount), (TvaInterface)tvaComboBox.getSelectionModel().getSelectedItem());


        invoiceState.insertNewProduct(tableItem);
        productsTable.refresh();

        setTotalPriceInformation();

    }

    @FXML
    private void onSaveEditChangesAction(){
        HideableItem<ClientInterface> clientItem = clientComboBox.getSelectionModel().getSelectedItem();
        if(clientItem == null || clientItem.getObject() == null){
            Alerts.showWarningAlert("Selecteaza un client!");
            return;
        }
        ClientInterface client = clientItem.getObject();
        client.setCui(cuiTextField.getText());
        client.setNr(nrTextField.getText());
        client.setHeadquarters(headquartersTextField.getText());
        client.setCounty(countyTextField.getText());
        client.setAccount(accountTextField.getText());
        client.setBank(bankTextField.getText());

        InvoiceInfoInterface info = invoiceState.getInfo();
        info.setSeria(invoiceSeriaTextField.getText());
        info.setNr(nrBatchTextField.getText());
        info.setNrAvize(nrAvizTextField.getText());
        info.setOra(hourTextField.getText());
        info.setScadenta(scadentaTextArea.getText());

        invoiceState.setClient(client);
        invoiceState.setTva((TvaInterface) tvaComboBox.getSelectionModel().getSelectedItem());
        invoiceState.setEmitor((EmitorInterface)editEmitorComboBox.getSelectionModel().getSelectedItem());
        invoiceState.setCar((CarInterface)editCarComboBox.getSelectionModel().getSelectedItem());
        if(invoiceFormat.getSelectedToggle().toString().contains("a4")){
            invoiceState.setInvoiceFormat("a4");
        }
        else{
            invoiceState.setInvoiceFormat("a5");
        }
        invoiceState.setDate(datePicker.getValue());
        String[] batchBoundaries = batchTextField.getText().replaceAll(" ", "").split("-");
        int lowerBoundary = Integer.parseInt(batchBoundaries[0]);
        int upperBoundary = Integer.parseInt(batchBoundaries[1]);
        int batchNr = Integer.parseInt(nrBatchTextField.getText());

        if(batchNr > upperBoundary || batchNr < lowerBoundary){
            Alerts.showWarningAlert("Nr. facturii e in afara topului curent!!");
            return;
        }

        invoiceState.getBatch().setLowerBoundary(lowerBoundary);
        invoiceState.getBatch().setUpperBoundary(upperBoundary);
        invoiceState.getBatch().setCurrentBatchNumber(batchNr);

        if(!invoiceState.editSelectedInvoice()){
            return;
        }


        refreshInterfaceOnEditAction();
        refreshInvoiceTable();

        invoiceTable.setDisable(false);
        editInvoiceVBox.setVisible(false);
        batchTextField.setDisable(true);
        nrBatchTextField.setDisable(true);
        searchInvoiceTextField.setDisable(false);
        searchInvoiceByYear.setDisable(false);
        searchInvoiceByBatch.setDisable(false);
    }

    @FXML
    private void onCancelEditChangesAction(){
        invoiceState.cancelEditInvoice();

        refreshInterfaceOnEditAction();

        invoiceTable.setDisable(false);
        editInvoiceVBox.setVisible(false);
        batchTextField.setDisable(true);
        nrBatchTextField.setDisable(true);
        searchInvoiceTextField.setDisable(false);
        searchInvoiceByYear.setDisable(false);
        searchInvoiceByBatch.setDisable(false);
    }

    @FXML
    private void onSearchInvoiceAction(){
        searchInvoiceString = searchInvoiceTextField.getText();
        refreshInvoiceTable();
    }

    @FXML
    private void onSearchInvoiceByBatchAction(){
        searchInvoiceBatch = (String)searchInvoiceByBatch.getSelectionModel().getSelectedItem();
        if(searchInvoiceBatch.contains("Toate")){
            searchInvoiceBatch = "";
        }
        refreshInvoiceTable();
    }

    @FXML
    private void onSearchInvoiceByYearAction(){
        searchInvoiceYear = (int)searchInvoiceByYear.getSelectionModel().getSelectedItem();
        refreshInvoiceTable();
    }

    @FXML
    private void clearProductsTable(){
        invoiceState.clearProductsTable();
        setTotalPriceInformation();
    }

    @FXML
    private void onExportProductsExcelAction(){

        try{
            ExcelExporter exporter = new ProductExcelExporter();
            exporter.createExcel(repository.getAllProducts());
        }catch (Exception e)
        {
            Alerts.showExceptionAlert("Nu s-a putut crea excelul", e);
        }
    }

    @FXML
    private void onExportClientsExcelAction(){

        try{
            ExcelExporter exporter = new ClientExcelExporter();
            exporter.createExcel(repository.getAllClients());
        }catch (Exception e)
        {
            Alerts.showExceptionAlert("Nu s-a putut crea excelul", e);
        }
    }

    @FXML
    private void onProductReportAction(){
        BorderPane report = null;
        try {
            report = new ProductReportView(repository);
        } catch (IOException e) {
            Alerts.showExceptionAlert("Eroare la incarcarea interfetei", e);
            return;
        }
        Stage dbStage = new Stage();
        dbStage.setTitle("Genereaza raport produs");
        dbStage.initModality(Modality.WINDOW_MODAL);

        //dbStage.initOwner(this.getScene().getWindow());

        Scene dbScene = new Scene(report);
        dbStage.setScene(dbScene);
        dbStage.setResizable(false);
        dbStage.setOnCloseRequest(event -> {

        });
        dbStage.showAndWait();
    }

    @FXML
    private void onClientReportAction(){
        BorderPane report = null;
        try {
            report = new ClientReportView(repository);
        } catch (IOException e) {
            Alerts.showExceptionAlert("Eroare la incarcarea interfetei", e);
            return;
        }
        Stage dbStage = new Stage();
        dbStage.setTitle("Genereaza raport client");
        dbStage.initModality(Modality.WINDOW_MODAL);

        //dbStage.initOwner(this.getScene().getWindow());

        Scene dbScene = new Scene(report);
        dbStage.setScene(dbScene);
        dbStage.setResizable(false);
        dbStage.setOnCloseRequest(event -> {

        });
        dbStage.showAndWait();
    }

    private void refreshInterfaceOnEditAction(){

        clientComboBox.getSelectionModel().select(new HideableItem<>(invoiceState.getClient()));
        if(invoiceState.getClient() == null){
            cuiTextField.setText("");
            nrTextField.setText("");
            headquartersTextField.setText("");
            countyTextField.setText("");
            accountTextField.setText("");
            bankTextField.setText("");
        }
        tvaComboBox.getSelectionModel().select(invoiceState.getTva());
        productsTable.refresh();
        editEmitorComboBox.getSelectionModel().select(invoiceState.getEmitor());
        editCarComboBox.getSelectionModel().select(invoiceState.getCar());

        if(invoiceState.getInvoiceFormat().equals("a4")){
            editA4Radio.fire();
        }else{
            editA5Radio.fire();
        }

        InvoiceInfoInterface info = invoiceState.getInfo();
        if(info != null){
            invoiceSeriaTextField.setText(info.getSeria());
            invoiceNrTextField.setText(info.getNr());
            nrAvizTextField.setText(info.getNrAvize());
            hourTextField.setText(info.getOra());
            scadentaTextArea.setText(info.getScadenta());
        }

        setTotalPriceInformation();

        refreshBatchInfo();
    }

    public void refreshBatchInfo(){
        batchTextField.setText(invoiceState.getBatch().getLowerBoundary() + " - " + invoiceState.getBatch().getUpperBoundary());
        nrBatchTextField.setText(invoiceState.getBatch().getCurrentBatchNumber() + "");
    }

    private void setTotalPriceInformation(){
        valueTextField.setText(invoiceState.getTotalValue().setScale(2, RoundingMode.HALF_UP).toString());
        tvaTextField.setText(invoiceState.getTotalTvaValue().setScale(2, RoundingMode.HALF_UP).toString());
        totalTextField.setText(invoiceState.getTotal().setScale(2, RoundingMode.HALF_UP).toString());
    }


}
