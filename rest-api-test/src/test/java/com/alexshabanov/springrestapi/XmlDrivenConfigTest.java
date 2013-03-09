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

import com.alexshabanov.springrestapi.support.ProfileController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;

import javax.inject.Inject;

import static com.alexshabanov.springrestapi.support.ProfileController.CONCRETE_PROFILE_RESOURCE;
import static com.alexshabanov.springrestapi.support.ProfileUtil.path;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Illustrates testing with xml-driven config.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/spring/XmlDrivenConfigTest-context.xml")
public final class XmlDrivenConfigTest {
    @Inject
    private RestOperations restClient;

    @Inject
    private ProfileController profileController;

    private final long id = 1;

    @Test
    public void shouldDeleteProfile() {
        doNothing().when(profileController).deleteProfile(id);
        restClient.delete(path(CONCRETE_PROFILE_RESOURCE), id);
        verify(profileController).deleteProfile(id);
    }

    @Test
    public void shouldThrowHttpClientErrorExceptionWithBadRequest() {
        doThrow(IllegalArgumentException.class).when(profileController).deleteProfile(id);
        try {
            restClient.delete(path(CONCRETE_PROFILE_RESOURCE), id);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        }
    }
}
