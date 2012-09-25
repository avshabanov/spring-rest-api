package com.alexshabanov.springrestapi.service;

import com.alexshabanov.springrestapi.domain.BankAccount;

public interface BankAccountService {

    void updateAccount(int userId, BankAccount account);

    int registerAccount(BankAccount account);
}
