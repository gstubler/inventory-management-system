import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller class for "ModifyProduct.fxml" form.
 */
public class ModifyProductController implements Initializable {

    /**
     * Holds data for the selected Product.
     */
    private Product selectedProduct;

    /**
     * Populates the textFields with the data from selectedProduct, and populates associatedPartsTable with Parts that
     * are associated with selectedProduct.
     * @param product
     */
    public void modifyProductData(Product product) {
        selectedProduct = product;

        idField.setText(String.valueOf(selectedProduct.getId()));
        nameField.setText(selectedProduct.getName());
        priceField.setText(String.valueOf(selectedProduct.getPrice()));
        stockField.setText(String.valueOf(selectedProduct.getStock()));
        minField.setText(String.valueOf(selectedProduct.getMin()));
        maxField.setText(String.valueOf(selectedProduct.getMax()));

        if (!selectedProduct.getAllAssociatedParts().isEmpty()) {
            for (int i = 0; i < selectedProduct.getAllAssociatedParts().size(); i++) {
                associatedPartsList.add(selectedProduct.getAllAssociatedParts().get(i));
            }
            associatedPartsTable.setItems(associatedPartsList);
        }
    }

    /**
     * Observable array lists for all available Parts, Parts that are associated with the Product, and associated Parts
     * to be removed when selectedProduct is saved..
     */
    private ObservableList<Part> partSearchList = FXCollections.observableArrayList();
    private ObservableList<Part> partFilterList = FXCollections.observableArrayList();
    private ObservableList<Part> associatedPartsList = FXCollections.observableArrayList();
    private ObservableList<Part> partRemoveList = FXCollections.observableArrayList();

    /**
     * Checks the input string of partSearchField. If the string only consists of numbers, the string is parsed to an
     * Integer to compare against the partId. Otherwise, it is left as a string to compare against the partName.
     * @param strNum
     * @return boolean
     */
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int id = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

//ModifyProduct.fxml Components
    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField priceField;
    @FXML private TextField stockField;
    @FXML private TextField minField;
    @FXML private TextField maxField;
    @FXML private TextField searchPartField;
    @FXML private Button addPartButton;
    @FXML private Button removePartButton;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private TableView<Part> allPartsTable;
    @FXML private TableView<Part> associatedPartsTable;
    @FXML private TableColumn<Part, Integer> partId;
    @FXML private TableColumn<Part, String> partName;
    @FXML private TableColumn<Part, Integer> partStock;
    @FXML private TableColumn<Part, Double> partPrice;
    @FXML private TableColumn<Part, Integer> associatedPartId;
    @FXML private TableColumn<Part, String> associatedPartName;
    @FXML private TableColumn<Part, Integer> associatedPartStock;
    @FXML private TableColumn<Part, Double> associatedPartPrice;

    /**
     * Contains the code to populate and allPartsTable and associatedPartsTable, as well as filter search results.
     * @param url
     * @param rb
     */
    public void initialize(URL url, ResourceBundle rb) {
        partId.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        partStock.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        partPrice.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
        allPartsTable.setItems(Inventory.getAllParts());

        //Filters allPartsTable search results
        partSearchList.addAll(Inventory.getAllParts());
        searchPartField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                partFilterList.clear();
                if (searchPartField.getText().isEmpty()) {
                    allPartsTable.setItems(Inventory.getAllParts());
                }
                else {
                    if (isNumeric(searchPartField.getText())) {
                        for (int i = 0; i < partSearchList.size(); i++) {
                            if (String.valueOf(partSearchList.get(i).getId()).contains(searchPartField.getText())) {
                                partFilterList.add(partSearchList.get(i));
                            }
                        }
                    }
                    else {
                        for (int i = 0; i < partSearchList.size(); i++) {
                            if (partSearchList.get(i).getName().contains(searchPartField.getText())) {
                                partFilterList.add(partSearchList.get(i));
                            }
                        }
                    }
                    if (partFilterList.isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Inventory Manager");
                        alert.setContentText("No parts found.");

                        alert.showAndWait();
                    }
                    else {
                        allPartsTable.setItems(partFilterList);
                    }
                }
            }
        });

        /*
        partSearchList.addAll(Inventory.getAllParts());
        FilteredList<Part> partFilteredList = new FilteredList<>(partSearchList, p -> true);
        partSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            partFilteredList.setPredicate(part -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (isNumeric(newValue)) {
                    if (String.valueOf(part.getId()).contains(newValue)) {
                        return true;
                    }
                }
                else if (part.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        SortedList<Part> partSortedList = new SortedList<>(partFilteredList);
        partSortedList.comparatorProperty().bind(allPartsTable.comparatorProperty());
        allPartsTable.setItems(partSortedList);
         */

        associatedPartId.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        associatedPartName.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        associatedPartStock.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        associatedPartPrice.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
    }

