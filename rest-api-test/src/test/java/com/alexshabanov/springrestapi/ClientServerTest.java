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

import com.alexshabanov.springrestapi.support.Profile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestOperations;

import javax.inject.Inject;

import static com.alexshabanov.springrestapi.support.ProfileController.*;
import static com.alexshabanov.springrestapi.support.ProfileUtil.path;
import static org.junit.Assert.assertEquals;

/**
 * Verifies that testing facilities introduced in the module works against the given spring version and sane.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/spring/ClientServerTest-context.xml")
public final class ClientServerTest {
    @Inject
    private RestOperations restClient;


    @Test
    public void shouldClientHandleGet() {
        final Profile expected = new Profile(1, "name");

        final Profile actual = restClient.getForObject(
                path(COMPLETE_PROFILE_RESOURCE),
                Profile.class,
                expected.getId(), expected.getName()
        );

        assertEquals(expected, actual);
    }

    @Test
    public void shouldClientHandlePost() {
        final Profile profile = new Profile(1, "name");
        final Profile expected = new Profile(profile.getId() * 2, profile.getName() + profile.getName());

        final Profile actual = restClient.postForObject(
                path(PROFILE_RESOURCE),
                profile,
                Profile.class
        );

        assertEquals(expected, actual);
    }

    @Test
    public void shouldThrowHttpClientErrorExceptionWithBadRequest() {
        try {
            restClient.getForObject(path(BAD_REQUEST_RESOURCE), Profile.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        }
    }

    @Test
    public void shouldReturnNotImplementedStatusCode() {
        try {
            restClient.getForEntity(path(UNSUPPORTED_RESOURCE), Profile.class);
        } catch (HttpServerErrorException e) {
            assertEquals(HttpStatus.NOT_IMPLEMENTED, e.getStatusCode());
        }
    }
}
