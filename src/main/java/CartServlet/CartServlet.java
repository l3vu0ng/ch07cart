package CartServlet;

import java.io.IOException;
import java.util.ArrayList;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import murach.business.Cart;
import murach.business.LineItem;
import murach.business.Product;
import murach.data.ProductIO;

@WebServlet(name = "CartServlet", urlPatterns = {"/cart"}, loadOnStartup = 1)
public class CartServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        ServletContext sc = getServletContext();
        String path = sc.getRealPath("/WEB-INF/products.txt");
        ArrayList<Product> products = ProductIO.getProducts(path);
        sc.setAttribute("products", products);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        ServletContext sc = getServletContext();

        // get current action
        String action = request.getParameter("action");
        if (action == null) {
            action = "cart"; // default action
        }

        // perform action and set url to appropriate page
        String url = "/index.jsp";
        if (action.equals("shop")) {
            url = "/index.jsp";
            if (sc.getAttribute("products") == null) {
                String path = sc.getRealPath("/WEB-INF/products.txt");
                ArrayList<Product> products = ProductIO.getProducts(path);
                sc.setAttribute("products", products);
            }
        } else if (action.equals("cart")) {
            String productCode = request.getParameter("productCode");
            String quantityString = request.getParameter("quantity");

            HttpSession session = request.getSession();
            Cart cart = (Cart) session.getAttribute("cart");
            if (cart == null) {
                cart = new Cart();
            }

            String path = sc.getRealPath("/WEB-INF/products.txt");
            Product product = ProductIO.getProduct(productCode, path);

            if (product != null && productCode != null) {
                if (quantityString == null) {
                    // Clicked from index.html (Add to Cart)
                    boolean found = false;
                    for (LineItem cartItem : cart.getItems()) {
                        if (cartItem.getProduct().getCode().equalsIgnoreCase(productCode)) {
                            cartItem.setQuantity(cartItem.getQuantity() + 1);
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        LineItem lineItem = new LineItem();
                        lineItem.setProduct(product);
                        lineItem.setQuantity(1);
                        cart.addItem(lineItem);
                    }
                } else {
                    // Clicked from cart.jsp (Update or Remove)
                    int quantity;
                    try {
                        quantity = Integer.parseInt(quantityString);
                        if (quantity < 0) {
                            quantity = 1;
                        }
                    } catch (NumberFormatException nfe) {
                        quantity = 1;
                    }

                    LineItem lineItem = new LineItem();
                    lineItem.setProduct(product);
                    lineItem.setQuantity(quantity);
                    if (quantity > 0) {
                        cart.addItem(lineItem);
                    } else if (quantity == 0) {
                        cart.removeItem(lineItem);
                    }
                }
            }

            session.setAttribute("cart", cart);
            url = "/cart.jsp";
        } else if (action.equals("checkOut")) {
            url = "/checkout.jsp";
        }

        sc.getRequestDispatcher(url).forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
