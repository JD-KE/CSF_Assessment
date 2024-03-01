package vttp.batch4.csf.ecommerce.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import vttp.batch4.csf.ecommerce.Exceptions.SQLInsertionException;
import vttp.batch4.csf.ecommerce.models.Cart;
import vttp.batch4.csf.ecommerce.models.LineItem;
import vttp.batch4.csf.ecommerce.models.Order;

@Repository
public class PurchaseOrderRepository {

  public static final String SQL_INSERT_ORDER = """
      insert into orders(order_id, name, address, priority, comments) values(?,?,?,?,?)
      """;

  public static final String SQL_INSERT_LINEITEM = """
      insert into lineitems(product_id, name, quantity, price, order_id) values(?,?,?,?,?)
      """;

  @Autowired
  private JdbcTemplate template;

  // IMPORTANT: DO NOT MODIFY THIS METHOD.
  // If this method is changed, any assessment task relying on this method will
  // not be marked
  // You may only add Exception to the method's signature
  public void create(Order order) throws SQLInsertionException {
    // TODO Task 3
    int orderRows = template.update(SQL_INSERT_ORDER, order.getOrderId(), order.getName(), order.getAddress(), order.getPriority(), order.getComments());

    if (orderRows <= 0) throw new SQLInsertionException("Order cannot be inserted");
    
    Cart cart = order.getCart();
    for (LineItem li : cart.getLineItems()) {
      int itemRows = template.update(SQL_INSERT_LINEITEM, li.getProductId(), li.getName(), li.getQuantity(), li.getPrice(), order.getOrderId());
      if (itemRows <= 0) throw new SQLInsertionException("Line Item cannot be inserted");
    }
  }
}
