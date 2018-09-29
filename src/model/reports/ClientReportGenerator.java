package model.reports;

import javafx.collections.ObservableList;
import model.InvoiceBlob;
import model.interfaces.ClientInterface;
import model.interfaces.InvoiceInterface;
import model.interfaces.ProductInterface;
import model.interfaces.TableItem;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

public class ClientReportGenerator {
    public static final String GROUP_BY_PRODUCT = "produs";
    public static final String GROUP_BY_DATE = "data";
    public static final String GROUP_BY_INVOICE = "factura";

    public ArrayList<ClientReportContent> generateClientReport(ObservableList<InvoiceBlob> invoices, ClientInterface client, LocalDate start, LocalDate end, String groupBy) throws IOException, ClassNotFoundException, SQLException {
        ArrayList<ClientReportContent> report = new ArrayList<>();

        for (InvoiceBlob blob : invoices) {
            if (!blob.isDeleted()) {
                ByteArrayInputStream instream = new ByteArrayInputStream(blob.getInvoiceBytes().getBytes(1, (int) blob.getInvoiceBytes().length()));
                ObjectInputStream objectInputStream = new ObjectInputStream(instream);

                InvoiceInterface invoice = (InvoiceInterface) objectInputStream.readObject();
                LocalDate date = invoice.getDate();
                if (start.compareTo(date) <= 0 && end.compareTo(date) >= 0) {
                    if (invoice.getClient().getName().equals(client.getName())) {
                        for (TableItem t : invoice.getProducts()) {
                            ClientReportContent content = new ClientReportContent(
                                    invoice.getClient().getName(),
                                    t.getProductName(),
                                    invoice.getInfo().getSeria() + " " + invoice.getInfo().getNr(),
                                    invoice.getDate(),
                                    t.getQuantity(),
                                    t.getUmValue()
                            );

                            report.add(content);
                        }
                    }

                }
            }
        }
        if (groupBy.equals(GROUP_BY_PRODUCT))
        {
            Collections.sort(report, (o1, o2) -> {
                int compare = o1.getProductName().compareTo(o2.getProductName());
                if (compare == 0) {
                    int compare2 = o1.getInvoiceName().compareTo(o2.getInvoiceName());
                    if (compare2 == 0) {
                        return o1.getDate().compareTo(o2.getDate());
                    }
                    return compare2;
                }
                return compare;
            });
        }
        else if (groupBy.equals(GROUP_BY_DATE))
        {
            Collections.sort(report, (o1, o2) -> {
                int compare = o1.getDate().compareTo(o2.getDate());
                if (compare == 0) {
                    int compare2 = o1.getInvoiceName().compareTo(o2.getInvoiceName());
                    if (compare2 == 0) {
                        return o1.getProductName().compareTo(o2.getProductName());
                    }
                    return compare2;
                }
                return compare;
            });
        }
        else
        {
            Collections.sort(report, (o1, o2) -> {
                int compare = o1.getInvoiceName().compareTo(o2.getInvoiceName());
                if (compare == 0) {
                    int compare2 = o1.getProductName().compareTo(o2.getProductName());
                    if (compare2 == 0) {
                        return o1.getDate().compareTo(o2.getDate());
                    }
                    return compare2;
                }
                return compare;
            });
        }
        return report;
    }

}
