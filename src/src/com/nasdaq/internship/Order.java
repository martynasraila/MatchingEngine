package src.com.nasdaq.internship;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParsePosition;

public class Order implements Comparable<Order> {
    private String timeStamp;
    private BigInteger id;
    private String client;
    private Side side;
    private String stock;
    private BigDecimal quantity;
    private BigDecimal price;

    Order(String line) {
        String[] parts = line.split("[ @]");
        if ((parts.length != 5) && (parts.length != 8)) {
            throw new IllegalArgumentException("Incorrect number of parameters " + parts.length);
        }

        int offset = 0;
        if (parts.length == 8) {
            offset = 3;
            timeStamp = parts[0] + " " + parts[1];
            id = new BigInteger(parts[2]);
        } else {
            timeStamp = Environment.getCurrentTimeStamp();
            id = Environment.getNextOrderId();
        }

        client = parts[0 + offset];
        side = Side.valueOf(parts[1 + offset]);
        stock = parts[2 + offset];
        quantity = (BigDecimal) Environment.decimalFormat.parse(parts[3 + offset], new ParsePosition(0));
        price = (BigDecimal) Environment.decimalFormat.parse(parts[4 + offset], new ParsePosition(0));
    }

    public String toString() {
        return timeStamp + " " +
                id + " " +
                toStringShort();
    }

    public String toStringShort() {
        return client + " " +
                side + " " +
                stock + " " +
                Environment.decimalFormat.format(quantity) + "@" +
                Environment.decimalFormat.format(price);
    }

    void decrease(BigDecimal decrement) {
        if (decrement.signum() <= 0) {
            throw new IllegalArgumentException("Decrement <= 0");
        }
        if (decrement.compareTo(quantity) > 0) {
            throw new IllegalArgumentException("Decrement > Quantity");
        }
        quantity = quantity.subtract(decrement);
    }

    String getTimeStamp() {
        return timeStamp;
    }

    String getClient() {
        return client;
    }

    Side getSide() {
        return side;
    }

    String getStock() {
        return stock;
    }

    BigDecimal getQuantity() {
        return quantity;
    }

    BigDecimal getPrice() {
        return price;
    }

    BigInteger getId() {
        return id;
    }

    @Override
    public int compareTo(Order o) {
        
        int compare = stock.compareTo(o.stock);
        if(compare == 0)
        {
            compare= o.price.compareTo(price);
            if(compare==0)
            {
                compare = timeStamp.compareTo(o.timeStamp);
            }
        }
        return compare;
    }
    @Override
    public boolean equals(Object o) {
        Order other = (Order) o;
        
        return stock.equals(other.stock);
    }
}
