package com.alexshabanov.springrestapi.rest.client;

import com.alexshabanov.springrestapi.domain.BankAccount;
import com.alexshabanov.springrestapi.domain.User;
import com.alexshabanov.springrestapi.rest.controller.RestController;
import com.alexshabanov.springrestapi.restapitest.config.MockWebMvcConfig;
import com.alexshabanov.springrestapi.service.UserService;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = UserRestClientServiceTest.Config.class)
public final class UserRestClientServiceTest {

    @Resource(name = "userServiceMock")
    private UserService userServiceMock;

    @Resource(name = "userServiceClient")
    private UserService userServiceClient;

    // test data
    private final User user = User.as(1, "name", BankAccount.as(1, 12L, "code"));

    @Test
    public void shouldRegisterUser() {
        when(userServiceMock.register(user.getName())).thenReturn(user.getId());
        assertEquals(user.getId(), userServiceClient.register(user.getName()));
    }

    @Test
    public void shouldFindUser() {
        when(userServiceMock.findById(user.getId())).thenReturn(user);
        assertEquals(user, userServiceClient.findById(user.getId()));
    }

    @Test
    public void shouldFindAllUsers() {
        final List<User> users = Arrays.asList(user, User.as(2, "name2"), user);
        when(userServiceMock.findAll()).thenReturn(users);
        assertEquals(users, userServiceClient.findAll());
    }


    /**
     * Test configuration context
     */
    @Configuration
    public static class Config extends MockWebMvcConfig {


        @Override
        public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
            converters.add(jacksonHttpMessageConverter());
        }

        @Bean
        public MappingJacksonHttpMessageConverter jacksonHttpMessageConverter() {
            final MappingJacksonHttpMessageConverter jacksonHttpMessageConverter =
                    new MappingJacksonHttpMessageConverter();
            jacksonHttpMessageConverter.getObjectMapper().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);

            return jacksonHttpMessageConverter;
        }

        @Bean(name = "userServiceMock")
        public UserService userServiceMock() {
            return mock(UserService.class);
        }

        @Bean(name = "userServiceClient")
        public UserService userServiceClient() {
            return new UserRestClientService("http://host/", testClient());
        }

        @Bean
        public RestController restController() {
            return new RestController(userServiceMock());
        }
    }
}
