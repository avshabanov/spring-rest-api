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

package com.alexshabanov.springrestapi;

import com.alexshabanov.springrestapi.rest.client.BankAccountRestClientService;
import com.alexshabanov.springrestapi.rest.client.UserRestClientService;
import com.alexshabanov.springrestapi.service.BankAccountService;
import com.alexshabanov.springrestapi.service.UserService;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Entry point.
 */
public final class App {
    private static final String APP_URL = "http://127.0.0.1:9090/server-app";


    public static void main(String[] args) {
        System.out.println("Client app demo, expecting server to be available at " + APP_URL);

        final ConfigurableApplicationContext applicationContext = new AnnotationConfigApplicationContext(App.Config.class);
        applicationContext.start();
        try {
            applicationContext.getBean(Runnable.class).run();
        } finally {
            applicationContext.close();
        }
    }

    public static class AppMain implements Runnable {
        @Inject
        private UserService userService;

        private static final String[] NAMES = { "alex", "alice", "ann", "bob", "boris", "cavin", "codd", "dan", "eva",
                "fred", "harry", "inna",  "james", "jane", "jeff", "kael", "laura", "mark", "neil", "ozzy", "peter",
                "richard", "suzannah", "trevor", "uma", "victor", "victoria", "wolf", "xena", "yvette", "zed"};

        private final Random random = new SecureRandom();

        @Override
        public void run() {
            // run application

            // [1] add user
            final int id = userService.register(NAMES[random.nextInt(NAMES.length)] + "#" + random.nextInt(10));
            System.out.println("Registered user with ID = " + id);

            // [2] get all the users
            System.out.println("Current users: " + userService.findAll());
        }
    }

    @Configuration
    public static class Config {

        @Bean
        public BankAccountService bankAccountService() {
            return new BankAccountRestClientService(APP_URL, restOperations());
        }

        @Bean
        public UserService userRestClientService() {
            return new UserRestClientService(APP_URL, restOperations());
        }

        @Bean
        public RestOperations restOperations() {
            return new RestTemplate();
        }

        @Bean
        public Runnable runner() {
            return new AppMain();
        }
    }
}
