/*
 * Copyright 2012 Alexander Shabanov - http://alexshabanov.com.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alexshabanov.springrestapi.rest.controller;

import com.alexshabanov.springrestapi.domain.BankAccount;
import com.alexshabanov.springrestapi.domain.User;
import com.alexshabanov.springrestapi.rest.common.InlineInt;
import com.alexshabanov.springrestapi.rest.common.InlineString;
import com.alexshabanov.springrestapi.service.BankAccountService;
import com.alexshabanov.springrestapi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

import static com.alexshabanov.springrestapi.rest.common.RestConstants.*;

/**
 * Exposes REST API methods for user api.
 */
@Controller
public final class RestController {

    private final UserService userService;

    private final BankAccountService bankAccountService;

    @Inject
    public RestController(UserService userService, BankAccountService bankAccountService) {
        Assert.notNull(userService, "userService can't be null");
        Assert.notNull(bankAccountService, "bankAccountService can't be null");

        this.userService = userService;
        this.bankAccountService = bankAccountService;
    }

    //
    // UserService exposure
    //

    @RequestMapping(value = REGISTER_USER_URI, method = RequestMethod.POST)
    @ResponseBody
    public InlineInt registerUser(@RequestBody InlineString name) {
        return InlineInt.as(userService.register(name.getValue()));
    }

    @RequestMapping(FIND_USER_BY_ID_URI)
    @ResponseBody
    public User findUserById(@PathVariable("id") int id) {
        return userService.findById(id);
    }

    @RequestMapping(FIND_ALL_USERS_URI)
    @ResponseBody
    public List<User> findAllUsers() {
        return userService.findAll();
    }


    //
    // BankAccountService exposure
    //

    @RequestMapping(value = UPDATE_BANK_ACCOUNT_URI, method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Void> updateBankAccount(@PathVariable("id") int userId, @RequestBody BankAccount account) {
        bankAccountService.updateAccount(userId, account);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = REGISTER_BANK_ACCOUNT_URI, method = RequestMethod.POST)
    @ResponseBody
    public InlineInt registerBankAccount(@RequestBody BankAccount account) {
        return InlineInt.as(bankAccountService.registerAccount(account));
    }
}
