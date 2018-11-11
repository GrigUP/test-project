package ru.grigorishin.database.impl;

import ru.grigorishin.database.AbstractDAO;
import ru.grigorishin.entity.Order;
import ru.grigorishin.exceprions.UncorrectTypeRuntimeException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO взаимодействия с таблицей "orders" из БД, определенной
 * в абстрактном родительском классе.
 */
public class OrdersDAO extends AbstractDAO {
    private final String tableName = "orders";
    private final String joinClientsTableName = "clients";
    private final String joinCatalogTableName = "products";

    /**
     * Метод чтения из БД, определенной в абстрактном родительском
     * классе, строки с соответвующим значением id.
     * @param id
     * @return
     * Метод возвращает единственный Order объкет, если такой существует,
     * либо null.
     */
    @Override
    public Order getById(int id) {
        Order order = null;

        String query = String.format("SELECT \n" +
                "    %1$s.id,\n" +
                "    %1$s.order_date,\n" +
                "    %1$s.client_id,\n" +
                "    %2$s.last_name,\n" +
                "    %2$s.first_name,\n" +
                "    %2$s.middle_name,\n" +
                "    %1$s.product_id,\n" +
                "    %3$s.product_name,\n" +
                "    %3$s.product_price,\n" +
                "    %1$s.quantity,\n" +
                "    %3$s.product_price*%1$s.quantity as 'total price'\n" +
                "    FROM\n" +
                "    %1$s\n" +
                "        JOIN\n" +
                "    %2$s ON %1$s.client_id = %2$s.id\n" +
                "        JOIN \n" +
                "    %3$s ON %1$s.product_id = %3$s.id\n" +
                "    WHERE orders.id=%4$d;", tableName, joinClientsTableName, joinCatalogTableName, id);

        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery()) {

            if(resultSet.next()) {
                order  = builtOrderFromResultSet(resultSet);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return order;
    }

    /**
     * Метод чтения из БД, определенной в абстрактном родительском
     * классе, всех строк таблицы order с присоединенными таблицами client и products.
     * @return
     * Метод возвращает список Order объектов. Каждый объект списка соответсвует
     * строке комбинированной таблицы.
     */
    @Override
    public List<Order> readAll() {
        List<Order> orderList = new ArrayList<>();

        String query = String.format("SELECT \n" +
                "    %1$s.id,\n" +
                "    %1$s.order_date,\n" +
                "    %1$s.client_id,\n" +
                "    %2$s.last_name,\n" +
                "    %2$s.first_name,\n" +
                "    %2$s.middle_name,\n" +
                "    %1$s.product_id,\n" +
                "    %3$s.product_name,\n" +
                "    %3$s.product_price,\n" +
                "    %1$s.quantity,\n" +
                "    %3$s.product_price*%1$s.quantity as 'total price'\n" +
                "   FROM\n" +
                "    %1$s\n" +
                "       JOIN\n" +
                "    %2$s ON %1$s.client_id = %2$s.id\n" +
                "       JOIN \n" +
                "   %3$s ON %1$s.product_id = %3$s.id\n" +
                "    ORDER BY order_date;", tableName, joinClientsTableName, joinCatalogTableName);

        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery()) {

            while(resultSet.next()) {
                Order newOrder = builtOrderFromResultSet(resultSet);
                orderList.add(newOrder);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return orderList;
    }

    /**
     * Метод создания записи в БД объекта класса Order, используя
     * явное приведение типов.
     * @param obj
     * В случае, если объект obj не является объектом класса Product,
     * то выбрасывется unchecked UncorrectTypeExceprion исключение.
     */
    @Override
    public void create(Object obj) {
        if ( !(obj instanceof Order) ) {
            throw new UncorrectTypeRuntimeException();
        }
        Order order = (Order) obj;

        String query = String.format("INSERT INTO %s VALUES(NULL, '%s', %d, %d, %d);",
                tableName, order.getOrder_date(), order.getClient_id(), order.getProduct_id(), order.getQuantity());

        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Метод удаления записи, соответсвубщей параметру id, из БД.
     * @param id
     */
    public void deleteById(int id) {
        String query = String.format("DELETE FROM %s WHERE id=%d;", tableName, id);

        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Метод обновления строки, соответсвующей параметру id, в БД
     * значениями полей объекта класса Order, используя явное приведение
     * типов
     * @param obj
     * В случае, если объект obj не является объектом класса Order,
     * то выбрасывется unchecked UncorrectTypeExceprion исключение.
     */
    @Override
    public void update(Object obj) {
        if ( !(obj instanceof Order) ) {
            throw new UncorrectTypeRuntimeException();
        }

        Order order = (Order) obj;

        String query = String.format("UPDATE %s SET order_date='%s', client_id='%d', product_id='%d', quantity='%d' WHERE id='%d'",
                tableName, order.getOrder_date(), order.getClient_id(), order.getProduct_id(), order.getQuantity(), order.getId());

        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Метод сборки объекта класса Order из элемента resultSet
     * @param resultSet
     * @return
     * Метод возвращает объект класса Order, соответсвующий строке в таблице БД.
     * @throws SQLException
     * В случае отсутсвия запрашиваемого к эллементу resultSet ключа
     * выбрасивается checked SQLExceprion исключение.
     */
    private Order builtOrderFromResultSet(ResultSet resultSet) throws SQLException {
        Order order = new Order();
        order.setId(resultSet.getInt("id"));
        order.setOrder_date(resultSet.getTimestamp("order_date"));
        order.setClient_id(resultSet.getInt("client_id"));
        order.setLast_name(resultSet.getString("last_name"));
        order.setFirst_name(resultSet.getString("first_name"));
        order.setMiddle_name(resultSet.getString("middle_name"));
        order.setProduct_id(resultSet.getInt("product_id"));
        order.setProduct_name(resultSet.getString("product_name"));
        order.setProduct_price(resultSet.getBigDecimal("product_price"));
        order.setQuantity(resultSet.getInt("quantity"));

        return order;
    }

}
