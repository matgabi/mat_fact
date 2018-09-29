package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Batch;
import model.NewInvoiceState;
import model.interfaces.BatchInterface;
import repository.interfaces.RepositoryInterface;
import view.alerts.Alerts;

import java.io.IOException;


public class BatchView extends VBox {

    //------------non fxml
    private RepositoryInterface repository;
    private BatchInterface batch;

    //------------fxml
    @FXML
    private TextField lowerBoundaryTextField;
    @FXML
    private TextField upperBoundaryTextField;
    @FXML
    private TextField currentNrTextField;
    @FXML
    private TextField batchSizeTextField;

    @FXML
    TextField batchTextField;
    @FXML
    private TextField nrBatchTextField;
    private NewInvoiceState invoiceState;

    public BatchView(RepositoryInterface repository, TextField batchTextField, TextField nrBatchTextField, NewInvoiceState invoiceState) throws IOException {
        this.repository = repository;
        this.batchTextField = batchTextField;
        this.nrBatchTextField = nrBatchTextField;
        this.invoiceState = invoiceState;

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("batchview.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }

    @FXML
    private void initialize() {
        batch = repository.getAllBatches().isEmpty() ? null : repository.getAllBatches().get(0);
        if (batch == null) {
            batch = new Batch();
            batch.setLowerBoundary(1);
            batch.setUpperBoundary(50);
            batch.setCurrentBatchNumber(1);
            batch.setBatchSize(50);

            repository.addNewBatch(batch);
            batch = repository.getAllBatches().get(0);
        }

        lowerBoundaryTextField.setText(Integer.toString(batch.getLowerBoundary()));
        upperBoundaryTextField.setText(Integer.toString(batch.getUpperBoundary()));
        currentNrTextField.setText(Integer.toString(batch.getCurrentBatchNumber()));
        batchSizeTextField.setText(Integer.toString(batch.getBatchSize()));
    }

    @FXML
    private void saveChanges() {
        try {
            int lower = Integer.parseInt(lowerBoundaryTextField.getText());
            int upper = Integer.parseInt(upperBoundaryTextField.getText());
            int size = Integer.parseInt(batchSizeTextField.getText());
            int current = Integer.parseInt(currentNrTextField.getText());
            if ( lower < 0 || upper < 0 || current < 0 || size < 0) {
                throw new Exception();
            }

            batch.setLowerBoundary(lower);
            batch.setUpperBoundary(upper);
            batch.setCurrentBatchNumber(current);
            batch.setBatchSize(size);

            repository.updateBatch(batch);
            batchTextField.setText(repository.getAllBatches().get(0).getLowerBoundary() + " - " + repository.getAllBatches().get(0).getUpperBoundary());
            nrBatchTextField.setText(repository.getAllBatches().get(0).getCurrentBatchNumber() + "");

            invoiceState.setBatch(repository.getAllBatches().get(0));

            ((Stage)this.getScene().getWindow()).close();
        }catch (Exception e) {
            Alerts.showExceptionAlert("Introdu date corecte", e);
        }
    }

}
