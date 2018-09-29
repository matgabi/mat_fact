package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import model.InvoiceBlob;
import model.interfaces.InvoiceInterface;
import repository.interfaces.RepositoryInterface;
import view.alerts.Alerts;

import java.awt.*;
import java.io.*;
import java.time.LocalDate;

public class DeletedInvoicesView extends TableView {

    @FXML
    private TableView<InvoiceInterface> table;

    private InvoiceView invoiceView;
    private RepositoryInterface repository;

    public DeletedInvoicesView(InvoiceView invoiceView, RepositoryInterface repository) throws IOException {
        this.invoiceView = invoiceView;
        this.repository = repository;

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("deletedinvoicesview.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }

    @FXML
    private void initialize(){
        TableColumn name = table.getColumns().get(0);
        TableColumn date = table.getColumns().get(1);

        name.setCellValueFactory(new PropertyValueFactory<InvoiceInterface,String>("name"));
        date.setCellValueFactory(new PropertyValueFactory<InvoiceInterface,LocalDate>("date"));

        ContextMenu rightClickMenu = new ContextMenu();
        MenuItem openPdf = new MenuItem("Deschide pdf");
        MenuItem redoDelete = new MenuItem("Restaureaza factura");

        openPdf.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                InvoiceInterface invoice = table.getSelectionModel().getSelectedItem();
                try {
                    Desktop.getDesktop().open(invoice.getPdf());
                } catch (IOException e) {
                    Alerts.showExceptionAlert("E posibil sa nu existe program default de deschis un PDF", e);
                }
            }
        });

        redoDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                InvoiceInterface invoice = table.getSelectionModel().getSelectedItem();

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
                invoiceBlob.setDeleted(false);
                invoiceBlob.setId(invoice.getId());
                repository.updateInvoice(invoiceBlob);
                refreshInvoiceTable();
            }
        });

        rightClickMenu.getItems().add(openPdf);
        rightClickMenu.getItems().add(redoDelete);



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

        refreshInvoiceTable();
    }

    public void refreshInvoiceTable() {
        ObservableList<InvoiceBlob> invoiceBlobs = repository.getAllInvoices();
        ObservableList<InvoiceInterface> invoices = FXCollections.observableArrayList();

        for(InvoiceBlob blob : invoiceBlobs){
            if(blob.isDeleted()){
                try {
                    ByteArrayInputStream instream = new ByteArrayInputStream(blob.getInvoiceBytes().getBytes(1, (int) blob.getInvoiceBytes().length()));
                    ObjectInputStream objectInputStream = new ObjectInputStream(instream);

                    InvoiceInterface invoice = (InvoiceInterface) objectInputStream.readObject();

                    invoice.setId(blob.getId());
                    invoices.add(0, invoice);


                }catch (Exception e){
                    Alerts.showExceptionAlert("Eroare la deserializarea facturii", e);
                }
            }
        }

        table.setItems(invoices);
    }

}
