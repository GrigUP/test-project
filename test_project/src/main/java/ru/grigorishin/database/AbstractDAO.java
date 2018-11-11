package ru.grigorishin.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

/**
 * Абстрактный класс для реализайций DAO, имеет обобщенный тип.
 * @param <E>
 */
public abstract class AbstractDAO<E> {

    // общие параметры для подключения к БД mySQL на локальной сервере
    protected final String databaseUrl = "jdbc:mysql://localhost:3306/internet_shop";
    protected final String login = "root";
    protected final String password = "root";

    public abstract List<E> readAll();

    public abstract E getById(int id);

    public abstract void create(E obj);
//
    public abstract void deleteById(int id);
//
    public abstract void update(E obj);

    // общий для всех потомков не переопределяемый метод получения коннекта к БД
    protected final Connection getConnection() {
        Connection resultConnection = null;
        try {
            resultConnection = DriverManager.getConnection(databaseUrl, login, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultConnection;
    }
}