//ModifyProduct.fxml Listeners

    /**
     * Listener for addPartButton. If no Part is selected in allPartsTable, or if the selected Part is already in the
     * associatedPartsTable, an error message is displayed. Otherwise, the selected Part is added to
     * associatedPartsTable.
     * @param actionEvent
     * @throws IOException
     */
    public void addPartButtonListener(ActionEvent actionEvent) throws IOException {
        if (allPartsTable.getSelectionModel().getSelectedItem() == null) {
            MessageWindowController.messageText = "Please select a part to add.";
            Parent parent = FXMLLoader.load(getClass().getResource("MessageWindow.fxml"));
            Stage popUp = new Stage();
            Scene messageWindow = new Scene(parent);
            popUp.setTitle("Inventory Manager");
            popUp.setScene(messageWindow);
            popUp.setResizable(false);
            popUp.show();
        }
        else {
            if (associatedPartsList.contains(allPartsTable.getSelectionModel().getSelectedItem())) {
                MessageWindowController.messageText = "Part has already been added.";
                Parent parent = FXMLLoader.load(getClass().getResource("MessageWindow.fxml"));
                Stage popUp = new Stage();
                Scene messageWindow = new Scene(parent);
                popUp.setTitle("Inventory Manager");
                popUp.setScene(messageWindow);
                popUp.setResizable(false);
                popUp.show();
            }
            else {
                associatedPartsList.add(allPartsTable.getSelectionModel().getSelectedItem());
                associatedPartsTable.setItems(associatedPartsList);
            }
        }
    }

    /**
     * Listener for removePartButton. If no Part is selected in associatedPartsTable, an error message is displayed.
     * Otherwise, the selected Part is removed from associatedPartsTable.
     * @param actionEvent
     * @throws IOException
     */
    public void removePartButtonListener(ActionEvent actionEvent) throws IOException {
        if (associatedPartsTable.getSelectionModel().getSelectedItem() == null) {
            MessageWindowController.messageText = "Please select a part to remove.";
            Parent parent = FXMLLoader.load(getClass().getResource("MessageWindow.fxml"));
            Stage popUp = new Stage();
            Scene messageWindow = new Scene(parent);
            popUp.setTitle("Inventory Manager");
            popUp.setScene(messageWindow);
            popUp.setResizable(false);
            popUp.show();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Inventory Manager");
            alert.setContentText("Are you sure you want to remove the part?");

            ButtonType buttonTypeYes = new ButtonType("Yes");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeYes) {
                associatedPartsList.remove(associatedPartsTable.getSelectionModel().getSelectedItem());
                associatedPartsTable.setItems(associatedPartsList);
            }
        }
    }

    /**
     * Listener for saveButton. Updates the Product's variables with the input data. Removes associated Parts in the
     * partRemoveList. Loads "Main.fxml" form. An error message is displayed if any of the inputs are of the wrong data
     * type, and if minField is not less than maxField, and stockField must be within the range of minField and
     * maxField.
     * @param actionEvent
     * @throws Exception
     */
    public void saveButtonListener(ActionEvent actionEvent) throws Exception {
        try {
            if (Integer.parseInt(minField.getText()) <= Integer.parseInt(stockField.getText()) && Integer.parseInt(stockField.getText()) <= Integer.parseInt(maxField.getText()) && Integer.parseInt(minField.getText()) < Integer.parseInt(maxField.getText())) {
                try {
                    selectedProduct.setName(nameField.getText());
                    selectedProduct.setPrice(Double.parseDouble(priceField.getText()));
                    selectedProduct.setStock(Integer.parseInt(stockField.getText()));
                    selectedProduct.setMin(Integer.parseInt(minField.getText()));
                    selectedProduct.setMax(Integer.parseInt(maxField.getText()));

                    for (int i = 0; i < partRemoveList.size(); i++) {
                        selectedProduct.deleteAssociatedPart(partRemoveList.get(i));
                    }

                    for (int i = 0; i < associatedPartsList.size(); i++) {
                        if (!selectedProduct.getAllAssociatedParts().contains(associatedPartsList.get(i))) {
                            selectedProduct.addAssociatedPart(associatedPartsList.get(i));
                        }
                    }

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