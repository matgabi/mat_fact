package model;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;

import com.sun.xml.internal.ws.util.StringUtils;
import model.interfaces.*;
import view.alerts.Alerts;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;

class DottedCell implements PdfPCellEvent {
    private int border = 0;
    public DottedCell(int border) {
        this.border = border;
    }
    public void cellLayout(PdfPCell cell, Rectangle position,
                           PdfContentByte[] canvases) {
        PdfContentByte canvas = canvases[PdfPTable.LINECANVAS];
        canvas.saveState();
        canvas.setLineDash(0, 2, 2);
        if ((border & PdfPCell.TOP) == PdfPCell.TOP) {
            canvas.moveTo(position.getRight(), position.getTop());
            canvas.lineTo(position.getLeft(), position.getTop());
        }
        if ((border & PdfPCell.BOTTOM) == PdfPCell.BOTTOM) {
            canvas.moveTo(position.getRight() - 20, position.getBottom());
            canvas.lineTo(position.getLeft(), position.getBottom());
        }
        if ((border & PdfPCell.RIGHT) == PdfPCell.RIGHT) {
            canvas.moveTo(position.getRight(), position.getTop());
            canvas.lineTo(position.getRight(), position.getBottom());
        }
        if ((border & PdfPCell.LEFT) == PdfPCell.LEFT) {
            canvas.moveTo(position.getLeft(), position.getTop());
            canvas.lineTo(position.getLeft(), position.getBottom());
        }
        canvas.stroke();
        canvas.restoreState();
    }
}

public class PdfGenerator {

    private static String TVA= "19";


    private Document document;
    private PdfWriter writer;
    private PdfPTable table;
    private Rectangle rect;
    private String format;

    private float[] columnWidths = { 1f, 3.5f, 1f, 2f, 2f, 2.5f, 2.5f };
    private float[] columnWidths2 = { 1f, 3.5f, 1f, 2f, 2f, 2.5f, 2.5f, 1.5f};

    public PdfGenerator() {
    }

    public File createPdf(String format, InvoiceInterface invoice) {
        this.format = format;
        table = new PdfPTable(7); // 3 columns.
        table.setWidthPercentage(100); // Width 100%
        table.setSpacingBefore(10f); // Space before table
        table.setSpacingAfter(10f); // Space after table
        File file = new File("c:\\temp\\facturier");
        if (!file.exists()) {
            if (file.mkdir()) {
                System.out.println("Directory is created!");
            } else {
                System.out.println("Failed to create directory!");
            }
        }

        if(format == "a4"){
            document = new Document(PageSize.A4,30,30,30,42);
            rect = new Rectangle(30, 42, 565, 818);
        }
        else{
            document = new Document(PageSize.A5.rotate(),10,10,10,10);
            rect = new Rectangle(10, 10, 585, 410);
        }
        writer = null;
        try {
            System.out.println(invoice.getPdfPath().replace("\\","/"));
            writer = PdfWriter.getInstance(document, new FileOutputStream(invoice.getPdfPath()+"\\"+invoice.getName()+".pdf"));
            document.open();

            PdfContentByte canvas = writer.getDirectContent();
            rect.setBorder(Rectangle.BOX);
            rect.setBorderWidth(0.5f);
            canvas.rectangle(rect);

            createInfoData(invoice.getSeller(),invoice.getClient(), invoice.getDate(), invoice.getBatch(), invoice.getInfo());
            createCotaTva(invoice.getTva());
            createFirstRow();
            createTableContent(invoice.getProducts(), invoice.getDiscount());
            document.add(table);
            //createObservatii(format, invoice.getInfo());
            createSignature(invoice.getEmitor(),invoice.getTotalValue(),invoice.getTotalTvaValue(),invoice.getCar(), invoice.getDate(), invoice.getInfo());

            document.newPage();
            canvas.rectangle(rect);
            table = new PdfPTable(7); // 3 columns.
            table.setWidthPercentage(100); // Width 100%
            table.setSpacingBefore(10f); // Space before table
            table.setSpacingAfter(10f); // Space after table

            createInfoData(invoice.getSeller(),invoice.getClient(), invoice.getDate(), invoice.getBatch(), invoice.getInfo());
            createCotaTva(invoice.getTva());
            createFirstRow();
            createTableContent(invoice.getProducts(), invoice.getDiscount());
            document.add(table);
            //createObservatii(format, invoice.getInfo());
            createSignature(invoice.getEmitor(),invoice.getTotalValue(),invoice.getTotalTvaValue(),invoice.getCar(), invoice.getDate(), invoice.getInfo());

            document.newPage();
            canvas.rectangle(rect);
            table = new PdfPTable(7); // 3 columns.
            table.setWidthPercentage(100); // Width 100%
            table.setSpacingBefore(10f); // Space before table
            table.setSpacingAfter(10f); // Space after table

            createInfoData(invoice.getSeller(),invoice.getClient(), invoice.getDate(), invoice.getBatch(), invoice.getInfo());

            createCotaTva(invoice.getTva());
            createFirstRow();
            createTableContent(invoice.getProducts(), invoice.getDiscount());
            document.add(table);
            //createObservatii(format, invoice.getInfo());
            createSignature(invoice.getEmitor(),invoice.getTotalValue(),invoice.getTotalTvaValue(),invoice.getCar(), invoice.getDate(), invoice.getInfo());

            document.newPage();
            canvas.rectangle(rect);
            table = new PdfPTable(8); // 3 columns.
            table.setWidthPercentage(100); // Width 100%
            table.setSpacingBefore(10f); // Space before table
            table.setSpacingAfter(10f); // Space after table

            createInfoData(invoice.getSeller(),invoice.getClient(), invoice.getDate(), invoice.getBatch(), invoice.getInfo());

            createCotaTva(invoice.getTva());
            createFirstRowDiscount();
            createTableContentDiscount(invoice.getProducts(), invoice.getDiscount());
            document.add(table);
            //createObservatii(format, invoice.getInfo());
            createSignature(invoice.getEmitor(),invoice.getTotalValue(),invoice.getTotalTvaValue(),invoice.getCar(), invoice.getDate(), invoice.getInfo());


        }catch (FileNotFoundException e){
            Alerts.showExceptionAlert("E posibil ca pdf-ul sa fie deschis intr-un alt program!!", e);
            return null;
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                document.close();
                writer.close();
            }catch (Exception e){

            }

        }
        File pdfFile = new File(invoice.getPdfPath()+"\\"+invoice.getName()+".pdf");

