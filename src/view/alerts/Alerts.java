package view.alerts;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Alerts {
    public static void showInformationAlert(String string)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informatii");
        alert.setContentText(string);
        alert.showAndWait();
    }

    public static void showWarningAlert(String string)
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Informatii");
        alert.setContentText(string);
        alert.showAndWait();
    }

    public static void showExceptionAlert(String string, Exception e)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Eroare");

        alert.setContentText(string);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("Stiva de apeluri:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

// Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }
}
