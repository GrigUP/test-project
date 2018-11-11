package ru.grigorishin.entity;

import java.math.BigDecimal;

/**
 * Класс, в соответсвие экземпляру которого ставится строка в таблице products.
 * Определены все соответствующие поля, гетеры и сетеры для них.
 */
public class Product implements Entity {
    private int id;
    private String product_name;
    private BigDecimal price;

    public Product() {
    }

    public Product(int id, String product_name, BigDecimal price) {
        this.id = id;
        this.product_name = product_name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
