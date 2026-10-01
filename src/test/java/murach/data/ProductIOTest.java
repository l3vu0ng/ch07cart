package murach.data;

import java.io.File;
import java.util.ArrayList;
import murach.business.Product;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProductIOTest {

    @Test
    public void testGetProductsFromFile() {
        File file = new File("src/main/webapp/WEB-INF/products.txt");
        assertTrue(file.exists(), "products.txt must exist");

        ArrayList<Product> products = ProductIO.getProducts(file.getAbsolutePath());
        assertNotNull(products, "Product list should not be null");
        assertEquals(4, products.size(), "There should be 4 products in products.txt");

        Product p1 = products.get(0);
        assertEquals("8601", p1.getCode());
        assertEquals("86 (the band) - True Life Songs and Pictures", p1.getDescription());
        assertEquals(14.95, p1.getPrice(), 0.001);

        Product p2 = products.get(1);
        assertEquals("pf01", p2.getCode());
        assertEquals("Paddlefoot - The first CD", p2.getDescription());
        assertEquals(12.95, p2.getPrice(), 0.001);
    }

    @Test
    public void testGetProductFromFile() {
        File file = new File("src/main/webapp/WEB-INF/products.txt");
        Product p = ProductIO.getProduct("pf02", file.getAbsolutePath());
        assertNotNull(p);
        assertEquals("pf02", p.getCode());
        assertEquals("Paddlefoot - The second CD", p.getDescription());
        assertEquals(14.95, p.getPrice(), 0.001);
    }

    @Test
    public void testGetProductsInvalidPath() {
        ArrayList<Product> emptyList = ProductIO.getProducts("non_existent_file.txt");
        assertNotNull(emptyList);
        assertTrue(emptyList.isEmpty());

        ArrayList<Product> nullPathList = ProductIO.getProducts(null);
        assertNotNull(nullPathList);
        assertTrue(nullPathList.isEmpty());
    }
}
