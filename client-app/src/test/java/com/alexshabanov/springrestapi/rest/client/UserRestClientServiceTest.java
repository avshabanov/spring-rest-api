package com.alexshabanov.springrestapi.rest.client;

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

    @Test
    public void shouldReturnExpectedProfile() {
        final int id = 1;
        final String name = "name";

        when(userServiceMock.register(name)).thenReturn(id);

        assertEquals(id, userServiceClient.register(name));
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
