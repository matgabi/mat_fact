package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.InvoiceInfo;
import model.NewInvoiceState;
import model.export.ExcelExporter;
import model.interfaces.*;
import repository.interfaces.RepositoryInterface;
import view.alerts.Alerts;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateInvoiceView extends BorderPane {

    //----------------------non fxml members
    private NewInvoiceState invoiceState;
    private RepositoryInterface repository;
    private InvoiceView invoiceView;

    //---------------------------fxml members
    @FXML
    private ComboBox emitorComboBox;
    @FXML
    private TextField seriaTextField;
    @FXML
    private TextField nrTextField;
    @FXML
    private TextField emitorTextField;

    @FXML
    private ComboBox carComboBox;
    @FXML
    private ToggleGroup formatFactura;
    @FXML
    private TextField dataTextField;
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
    @FXML
    private Label scadenteAddUpLabel;


    public CreateInvoiceView(NewInvoiceState invoiceState, RepositoryInterface repository, InvoiceView invoiceView) throws IOException {
        this.invoiceState = invoiceState;
        this.repository = repository;
        this.invoiceView = invoiceView;

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("createinvoiceview.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();

    }

    @FXML
    private void initialize(){
        emitorComboBox.setItems(repository.getAllEmitors());
        emitorComboBox.setConverter(new StringConverter() {
            @Override
            public String toString(Object object) {
                return ((EmitorInterface)object).getFirstName() + " " + ((EmitorInterface)object).getLastName();
            }

            @Override
            public Object fromString(String string) {
                return null;
            }
        });

        carComboBox.setItems(repository.getAllCars());
        carComboBox.setConverter(new StringConverter() {
            @Override
            public String toString(Object object) {
                return ((CarInterface)object).getDescription();
            }

            @Override
            public Object fromString(String string) {
                return null;
            }
        });
        carComboBox.getSelectionModel().selectFirst();

        LocalDate date = invoiceState.getDate();
        String stringDate = date.getDayOfMonth() + "." + (date.getMonthValue() < 10 ? "0" + date.getMonthValue() : date.getMonthValue()) + "." + date.getYear();

        dataTextField.setText(stringDate);
        invoiceNrTextField.setText(Integer.toString(invoiceState.getBatch().getCurrentBatchNumber()));
    }

    @FXML
    private void onEmitorComboBoxAction(){
        EmitorInterface emitor = (EmitorInterface)emitorComboBox.getSelectionModel().getSelectedItem();
        seriaTextField.setText(emitor.getSeria());
        nrTextField.setText(emitor.getNr());
        emitorTextField.setText(emitor.getCiEmitor());
    }

    @FXML
    private void createInvoice(){

        EmitorInterface emitor = (EmitorInterface)emitorComboBox.getSelectionModel().getSelectedItem();
        if(emitor == null){
            Alerts.showWarningAlert("Selecteaza un emitor pt factura curenta!");
            return;
        }

        CarInterface car = (CarInterface)carComboBox.getSelectionModel().getSelectedItem();
        if(car == null){
            Alerts.showWarningAlert("Selecteaza un mijloc de transport!");
            return;
        }

        String seria = invoiceSeriaTextField.getText();
        if(seria == null || seria.equals("")){
            Alerts.showWarningAlert( "Introdu seria facturii!");
            return;
        }

        String nr = invoiceNrTextField.getText();
        if(nr == null || nr.equals("")){
            Alerts.showWarningAlert( "Introdu nr facturii!");
            return;
        }

        String nrAviz = nrAvizTextField.getText();
        if(nrAviz == null || nrAviz.equals("")){
            Alerts.showWarningAlert( "Introdu nr avize!");
            return;
        }

        String hour = hourTextField.getText();
        if(hour == null || hour.equals("")){
            Alerts.showWarningAlert( "Introdu ora!");
            return;
        }

        InvoiceInfoInterface info = new InvoiceInfo();
        info.setNr(nr);
        info.setNrAvize(nrAviz);
        info.setOra(hour);

        String scadenta = scadentaTextArea.getText();
        String a = scadenta.replaceAll("\\{[\\s]*", "");
        System.out.println(a);
        String b = a.replaceAll("[\\s]*\\}", "");
        System.out.println(b);

        info.setScadenta(b);
        info.setSeria(seria);

        String format = formatFactura.getSelectedToggle().toString();
        String formatInvoice = "a4";
        if(format.contains("a4")){
            formatInvoice = "a4";
        }
        else{
            formatInvoice = "a5";
        }

        emitor.setSeria(seriaTextField.getText());
        emitor.setNr(nrTextField.getText());
        emitor.setCiEmitor(emitorTextField.getText());

        invoiceState.setEmitor(emitor);
        invoiceState.setCar(car);
        invoiceState.setInvoiceFormat(formatInvoice);
        invoiceState.setInfo(info);

        BatchInterface batch = invoiceState.getBatch();
        if(batch.getCurrentBatchNumber() == batch.getUpperBoundary())
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informatii");
            alert.setHeaderText(null);
            alert.setContentText("S-a trecut la un alt toc de facturi");

            alert.showAndWait();
        }
        invoiceState.generateInvoice();
        invoiceView.refreshInvoiceTable();
        invoiceView.refreshBatchInfo();
        ((Stage)this.getScene().getWindow()).close();

    }

    @FXML
    private void onTextAreaKeyPressed()
    {
        String scadenta = scadentaTextArea.getText();

        Pattern pricePattern = Pattern.compile("\\{([^\\}]+)\\}");
        Matcher matcher = pricePattern.matcher(scadenta);


        ArrayList<String> scadenteStrings = new ArrayList<>();
        while(matcher.find())
        {
            scadenteStrings.add(matcher.group(1));
        }

        ArrayList<String> trimmedScadenteStrings = new ArrayList<>();
        scadenteStrings.forEach(e -> {
            trimmedScadenteStrings.add(e.trim());
        });

        ArrayList<BigDecimal> bigDecimals = new ArrayList<>();

        trimmedScadenteStrings.forEach(e -> {
            BigDecimal bd = null;
            try{
                bd = new BigDecimal(e);
                bigDecimals.add(bd);
            }catch (Exception ex)
            {
            }

        });
        BigDecimal total = invoiceState.getTotal();
        BigDecimal sum = BigDecimal.ZERO;
        for(BigDecimal bd : bigDecimals)
        {
            sum = sum.add(bd);
        }
        if(sum.equals(total.setScale(2, RoundingMode.HALF_UP)))
        {
            scadenteAddUpLabel.setTextFill(Color.GREEN);
        }
        else
        {
            scadenteAddUpLabel.setTextFill(Color.RED);
        }

        scadenteAddUpLabel.setText("Total scadente: " + sum +"     " + "total factura: " + total.setScale(2, RoundingMode.HALF_UP));
    }

}
