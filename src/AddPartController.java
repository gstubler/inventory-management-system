import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller class for "AddPart.fxml" form.
 */
public class AddPartController {

    /**
     * Stores an int to automatically assign a unique ID number to partId. Increments whenever a new Part is created, so
     * that even a deleted Part's partId value cannot be re-used, unless a Part's subclass is modified, which requires
     * the old Part to be deleted and replaced with a new Part.
     */
    private static int partIdNum = 1;

    //AddPart.fxml Components
    @FXML private RadioButton inHouseRadio;
    @FXML private RadioButton outsourcedRadio;
    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField priceField;
    @FXML private TextField stockField;
    @FXML private TextField minField;
    @FXML private TextField maxField;
    @FXML private TextField machineIdField;
    @FXML private TextField companyNameField;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private Label toggleLabel;

    //AddPart.fxml Listeners
    /**
     * Listener for inHouseRadio and outsourcedRadio. If inHouseRadio is selected, machineIdField is shown and enabled
     * while companyNameField is hidden and disabled. Otherwise if outsourcedRadio is selected, companyNameField is
     * shown and enabled while machineIdField is hidden and disabled. The text in toggleLabel is set to reflect the
     * correct field.
     * @param actionEvent
     */
    public void toggleListener(ActionEvent actionEvent) {
        if (inHouseRadio.isSelected()) {
            toggleLabel.setText("Machine ID");
            machineIdField.setVisible(true);
            machineIdField.setDisable(false);
            companyNameField.setVisible(false);
            companyNameField.setDisable(true);
        }
        else if (outsourcedRadio.isSelected()) {
            toggleLabel.setText("Company Name");
            companyNameField.setVisible(true);
            companyNameField.setDisable(false);
            machineIdField.setVisible(false);
            machineIdField.setDisable(true);
        }
    }

    /**
     * Listener for saveButton. If inHouseRadio is selected, a new InHouse is created. If outsourcedRadio is selected,
     * a new Outsourced is created. The textField inputs are parsed and assigned to the new Part. partIdNum is assigned
     * to partId and then incremented. The new Part is added to the static Observable Array List in Inventory. Loads
     * "Main.fxml" form. An error message is displayed if any of the inputs are of the wrong data type, and if minField
     * is not less than maxField, and stockField must be within the range of minField and maxField.
     * @param actionEvent
     * @throws Exception
     */
    public void saveButtonListener(ActionEvent actionEvent) throws Exception {
        if (inHouseRadio.isSelected()) {
            try {
                if (Integer.parseInt(minField.getText()) <= Integer.parseInt(stockField.getText()) && Integer.parseInt(stockField.getText()) <= Integer.parseInt(maxField.getText()) && Integer.parseInt(minField.getText()) < Integer.parseInt(maxField.getText())) {
                    try {
                        InHouse newInHouse = new InHouse(   partIdNum,
                                                            nameField.getText(),
                                                            Double.parseDouble(priceField.getText()),
                                                            Integer.parseInt(stockField.getText()),
                                                            Integer.parseInt(minField.getText()),
                                                            Integer.parseInt(maxField.getText()),
                                                            Integer.parseInt(machineIdField.getText()));
                        Inventory.addPart(newInHouse);
                        partIdNum++;

                        Parent parent = FXMLLoader.load(getClass().getResource("Main.fxml"));
                        Scene scene = new Scene(parent);
                        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                        stage.setScene(scene);
                        stage.show();

                    }
                    catch (NumberFormatException e) {
                        MessageWindowController.messageText = "Invalid data input.";
                        Parent parent = FXMLLoader.load(getClass().getResource("MessageWindow.fxml"));
                        Stage popUp = new Stage();
                        Scene messageWindow = new Scene(parent);
                        popUp.setTitle("Inventory Manager");
                        popUp.setScene(messageWindow);
                        popUp.setResizable(false);
                        popUp.show();
                    }
                }
                else {
                    MessageWindowController.messageText = "Minimum inventory must be less than maximum inventory. Current stock cannot exceed this range.";
                    Parent parent = FXMLLoader.load(getClass().getResource("MessageWindow.fxml"));
                    Stage popUp = new Stage();
                    Scene messageWindow = new Scene(parent);
                    popUp.setTitle("Inventory Manager");
                    popUp.setScene(messageWindow);
                    popUp.setResizable(false);
                    popUp.show();
                }
            }
            catch (NumberFormatException e) {
                MessageWindowController.messageText = "Invalid data input.";
                Parent parent = FXMLLoader.load(getClass().getResource("MessageWindow.fxml"));
                Stage popUp = new Stage();
                Scene messageWindow = new Scene(parent);
                popUp.setTitle("Inventory Manager");
                popUp.setScene(messageWindow);
                popUp.setResizable(false);
                popUp.show();
            }
        }
        else if (outsourcedRadio.isSelected()) {
            try {
                if (Integer.parseInt(minField.getText()) <= Integer.parseInt(stockField.getText()) && Integer.parseInt(stockField.getText()) <= Integer.parseInt(maxField.getText()) && Integer.parseInt(minField.getText()) < Integer.parseInt(maxField.getText())) {
                    try {
                        Outsourced newOutsourced = new Outsourced(  partIdNum,
                                                                    nameField.getText(),
                                                                    Double.parseDouble(priceField.getText()),
                                                                    Integer.parseInt(stockField.getText()),
                                                                    Integer.parseInt(minField.getText()),
                                                                    Integer.parseInt(maxField.getText()),
                                                                    companyNameField.getText());
                        Inventory.addPart(newOutsourced);
                        partIdNum++;

                        Parent parent = FXMLLoader.load(getClass().getResource("Main.fxml"));
                        Scene scene = new Scene(parent);
                        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                        stage.setScene(scene);
                        stage.show();

                    }
                    catch (NumberFormatException e) {
                        MessageWindowController.messageText = "Invalid data input.";
                        Parent parent = FXMLLoader.load(getClass().getResource("MessageWindow.fxml"));
                        Stage popUp = new Stage();
                        Scene messageWindow = new Scene(parent);
                        popUp.setTitle("Inventory Manager");
                        popUp.setScene(messageWindow);
                        popUp.setResizable(false);
                        popUp.show();
                    }
                }
                else {
                    MessageWindowController.messageText = "Minimum inventory must be less than maximum inventory. Current stock cannot exceed this range.";
                    Parent parent = FXMLLoader.load(getClass().getResource("MessageWindow.fxml"));
                    Stage popUp = new Stage();
                    Scene messageWindow = new Scene(parent);
                    popUp.setTitle("Inventory Manager");
                    popUp.setScene(messageWindow);
                    popUp.setResizable(false);
                    popUp.show();
                }
            }
            catch (NumberFormatException e) {
                MessageWindowController.messageText = "Invalid data input.";
                Parent parent = FXMLLoader.load(getClass().getResource("MessageWindow.fxml"));
                Stage popUp = new Stage();
                Scene messageWindow = new Scene(parent);
                popUp.setTitle("Inventory Manager");
                popUp.setScene(messageWindow);
                popUp.setResizable(false);
                popUp.show();
            }
        }
    }

    /**
     * Listener for cancelButton. Loads "Main.fxml" form.
     * @param actionEvent
     * @throws Exception
     */
    public void cancelButtonListener(ActionEvent actionEvent) throws Exception {
        Parent parent = FXMLLoader.load(getClass().getResource("Main.fxml"));
        Scene scene = new Scene(parent);
        Stage stage  = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}