package com.buesimples.posfx.services;

import com.buesimples.posfx.models.Checkout;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sienekib
 */
public class CartService {

    private static CartService instance;
    private List<Checkout> checkoutList;

    private CartService() {
        checkoutList = new  ArrayList<>();
    }

    public static CartService getInstance() {
        if (instance == null) {
            instance = new CartService();
        }
        return instance;
    }

    public void addItem(Checkout item) {
        checkoutList.add(item);
    }

    public void removeItem(int idArtigo) {
        checkoutList.removeIf(item -> item.getId() == idArtigo);
    }

    public List<Checkout> getItems() {
        return checkoutList;
    }

    public void clearCart() {
        checkoutList.clear();
    }
}
