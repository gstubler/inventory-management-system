import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MessageWindowController implements Initializable {
    public static String messageText = "null";

    @FXML private Button okButton;
    @FXML private Label messageLabel;

    public void initialize(URL url, ResourceBundle rb) {
        messageLabel.setText(messageText);
    }

    public void okButtonListener(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
