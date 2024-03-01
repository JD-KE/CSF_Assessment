package vttp.batch4.csf.ecommerce.controllers;


import java.io.StringReader;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import vttp.batch4.csf.ecommerce.Exceptions.SQLInsertionException;
import vttp.batch4.csf.ecommerce.models.Cart;
import vttp.batch4.csf.ecommerce.models.LineItem;
import vttp.batch4.csf.ecommerce.models.Order;
import vttp.batch4.csf.ecommerce.services.PurchaseOrderService;

@Controller
@CrossOrigin(origins = {"http://localhost:4200"})
@RequestMapping(path="/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {

  @Autowired
  private PurchaseOrderService poSvc;

  // IMPORTANT: DO NOT MODIFY THIS METHOD.
  // If this method is changed, any assessment task relying on this method will
  // not be marked
  
  @PostMapping(path="/order", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<String> postOrder(@RequestBody String payload) {
    JsonObjectBuilder builder = Json.createObjectBuilder();

    // TODO Task 3
    System.out.println("Order received");
    System.out.println(payload);
    JsonReader jsonReader = Json.createReader(new StringReader(payload));
    JsonObject orderObject = jsonReader.readObject();
    Order currentOrder = new Order();
    currentOrder.setName(orderObject.getString("name"));
    currentOrder.setAddress(orderObject.getString("address"));
    currentOrder.setPriority(orderObject.getBoolean("priority"));
    currentOrder.setComments(orderObject.getString("comments"));
    // currentOrder.setDate();
    Cart cart = new Cart();
    List<LineItem> lineItems = orderObject.getJsonArray("cart").stream()
      .map(j -> {
        JsonObject liObject = j.asJsonObject();
        LineItem li = new LineItem();
        li.setName(liObject.getString("name"));
        li.setPrice(Float.parseFloat(liObject.get("price").toString()));
        li.setQuantity(liObject.getInt("quantity"));
        li.setProductId(liObject.getString("prodId"));
        return li;
      }).toList();
    cart.setLineItems(lineItems);
    currentOrder.setCart(cart);

    try {
      poSvc.createNewPurchaseOrder(currentOrder);
    } catch (SQLInsertionException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      builder.add("message", e.getMessage());
      return ResponseEntity.status(400).body(builder.build().toString());
    }
    builder.add("orderId", currentOrder.getOrderId());
    
	 
	 return ResponseEntity.ok().body(builder.build().toString());
  }
}
