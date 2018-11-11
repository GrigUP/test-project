package ru.grigorishin.entity;

/**
 * Класс, в соответсвие экземпляру которого ставится строка в таблице сlients.
 * Определены все соответствующие поля, гетеры и сетеры для них.
 */
public class Client implements Entity {
    private int id;
    private String first_name;
    private String middle_name;
    private String last_name;

    public Client() {
    }

    public Client(int id, String first_name, String middle_name, String last_name) {
        this.id = id;
        this.first_name = first_name;
        this.middle_name = middle_name;
        this.last_name = last_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }
}
