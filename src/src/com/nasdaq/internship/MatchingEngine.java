package src.com.nasdaq.internship;

import java.util.List;

interface MatchingEngine {
    List<Trade> enterOrder(Order orderNew);
}
