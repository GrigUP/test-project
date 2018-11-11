package ru.grigorishin.database;

import ru.grigorishin.database.impl.ClientsDAO;
import ru.grigorishin.database.impl.OrdersDAO;
import ru.grigorishin.database.impl.ProductsDAO;
import ru.grigorishin.entity.Client;
import ru.grigorishin.entity.EntityType;
import ru.grigorishin.entity.Order;
import ru.grigorishin.entity.Product;

/**
 * Синглтон фабрика для создания экземпляров разных типов DAO
 */
public class DAOFactory {
    private static AbstractDAO<Order> ordersDaoInstance;
    private static AbstractDAO<Client> clientsDaoInstance;
    private static AbstractDAO<Product> productsDaoInstance;

    /**
     * Приватный метод для исключения создания экземпляров класса фабрики
     */
    private DAOFactory() { }

    /**
     * Статический метод для получения экземпляра DAO типа type,
     * где type - элемент из перечисления соответсвующий типу таблицы БД
     * @param type
     * @return
     * В зависимости от переданного типа возвращается определенный тип DAO.
     * В случае, если реализации DAO не найдено для определенного типа, возвращается null.
     */
    public static AbstractDAO getDao(EntityType type) {
        switch (type) {
            case ORDERS:
                if (ordersDaoInstance == null) {
                    ordersDaoInstance = new OrdersDAO();
                }
                return ordersDaoInstance;
            case CLIENTS:
                if (clientsDaoInstance == null) {
                    clientsDaoInstance = new ClientsDAO();
                }
                return clientsDaoInstance;
            case PRODUCTS:
                if (productsDaoInstance == null) {
                    productsDaoInstance = new ProductsDAO();
                }
                return productsDaoInstance;
        }

        return null;
    }
}
