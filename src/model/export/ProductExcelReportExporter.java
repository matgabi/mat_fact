package model.export;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.*;
import jxl.write.Label;
import jxl.write.Number;
import model.reports.ProductReportContent;
import model.reports.ProductReportGenerator;
import view.alerts.Alerts;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

public class ProductExcelReportExporter implements ExcelExporter {

    private LocalDate date;
    private String path;
    private WritableWorkbook workbook;
    private WritableSheet sheet;
    private WritableSheet sheet2;
    private File excel;
    private String group;
    private String productName;
    private String period;

    public ProductExcelReportExporter(String productName, String group, String period) throws IOException {
        this.group = group;
        this.productName = productName;
        this.period = period;
        date = LocalDate.now();
        path = "c:/temp/facturier/excel/Raport_" + productName.replaceAll(" ", "_") + "_" + getFormattedDate().replaceAll(" ", "_") + ".xls";
        File file = new File("c:/temp/facturier/excel");
        if (!file.exists()) {
            if (file.mkdir()) {
                Alerts.showInformationAlert("S-a creat directorul c:/temp/facturier/excel!");
            } else {
                Alerts.showWarningAlert("Nu s-a putut crea directorul c:/temp/facturier/excel! Creeaza-l manual!");
            }
        }
        excel = new File(path.replace(" ", "_"));
        workbook = Workbook.createWorkbook(excel);
        sheet = workbook.createSheet("Raport lista" , 0);
        sheet2 = workbook.createSheet("Raport insumat", 0);
    }

    @Override
    public void createExcel(List content) throws WriteException, IOException {
        ArrayList<ProductReportContent> contents = new ArrayList<>(content);
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

        createListTitle(productName);
        createListHeader();
        createListContent(contents);

        createSumTitle(productName);
        createSumHeader(group);
        createSumContent(entries, contents.get(0) != null ? contents.get(0).getUm() : "");

        workbook.write();
        workbook.close();

        Desktop.getDesktop().open(excel);
    }

    private void createSumContent(Set<Map.Entry<String, Double>> entries, String um) throws WriteException {
        int row = 6;
        int counter = 1;

        WritableCellFormat cellFormat = new WritableCellFormat();
        cellFormat.setWrap(true);
        cellFormat.setBorder(jxl.format.Border.ALL, BorderLineStyle.THIN);
        cellFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);

        Number nr;
        Label group;
        Number quantity;
        Label umLabel;


