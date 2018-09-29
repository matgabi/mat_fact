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
import model.export.ExcelExporter;
import model.export.ProductExcelReportExporter;
import model.interfaces.ProductInterface;
import model.reports.ProductReportContent;
import model.reports.ProductReportGenerator;
import repository.interfaces.RepositoryInterface;
import view.alerts.Alerts;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class ProductReportView extends BorderPane {

    private RepositoryInterface repository;

    private ObservableList<SearchableComboBox.HideableItem<ProductInterface>> filteredProductItems;
    @FXML
    private ComboBox<SearchableComboBox.HideableItem<ProductInterface>> productComboBox;
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;
    @FXML
    private ComboBox<String> groupComboBox;
    @FXML
    private TextArea textArea;

    public ProductReportView(RepositoryInterface repository) throws IOException {
        this.repository = repository;

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("productreport.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }

    @FXML
    private void initialize(){
        filteredProductItems = SearchableComboBox.createComboBoxWithAutoCompletionSupport(repository.getAllProducts(), productComboBox);
        groupComboBox.setItems(FXCollections.observableArrayList("client", "data"));
        LocalDate start = LocalDate.of(LocalDate.now().getYear(), 1, 1);
        LocalDate end = LocalDate.of(LocalDate.now().getYear(), 12, 31);
        startDate.setValue(start);
        endDate.setValue(end);

        groupComboBox.getSelectionModel().select(1);
    }

    @FXML
    private void generateExcel()
    {
        ProductReportGenerator reportGenerator = new ProductReportGenerator();

        ProductInterface product = null;
        SearchableComboBox.HideableItem<ProductInterface> productItem = productComboBox.getSelectionModel().getSelectedItem();
        if (null != productItem)
        {
            product = productItem.getObject();
        }
        if(product == null)
        {
            Alerts.showWarningAlert("Alege un produs");
            return;
        }

        String group = groupComboBox.getSelectionModel().getSelectedItem().equals("client") ? ProductReportGenerator.GROUP_BY_CLIENT : ProductReportGenerator.GROUP_BY_DATE;

        ArrayList<ProductReportContent> contents = null;

        try {
            contents = reportGenerator.generateProductReport(
                    repository.getAllInvoices(),
                    product,
                    startDate.getValue(),
                    endDate.getValue(),
                    group
            );

            String start = getFormattedDate(startDate.getValue());
            String end = getFormattedDate(endDate.getValue());

            String period = start + " - " + end;

            ExcelExporter exporter = new ProductExcelReportExporter(product.getProductName(), group, period);
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
        ProductReportGenerator reportGenerator = new ProductReportGenerator();

        ProductInterface product = null;
        SearchableComboBox.HideableItem<ProductInterface> productItem = productComboBox.getSelectionModel().getSelectedItem();
        if (null != productItem)
        {
             product = productItem.getObject();
        }
        if(product == null)
        {
            Alerts.showWarningAlert("Alege un produs");
            return;
        }

        String group = groupComboBox.getSelectionModel().getSelectedItem().equals("client") ? ProductReportGenerator.GROUP_BY_CLIENT : ProductReportGenerator.GROUP_BY_DATE;

        ArrayList<ProductReportContent> contents = null;

        try{
            contents = reportGenerator.generateProductReport(
                    repository.getAllInvoices(),
                    product,
                    startDate.getValue(),
                    endDate.getValue(),
                    group
            );

            createHeader(product.getProductName());
            Map sums = null;

            if(group.equals(ProductReportGenerator.GROUP_BY_CLIENT))
            {
                sums = sumOverClients(contents);
            }
            else
            {
                sums = sumOverDate(contents);
            }
            Set<Map.Entry<String, Double>> entries = sums.entrySet();

            printContent(contents);

            printSums(entries, product.getUm());

        }catch (Exception e){
            Alerts.showExceptionAlert("Eroare la generarea raportului", e);
            return;
        }
    }

    private void printSums(Set<Map.Entry<String, Double>> entries, String um) {
        textArea.appendText("\n\nInsumare\n\n");
        for(Map.Entry<String, Double> entry_ : entries)
        {
            textArea.appendText(entry_.getKey() + " ---- " + entry_.getValue() + " " + um + "\n");
        }
    }

    private void printContent(ArrayList<ProductReportContent> contents) {
        for(int i = 0 ; i < contents.size() ; i++)
        {
            ProductReportContent p = contents.get(i);
            textArea.appendText(p.toString());
        }
    }

    private Map sumOverDate(ArrayList<ProductReportContent> contents) {
        Map<String, Double> sums = new TreeMap<String, Double>();
        for(ProductReportContent p : contents) {
            String key = p.getDate().getMonthValue() + "-" + p.getDate().getYear();
            if (!sums.containsKey(key)) {
                sums.put(key, p.getQuantity());
            } else {
                double value = sums.get(key);
                value += p.getQuantity();
                sums.put(key, value);
            }
        }
        return sums;
    }

    private Map<String,Double> sumOverClients(ArrayList<ProductReportContent> contents) {
        Map<String, Double> sums = new TreeMap<String, Double>();
        for(ProductReportContent p : contents) {
            if (!sums.containsKey(p.getClientName())) {
                sums.put(p.getClientName(), p.getQuantity());
            } else {
                double value = sums.get(p.getClientName());
                value += p.getQuantity();
                sums.put(p.getClientName(), value);
            }
        }
        return sums;
    }

    private void createHeader(String product) {
        textArea.appendText("Raport produs: " + product + "\n");
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
