package murach.business;

import java.io.Serializable;
import java.text.NumberFormat;

public class LineItem implements Serializable {

    private Product product;
    private int quantity;

    public LineItem() {
    }

    public void setProduct(Product p) {
        this.product = p;
    }

    public Product getProduct() {
        return product;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotal() {
        if (product != null) {
            return product.getPrice() * quantity;
        }
        return 0;
    }

    public String getTotalCurrencyFormat() {
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        return currency.format(this.getTotal());
    }
}
