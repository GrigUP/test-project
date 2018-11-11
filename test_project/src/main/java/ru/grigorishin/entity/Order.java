package ru.grigorishin.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Класс, в соответсвие экземпляру которого ставится строка в таблице orders
 * с присоединенными таблицами clients и products.
 * Определены все соответствующие поля, гетеры и сетеры для них.
 */
public class Order implements Entity {
    private int id;
    private Timestamp order_date;
    private int client_id;
    private String first_name;
    private String middle_name;
    private String last_name;
    private int product_id;
    private String product_name;
    private BigDecimal product_price;
    private int quantity;

    public Order() {
    }

    public Order(int id, Timestamp order_date, int client_id, int product_id, int quantity) {
        this.id = id;
        this.order_date = order_date;
        this.client_id = client_id;
        this.product_id = product_id;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getOrder_date() {
        return order_date;
    }

    public void setOrder_date(Timestamp order_date) {
        this.order_date = order_date;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public BigDecimal getProduct_price() {
        return product_price;
    }

    public void setProduct_price(BigDecimal product_price) {
        this.product_price = product_price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
