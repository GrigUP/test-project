package ru.grigorishin.services;

import ru.grigorishin.entity.Entity;
import ru.grigorishin.exceprions.UncorrectCollectionLengthRuntimeException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Класс-сервис, статические методы которого используются для отображения
 * списков в табличном виде. Для реализации методов использовалось Reflection API.
 */
public class TableWrapper {
    private static String upperDelimeter = "_";
    private static String edgeDelimeter = "|";

    /**
     * Метод для представления объектов коллекции collectionOfObj в табличнов виде.
     * @param collectionOfObj
     */
    public static void print(Collection collectionOfObj) {
        if (collectionOfObj == null) {
            System.out.println("Невозможно отобразить страницу, возможно объект не существует.");
            return;
        }

        if (collectionOfObj.size() == 0) {
            throw new UncorrectCollectionLengthRuntimeException();
        }


        // получение имен колонок для "шапки" таблицы
        Collection collectionOfColumnName = getColumnNameCollection(collectionOfObj);

        // получение максимальной ширины для каждой из колонок
        int[] maxColumnLength = getMaxUnderArrayOfLength(getMaxColumnNameLength(collectionOfColumnName), getMaxObjLength(collectionOfObj));

        // "обертывание" коллекции в таблицу
        wrappToTable(collectionOfObj, collectionOfColumnName, maxColumnLength);

    }

    /**
     * Метод для представления объекта помеченного интерфейсом маркером Entity в табличнов виде.
     */
    public static void print(Entity entity) {
        if (entity == null) {
            System.out.println("Невозможно отобразить страницу, возможно объект не существует.");
            return;
        }

        List list = new ArrayList();
        list.add(entity);
        print(list);
    }

