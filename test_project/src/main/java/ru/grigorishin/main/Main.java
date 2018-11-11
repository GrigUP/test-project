package ru.grigorishin.main;

import ru.grigorishin.database.AbstractDAO;
import ru.grigorishin.database.DAOFactory;
import ru.grigorishin.entity.EntityType;
import ru.grigorishin.entity.Order;
import ru.grigorishin.services.TableWrapper;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // создание DAO заказов, используя фабрику
        AbstractDAO<Order> orderDao = DAOFactory.getDao(EntityType.ORDERS);

        // получение листа всех заказов
        List<Order> orderList = orderDao.readAll();

        // отображение листа заказов в табличном виде
        TableWrapper.print(orderList);
    }
}
