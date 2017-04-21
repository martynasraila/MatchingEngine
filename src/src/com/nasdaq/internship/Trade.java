package src.com.nasdaq.internship;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Trade {
    private String timeStamp;
    private BigInteger id;
    private String seller;
    private String buyer;
    private String stock;
    private BigDecimal quantity;
    private BigDecimal price;

    Trade(Order orderSell, Order orderBuy, BigDecimal quantity, BigDecimal price) {

//        //Validation removed in order to simplify the assignment
//        if (orderBuy.getClient().equals(orderSell.getClient()))
//            throw new IllegalArgumentException("BUY and SELL client is the same");

        if (orderBuy.getSide() != Side.BUY || orderSell.getSide() != Side.SELL) {
            throw new IllegalArgumentException("Wrong side");
        }
        if (!orderBuy.getStock().equals(orderSell.getStock())) {
            throw new IllegalArgumentException("Stock names do not match");
        }
        if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantity <= 0");
        }
        if (orderBuy.getQuantity().compareTo(quantity) < 0 || orderSell.getQuantity().compareTo(quantity) < 0) {
            throw new IllegalArgumentException("Quantity too high");
        }
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price <= 0");
        }
        if (orderBuy.getPrice().compareTo(price) < 0) {
            throw new IllegalArgumentException("Price too high");
        }
        if (orderSell.getPrice().compareTo(price) > 0) {
            throw new IllegalArgumentException("Price too low");
        }
        this.timeStamp = Environment.getCurrentTimeStamp();
        this.id = Environment.getNextOrderId();
        this.buyer = orderBuy.getClient();
        this.seller = orderSell.getClient();
        this.stock = orderBuy.getStock();
        this.quantity = quantity;
        this.price = price;
    }

    public String toString() {
        return timeStamp + " " +
                id + " " +
                toStringShort();
    }

    public String toStringShort() {
        return seller + " -> " +
                buyer + " " +
                stock + " " +
                quantity + "@" +
                price;
    }

    BigDecimal getQuantity() {
        return quantity;
    }
}