        for(Map.Entry<String, Double> entry_ : entries)
        {
            nr = new Number(0, row, counter, cellFormat);
            group = new Label(1, row, entry_.getKey(), cellFormat);
            quantity = new Number(2, row, entry_.getValue(), cellFormat);
            umLabel = new Label(3, row, um, cellFormat);

            sheet2.addCell(nr);
            sheet2.addCell(group);
            sheet2.addCell(quantity);
            sheet2.addCell(umLabel);

            row += 1;
            counter += 1;
        }
    }

    private void createSumHeader(String group) throws WriteException {
        WritableCellFormat headerFormat = new WritableCellFormat();
        WritableFont headerFont
                = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
        headerFormat.setFont(headerFont);
        headerFormat.setWrap(true);
        headerFormat.setBorder(jxl.format.Border.ALL, BorderLineStyle.THIN);

        Label headerLabel = new Label(0, 5, "Nr.", headerFormat);
        sheet2.setColumnView(0, 5);
        sheet2.addCell(headerLabel);

        headerLabel = new Label(1, 5, group.equals(ProductReportGenerator.GROUP_BY_CLIENT) ? "Client" : "Luna" , headerFormat);
        sheet2.setColumnView(1, 50);
        sheet2.addCell(headerLabel);

        headerLabel = new Label(2, 5, "Cantitatea", headerFormat);
        sheet2.setColumnView(2, 15);
        sheet2.addCell(headerLabel);

        headerLabel = new Label(3, 5, "UM", headerFormat);
        sheet2.addCell(headerLabel);

    }

    private void createSumTitle(String productName) throws WriteException {
        sheet2.mergeCells(0,0,3, 0);
        sheet2.mergeCells(0,1,3, 1);
        sheet2.mergeCells(0,2,3, 2);
        sheet2.mergeCells(0,3,3, 3);
        sheet2.mergeCells(0,4,3, 4);

        WritableCellFormat titleFormat = new WritableCellFormat();
        WritableFont titleFont
                = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
        titleFormat.setFont(titleFont);
        titleFormat.setWrap(false);
        titleFormat.setAlignment(Alignment.LEFT);

        Label title = new Label(0, 0,"Raport: " + productName, titleFormat);
        Label date = new Label(0, 1, "Generat la data de " + getFormattedDate(), titleFormat);
        Label period = new Label(0, 2, "Perioada: " + this.period, titleFormat);

        sheet2.addCell(title);
        sheet2.addCell(date);
        sheet2.addCell(period);
    }

    private void createListContent(ArrayList<ProductReportContent> contents) throws WriteException {
        int row = 6;
        int counter = 1;

        WritableCellFormat cellFormat = new WritableCellFormat();
        cellFormat.setWrap(true);
        cellFormat.setBorder(jxl.format.Border.ALL, BorderLineStyle.THIN);
        cellFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);

        Number nr;
        Label client;
        Number quantity;
        Label um;
        Label invoice;
        Label date;

        for(ProductReportContent p : contents)
        {
            nr = new Number(0,row, counter, cellFormat);
            client = new Label(1, row, p.getClientName(), cellFormat);
            quantity = new Number(2, row, p.getQuantity(), cellFormat);
            um = new Label(3, row, p.getUm(), cellFormat);
            invoice = new Label(4, row, p.getInvoiceName(), cellFormat);
            date = new Label(5, row, getFormattedDate_(p.getDate()), cellFormat);

            sheet.addCell(nr);
            sheet.addCell(client);
            sheet.addCell(quantity);
            sheet.addCell(um);
            sheet.addCell(invoice);
            sheet.addCell(date);

            row += 1;
            counter += 1;
        }
    }

    private void createListHeader() throws WriteException {
        WritableCellFormat headerFormat = new WritableCellFormat();
        WritableFont headerFont
                = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
        headerFormat.setFont(headerFont);
        headerFormat.setWrap(true);
        headerFormat.setBorder(jxl.format.Border.ALL, BorderLineStyle.THIN);

        Label headerLabel = new Label(0, 5, "Nr.", headerFormat);
        sheet.setColumnView(0, 5);
        sheet.addCell(headerLabel);

        headerLabel = new Label(1, 5, "Client", headerFormat);
        sheet.setColumnView(1, 50);
        sheet.addCell(headerLabel);

        headerLabel = new Label(2, 5, "Cantitatea", headerFormat);
        sheet.setColumnView(2, 15);
        sheet.addCell(headerLabel);

        headerLabel = new Label(3, 5, "UM", headerFormat);
        sheet.addCell(headerLabel);

        headerLabel = new Label(4, 5, "Factura", headerFormat);
        sheet.setColumnView(4, 20);
        sheet.addCell(headerLabel);

        headerLabel = new Label(5, 5, "Data", headerFormat);
        sheet.setColumnView(5, 15);
        sheet.addCell(headerLabel);
    }

    private void createListTitle(String productName) throws WriteException {
        sheet.mergeCells(0,0,5, 0);
        sheet.mergeCells(0,1,5, 1);
        sheet.mergeCells(0,2,5, 2);
        sheet.mergeCells(0,3,5, 3);
        sheet.mergeCells(0,4,5, 4);

        WritableCellFormat titleFormat = new WritableCellFormat();
        WritableFont titleFont
                = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
        titleFormat.setFont(titleFont);
        titleFormat.setWrap(false);
        titleFormat.setAlignment(Alignment.LEFT);

        Label title = new Label(0, 0,"Raport: " + productName, titleFormat);
        Label date = new Label(0, 1, "Generat la data de " + getFormattedDate(), titleFormat);
        Label period = new Label(0, 2, "Perioada: " + this.period, titleFormat);

        sheet.addCell(title);
        sheet.addCell(date);
        sheet.addCell(period);
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

    private  String getFormattedDate() {
        String _date = "";

        String day = Integer.toString(date.getDayOfMonth());
        String month = "";

        switch (date.getMonthValue())
        {
            case 1: month = "ianuarie"; break;
            case 2: month = "februarie"; break;
            case 3: month = "martie"; break;
            case 4: month = "aprilie"; break;
            case 5: month = "mai"; break;
            case 6: month = "iunie"; break;
            case 7: month = "iulie"; break;
            case 8: month = "august"; break;
            case 9: month = "septembrie"; break;
            case 10: month = "octombrie"; break;
            case 11: month = "noiembrie"; break;
            case 12: month = "decembrie"; break;
        }

        String year = Integer.toString(date.getYear());

        _date += day.length() == 1 ? "0" + day : day;
        _date += " ";
        _date += month;
        _date += " ";
        _date += year;

        return _date;
    }

    private String getFormattedDate_(LocalDate date) {
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
