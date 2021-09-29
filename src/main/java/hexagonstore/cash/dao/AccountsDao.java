package hexagonstore.cash.dao;

import java.util.HashMap;

public class AccountsDao {

    public static HashMap<String, Double> accounts;

    static {
        accounts = new HashMap<>();
    }
}
