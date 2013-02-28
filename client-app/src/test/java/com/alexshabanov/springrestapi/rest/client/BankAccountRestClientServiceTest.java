package com.alexshabanov.springrestapi.rest.client;

import com.alexshabanov.springrestapi.domain.BankAccount;
import com.alexshabanov.springrestapi.rest.controller.RestController;
import com.alexshabanov.springrestapi.restapitest.config.MockWebMvcConfig;
import com.alexshabanov.springrestapi.service.BankAccountService;
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
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BankAccountRestClientServiceTest.Config.class)
public final class BankAccountRestClientServiceTest {

    @Resource(name = "bankAccountServiceMock")
    private BankAccountService bankAccountServiceMock;

    @Resource(name = "bankAccountServiceClient")
    private BankAccountService bankAccountServiceClient;

    // test data
    private final BankAccount bankAccount = BankAccount.as(1, 12L, "code");

    @Test
    public void shouldUpdateAccount() {
        final int userId = 1;
        doNothing().when(bankAccountServiceMock).updateAccount(userId, bankAccount);

        bankAccountServiceClient.updateAccount(userId, bankAccount);

        verify(bankAccountServiceMock).updateAccount(userId, bankAccount);
    }

    @Test
    public void shouldRegisterAccount() {
        when(bankAccountServiceMock.registerAccount(bankAccount)).thenReturn(bankAccount.getId());
        assertEquals(bankAccount.getId(), bankAccountServiceClient.registerAccount(bankAccount));
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

        @Bean(name = "bankAccountServiceMock")
        public BankAccountService bankAccountServiceMock() {
            return mock(BankAccountService.class);
        }

        @Bean(name = "bankAccountServiceClient")
        public BankAccountService bankAccountServiceClient() {
            return new BankAccountRestClientService("http://host/", testClient());
        }

        @Bean
        public RestController restController() {
            return new RestController(mock(UserService.class), bankAccountServiceMock());
        }
    }
}
