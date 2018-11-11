package ru.grigorishin.database.impl;

import ru.grigorishin.database.AbstractDAO;
import ru.grigorishin.entity.Client;
import ru.grigorishin.exceprions.UncorrectTypeRuntimeException;

import java.sql.*;
import java.util.List;

/**
 * DAO взаимодействия с таблицей "clients" из БД, определенной
 * в абстрактном родительском классе.
 */
public class ClientsDAO extends AbstractDAO {
    private final String tableName = "clients";

    /**
     * Метод чтения из БД, определенной в абстрактном родительском
     * классе, строки с соответвующим значением id.
     * @param id
     * @return
     * Метод возвращает единственный Client объект, если такой существует,
     * либо null.
     */
    @Override
    public Client getById(int id) {
        Client client = null;

        String query = String.format("SELECT * FROM %s WHERE id=%d;", tableName, id);

        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery()) {

            if(resultSet.next()) {
                client = builtClientFromResultSet(resultSet);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return client;
    }

    /**
     * Метод чтения из БД, определенной в абстрактном родительском
     * классе, всех строк таблицы clients.
     * @return
     * Метод возвращает список Client объектов. Каждый объект списка соответсвует
     * строке таблицы.
     */
    @Override
    public List<Client> readAll() {
        List<Client> clientList = null;

        String query = String.format("SELECT * FROM %s;", tableName);

        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery()) {

            while(resultSet.next()) {
                Client newClient = builtClientFromResultSet(resultSet);
                clientList.add(newClient);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return clientList;
    }

    /**
     * Метод создания записи в БД объекта класса Client, используя
     * явное приведение типов.
     * @param obj
     * В случае, если объект obj не является объектом класса Client,
     * то выбрасывется unchecked UncorrectTypeExceprion исключение.
     */
    @Override
    public void create(Object obj) {
        if( !(obj instanceof Client) ) {
            throw new UncorrectTypeRuntimeException();
        }

        Client client = (Client) obj;

        String query = String.format("INSERT INTO %s VALUES(NULL, '%s', '%s', '%s');",
                tableName, client.getFirst_name(), client.getMiddle_name(), client.getLast_name());

        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Метод удаления записи, соответсвующей параметру id, из БД.
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
     * значениями полей объекта класса Client, используя явное приведение
     * типов
     * @param obj
     * В случае, если объект obj не является объектом класса Client,
     * то выбрасывется unchecked UncorrectTypeExceprion исключение.
     */
    @Override
    public void update(Object obj) {
        if( !(obj instanceof Client) ) {
            throw new UncorrectTypeRuntimeException();
        }
        Client client = (Client) obj;

        String query = String.format("UPDATE %s SET first_name='%s', middle_name='%s', last_name='%s' WHERE id=%d",
                tableName, client.getFirst_name(), client.getMiddle_name(), client.getLast_name(), client.getId());

        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Метод сборки объекта класса Client из элемента resultSet
     * @param resultSet
     * @return
     * Метод возвращает объект класса Client, соответсвующий строке в таблице БД.
     * @throws SQLException
     * В случае отсутсвия запрашиваемого к эллементу resultSet ключа
     * выбрасивается checked SQLExceprion исключение.
     */
    private Client builtClientFromResultSet(ResultSet resultSet) throws SQLException {
        Client client = new Client();
        client.setId(resultSet.getInt("id"));
        client.setLast_name(resultSet.getString("last_name"));
        client.setFirst_name(resultSet.getString("first_name"));
        client.setMiddle_name(resultSet.getString("middle_name"));

        return client;
    }
}
