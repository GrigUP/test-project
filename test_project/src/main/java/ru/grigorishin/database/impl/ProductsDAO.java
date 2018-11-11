package ru.grigorishin.database.impl;

import ru.grigorishin.database.AbstractDAO;
import ru.grigorishin.entity.Product;
import ru.grigorishin.exceprions.UncorrectTypeRuntimeException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO взаимодействия с таблицей "product" из БД, определенной
 * в абстрактном родительском классе.
 */
public class ProductsDAO extends AbstractDAO {
    private final String tableName = "products";

    /**
     * Метод чтения из БД, определенной в абстрактном родительском
     * классе, всех строк таблицы products.
     * @return
     * Метод возвращает список Product объектов. Каждый объект списка соответсвует
     * строке таблицы.
     */
    @Override
    public List<Product> readAll() {
        List<Product> productList = new ArrayList<>();

        String query = String.format("SELECT * FROM %s;", tableName);

        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery()) {

            // для каждого элемента resultSet вызывается метод,
            // собирающий Product объект
            while(resultSet.next()) {
                Product newProduct = builtProductFromResultSet(resultSet);
                productList.add(newProduct);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return productList;
    }

    /**
     * Метод чтения из БД, определенной в абстрактном родительском
     * классе, строки с соответвующим значением id.
     * @param id
     * @return
     * Метод возвращает единственный Product объкет, если такой существует,
     * либо null.
     */
    @Override
    public Object getById(int id) {
        Product product = null;

        String query = String.format("SELECT * FROM %s WHERE id=%d;", tableName, id);

        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery()) {

            // для единственного элемента resultSet вызывается метод,
            // собирающий Product объект
            if(resultSet.next()) {
                product = builtProductFromResultSet(resultSet);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return product;
    }

    /**
     * Метод создания записи в БД объекта класса Product, используя
     * явное приведение типов.
     * @param obj
     * В случае, если объект obj не является объектом класса Product,
     * то выбрасывется unchecked UncorrectTypeExceprion исключение.
     */
    @Override
    public void create(Object obj) {
        if ( !(obj instanceof Product) ) {
            throw new UncorrectTypeRuntimeException();
        }
        Product product = (Product) obj;

        String query = String.format("INSERT INTO %s VALUES(NULL, '%s', '%s');",
                tableName, product.getProduct_name(), product.getPrice());

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
    @Override
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
     * значениями полей объекта класса Product, используя явное приведение
     * типов
     * @param obj
     * В случае, если объект obj не является объектом класса Product,
     * то выбрасывется unchecked UncorrectTypeExceprion исключение.
     */
    @Override
    public void update(Object obj) {
        if ( !(obj instanceof Product) ) {
            throw new UncorrectTypeRuntimeException();
        }

        Product product = (Product) obj;

        String query = String.format("UPDATE %s SET product_name='%s', product_price='%s' WHERE id=%d",
                tableName, product.getProduct_name(), product.getPrice(), product.getId());

        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Метод сборки объекта класса Product из элемента resultSet
     * @param resultSet
     * @return
     * Метод возвращает объект класса Product, соответсвующий строке в таблице БД.
     * @throws SQLException
     * В случае отсутсвия запрашиваемого к эллементу resultSet ключа
     * выбрасивается checked SQLExceprion исключение.
     */
    private Product builtProductFromResultSet(ResultSet resultSet) throws SQLException {
        Product product = new Product();
        product.setId(resultSet.getInt("id"));
        product.setProduct_name(resultSet.getString("product_name"));
        product.setPrice(resultSet.getBigDecimal("product_price"));

        return product;
    }
}
