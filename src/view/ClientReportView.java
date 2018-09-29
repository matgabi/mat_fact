package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import model.combo.SearchableComboBox;
import model.export.ClientExcelReportExporter;
import model.export.ExcelExporter;
import model.interfaces.ClientInterface;
import model.reports.ClientReportContent;
import model.reports.ClientReportGenerator;
import repository.interfaces.RepositoryInterface;
import view.alerts.Alerts;


import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ClientReportView extends BorderPane{

    private RepositoryInterface repository;

    private ObservableList<SearchableComboBox.HideableItem<ClientInterface>> filteredClientsItems;
    @FXML
    private ComboBox<SearchableComboBox.HideableItem<ClientInterface>> clientsComboBox;
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;
    @FXML
    private ComboBox<String> groupComboBox;
    @FXML
    private TextArea textArea;

    public ClientReportView(RepositoryInterface repository) throws IOException {
        this.repository = repository;

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("clientreport.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }

    @FXML
    private void initialize(){
        filteredClientsItems = SearchableComboBox.createComboBoxWithAutoCompletionSupport(repository.getAllClients(), clientsComboBox);
        groupComboBox.setItems(FXCollections.observableArrayList("produs", "data", "factura"));
        LocalDate start = LocalDate.of(LocalDate.now().getYear(), 1, 1);
        LocalDate end = LocalDate.of(LocalDate.now().getYear(), 12, 31);
        startDate.setValue(start);
        endDate.setValue(end);

        groupComboBox.getSelectionModel().select(0);
    }

    @FXML
    private void generateExcel()
    {
        ClientReportGenerator reportGenerator = new ClientReportGenerator();

        ClientInterface client = null;
        SearchableComboBox.HideableItem<ClientInterface> clientItem = clientsComboBox.getSelectionModel().getSelectedItem();
        if (null != clientItem)
        {
            client = clientItem.getObject();
        }
        if(client == null)
        {
            Alerts.showWarningAlert("Alege un produs");
            return;
        }

        String group = groupComboBox.getSelectionModel().getSelectedItem();

        ArrayList<ClientReportContent> contents = null;

        try {
            contents = reportGenerator.generateClientReport(
                    repository.getAllInvoices(),
                    client,
                    startDate.getValue(),
                    endDate.getValue(),
                    group
            );

            String start = getFormattedDate(startDate.getValue());
            String end = getFormattedDate(endDate.getValue());

            String period = start + " - " + end;

            ExcelExporter exporter = new ClientExcelReportExporter(client.getName(), group, period);
            exporter.createExcel(contents);
        }
        catch (Exception e)
        {
            Alerts.showExceptionAlert("Eroare la generarea excelului", e);
            return;
        }
    }

    @FXML
    private void generateReport(){
        textArea.clear();
        textArea.clear();
        ClientReportGenerator reportGenerator = new ClientReportGenerator();

        ClientInterface client = null;
        SearchableComboBox.HideableItem<ClientInterface> clientItem = clientsComboBox.getSelectionModel().getSelectedItem();
        if (null != clientItem)
        {
            client = clientItem.getObject();
        }
        if(client == null)
        {
           Alerts.showWarningAlert("Alege un produs");
            return;
        }

        String group = groupComboBox.getSelectionModel().getSelectedItem();

        ArrayList<ClientReportContent> contents = null;

        try{
            contents = reportGenerator.generateClientReport(
                    repository.getAllInvoices(),
                    client,
                    startDate.getValue(),
                    endDate.getValue(),
                    group
            );

            createHeader(client.getName());
            Map sums = null;


            sums = sumOverProducts(contents);

            Set<Map.Entry<String, Double>> entries = sums.entrySet();

            printContent(contents);
            printSums(entries);

        }catch (Exception e){
            Alerts.showExceptionAlert("Eroare la generarea raportului", e);
            return;
        }
    }

    private void printSums(Set<Map.Entry<String, Double>> entries) {
        textArea.appendText("\n\nInsumare\n\n");
        for(Map.Entry<String, Double> entry_ : entries)
        {
            String[] name_um = entry_.getKey().split("%");
            textArea.appendText(name_um[0] + " ---- " + entry_.getValue() + name_um[1] + "\n");
        }
    }

    private void printContent(ArrayList<ClientReportContent> contents) {
        for(int i = 0 ; i < contents.size() ; i++)
        {
            ClientReportContent p = contents.get(i);
            textArea.appendText(p.toString());
        }
    }


    private Map<String,Double> sumOverProducts(ArrayList<ClientReportContent> contents) {
        Map<String, Double> sums = new TreeMap<String, Double>();
        for(ClientReportContent c : contents) {
            if (!sums.containsKey(c.getProductName() + "%" + c.getUm())) {
                sums.put(c.getProductName() + "%" + c.getUm(), c.getQuantity());
            } else {
                double value = sums.get(c.getProductName() + "%" + c.getUm());
                value += c.getQuantity();
                sums.put(c.getProductName() + "%" + c.getUm(), value);
            }
        }
        return sums;
    }

    private void createHeader(String client) {
        textArea.appendText("Raport client: " + client + "\n");
        textArea.appendText("Generat la data de: " + getFormattedDate(LocalDate.now()) + "\n");
        textArea.appendText("Perioada: " + getFormattedDate(startDate.getValue()) + " - " + getFormattedDate(endDate.getValue()));
        textArea.appendText("\n\n\n");

    }

    private String getFormattedDate(LocalDate date) {
        String _date = "";

        String day = Integer.toString(date.getDayOfMonth());
        String month = Integer.toString(date.getMonthValue());
        String year = Integer.toString(date.getYear());

        _date += day.length() == 1 ? "0" + day : day;
        _date += ".";
        _date += month.length() == 1 ? "0" + month : month;
        _date += ".";
        _date += year;

        return _date;
    }
}
