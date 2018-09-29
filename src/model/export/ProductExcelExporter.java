package model.export;

import javafx.collections.ObservableList;
import jxl.Workbook;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.*;
import jxl.write.Label;
import jxl.write.Number;
import model.interfaces.ProductInterface;
import view.alerts.Alerts;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProductExcelExporter implements ExcelExporter{

    LocalDate date;
    String path;
    WritableWorkbook workbook;
    WritableSheet sheet;
    File excel;

    public ProductExcelExporter() throws IOException {
        date = LocalDate.now();
        path = "c:/temp/facturier/excel/Produse" + getFormattedDate() + ".xls";
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
        sheet = workbook.createSheet("Mati Consult " + getFormattedDate(), 0);
    }

    public void createExcel(List products) throws WriteException, IOException {

        ArrayList<ProductInterface> _products = new ArrayList<>(products);
        createTitle();
        createHeader();

        createContent(_products);
        workbook.write();
        workbook.close();

        Desktop.getDesktop().open(excel);
    }

    private void createContent(ArrayList<ProductInterface> products) throws WriteException {


        int row = 4;
        int counter = 1;

        WritableCellFormat cellFormat = new WritableCellFormat();
        cellFormat.setWrap(true);
        cellFormat.setBorder(jxl.format.Border.ALL, BorderLineStyle.THIN);
        cellFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);

        WritableCellFormat umCellFormat = new WritableCellFormat();
        umCellFormat.setWrap(true);
        umCellFormat.setBorder(jxl.format.Border.ALL, BorderLineStyle.THIN);
        umCellFormat.setAlignment(jxl.format.Alignment.CENTRE);
        umCellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);

        Number nrLabel;
        Label denumireLabel;
        Label umLabel;
        Number priceNumber;

        for(ProductInterface p : products){
            nrLabel = new Number(0, row, counter, cellFormat);
            denumireLabel = new Label(1, row, p.getProductName(), cellFormat);
            umLabel = new Label(2, row, p.getUm(), umCellFormat);
            priceNumber = new Number(3, row, p.getPrice().doubleValue(), cellFormat);

            sheet.addCell(nrLabel);
            sheet.addCell(denumireLabel);
            sheet.addCell(umLabel);
            sheet.addCell(priceNumber);

            row += 1;
            counter += 1;
        }

    }

    private void createHeader() throws WriteException {
        WritableCellFormat headerFormat = new WritableCellFormat();
        WritableFont headerFont
                = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD);
        headerFormat.setFont(headerFont);
        headerFormat.setWrap(true);
        headerFormat.setBorder(jxl.format.Border.ALL, BorderLineStyle.THIN);

        WritableCellFormat umCellFormat = new WritableCellFormat();
        WritableFont cellFont
                = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD);
        umCellFormat.setFont(cellFont);
        umCellFormat.setWrap(true);
        umCellFormat.setBorder(jxl.format.Border.ALL, BorderLineStyle.THIN);
        umCellFormat.setAlignment(jxl.format.Alignment.CENTRE);
        umCellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);

        Label headerLabel = new Label(0, 3, "Nr.", headerFormat);
        sheet.setColumnView(0, 5);
        sheet.addCell(headerLabel);

        headerLabel = new Label(1, 3, "Denumire produs", headerFormat);
        sheet.setColumnView(1, 50);
        sheet.addCell(headerLabel);

        headerLabel = new Label(2, 3, "UM", umCellFormat);
        sheet.addCell(headerLabel);

        headerLabel = new Label(3,3,"Pret", umCellFormat);
        sheet.setColumnView(3,20);
        sheet.addCell(headerLabel);
    }

    private void createTitle() throws WriteException {
        sheet.mergeCells(0,0,3,0);
        sheet.mergeCells(0,1,3,1);
        sheet.mergeCells(0,2,3,2);

        WritableCellFormat titleFormat = new WritableCellFormat();
        WritableFont titleFont
                = new WritableFont(WritableFont.ARIAL, 16, WritableFont.BOLD);
        titleFormat.setFont(titleFont);
        titleFormat.setWrap(false);
        titleFormat.setAlignment(jxl.format.Alignment.CENTRE);

        Label titleLabel = new Label(0, 0, "Lista produse", titleFormat);
        sheet.addCell(titleLabel);

        WritableCellFormat dateFormat = new WritableCellFormat();
        WritableFont dateFont
                = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
        dateFormat.setFont(dateFont);
        dateFormat.setWrap(false);
        dateFormat.setAlignment(jxl.format.Alignment.CENTRE);

        Label dateCell = new Label(0,1,getFormattedDate(), dateFormat);
        sheet.addCell(dateCell);
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
}
