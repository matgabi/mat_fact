package model.reports;


import javafx.collections.ObservableList;
import model.InvoiceBlob;
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

public class ProductReportGenerator {

    public static final String GROUP_BY_CLIENT = "cl";
    public static final String GROUP_BY_DATE = "dt";

    public ArrayList<ProductReportContent> generateProductReport(ObservableList<InvoiceBlob> invoices, ProductInterface product, LocalDate start, LocalDate end, String groupBy) throws IOException, ClassNotFoundException, SQLException {
        ArrayList<ProductReportContent> report = new ArrayList<>();

        for(InvoiceBlob blob : invoices){
            if(!blob.isDeleted())
            {
                ByteArrayInputStream instream = new ByteArrayInputStream(blob.getInvoiceBytes().getBytes(1, (int) blob.getInvoiceBytes().length()));
                ObjectInputStream objectInputStream = new ObjectInputStream(instream);

                InvoiceInterface invoice = (InvoiceInterface) objectInputStream.readObject();
                LocalDate date = invoice.getDate();
                if(start.compareTo(date) <= 0 && end.compareTo(date) >= 0)
                {
                    for(TableItem t : invoice.getProducts())
                    {
                        if(t.getProductName().equals(product.getProductName()))
                        {
                            ProductReportContent content = new ProductReportContent(
                                    product.getProductName(),
                                     invoice.getClient().getName(),
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
        if(groupBy.equals(GROUP_BY_CLIENT))
        {
            Collections.sort(report, (o1, o2) -> {
                int compare = o1.getClientName().compareTo(o2.getClientName());
                if(compare == 0)
                {
                    int compare2 = o1.getDate().compareTo(o2.getDate());
                    if(compare2 == 0)
                    {
                        return o1.getInvoiceName().compareTo(o2.getInvoiceName());
                    }
                    return compare2;
                }
                return compare;
            });
        }
        else
        {
            Collections.sort(report, (o1, o2) -> {
                int compare = o1.getDate().compareTo(o2.getDate());
                if(compare == 0)
                {
                    int compare2 = o1.getClientName().compareTo(o2.getClientName());
                    if(compare2 == 0)
                    {
                        return o1.getInvoiceName().compareTo(o2.getInvoiceName());
                    }
                    return compare2;
                }
                return compare;
            });
        }
        return report;
    }

}
