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
import com.alexshabanov.springrestapi.support.Profile;
import com.alexshabanov.springrestapi.support.ProfileController;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestOperations;

import javax.inject.Inject;
import java.util.List;

import static com.alexshabanov.springrestapi.support.ProfileController.COMPLETE_PROFILE_RESOURCE;
import static com.alexshabanov.springrestapi.support.ProfileUtil.path;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    @Test
    public void shouldReturnExpectedProfile() {
        final int id = 1;
        final String name = "name";
        final Profile profile = new Profile(20, "alice");
        when(profileController.getProfile(id, name)).thenReturn(profile);

        final Profile actual = restClient.getForObject(path(COMPLETE_PROFILE_RESOURCE), Profile.class, id, name);
        assertEquals(profile, actual);
    }

    /**
     * Test configuration context
     */
    @Configuration
    public static class Config extends MockWebMvcConfig {

        @Override
        public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
            final MappingJacksonHttpMessageConverter jacksonHttpMessageConverter = new MappingJacksonHttpMessageConverter();
            jacksonHttpMessageConverter.getObjectMapper().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
            converters.add(jacksonHttpMessageConverter);
        }

        @Bean
        public ProfileController profileController() {
            return mock(ProfileController.class);
        }
    }
}
