package com.alexshabanov.springrestapi.domain;

public final class BankAccount extends DomainObject {
    private int id;
    private long money;
    private String code;

    public static BankAccount as(int id, long money, String code) {
        final BankAccount account = new BankAccount();
        account.setId(id);
        account.setMoney(money);
        account.setCode(code);
        return account;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
