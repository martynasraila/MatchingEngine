package src.com.nasdaq.internship;

import static java.lang.Math.abs;
import java.math.BigDecimal;
import java.util.*;
import static src.com.nasdaq.internship.Process.buy;
import static src.com.nasdaq.internship.Process.sell;

class MyEngine implements MatchingEngine {

    MyEngine() {
    }

    /**
     * Finds biggest order which matches criteria of new buy order and returns
     * its index
     *
     * @param stockList list to look in for a match
     * @param wantedStock order of match to be found
     * @return index of found matching order
     */
    public static int findMatch(List<Order> stockList, Order wantedStock) {
        Collections.sort(stockList);
        int index = -1;
        int indextemp = -1;
        for (Order order : stockList) {
            indextemp++;
            if (order.equals(wantedStock) && (order.getPrice().doubleValue() >= wantedStock.getPrice().doubleValue())) {
                return indextemp;
            }
        }
        return index;
    }

    /**
     * Finds biggest order which matches criteria of new sell order and returns
     * its index
     *
     * @param stockList list to look in for a match
     * @param wantedStock order of match to be found
     * @return index of found matching order
     */
    public static int findMatchSell(List<Order> stockList, Order wantedStock) {
        Collections.sort(stockList);
        int index = -1;
        int indextemp = -1;
        for (Order order : stockList) {
            indextemp++;
            if (order.equals(wantedStock) && (order.getPrice().doubleValue() <= wantedStock.getPrice().doubleValue())) {
                return indextemp;
            }
        }
        return index;
    }

    /**
     * Checks if there's matching order to the buy Order in the sellOrder List
     *
     * @param sellOrder given sell order
     * @param trades pending buy Order List
     * @return matching trades list
     */
    public static List<Trade> matchMethodSell(Order sellOrder, List<Trade> trades) {
        if (buy.contains(sellOrder)) {
            int index = findMatch(buy, sellOrder);
            if (index >= 0) {
                Order matching = buy.get(index);
                int quantity = (matching.getQuantity().intValue() - sellOrder.getQuantity().intValue());
                BigDecimal quantityBD = new BigDecimal(quantity);
                if (quantity > 0) {
                    sell.remove(sell.size() - 1);
                    Trade trade = new Trade(sellOrder, matching, sellOrder.getQuantity(), matching.getPrice());
                    trades.add(trade);
                    matching.decrease(sellOrder.getQuantity());
                    buy.set(index, matching);
                }
                if (quantity <= 0) {
                    buy.remove(index);
                    int left = (sellOrder.getQuantity().intValue() - abs(quantity));
                    BigDecimal leftBD = new BigDecimal(left);
                    Trade trade = new Trade(sellOrder, matching, leftBD, matching.getPrice());
                    trades.add(trade);
                    sellOrder.decrease(leftBD);
                    matchMethodSell(sellOrder, trades);
                }
            }
        }
        return trades;
    }

    /**
     * Checks if there's matching order to the sell Order in the buyOrder List
     *
     * @param buyOrder given buy order
     * @param trades pending sell Order List
     * @return matching trades list
     */
    public static List<Trade> matchMethodBuy(Order buyOrder, List<Trade> trades) {
        if (sell.contains(buyOrder)) {
            int index = findMatchSell(sell, buyOrder);
            if (index >= 0) {
                Order matching = sell.get(index);
                int quantity = (matching.getQuantity().intValue() - buyOrder.getQuantity().intValue());
                BigDecimal quantityBD = new BigDecimal(quantity);
                if (quantity > 0) {

                    buy.remove(buy.size() - 1);
                    Trade trade = new Trade(matching, buyOrder, buyOrder.getQuantity(), matching.getPrice());
                    trades.add(trade);
                    matching.decrease(buyOrder.getQuantity());
                    sell.set(index, matching);
                }
                if (quantity <= 0) {
                    sell.remove(index);
                    int left = (buyOrder.getQuantity().intValue() - abs(quantity));
                    BigDecimal leftBD = new BigDecimal(left);
                    Trade trade = new Trade(matching, buyOrder, leftBD, matching.getPrice());
                    trades.add(trade);
                    buyOrder.decrease(leftBD);
                    matchMethodBuy(buyOrder, trades);
                }
            }
        }
        return trades;
    }

    /**
     * Adds to different lists, based of side type.
     *
     * @param order
     * @return new trades generated from new order list
     */
    public List<Trade> enterOrder(Order order) {
        List<Trade> trades = new ArrayList<>();

        if (order.getSide() == Side.BUY) {
            List<Trade> tradestemp = new ArrayList<>();
            buy.add(order);
            trades = MyEngine.matchMethodBuy(order, tradestemp);
        } else {
            List<Trade> tradestemp = new ArrayList<>();
            sell.add(order);
            trades = MyEngine.matchMethodSell(order, tradestemp);
        }

        return trades;
    }
}
