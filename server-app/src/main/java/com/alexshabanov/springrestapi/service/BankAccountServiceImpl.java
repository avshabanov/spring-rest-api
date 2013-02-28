package com.alexshabanov.springrestapi.service;

import com.alexshabanov.springrestapi.domain.BankAccount;
import org.springframework.stereotype.Service;

/**
 * Default implementation of the {@link BankAccountService}
 *
 * @author Alexander Shabanov
 */
@Service
public final class BankAccountServiceImpl implements BankAccountService {
    @Override
    public void updateAccount(int userId, BankAccount account) {
        // ok!
    }

    @Override
    public int registerAccount(BankAccount account) {
        // ok!
        return (int) System.currentTimeMillis();
    }
}
