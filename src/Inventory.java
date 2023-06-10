import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

/**
 * Inventory class.
 */
public class Inventory {

    /**
     * Array lists for Parts and Products.
     */
    private static List<Part> allParts = new ArrayList<Part>();
    private static List<Product> allProducts = new ArrayList<Product>();

    /**
     * Adds a new Part to allParts.
     * @param newPart
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * Adds a new Product to allProducts.
     * @param newProduct
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * Used to search for a specific Part by partId.
     * @param partId
     * @return Part
     */
    public static Part lookupPart(int partId) {
        Part part = null;

        for (int i = 0; i < allParts.size(); i++) {
            if (allParts.get(i).getId() == partId) {
                part = allParts.get(i);
            }
        }

        return part;
    }

    /**
     * Used to search for a specific Product by productId.
     * @param productId
     * @return Part
     */
    public static Product lookupProduct(int productId) {
        Product product = null;

        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getId() == productId) {
                product = allProducts.get(i);
            }
        }

        return product;
    }

    /**
     * Used to search for a specific Part by partName.
     * @param partName
     * @return Part
     */
    public static Part lookupPart(String partName) {
        Part part = null;

        for (int i = 0; i < allParts.size(); i++) {
            if (allParts.get(i).getName() == partName.toLowerCase()) {
                part = allParts.get(i);
            }
        }

        return part;
    }

    /**
     * Used to search for a specific Product by productName.
     * @param productName
     * @return Part
     */
    public static Product lookupProduct(String productName) {
        Product product = null;

        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getName() == productName.toLowerCase()) {
                product = allProducts.get(i);
            }
        }

        return product;
    }

    /**
     * Updates selectedPart when modified.
     * @param index
     * @param selectedPart
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     * Updates selectedProduct when modified.
     * @param index
     * @param selectedProduct
     */
    public static void updateProduct(int index, Product selectedProduct) {
        allProducts.set(index, selectedProduct);
    }

    /**
     * Removes selectedPart from allParts.
     * @param selectedPart
     * @return
     */
    public static boolean deletePart(Part selectedPart) {
        if (allParts.contains(selectedPart)) {
            allParts.remove(selectedPart);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Removes selectedProduct from allProducts.
     * @param selectedProduct
     * @return
     */
    public static boolean deleteProduct(Product selectedProduct){
        if (allProducts.contains(selectedProduct)) {
            allProducts.remove(selectedProduct);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Returns a public ObservableList to populate partTable in "Main.fxml" form.
     * @return ObservableList<Part>
     */
    public static ObservableList<Part> getAllParts() {
        ObservableList<Part> parts = FXCollections.observableArrayList();
        for (int i = 0; i < allParts.size(); i++) {
            parts.add(allParts.get(i));
        }
        return parts;
    }

    /**
     * Returns a public ObservableList to populate productTable in "Main.fxml" form.
     * @return ObservableList<Product>
     */
    public static ObservableList<Product> getAllProducts() {
        ObservableList<Product> products = FXCollections.observableArrayList();
        for (int i = 0; i < allProducts.size(); i++) {
            products.add(allProducts.get(i));
        }
        return products;
    }
}
