import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

/**
 * Product class.
 */
public class Product {
    private List<Part> associatedParts = new ArrayList<Part>();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * Sets id.
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets name.
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets price.
     * @param price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Sets stock.
     * @param stock
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Sets min.
     * @param min
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Sets max.
     * @param max
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return price
     */
    public double getPrice() {
        return price;
    }

    /**
     *
     * @return stock
     */
    public int getStock() {
        return stock;
    }

    /**
     *
     * @return min
     */
    public int getMin() {
        return min;
    }

    /**
     *
     * @return max
     */
    public int getMax() {
        return max;
    }

    /**
     * Adds part to associatedParts array list.
     * @param part
     */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    /**
     * Removes part from associatedParts array list.
     * @param part
     * @return boolean
     */
    public boolean deleteAssociatedPart(Part part) {
        if (associatedParts.contains(part)) {
            associatedParts.remove(part);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Returns a public ObservableList to populate associatedPartsTable in "ModifyProduct.fxml" form.
     * @return ObservableList<Part>
     */
    public ObservableList<Part> getAllAssociatedParts() {
        ObservableList<Part> parts = FXCollections.observableArrayList();
        for (int i = 0; i < associatedParts.size(); i++) {
            parts.add(associatedParts.get(i));
        }
        return parts;
    }
}