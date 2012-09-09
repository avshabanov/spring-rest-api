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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestOperations;

import javax.inject.Inject;

import static org.junit.Assert.*;

/**
 * Verifies that testing facilities introduced in the module works against the given spring version and sane.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/spring/ClientServerTest-context.xml")
public final class ClientServerTest {

    //private static final String CONTENT_TYPE = "application/json";

    private static final String REST_API_METHOD_PREFIX = "/rest/test";

    // Relative URLs for the exposed REST methods.
    private static final String GET_PROFILE = "/profile/{id}/{name}";

    private static final String PROFILE = "/profile";

    @Inject
    private RestOperations restClient;


    @Test
    public void shouldClientHandleGet() {
        final Profile expected = new Profile(1, "name");

        final Profile actual = restClient.getForObject(
                path(GET_PROFILE),
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
                path(PROFILE),
                profile,
                Profile.class
        );

        assertEquals(expected, actual);
    }


    private String path(String relativePath) {
        return "http://127.0.0.1" + REST_API_METHOD_PREFIX + relativePath;
    }

    /**
     * Test configuration context
     */
    @Configuration
    public static class Config {

    }

    /**
     * Controller for tests
     */
    @Controller
    @RequestMapping(value = REST_API_METHOD_PREFIX)
    public static final class TestController {

        @RequestMapping(GET_PROFILE)
        @ResponseBody
        public Profile getProfile(@PathVariable("id") int id, @PathVariable("name") String name) {
            return new Profile(id, name);
        }

        @RequestMapping(value = PROFILE, method = RequestMethod.POST)
        @ResponseBody
        public Profile upgradeProfile(@RequestBody Profile profile) {
            return new Profile(profile.id * 2, profile.getName() + profile.getName());
        }
    }

    // domain objects
    public static class Profile {
        private int id;
        private String name;

        public Profile() {}

        public Profile(int id, String name) {
            this();
            setId(id);
            setName(name);
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Profile profile = (Profile) o;

            return id == profile.id && !(name != null ? !name.equals(profile.name) : profile.name != null);

        }

        @Override
        public int hashCode() {
            int result = id;
            result = 31 * result + (name != null ? name.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "Profile{" +
                    "id=" + getId() +
                    ", name='" + getName() + '\'' +
                    '}';
        }
    }
}
