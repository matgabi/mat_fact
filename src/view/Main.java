package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.NewInvoiceState;
import model.PdfGenerator;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import repository.*;
import repository.interfaces.*;

import java.util.Locale;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Locale.setDefault(new Locale("ro", "RO"));

        Configuration configuration = new Configuration();
        configuration.configure("hib_config/hibernate.cfg.xml");

        SessionFactory sessionFactory = configuration.buildSessionFactory();

        BatchRepositoryInterface batchRepository = new BatchesRepository(sessionFactory);
        CarsRepositoryInterface carsRepository = new CarsRepository(sessionFactory);
        ClientsRepositoryInterface clientsRepository = new ClientsRepository(sessionFactory);
        EmitorsRepositoryInterface emitorsRepository = new EmitorsRepository(sessionFactory);
        InvoiceRepositoryInterface invoiceRepository = new InvoiceRepository(sessionFactory);
        ProductsRepositoryInterface productsRepository = new ProductsRepository(sessionFactory);
        SellersRepositoryInterface sellersRepository = new SellersRepository(sessionFactory);
        TVARepositoryInterface tvaRepository = new TVARepository(sessionFactory);
        UMRepositoryInterface umRepository = new UMRepository(sessionFactory);

        RepositoryInterface repository = new Repository(carsRepository,
                clientsRepository,
                emitorsRepository,
                invoiceRepository,
                productsRepository,
                sellersRepository,
                tvaRepository,
                umRepository,
                batchRepository);

        PdfGenerator pdfGenerator = new PdfGenerator();
        NewInvoiceState invoiceState = new NewInvoiceState(repository, pdfGenerator);

        InvoiceView invoiceView = new InvoiceView(invoiceState, repository);
        Scene mainScene = new Scene(invoiceView);

        primaryStage.setTitle("SC MATI CONSULT SRL");
        primaryStage.setScene(mainScene);
        primaryStage.show();
        primaryStage.setMaximized(true);
        primaryStage.setMinHeight(720);
        primaryStage.setMinWidth(1280);

        primaryStage.setOnCloseRequest(event -> {
            sessionFactory.close();
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}