    /**
     * Статический метод для опеределения максимальных ширин для каждой из колонки
     * по объектам коллекции.
     * @param collection
     * @return
     * Возвращает массив целых чисел, соответсвующих максимальным значениям ширины
     * колонок среди объектов коллекции.
     */
    private static int[] getMaxObjLength(Collection collection) {
        Object[] collectionArray = collection.toArray();

        // получение массива объявленных полей для класса, содержавшегося в коллекции
        Field[] fields = collectionArray[0].getClass().getDeclaredFields();

        // предоставление доступа для чтения приватных полей
        Field.setAccessible(fields, true);

        int[] cellMaxLengths = new int[fields.length];
        Arrays.fill(cellMaxLengths, 0);

        // получение максимальной длины для колонки среди всех возможных объектов в коллекции
        for(Object obj:collectionArray) {
            for(int fieldIndex = 0; fieldIndex < fields.length; fieldIndex++) {
                try {
                    Object valueInObj = fields[fieldIndex].get(obj);
                    if (valueInObj.toString().length() > cellMaxLengths[fieldIndex]){
                        cellMaxLengths[fieldIndex] = valueInObj.toString().length();
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return cellMaxLengths;
    }

    /**
     * Статический метод для опеределения максимальных ширин для каждой из колонки
     * по полям объектов коллекции.
     * @param collection
     * @return
     * Возвращает массив целых чисел, соответсвующих максимальным значениям ширины
     * колонок среди полей объектов коллекции.
     */
    private static int[] getMaxColumnNameLength(Collection collection) {
        int[] cellMaxLengths = new int[collection.size()];
        Arrays.fill(cellMaxLengths, 0);

        Object[] collectionArray = collection.toArray();

        // получение максимальной длины для колонки среди всех возможных полей объекта в коллекции
        for(int index = 0; index < collectionArray.length; index++) {
            if(collectionArray[index].toString().length() > cellMaxLengths[index]) {
                cellMaxLengths[index] = collectionArray[index].toString().length();
            }
        }

        return cellMaxLengths;
    }

    /**
     * Статический метод, опеределяющий массив максимальных значений среди двух сравниваемых
     * массивов целых чисел. Значения массива соответсвуют ширине колонок таблицы.
     * @param array1
     * @param array2
     * @return
     * Возвращает массив целых чисел, соответсвующих максимальным значениям ширины
     * колонок для таблицы.
     */
    private static int[] getMaxUnderArrayOfLength(int[] array1, int[] array2) {
        if (array1.length != array2.length) return null;

        int[] resultArray = new int[array1.length];

        for(int indexOfArrays = 0; indexOfArrays < array1.length; indexOfArrays++) {
            resultArray[indexOfArrays] = Math.max(array1[indexOfArrays], array2[indexOfArrays]);
        }

        return resultArray;
    }

    /**
     * Статический метод, определяющий имена колонок создаваемой таблицы по именам
     * полей класса объектов, содержащихся в коллекции.
     * @param collection
     * @return
     * Возвращает коллекцию с именами колонок таблицы.
     */
    private static Collection getColumnNameCollection(Collection collection) {
        if (collection.size() == 0) {
            return null;
        }

        Object[] collectionArray = collection.toArray();

        // получение массива объявленных полей для класса, содержавшегося в коллекции
        Field[] fields = collectionArray[0].getClass().getDeclaredFields();

        Collection columnNameCollection = new ArrayList();

        // имя каждого из полей помещается в возвращаемую коллекцию
        for(Field field:fields) {
            columnNameCollection.add(field.getName());
        }

        return columnNameCollection;
    }

    /**
     * Статический метод, опередляющий алгоритм прорисовки таблицы.
     * @param dataOfTableCollection Коллекция объектов, заполняемых в таблицу.
     * @param headOfTableCollection Коллекция имен для колонок таблицы.
     * @param maxCellLengths Массив целых чисел широт каждого из столбцов.
     */
    private static void wrappToTable(Collection dataOfTableCollection, Collection headOfTableCollection, int[] maxCellLengths) {
        drawUpperDelimeter(maxCellLengths);
        drawHead(headOfTableCollection, maxCellLengths);
        drawUpperDelimeter(maxCellLengths);
        drawData(dataOfTableCollection, maxCellLengths);
        drawUpperDelimeter(maxCellLengths);
    }

    /**
     * Статический метод, отвечающий за отрисовку горизонтальных линий в таблице.
     * @param maxCellLenghts Массив целых чисел широт каждого из столбцов.
     */
    private static void drawUpperDelimeter(int[] maxCellLenghts) {
        int totalSizeOfRaw = 0;

        // получение общей ширины таблицы
        for (int length:maxCellLenghts) {
            totalSizeOfRaw += length+2;
        }

        // добавление к ширине количества вертикальных разделителей
        totalSizeOfRaw += maxCellLenghts.length+1;

        // циклическая отрисовка в консоли
        for(int index = 0; index < totalSizeOfRaw; index++) {
            System.out.print(upperDelimeter);
        }
        System.out.println();
    }

    /**
     * Статический метод, отвечающий за заполнение ячейки таблицы.
     * @param obj Данные для заполнения.
     * @param length Ширина столбца.
     */
    private static void drawOneCell(Object obj, int length) {
        System.out.print("|");
        String regex = "%" + (length+2) + "s";
        System.out.print(String.format(regex, obj));
    }

    /**
     * Статический метод, отвечающий за цикличное заполнения всех ячеек
     * таблицы данными из коллекции.
     * @param data Коллекция с данными.
     * @param maxCellLengths Массив целых чисел широт каждого из столбцов.
     */
    private static void drawData(Collection data, int[] maxCellLengths) {
        Object[] collectionArray = data.toArray();

        // получение массива объявленных полей для класса, содержавшегося в коллекции
        Field[] fields = collectionArray[0].getClass().getDeclaredFields();

        // предоставление доступа для чтения приватных полей
        Field.setAccessible(fields, true);

        // для каждого объекта коллекции, используя полученные с помощью рефлексии поля,
        // заполняются соответсвенные ячейки таблицы
        for(Object obj:collectionArray) {
            for(int fieldIndex = 0; fieldIndex < fields.length; fieldIndex++) {
                try {
                    Object valueInObj = fields[fieldIndex].get(obj);
                    drawOneCell(valueInObj, maxCellLengths[fieldIndex]);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            System.out.print(edgeDelimeter + "\n");
        }
    }

    /**
     * Статический метод, отвечающий за отрисовку имен колонок таблицы.
     * @param head Коллекция с именами колонок.
     * @param maxCellLength Массив целых чисел широт каждой из колонок.
     */
    private static void drawHead(Collection head, int[] maxCellLength) {
        Object[] headArray = head.toArray();

        for(int indexOfColumn = 0; indexOfColumn < headArray.length; indexOfColumn++) {
            drawOneCell(headArray[indexOfColumn], maxCellLength[indexOfColumn]);
        }
        System.out.print(edgeDelimeter + "\n");
    }
}