        try {
            Desktop.getDesktop().open(pdfFile);
        }catch (Exception e){
            e.printStackTrace();
        }
        return pdfFile;
    }

    private void createSignature(EmitorInterface emitor, BigDecimal totalValue, BigDecimal totalTvaValue, CarInterface car, LocalDate date, InvoiceInfoInterface info) throws DocumentException {

        Font fontbold = FontFactory.getFont("defaultEncoding", 10, Font.NORMAL);
        PdfPTable tableInfo = new PdfPTable(5); // 3 columns.
        tableInfo.setWidthPercentage(100); // Width 100%
        tableInfo.setSpacingBefore(10f); // Space before table
        tableInfo.setSpacingAfter(10f); // Space after table
        float[] widths={1.4f,3f,1.3f,1f,1f};
        tableInfo.setWidths(widths);

        PdfPCell furnizorCell = new PdfPCell(new Paragraph("Semnatura si stampila furnizorului",fontbold));
        furnizorCell.setRowspan(8);
        furnizorCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        furnizorCell.setVerticalAlignment(Element.ALIGN_TOP);

        PdfPCell c2r1 = new PdfPCell(new Paragraph("Date privind expeditia:",fontbold));
        c2r1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c2r1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c2r1.setBorder(Rectangle.TOP);

        PdfPCell c2r2 = new PdfPCell(new Paragraph("Numele delegatului: " + emitor.getFirstName() + " " + emitor.getLastName(),fontbold));
        c2r2.setHorizontalAlignment(Element.ALIGN_LEFT);
        c2r2.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c2r2.setBorder(Rectangle.NO_BORDER);

        PdfPCell c2r3 = new PdfPCell(new Paragraph("B.I/C.I : " + "seria " + emitor.getSeria() +", nr. " + emitor.getNr() +  ", ",fontbold));
        c2r3.setHorizontalAlignment(Element.ALIGN_LEFT);
        c2r3.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c2r3.setBorder(Rectangle.NO_BORDER);

        PdfPCell c2r4 = new PdfPCell(new Paragraph( "eliberat(a) " + emitor.getCiEmitor(),fontbold));
        c2r4.setHorizontalAlignment(Element.ALIGN_LEFT);
        c2r4.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c2r4.setBorder(Rectangle.NO_BORDER);

        PdfPCell c2r5 = new PdfPCell(new Paragraph("Mijlocul de transport: " + car,fontbold));
        c2r5.setHorizontalAlignment(Element.ALIGN_LEFT);
        c2r5.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c2r5.setBorder(Rectangle.NO_BORDER);

        PdfPCell c2r6 = new PdfPCell(new Paragraph("Expedierea s-a facut in prezenta noastra la",fontbold));
        c2r6.setHorizontalAlignment(Element.ALIGN_LEFT);
        c2r6.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c2r6.setBorder(Rectangle.NO_BORDER);

        int month = date.getMonthValue();
        String data = date.getDayOfMonth() + "." + (month < 10 ? "0" + month : month) + "." + date.getYear();

        PdfPCell c2r7 = new PdfPCell(new Paragraph("data de: " + data + ", ora: " + info.getOra(),fontbold));
        c2r7.setHorizontalAlignment(Element.ALIGN_LEFT);
        c2r7.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c2r7.setBorder(Rectangle.NO_BORDER);

        PdfPCell c2r8 = new PdfPCell(new Paragraph("Semnaturile:..........................................." ,fontbold));
        c2r8.setHorizontalAlignment(Element.ALIGN_LEFT);
        c2r8.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c2r8.setBorder(Rectangle.BOTTOM);

        PdfPCell c3r1 = new PdfPCell(new Paragraph("Total: ",fontbold));
        c3r1.setHorizontalAlignment(Element.ALIGN_LEFT);
        c3r1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c3r1.setBorder(Rectangle.TOP | Rectangle.LEFT);


        PdfPCell c3r2 = new PdfPCell(new Paragraph("din care accize",fontbold));
        c3r2.setHorizontalAlignment(Element.ALIGN_LEFT);
        c3r2.setVerticalAlignment(Element.ALIGN_MIDDLE);
        c3r2.setBorder(Rectangle.BOTTOM);
        c3r2.setBorder(Rectangle.LEFT);

        PdfPCell c3r3 = new PdfPCell(new Paragraph("Semnatura de primire",fontbold));
        c3r3.setHorizontalAlignment(Element.ALIGN_LEFT);
        c3r3.setVerticalAlignment(Element.ALIGN_TOP);
        c3r3.setRowspan(6);

        PdfPCell c4r1 = new PdfPCell(new Paragraph((totalValue.setScale(2, RoundingMode.HALF_UP).toString()),fontbold));
        c4r1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c4r1.setVerticalAlignment(Element.ALIGN_CENTER);

        PdfPCell c4r2 = new PdfPCell(new Paragraph("0.00",fontbold));
        c4r2.setHorizontalAlignment(Element.ALIGN_CENTER);
        c4r2.setVerticalAlignment(Element.ALIGN_CENTER);

        PdfPCell c4r3 = new PdfPCell();
        c4r3.addElement(new Chunk("\nTotal de plata\n", FontFactory.getFont("defaultEncoding", 10, Font.BOLD)));
        c4r3.addElement(new Chunk(((totalValue.add(totalTvaValue).setScale(2, RoundingMode.HALF_UP).toString())),FontFactory.getFont("defaultEncoding", 12, Font.BOLD)));

        c4r3.setHorizontalAlignment(Element.ALIGN_CENTER);
        c4r3.setVerticalAlignment(Element.ALIGN_CENTER);
        c4r3.setColspan(2);
        c4r3.setRowspan(6);
        c4r3.setPaddingLeft(30);

        PdfPCell c5r1 = new PdfPCell(new Paragraph((totalTvaValue.setScale(2, RoundingMode.HALF_UP).toString()),fontbold));
        c5r1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c5r1.setVerticalAlignment(Element.ALIGN_CENTER);
        c5r1.setPaddingLeft(5);

        PdfPCell c5r2 = new PdfPCell(new Paragraph("x"));
        c5r2.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        c5r2.setPaddingLeft(33);
        c5r2.setVerticalAlignment(Element.ALIGN_MIDDLE);



        tableInfo.addCell(furnizorCell);
        tableInfo.addCell(c2r1);
        tableInfo.addCell(c3r1);
        tableInfo.addCell(c4r1);
        tableInfo.addCell(c5r1);
        tableInfo.addCell(c2r2);
        tableInfo.addCell(c3r2);
        tableInfo.addCell(c4r2);
        tableInfo.addCell(c5r2);
        tableInfo.addCell(c2r3);
        tableInfo.addCell(c3r3);
        tableInfo.addCell(c4r3);
        tableInfo.addCell(c2r4);
        tableInfo.addCell(c2r5);
        tableInfo.addCell(c2r6);
        tableInfo.addCell(c2r7);
        tableInfo.addCell(c2r8);

        writeFooterTable(writer, document, tableInfo, info.getScadenta());
    }

    private void writeFooterTable(PdfWriter writer, Document document, PdfPTable table, String scadenta) {
        final int FIRST_ROW = 0;
        final int LAST_ROW = -1;
        // Table must have absolute width set.
        if (table.getTotalWidth() == 0)
            table.setTotalWidth((document.right() - document.left()) * table.getWidthPercentage() / 100f);
        table.writeSelectedRows(FIRST_ROW, LAST_ROW, document.left(), document.bottom() + table.getTotalHeight(),
                writer.getDirectContent());

        PdfPTable tableInfo = new PdfPTable(1); // 3 columns.
        tableInfo.setWidthPercentage(100); // Width 100%


        Font fontbold = FontFactory.getFont("defaultEncoding", 10, Font.BOLD);

        String findStr = "-";
        int scadenta_rows = scadenta.split(findStr, -1).length - 1;

        if(scadenta_rows != 0)
        {
            Paragraph p = new Paragraph("Scadenta:  ", fontbold);
            if(format == "a4"){
                p.setSpacingBefore(15);
            }
            p.setIndentationLeft(20);

            fontbold = FontFactory.getFont("defaultEncoding", 10, Font.NORMAL);
            Paragraph p2 = new Paragraph(scadenta, fontbold);
            p2.setIndentationLeft(20);

            PdfPCell furnizorCell = new PdfPCell();
            furnizorCell.addElement(p);
            furnizorCell.addElement(p2);
            furnizorCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            furnizorCell.setVerticalAlignment(Element.ALIGN_TOP);
            furnizorCell.setBorder(Rectangle.NO_BORDER);
            tableInfo.addCell(furnizorCell);

            if (tableInfo.getTotalWidth() == 0)
                tableInfo.setTotalWidth((document.right() - document.left()) * tableInfo.getWidthPercentage() / 100f);
            tableInfo.writeSelectedRows(FIRST_ROW, LAST_ROW, document.left(), document.bottom() + 160 + 13 * scadenta_rows,
                    writer.getDirectContent());
        }
    }

    private void createObservatii(String format, InvoiceInfoInterface info) throws DocumentException {
        // TODO Auto-generated method stub
        Font fontbold = FontFactory.getFont("defaultEncoding", 10, Font.BOLD);

        Paragraph p = new Paragraph("Scadenta:  ", fontbold);
        if(format == "a4"){
            p.setSpacingBefore(15);
        }
        p.setIndentationLeft(20);

        fontbold = FontFactory.getFont("defaultEncoding", 10, Font.NORMAL);
        Paragraph p2 = new Paragraph(info.getScadenta(), fontbold);
        p2.setIndentationLeft(20);

        document.add(p);
        document.add(p2);
    }

    private void createCotaTva(TvaInterface tva) throws DocumentException {
        Font fontbold = FontFactory.getFont("defaultEncoding", 10, Font.NORMAL);
        Paragraph p = new Paragraph("Cota TVA: "+ tva.getDescription(),fontbold);
        //p.setSpacingBefore(10f);
        //p.setSpacingAfter(15f);
        p.setIndentationLeft(10f);

        document.add(p);

    }

    private void createInfoData(SellerInterface seller, ClientInterface client, LocalDate date, BatchInterface batch, InvoiceInfoInterface info) throws DocumentException {
        // TODO Auto-generated method stub

        Font fontbold = FontFactory.getFont("defaultEncoding", 10, Font.BOLD);
        Font fontnormal = FontFactory.getFont("defaultEncoding", 10, Font.NORMAL);
        PdfPTable tableInfo = new PdfPTable(3); // 3 columns.
        tableInfo.setWidthPercentage(100); // Width 100%
        tableInfo.setSpacingBefore(10f); // Space before table
        tableInfo.setSpacingAfter(10f); // Space after table
        float[] widths = { 2f, 1.2f, 2f };
        tableInfo.setWidths(widths);


        PdfPCell furnizorCell = new PdfPCell(createInfoPhrase("Furnizor: ",seller.getName()));
        furnizorCell.setPaddingLeft(10);
        furnizorCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        furnizorCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        furnizorCell.setBorder(Rectangle.NO_BORDER);

        PdfPCell cuiCell = new PdfPCell(createInfoPhrase("C.I.F/CUI : " , seller.getCui()));
        cuiCell.setPaddingLeft(10);
        cuiCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cuiCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cuiCell.setBorder(Rectangle.NO_BORDER);

        PdfPCell nrOrdCell = new PdfPCell(createInfoPhrase("Nr. Ord. Reg. Com./An : " , seller.getNr()));
        nrOrdCell.setPaddingLeft(10);
        nrOrdCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        nrOrdCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        nrOrdCell.setBorder(Rectangle.NO_BORDER);

        PdfPCell sediuCell = new PdfPCell(createInfoPhrase("Sediul : " , seller.getHeadquarters()));
        sediuCell.setPaddingLeft(10);
        sediuCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        sediuCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        sediuCell.setBorder(Rectangle.NO_BORDER);

        PdfPCell capitalCell = new PdfPCell(createInfoPhrase("Capital social : " , seller.getCapital()));
        capitalCell.setPaddingLeft(10);
        capitalCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        capitalCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        capitalCell.setBorder(Rectangle.NO_BORDER);

        PdfPCell judetCell = new PdfPCell(createInfoPhrase("Judetul : " , seller.getCounty()));
        judetCell.setPaddingLeft(10);
        judetCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        judetCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        judetCell.setBorder(Rectangle.NO_BORDER);

        PdfPCell contCell = new PdfPCell(createInfoPhrase("Contul : " , seller.getAccount()));
        contCell.setPaddingLeft(10);
        contCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        contCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        contCell.setBorder(Rectangle.NO_BORDER);

        PdfPCell bancaCell = new PdfPCell(createInfoPhrase("Banca : " , seller.getBank()));
        bancaCell.setPaddingLeft(10);
        bancaCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        bancaCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        bancaCell.setBorder(Rectangle.NO_BORDER);

        // cumparator

        PdfPCell furnizorCellC = new PdfPCell(createInfoPhrase("Cumparator : ", client.getName()));
        furnizorCellC.setPaddingLeft(10);
        furnizorCellC.setHorizontalAlignment(Element.ALIGN_LEFT);
        furnizorCellC.setVerticalAlignment(Element.ALIGN_MIDDLE);
        furnizorCellC.setBorder(Rectangle.NO_BORDER);

        PdfPCell cuiCellC = new PdfPCell(createInfoPhrase("C.I.F/CUI : ", client.getCui()));
        cuiCellC.setPaddingLeft(10);
        cuiCellC.setHorizontalAlignment(Element.ALIGN_LEFT);
        cuiCellC.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cuiCellC.setBorder(Rectangle.NO_BORDER);

        PdfPCell nrOrdCellC = new PdfPCell(createInfoPhrase("Nr. Ord. Reg. Com./An : ", client.getNr()));
        nrOrdCellC.setPaddingLeft(10);
        nrOrdCellC.setHorizontalAlignment(Element.ALIGN_LEFT);
        nrOrdCellC.setVerticalAlignment(Element.ALIGN_MIDDLE);
        nrOrdCellC.setBorder(Rectangle.NO_BORDER);

        PdfPCell sediuCellC = new PdfPCell(createInfoPhrase("Sediul : ", client.getHeadquarters()));
        sediuCellC.setPaddingLeft(10);
        sediuCellC.setHorizontalAlignment(Element.ALIGN_LEFT);
        sediuCellC.setVerticalAlignment(Element.ALIGN_MIDDLE);
        sediuCellC.setBorder(Rectangle.NO_BORDER);

        PdfPCell capitalCellC = new PdfPCell(createInfoPhrase("Capital social : ", "200"));
        capitalCellC.setPaddingLeft(10);
        capitalCellC.setHorizontalAlignment(Element.ALIGN_LEFT);
        capitalCellC.setVerticalAlignment(Element.ALIGN_MIDDLE);
        capitalCellC.setBorder(Rectangle.NO_BORDER);

        PdfPCell judetCellC = new PdfPCell(createInfoPhrase("Judetul : ", client.getCounty()));
        judetCellC.setPaddingLeft(10);
        judetCellC.setHorizontalAlignment(Element.ALIGN_LEFT);
        judetCellC.setVerticalAlignment(Element.ALIGN_MIDDLE);
        judetCellC.setBorder(Rectangle.NO_BORDER);

        PdfPCell contCellC = new PdfPCell(createInfoPhrase("Contul : ", client.getAccount()));
        contCellC.setPaddingLeft(10);
        contCellC.setHorizontalAlignment(Element.ALIGN_LEFT);
        contCellC.setVerticalAlignment(Element.ALIGN_MIDDLE);
        contCellC.setBorder(Rectangle.NO_BORDER);

        PdfPCell bancaCellC = new PdfPCell(createInfoPhrase("Banca : ", client.getBank()));
        bancaCellC.setPaddingLeft(10);
        bancaCellC.setHorizontalAlignment(Element.ALIGN_LEFT);
        bancaCellC.setVerticalAlignment(Element.ALIGN_MIDDLE);
        bancaCellC.setBorder(Rectangle.NO_BORDER);

        // titlu
        PdfPCell empty = new PdfPCell(new Paragraph(""));
        empty.setBorder(0);
        Paragraph title = new Paragraph(50);
        title.add(new Chunk("FACTURA FISCALA" + Chunk.NEWLINE,fontbold));

        Paragraph serie = new Paragraph();
        serie.add(new Chunk("Seria: ", fontnormal));
        serie.add(new Chunk(info.getSeria(), fontbold));

        String nr = "000000";
        nr = nr + info.getNr();

        serie.add(new Chunk( ", Nr.: " + nr.substring(nr.length()-6, nr.length()) + Chunk.NEWLINE, fontnormal));
        PdfPCell seriecell = new PdfPCell(serie);
        seriecell.setBorder(0);;

        int month = date.getMonth().getValue();
        Paragraph data = new Paragraph();
        data.add(new Chunk("Data: " + date.getDayOfMonth() + "." + ( month < 10 ? "0" + month : month )  + "." + date.getYear() + Chunk.NEWLINE,fontnormal));
        PdfPCell datacell = new PdfPCell(data);
        datacell.setBorder(0);

        Paragraph nraviz = new Paragraph();
        nraviz.add(new Chunk("Nr. aviz insot. marfa:" + Chunk.NEWLINE,fontnormal));
        PdfPCell nravizcell = new PdfPCell(nraviz);
        nravizcell.setBorder(0);
        //title.setLeading(150,0);
        PdfPCell titlu = new PdfPCell(title);


        Paragraph nravizenum = new Paragraph();
        nravizenum.add(new Chunk(info.getNrAvize() + Chunk.NEWLINE, fontnormal));
        PdfPCell nravizenumcell = new PdfPCell(nravizenum);
        nravizenumcell.setBorder(Rectangle.NO_BORDER);
        nravizenumcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        nravizenumcell.setCellEvent(new DottedCell(PdfPCell.BOTTOM));
        nravizenumcell.setPaddingRight(20);


        titlu.setHorizontalAlignment(Element.ALIGN_LEFT);
        titlu.setVerticalAlignment(Element.ALIGN_MIDDLE);
        titlu.setBorder(Rectangle.NO_BORDER);
        titlu.setRowspan(1);

        tableInfo.addCell(furnizorCell);
        tableInfo.addCell(empty);
        tableInfo.addCell(furnizorCellC);
        tableInfo.addCell(cuiCell);
        tableInfo.addCell(empty);
        tableInfo.addCell(cuiCellC);
        tableInfo.addCell(nrOrdCell);
        tableInfo.addCell(titlu);
        tableInfo.addCell(nrOrdCellC);
        tableInfo.addCell(sediuCell);
        tableInfo.addCell(seriecell);
        tableInfo.addCell(sediuCellC);
        tableInfo.addCell(capitalCell);
        tableInfo.addCell(datacell);
        tableInfo.addCell(judetCellC);
        tableInfo.addCell(judetCell);
        tableInfo.addCell(nravizcell);
        tableInfo.addCell(contCellC);
        tableInfo.addCell(contCell);
        tableInfo.addCell(nravizenumcell);
        tableInfo.addCell(bancaCellC);
        tableInfo.addCell(bancaCell);
        tableInfo.addCell(empty);
        tableInfo.addCell(empty);

        document.add(tableInfo);

    }


    private void createTableContent(ArrayList<TableItem> productsList, TableItem discount) {
        // TODO Auto-generated method stub
        Font fontbold = FontFactory.getFont("defaultEncoding", 10, Font.NORMAL);
        Integer nrCrt = new Integer(1);

        for (TableItem product : productsList) {
            PdfPCell cell0 = new PdfPCell(new Paragraph(nrCrt.toString(),fontbold));
            cell0.setPaddingLeft(10);
            cell0.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell0.setVerticalAlignment(Element.ALIGN_MIDDLE);
            nrCrt++;

            PdfPCell cell1 = new PdfPCell(new Paragraph(product.getProductName(),fontbold));
            cell1.setPaddingLeft(10);
            cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPCell cell2 = new PdfPCell(new Paragraph(product.getUmValue(),fontbold));
            cell2.setPaddingLeft(10);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPCell cell3 = new PdfPCell(new Paragraph(String.valueOf(BigDecimal.valueOf(product.getQuantity()).setScale(2,RoundingMode.HALF_UP).doubleValue()), fontbold));
            cell3.setPaddingLeft(10);
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPCell cell4 = new PdfPCell(new Paragraph(String.valueOf(product.getProductPrice().setScale(2,RoundingMode.HALF_UP)),fontbold));
            cell4.setPaddingLeft(10);
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPCell cell5 = new PdfPCell(new Paragraph(String.valueOf(product.getProductValue().setScale(2,RoundingMode.HALF_UP)),fontbold));
            cell5.setPaddingLeft(10);
            cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPCell cell6 = new PdfPCell(new Paragraph(String.valueOf(product.getProductTvaValue().setScale(2,RoundingMode.HALF_UP)),fontbold));
            cell6.setPaddingLeft(10);
            cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);

            table.addCell(cell0);
            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);
            table.addCell(cell4);
            table.addCell(cell5);
            table.addCell(cell6);
        }
        if(discount.getProductValue().equals(BigDecimal.ZERO))
        {
            PdfPCell cell0 = new PdfPCell(new Paragraph(nrCrt.toString(),fontbold));
            cell0.setPaddingLeft(10);
            cell0.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell0.setVerticalAlignment(Element.ALIGN_MIDDLE);
            nrCrt++;

            PdfPCell cell1 = new PdfPCell(new Paragraph(discount.getProductName(),fontbold));
            cell1.setPaddingLeft(10);
            cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPCell cell2 = new PdfPCell(new Paragraph(discount.getUmValue(),fontbold));
            cell2.setPaddingLeft(10);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPCell cell3 = new PdfPCell(new Paragraph(String.valueOf(BigDecimal.valueOf(discount.getQuantity()).setScale(2,RoundingMode.HALF_UP).doubleValue()),fontbold));
            cell3.setPaddingLeft(10);
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPCell cell4 = new PdfPCell(new Paragraph("",fontbold));
            cell4.setPaddingLeft(10);
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPCell cell5 = new PdfPCell(new Paragraph(discount.getProductValue().setScale(2, RoundingMode.HALF_UP).toString(),fontbold));
            cell5.setPaddingLeft(10);
            cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPCell cell6 = new PdfPCell(new Paragraph(discount.getProductTvaValue().setScale(2, RoundingMode.HALF_UP).toString(),fontbold));
            cell6.setPaddingLeft(10);
            cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);

            table.addCell(cell0);
            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);
            table.addCell(cell4);
            table.addCell(cell5);
            table.addCell(cell6);
        }
    }

    private void createTableContentDiscount(ArrayList<TableItem> productsList, TableItem discount) {
        // TODO Auto-generated method stub
        Font fontbold = FontFactory.getFont("defaultEncoding", 10, Font.NORMAL);
        Integer nrCrt = new Integer(1);

        for (TableItem product : productsList) {
            PdfPCell cell0 = new PdfPCell(new Paragraph(nrCrt.toString(),fontbold));
            cell0.setPaddingLeft(10);
            cell0.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell0.setVerticalAlignment(Element.ALIGN_MIDDLE);
            nrCrt++;

            PdfPCell cell1 = new PdfPCell(new Paragraph(product.getProductName(),fontbold));
            cell1.setPaddingLeft(10);
            cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPCell cell2 = new PdfPCell(new Paragraph(product.getUmValue(),fontbold));
            cell2.setPaddingLeft(10);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPCell cell3 = new PdfPCell(new Paragraph(String.valueOf(BigDecimal.valueOf(discount.getQuantity()).setScale(2,RoundingMode.HALF_UP).doubleValue()),fontbold));
            cell3.setPaddingLeft(10);
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPCell cell4 = new PdfPCell(new Paragraph(product.getProductPrice().setScale(2,RoundingMode.HALF_UP).toString(),fontbold));
            cell4.setPaddingLeft(10);
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPCell cell5 = new PdfPCell(new Paragraph(product.getProductValue().setScale(2,RoundingMode.HALF_UP).toString(),fontbold));
            cell5.setPaddingLeft(10);
            cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPCell cell6 = new PdfPCell(new Paragraph(product.getProductTvaValue().setScale(2,RoundingMode.HALF_UP).toString(),fontbold));
            cell6.setPaddingLeft(10);
            cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPCell cell7 = new PdfPCell(new Paragraph(product.getDiscount().setScale(2,RoundingMode.HALF_UP).toString()+"%",fontbold));
            cell7.setPaddingLeft(10);
            cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell7.setVerticalAlignment(Element.ALIGN_MIDDLE);

            table.addCell(cell0);
            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);
            table.addCell(cell4);
            table.addCell(cell5);
            table.addCell(cell6);
            table.addCell(cell7);


        }
        PdfPCell cell0 = new PdfPCell(new Paragraph(nrCrt.toString(),fontbold));
        cell0.setPaddingLeft(10);
        cell0.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell0.setVerticalAlignment(Element.ALIGN_MIDDLE);
        nrCrt++;

        PdfPCell cell1 = new PdfPCell(new Paragraph(discount.getProductName(),fontbold));
        cell1.setPaddingLeft(10);
        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cell2 = new PdfPCell(new Paragraph(discount.getUmValue(),fontbold));
        cell2.setPaddingLeft(10);
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cell3 = new PdfPCell(new Paragraph(new Double(discount.getQuantity()).toString(),fontbold));
        cell3.setPaddingLeft(10);
        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cell4 = new PdfPCell(new Paragraph("",fontbold));
        cell4.setPaddingLeft(10);
        cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cell5 = new PdfPCell(new Paragraph(discount.getProductValue().setScale(2, RoundingMode.HALF_UP).toString(),fontbold));
        cell5.setPaddingLeft(10);
        cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cell6 = new PdfPCell(new Paragraph(discount.getProductTvaValue().setScale(2, RoundingMode.HALF_UP).toString(),fontbold));
        cell6.setPaddingLeft(10);
        cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cell7 = new PdfPCell(new Paragraph("-",fontbold));
        cell6.setPaddingLeft(10);
        cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table.addCell(cell0);
        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);
        table.addCell(cell4);
        table.addCell(cell5);
        table.addCell(cell6);
        table.addCell(cell7);
    }


    private void createFirstRow() throws DocumentException {
        // TODO Auto-generated method stub
        table.setWidths(columnWidths);

        PdfPCell cell0 = new PdfPCell(new Paragraph("Nr. crt.",FontFactory.getFont("defaultEncoding", 12, Font.NORMAL)));
        cell0.setPaddingLeft(10);
        cell0.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell0.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cell1 = new PdfPCell(new Paragraph("Produs"));
        cell1.setPaddingLeft(10);
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cell2 = new PdfPCell(new Paragraph("UM"));
        cell2.setPaddingLeft(10);
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cell3 = new PdfPCell(new Paragraph("Cantitate"));
        cell3.setPaddingLeft(10);
        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cell4 = new PdfPCell(new Paragraph("Pret unitar"));
        cell4.setPaddingLeft(10);
        cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cell5 = new PdfPCell(new Paragraph("Valoare"));
        cell5.setPaddingLeft(10);
        cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cell6 = new PdfPCell(new Paragraph("TVA"));
        cell6.setPaddingLeft(10);
        cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table.addCell(cell0);
        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);
        table.addCell(cell4);
        table.addCell(cell5);
        table.addCell(cell6);

    }

    private void createFirstRowDiscount() throws DocumentException {
        // TODO Auto-generated method stub
        table.setWidths(columnWidths2);

        PdfPCell cell0 = new PdfPCell(new Paragraph("Nr. crt.",FontFactory.getFont("defaultEncoding", 12, Font.NORMAL)));
        cell0.setPaddingLeft(10);
        cell0.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell0.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cell1 = new PdfPCell(new Paragraph("Produs"));
        cell1.setPaddingLeft(10);
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cell2 = new PdfPCell(new Paragraph("UM"));
        cell2.setPaddingLeft(10);
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cell3 = new PdfPCell(new Paragraph("Cantitate"));
        cell3.setPaddingLeft(10);
        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cell4 = new PdfPCell(new Paragraph("Pret unitar"));
        cell4.setPaddingLeft(10);
        cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cell5 = new PdfPCell(new Paragraph("Valoare"));
        cell5.setPaddingLeft(10);
        cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cell6 = new PdfPCell(new Paragraph("TVA"));
        cell6.setPaddingLeft(10);
        cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cell7 = new PdfPCell(new Paragraph("D%"));
        cell7.setPaddingLeft(10);
        cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell7.setVerticalAlignment(Element.ALIGN_MIDDLE);


        table.addCell(cell0);
        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);
        table.addCell(cell4);
        table.addCell(cell5);
        table.addCell(cell6);
        table.addCell(cell7);

    }

    private Phrase createInfoPhrase(String camp,String valoare){
        Font fontbold = FontFactory.getFont("defaultEncoding", 10, Font.BOLD);
        Font fontnormal = FontFactory.getFont("defaultEncoding", 10, Font.NORMAL);
        Phrase phrase = new Phrase();
        phrase.add(
                new Chunk(camp,  fontbold)
        );
        phrase.add(new Chunk(valoare, fontnormal));
        return phrase;
    }

}
