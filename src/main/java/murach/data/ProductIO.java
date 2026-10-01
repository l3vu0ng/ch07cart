package murach.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import murach.business.Product;

public class ProductIO {

    public static ArrayList<Product> getProducts(String filepath) {
        ArrayList<Product> products = new ArrayList<>();
        if (filepath == null || filepath.trim().isEmpty()) {
            return products;
        }

        File file = new File(filepath);
        if (!file.exists()) {
            return products;
        }

        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = in.readLine()) != null) {
                StringTokenizer t = new StringTokenizer(line, "\t");
                if (t.countTokens() >= 3) {
                    String code = t.nextToken().trim();
                    String description = t.nextToken().trim();
                    String priceAsString = t.nextToken().trim();
                    try {
                        double price = Double.parseDouble(priceAsString);
                        Product p = new Product();
                        p.setCode(code);
                        p.setDescription(description);
                        p.setPrice(price);
                        products.add(p);
                    } catch (NumberFormatException e) {
                        // ignore malformed price line
                    }
                }
            }
        } catch (IOException e) {
            // return loaded products so far
        }
        return products;
    }

    public static Product getProduct(String productCode, String filepath) {
        if (productCode == null) {
            return null;
        }

        // Try reading from file first
        if (filepath != null) {
            ArrayList<Product> products = getProducts(filepath);
            for (Product p : products) {
                if (p.getCode() != null && p.getCode().equalsIgnoreCase(productCode)) {
                    return p;
                }
            }
        }

        // Fallback built-in catalog
        Product p = new Product();
        p.setCode(productCode);
        if (productCode.equalsIgnoreCase("8601")) {
            p.setDescription("86 (the band) - True Life Songs and Pictures");
            p.setPrice(14.95);
        } else if (productCode.equalsIgnoreCase("pf01")) {
            p.setDescription("Paddlefoot - The first CD");
            p.setPrice(12.95);
        } else if (productCode.equalsIgnoreCase("pf02")) {
            p.setDescription("Paddlefoot - The second CD");
            p.setPrice(14.95);
        } else if (productCode.equalsIgnoreCase("jr01")) {
            p.setDescription("Joe Rut - Genuine Wood Grained Finish");
            p.setPrice(14.95);
        } else {
            p.setDescription("Unknown Product");
            p.setPrice(0.0);
        }
        return p;
    }
}
