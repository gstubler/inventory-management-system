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
 * Controller class for "Main.fxml" form.
 *
 * FUTURE ENHANCEMENT: Added Parts and Products could be exported to an external file to save data when the program is
 * closed, that could be re-imported when the program is opened again.
 */
public class MainController implements Initializable {

    /**
     * Observable array lists for Part and Product instances.
     */
    private ObservableList<Part> partSearchList = FXCollections.observableArrayList();
    private ObservableList<Part> partFilterList = FXCollections.observableArrayList();
    private ObservableList<Product> productSearchList = FXCollections.observableArrayList();
    private ObservableList<Product> productFilterList = FXCollections.observableArrayList();


    /**
     * Checks the input string of searchPartField or searchProductField. If the string only consists of numbers, the
     * string is parsed to an Integer to compare against the partId or productId. Otherwise, it is left as a string to
     * compare against the partName or productName.
     * @param strNum
     * @return boolean
     */
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int id = Integer.parseInt(strNum);
        }
        catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * Removes the original Part from the partTable when a Part is "modified" from one subclass to another, since this
     * operation actually deletes the Part and replaces it with a new one.
     * @param deletePart
     */
    public void partSubclassSwapDelete(Part deletePart) {
        partTable.getItems().remove(deletePart);
    }

    /**
     * Iterates through Part to determine if the parameter Part is associated with a Product, in which case partProtect
     * is true. This is checked when a Part is selected for deletion, as a Part is not allowed to be deleted while it is
     * associated with a Product.
     * @param part
     * @return boolean
     */
    private boolean partProtect(Part part) {
        boolean partProtect = false;
        for (int i = 0; i < Inventory.getAllProducts().size(); i++) {
            if (Inventory.getAllProducts().get(i).getAllAssociatedParts().contains(partTable.getSelectionModel().getSelectedItem())) {
                partProtect = true;
                break;
            }
            else {
                partProtect = false;
            }
        }
        return partProtect;
    }

    //Main.fxml Components
    @FXML private TextField searchPartField;
    @FXML private TextField searchProductField;
    @FXML private TableView<Part> partTable;
    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Part, Integer> partId;
    @FXML private TableColumn<Part, String> partName;
    @FXML private TableColumn<Part, Integer> partStock;
    @FXML private TableColumn<Part, Double> partPrice;
    @FXML private TableColumn<Product, Integer> productId;
    @FXML private TableColumn<Product, String> productName;
    @FXML private TableColumn<Product, Integer> productStock;
    @FXML private TableColumn<Product, Double> productPrice;
    @FXML private Button addPartButton;
    @FXML private Button modifyPartButton;
    @FXML private Button deletePartButton;
    @FXML private Button addProductButton;
    @FXML private Button modifyProductButton;
    @FXML private Button deleteProductButton;
    @FXML private Button exitButton;

    @FXML private Button yesButton;
    @FXML private Button noButton;

    /**
     * Contains the code to populate and partTable and productTable, as well as filter search results.
     * @param url
     * @param rb
     */
    public void initialize(URL url, ResourceBundle rb) {

        //Populates partTable
        partId.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        partStock.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        partPrice.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
        partTable.setItems(Inventory.getAllParts());

        //Filters partTable search results
        partSearchList.addAll(Inventory.getAllParts());
        searchPartField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                partFilterList.clear();
                if (searchPartField.getText().isEmpty()) {
                    partTable.setItems(Inventory.getAllParts());
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
                        partTable.setItems(partFilterList);
                    }
                }
            }
        });

        /*
        partSearchList.addAll(Inventory.getAllParts());
        FilteredList<Part> partFilteredList = new FilteredList<>(partSearchList, p -> true);
        searchPartField.textProperty().addListener((observable, oldValue, newValue) -> {
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
        partSortedList.comparatorProperty().bind(partTable.comparatorProperty());
        partTable.setItems(partSortedList);
         */

        //Populates productTable
        productId.setCellValueFactory(new PropertyValueFactory<Product, Integer>("id"));
        productName.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        productStock.setCellValueFactory(new PropertyValueFactory<Product, Integer>("stock"));
        productPrice.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
        productTable.setItems(Inventory.getAllProducts());

        //Filters productTable search results
        productSearchList.addAll(Inventory.getAllProducts());
        searchProductField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                productFilterList.clear();
                if (searchProductField.getText().isEmpty()) {
                    productTable.setItems(Inventory.getAllProducts());
                }
                else {
                    if (isNumeric(searchProductField.getText())) {
                        for (int i = 0; i < productSearchList.size(); i++) {
                            if (String.valueOf(productSearchList.get(i).getId()).contains(searchProductField.getText())) {
                                productFilterList.add(productSearchList.get(i));
                            }
                        }
                    }
                    else {
                        for (int i = 0; i < productSearchList.size(); i++) {
                            if (productSearchList.get(i).getName().contains(searchProductField.getText())) {
                                productFilterList.add(productSearchList.get(i));
                            }
                        }
                    }
                    if (productFilterList.isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Inventory Manager");
                        alert.setContentText("No parts found.");

                        alert.showAndWait();
                    }
                    else {
                        productTable.setItems(productFilterList);
                    }
                }
            }
        });

        /*
        productSearchList.addAll(Inventory.getAllProducts());
        FilteredList<Product> productFilteredList = new FilteredList<>(productSearchList, p -> true);
        searchProductField.textProperty().addListener((observable, oldValue, newValue) -> {
            productFilteredList.setPredicate(product -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (isNumeric(newValue)) {
                    if (String.valueOf(product.getId()).contains(newValue)) {
                        return true;
                    }
                }
                else if (product.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        SortedList<Product> productSortedList = new SortedList<>(productFilteredList);
        productSortedList.comparatorProperty().bind(productTable.comparatorProperty());
        productTable.setItems(productSortedList);
         */
    }

    //Main.fxml Listeners

    /**
     * Listener for addPartButton. Loads "AddPart.fxml" form.
     * @param actionEvent
     * @throws Exception
     */
    public void addPartButtonListener(ActionEvent actionEvent) throws Exception {
        Parent parent = FXMLLoader.load(getClass().getResource("AddPart.fxml"));
        Scene scene = new Scene(parent);
        Stage stage  = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Listener for modifyPartButton. Shows an error message if no Part is selected. Otherwise, loads "ModifyPart.fxml"
     * and gets the selected Part to populate the fields in "ModifyPart.fxml".
     * @param actionEvent
     * @throws Exception
     */
    public void modifyPartButtonListener(ActionEvent actionEvent) throws Exception {
        if (partTable.getSelectionModel().getSelectedItem() == null) {
            MessageWindowController.messageText = "Please select a part to modify.";
            Parent parent = FXMLLoader.load(getClass().getResource("MessageWindow.fxml"));
            Stage popUp = new Stage();
            Scene messageWindow = new Scene(parent);
            popUp.setTitle("Inventory Manager");
            popUp.setScene(messageWindow);
            popUp.setResizable(false);
            popUp.show();
        }
        else {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("ModifyPart.fxml"));
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            ModifyPartController controller = loader.getController();
            controller.modifyPartData(partTable.getSelectionModel().getSelectedItem());
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            System.out.println("Modifying " + partTable.getSelectionModel().getSelectedItem().getClass() + " part '" + partTable.getSelectionModel().getSelectedItem().getName() + "'.");
        }
    }

    /**
     * Listener for deletePartButton. Shows an error message if no Part is selected. Checks if partProtect() returns
     * true, in which case the Part is not allowed to be deleted. Otherwise, a delete confirmation Alert is displayed
     * and the Part is deleted if "Yes" is clicked.
     * @param actionEvent
     * @throws IOException
     */
    public void deletePartButtonListener(ActionEvent actionEvent) throws IOException {
        if (partTable.getSelectionModel().getSelectedItem() == null) {
            MessageWindowController.messageText = "Please select a part to delete.";
            Parent parent = FXMLLoader.load(getClass().getResource("MessageWindow.fxml"));
            Stage popUp = new Stage();
            Scene messageWindow = new Scene(parent);
            popUp.setTitle("Inventory Manager");
            popUp.setScene(messageWindow);
            popUp.setResizable(false);
            popUp.show();
        }
        else {
            if (partProtect(partTable.getSelectionModel().getSelectedItem())) {
                MessageWindowController.messageText = "Part cannot be deleted while associated with product.";
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
                alert.setContentText("Are you sure you want to delete the selected product?");

                ButtonType buttonTypeYes = new ButtonType("Yes");
                ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

                alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeCancel);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == buttonTypeYes) {
                    Part deletePart = partTable.getSelectionModel().getSelectedItem();
                    Inventory.deletePart(deletePart);
                    partTable.setItems(Inventory.getAllParts());
                }
            }
        }
    }

    /**
     * Listener for addProductButton. Loads "AddProduct.fxml" form.
     * @param actionEvent
     * @throws Exception
     */
    public void addProductButtonListener(ActionEvent actionEvent) throws Exception {
        Parent parent = FXMLLoader.load(getClass().getResource("AddProduct.fxml"));
        Scene scene = new Scene(parent);
        Stage stage  = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Listener for modifyProductButton. Shows an error message if no Product is selected. Otherwise, loads
     * "ModifyProduct.fxml" and gets the selected Product to populate the fields in "ModifyProduct.fxml".
     * @param actionEvent
     * @throws Exception
     */
    public void modifyProductButtonListener(ActionEvent actionEvent) throws Exception {
        if (productTable.getSelectionModel().getSelectedItem() == null) {
            MessageWindowController.messageText = "Please select a product to modify.";
            Parent parent = FXMLLoader.load(getClass().getResource("MessageWindow.fxml"));
            Stage popUp = new Stage();
            Scene messageWindow = new Scene(parent);
            popUp.setTitle("Inventory Manager");
            popUp.setScene(messageWindow);
            popUp.setResizable(false);
            popUp.show();
        }
        else {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("ModifyProduct.fxml"));
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            ModifyProductController controller = loader.getController();
            controller.modifyProductData(productTable.getSelectionModel().getSelectedItem());
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            System.out.println("Modifying product '" + productTable.getSelectionModel().getSelectedItem().getName() + "'.");
        }
    }

    /**
     * Listener for deleteProductButton. Shows an error message if no Product is selected. Checks if the selected
     * Product has any Parts associated with it, in which case the Product is not allowed to be deleted and an error
     * message is displayed. Otherwise, a delete confirmation Alert is displayed and the Product is deleted if "Yes"
     * is clicked.
     * @param actionEvent
     * @throws IOException
     */
    public void deleteProductButtonListener(ActionEvent actionEvent) throws IOException {
        if (productTable.getSelectionModel().getSelectedItem() == null) {
            MessageWindowController.messageText = "Please select a product to delete.";
            Parent parent = FXMLLoader.load(getClass().getResource("MessageWindow.fxml"));
            Stage popUp = new Stage();
            Scene messageWindow = new Scene(parent);
            popUp.setTitle("Inventory Manager");
            popUp.setScene(messageWindow);
            popUp.setResizable(false);
            popUp.show();

        }
        else {
            if (!productTable.getSelectionModel().getSelectedItem().getAllAssociatedParts().isEmpty()) {
                MessageWindowController.messageText = "Associated parts must be removed before product can be deleted.";
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
                alert.setContentText("Are you sure you want to delete the selected product?");

                ButtonType buttonTypeYes = new ButtonType("Yes");
                ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

                alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeCancel);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == buttonTypeYes) {
                    Product deleteProduct = productTable.getSelectionModel().getSelectedItem();
                    Inventory.deleteProduct(deleteProduct);
                    productTable.setItems(Inventory.getAllProducts());
                }
            }
        }
    }

    /**
     * Listener for exitButton. Displays a confirmation Alert. The program is closed if "Yes" is clicked.
     * @param actionEvent
     * @throws Exception
     */
    public void exitButtonListener(ActionEvent actionEvent) throws Exception {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Inventory Manager");
        alert.setContentText("Are you sure you want to exit?");

        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeYes) {
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.close();
        }
    }
}