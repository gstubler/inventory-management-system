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
 * Controller class for "ModifyPart.fxml" form.
 */
public class ModifyPartController {

    /**
     * Holds data for the selected Part.
     */
    private Part selectedPart;

    /**
     * Holds data for the selected InHouse.
     */
    private InHouse selectedInHouse;

    /**
     * Holds data for the selected Outsourced.
     */
    private Outsourced selectedOutsourced;

    /**
     * Determines which subclass selectedPart is a member of and casts it to either selectedInHouse or
     * selectedOutsourced. The textFields are the populated and the correct RadioButton is selected.
     * @param part
     */
    public void modifyPartData(Part part) {
        selectedPart = part;

        if (selectedPart instanceof InHouse) {
            inHouseRadio.setSelected(true);
            toggleLabel.setText("Machine ID");
            machineIdField.setVisible(true);
            machineIdField.setDisable(false);
            companyNameField.setVisible(false);
            companyNameField.setDisable(true);

            selectedInHouse = (InHouse) selectedPart;

            idField.setText(String.valueOf(selectedInHouse.getId()));
            nameField.setText(selectedInHouse.getName());
            priceField.setText(String.valueOf(selectedInHouse.getPrice()));
            stockField.setText(String.valueOf(selectedInHouse.getStock()));
            minField.setText(String.valueOf(selectedInHouse.getMin()));
            maxField.setText(String.valueOf(selectedInHouse.getMax()));
            machineIdField.setText(String.valueOf(selectedInHouse.getMachineId()));
        }
        else if (selectedPart instanceof Outsourced) {
            outsourcedRadio.setSelected(true);
            toggleLabel.setText("Company Name");
            companyNameField.setVisible(true);
            companyNameField.setDisable(false);
            machineIdField.setVisible(false);
            machineIdField.setDisable(true);

            selectedOutsourced = (Outsourced) selectedPart;

            idField.setText(String.valueOf(selectedOutsourced.getId()));
            nameField.setText(selectedOutsourced.getName());
            priceField.setText(String.valueOf(selectedOutsourced.getPrice()));
            stockField.setText(String.valueOf(selectedOutsourced.getStock()));
            minField.setText(String.valueOf(selectedOutsourced.getMin()));
            maxField.setText(String.valueOf(selectedOutsourced.getMax()));
            companyNameField.setText(selectedOutsourced.getCompanyName());
        }
    }

    //ModifyPart.fxml Components
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

    //ModifyPart.fxml Listeners
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
     * Listener for saveButton. Checks the selected RadioButton against the subclass of the Part being modified. If the
     * subclass is the same, the Part's variables are updated. If the subclass is different, the old Part is deleted
     * and replaced with a new Part as an instance of the selected subclass. The partId is retained and assigned to the
     * new Part. Loads "Main.fxml" form. An error message is displayed if any of the inputs are of the wrong data type,
     * and if minField is not less than maxField, and stockField must be within the range of minField and maxField.
     *
     * RUNTIME ERROR: When a Part is "modified" from one subclass to the other, the original Part is actually deleted,
     * and a new one is created under the selected subclass. The old Part was being correctly deleted from the allParts
     * list in the Inventory class, but it would still be displayed in partTable in "Main.fxml". Boolean partSwap is
     * assigned "true" if the Part's updated subclass is different. If partSwap is "true", the MainController method
     * partSubclassSwapDelete is accessed remotely to properly remove the old Part from partTable in "Main.fxml".
     *
     * @param actionEvent
     * @throws Exception
     */
    public void saveButtonListener(ActionEvent actionEvent) throws Exception {
    Boolean partSwap = false;
        if (inHouseRadio.isSelected()) {
            try {
                if (Integer.parseInt(minField.getText()) <= Integer.parseInt(stockField.getText()) && Integer.parseInt(stockField.getText()) <= Integer.parseInt(maxField.getText()) && Integer.parseInt(minField.getText()) < Integer.parseInt(maxField.getText())) {
                    try {
                        if (selectedPart instanceof Outsourced) {
                            InHouse newInHouse = new InHouse(   selectedPart.getId(),
                                                                nameField.getText(),
                                                                Double.parseDouble(priceField.getText()),
                                                                Integer.parseInt(stockField.getText()),
                                                                Integer.parseInt(minField.getText()),
                                                                Integer.parseInt(maxField.getText()),
                                                                Integer.parseInt(machineIdField.getText()));
                            Inventory.addPart(newInHouse);
                            Inventory.deletePart(selectedPart);
                            partSwap = true;
                        }
                        else if (selectedPart instanceof InHouse) {
                            selectedInHouse.setName(nameField.getText());
                            selectedInHouse.setPrice(Double.parseDouble(priceField.getText()));
                            selectedInHouse.setStock(Integer.parseInt(stockField.getText()));
                            selectedInHouse.setMin(Integer.parseInt(minField.getText()));
                            selectedInHouse.setMax(Integer.parseInt(maxField.getText()));
                            selectedInHouse.setMachineId(Integer.parseInt(machineIdField.getText()));
                            partSwap = false;
                        }
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("Main.fxml"));
                        Parent parent = loader.load();
                        Scene scene = new Scene(parent);

                        if (partSwap) {
                            MainController controller = loader.getController();
                            controller.partSubclassSwapDelete(selectedPart);
                        }

                        Stage stage  = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
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
                        if (selectedPart instanceof InHouse) {
                            Outsourced newOutsourced = new Outsourced(  selectedPart.getId(),
                                                                        nameField.getText(),
                                                                        Double.parseDouble(priceField.getText()),
                                                                        Integer.parseInt(stockField.getText()),
                                                                        Integer.parseInt(minField.getText()),
                                                                        Integer.parseInt(maxField.getText()),
                                                                        companyNameField.getText());
                            Inventory.addPart(newOutsourced);
                            Inventory.deletePart(selectedPart);
                            partSwap = true;
                        }
                        else if (selectedPart instanceof Outsourced) {
                            selectedOutsourced.setName(nameField.getText());
                            selectedOutsourced.setPrice(Double.parseDouble(priceField.getText()));
                            selectedOutsourced.setStock(Integer.parseInt(stockField.getText()));
                            selectedOutsourced.setMin(Integer.parseInt(minField.getText()));
                            selectedOutsourced.setMax(Integer.parseInt(maxField.getText()));
                            selectedOutsourced.setCompanyName(companyNameField.getText());
                            partSwap = false;
                        }
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("Main.fxml"));
                        Parent parent = loader.load();
                        Scene scene = new Scene(parent);

                        if (partSwap) {
                            MainController controller = loader.getController();
                            controller.partSubclassSwapDelete(selectedPart);
                        }

                        Stage stage  = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
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