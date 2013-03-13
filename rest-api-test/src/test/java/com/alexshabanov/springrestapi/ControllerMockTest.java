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

import com.alexshabanov.springrestapi.restapitest.config.MockWebMvcConfig;
import com.alexshabanov.springrestapi.support.ErrorDesc;
import com.alexshabanov.springrestapi.support.Profile;
import com.alexshabanov.springrestapi.support.ProfileController;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestOperations;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

import static com.alexshabanov.springrestapi.support.ProfileController.*;
import static com.alexshabanov.springrestapi.support.ProfileUtil.path;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Tests controller mock.
 * Illustrates usage of class-based config for testing as well.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ControllerMockTest.Config.class)
public class ControllerMockTest {

    @Inject
    private ProfileController profileController;

    @Inject
    private RestOperations restClient;

    @Inject
    private MappingJacksonHttpMessageConverter jacksonHttpMessageConverter;

    private final int id = 1;
    private final Profile profile = new Profile(20, "alice");
    private final ErrorDesc errorDesc = new ErrorDesc(1, "reason");

    @Before
    public void resetMock() {
        reset(profileController);
    }

    @Test
    public void shouldReturnExpectedProfile() {
        final String name = "name";
        when(profileController.getProfile(id, name)).thenReturn(profile);
        assertEquals(profile, restClient.getForObject(path(COMPLETE_PROFILE_RESOURCE), Profile.class, id, name));
    }

    @Test
    public void shouldUpgradeProfile() {
        final Profile profile2 = new Profile(30, "bob");
        when(profileController.upgradeProfile(profile)).thenReturn(profile2);
        assertEquals(profile2, restClient.postForObject(path(PROFILE_RESOURCE), profile, Profile.class));
    }

    @Test
    public void shouldDeleteProfile() {
        doNothing().when(profileController).deleteProfile(id);
        restClient.delete(path(CONCRETE_PROFILE_RESOURCE), id);
        verify(profileController).deleteProfile(id);
    }

    @Test
    public void shouldPutProfile() {
        doNothing().when(profileController).putProfile(id, profile);
        restClient.put(path(CONCRETE_PROFILE_RESOURCE), profile, id);
        verify(profileController).putProfile(id, profile);
    }

    @Test
    public void shouldPutQueryParam() {
        final long a = 1;
        final long b = 2;
        final int c = 3;
        doNothing().when(profileController).putQueryParam(a, b, c);
        restClient.put(path(PROFILE_RESOURCE + "?a={a}&b={b}&c={c}"), null, a, b, c);
        verify(profileController).putQueryParam(a, b, c);
    }

    @Test
    public void shouldPutQueryParamHandleOmittedParams() {
        final int c = 1;
        doNothing().when(profileController).putQueryParam(null, null, c);
        restClient.put(path(PROFILE_RESOURCE + "?a={a}&b={b}&c={c}"), null, null, null, c);
        verify(profileController).putQueryParam(null, null, c);
    }

    @Test
    public void shouldGetBadRequest() throws IOException {
        doThrow(IllegalArgumentException.class).when(profileController).deleteProfile(id);
        when(profileController.handleIllegalArgumentException()).thenReturn(errorDesc);

        try {
            restClient.delete(path(CONCRETE_PROFILE_RESOURCE), id);
            fail();
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            final ErrorDesc actual = getObjectMapper().reader(ErrorDesc.class).readValue(e.getResponseBodyAsString());
            assertEquals(errorDesc, actual);
        }
    }

    @Test
    public void shouldGetInternalServerError() throws IOException {
        doThrow(UnsupportedOperationException.class).when(profileController).deleteProfile(id);
        when(profileController.handleUnsupportedOperationException()).thenReturn(errorDesc);

        try {
            restClient.delete(path(CONCRETE_PROFILE_RESOURCE), id);
            fail();
        } catch (HttpServerErrorException e) {
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, e.getStatusCode());
            final ErrorDesc actual = getObjectMapper().reader(ErrorDesc.class).readValue(e.getResponseBodyAsString());
            assertEquals(errorDesc, actual);
        }
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
            final MappingJacksonHttpMessageConverter converter = new MappingJacksonHttpMessageConverter();
            converter.getObjectMapper().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
            return converter;
        }

        @Bean
        public ProfileController profileController() {
            return mock(ProfileController.class);
        }
    }

    private ObjectMapper getObjectMapper() {
        return jacksonHttpMessageConverter.getObjectMapper();
    }
}
