package com.alexshabanov.springrestapi.rest.client;

import com.alexshabanov.springrestapi.domain.BankAccount;
import com.alexshabanov.springrestapi.rest.common.InlineInt;
import com.alexshabanov.springrestapi.rest.common.RestConstants;
import com.alexshabanov.springrestapi.service.BankAccountService;
import org.springframework.web.client.RestOperations;

/**
 * Represents REST client implementation over the {@link BankAccountService} facade.
 */
public final class BankAccountRestClientService extends AbstractRestClientService implements BankAccountService {

    public BankAccountRestClientService(String baseUrl, RestOperations restOperations) {
        super(baseUrl, restOperations);
    }

    @Override
    public void updateAccount(int userId, BankAccount account) {
        getRestOperations().put(getMethodUri(RestConstants.UPDATE_BANK_ACCOUNT_URI), account, userId);
    }

    @Override
    public int registerAccount(BankAccount account) {
        return getRestOperations().postForObject(getMethodUri(RestConstants.REGISTER_BANK_ACCOUNT_URI),
                account, InlineInt.class).getValue();
    }
}
