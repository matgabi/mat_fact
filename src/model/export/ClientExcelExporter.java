package model.export;

import javafx.collections.ObservableList;
import jxl.Workbook;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.*;
import model.interfaces.ClientInterface;
import view.alerts.Alerts;

import java.awt.*;
import java.awt.Label;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClientExcelExporter implements ExcelExporter {


    LocalDate date;
    String path;
    WritableWorkbook workbook;
    WritableSheet sheet;
    File excel;

    public ClientExcelExporter() throws IOException {
        date = LocalDate.now();
        path = "c:/temp/facturier/excel/Clienti " + getFormattedDate() + ".xls";
        File file = new File("c:/temp/facturier/excel");
        if (!file.exists()) {
            if (file.mkdir()) {
                Alerts.showInformationAlert("S-a creat directorul c:/temp/facturier/excel/Clienti!");
            } else {
                Alerts.showWarningAlert("Nu s-a putut crea directorul c:/temp/facturier/excel/Clienti! Creeaza-l manual!");
            }
        }
        excel = new File(path.replace(" ", "_"));
        workbook = Workbook.createWorkbook(excel);
        sheet = workbook.createSheet("Mati Consult " + getFormattedDate(), 0);
    }

    public void createExcel(List clients) throws WriteException, IOException {
        ArrayList<ClientInterface> _clients = new ArrayList<>(clients);

        createTitle();
        createHeader();

        createContent(_clients);
        workbook.write();
        workbook.close();

        Desktop.getDesktop().open(excel);
    }

    private void createContent(ArrayList<ClientInterface> clients) throws WriteException {
        int row = 4;
        int counter = 1;

        WritableCellFormat cellFormat = new WritableCellFormat();
        cellFormat.setWrap(true);
        cellFormat.setBorder(jxl.format.Border.ALL, BorderLineStyle.THIN);
        cellFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);

        jxl.write.Number nrLabel;
        jxl.write.Label clientLabel;
        jxl.write.Label cuiLabel;
        jxl.write.Label nrRegLabel;
        jxl.write.Label sediuLabel;
        jxl.write.Label judetLabel;
        jxl.write.Label contLabel;
        jxl.write.Label bancaLabel;


        for(ClientInterface c : clients){
            nrLabel = new jxl.write.Number(0, row, counter, cellFormat);
            clientLabel = new jxl.write.Label(1, row, c.getName(), cellFormat);
            cuiLabel = new jxl.write.Label(2, row, c.getCui(), cellFormat);
            nrRegLabel = new jxl.write.Label(3, row, c.getNr(), cellFormat);
            sediuLabel= new jxl.write.Label(4, row, c.getHeadquarters(), cellFormat);
            judetLabel = new jxl.write.Label(5, row, c.getCounty(), cellFormat);
            contLabel = new jxl.write.Label(6, row, c.getAccount(), cellFormat);
            bancaLabel = new jxl.write.Label(7, row, c.getBank(), cellFormat);

            sheet.addCell(nrLabel);
            sheet.addCell(clientLabel);
            sheet.addCell(cuiLabel);
            sheet.addCell(nrRegLabel);
            sheet.addCell(sediuLabel);
            sheet.addCell(judetLabel);
            sheet.addCell(contLabel);
            sheet.addCell(bancaLabel);

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

        jxl.write.Label headerLabel = new jxl.write.Label(0, 3, "Nr.", headerFormat);
        sheet.setColumnView(0, 5);
        sheet.addCell(headerLabel);

        headerLabel = new jxl.write.Label(1, 3, "Client", headerFormat);
        sheet.setColumnView(1, 35);
        sheet.addCell(headerLabel);

        headerLabel = new jxl.write.Label(2, 3, "C.U.I/C.I.F", headerFormat);
        sheet.setColumnView(2,20);
        sheet.addCell(headerLabel);

        headerLabel = new jxl.write.Label(3,3,"Nr. ordine Reg. Com./An", headerFormat);
        sheet.setColumnView(3,20);
        sheet.addCell(headerLabel);

        headerLabel = new jxl.write.Label(4,3,"Sediul", headerFormat);
        sheet.setColumnView(4,25);
        sheet.addCell(headerLabel);

        headerLabel = new jxl.write.Label(5,3,"Judetul", headerFormat);
        sheet.setColumnView(5,15);
        sheet.addCell(headerLabel);

        headerLabel = new jxl.write.Label(6,3,"Contul", headerFormat);
        sheet.setColumnView(6,25);
        sheet.addCell(headerLabel);

        headerLabel = new jxl.write.Label(7,3,"Banca", headerFormat);
        sheet.setColumnView(7,25);
        sheet.addCell(headerLabel);
    }

    private void createTitle() throws WriteException {
        sheet.mergeCells(0,0,7,0);
        sheet.mergeCells(0,1,7,1);
        sheet.mergeCells(0,2,7,2);

        WritableCellFormat titleFormat = new WritableCellFormat();
        WritableFont titleFont
                = new WritableFont(WritableFont.ARIAL, 16, WritableFont.BOLD);
        titleFormat.setFont(titleFont);
        titleFormat.setWrap(false);
        titleFormat.setAlignment(jxl.format.Alignment.CENTRE);

        jxl.write.Label titleLabel = new jxl.write.Label(0, 0, "Lista clienti", titleFormat);
        sheet.addCell(titleLabel);

        WritableCellFormat dateFormat = new WritableCellFormat();
        WritableFont dateFont
                = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
        dateFormat.setFont(dateFont);
        dateFormat.setWrap(false);
        dateFormat.setAlignment(jxl.format.Alignment.CENTRE);

        jxl.write.Label dateCell = new jxl.write.Label(0,1,getFormattedDate(), dateFormat);
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
