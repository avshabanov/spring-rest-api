package com.alexshabanov.springrestapi.domain;

public final class User extends DomainObject {
    private int id;
    private String name;
    private BankAccount account;

    public User() {
    }

    public static User as(int id, String name) {
        final User user = new User();
        user.setId(id);
        user.setName(name);
        return user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BankAccount getAccount() {
        return account;
    }

    public void setAccount(BankAccount account) {
        this.account = account;
    }
}
