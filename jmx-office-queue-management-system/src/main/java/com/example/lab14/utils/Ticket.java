package com.example.lab14.utils;

public class Ticket implements TicketMBean {
    public final static int MAX_NUMBER = 9999;
    protected String category;
    protected Integer number;

    public Ticket() {

    }

    public Ticket(String category, Integer number) {
        this.category = category;
        this.number = number;
    }

    @Override
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return category + String.format("%04d", number);
    }
}
