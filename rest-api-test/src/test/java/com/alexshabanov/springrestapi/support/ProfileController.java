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

package com.alexshabanov.springrestapi.support;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * Controller that exposes REST API methods for working with {@link Profile} objects.
 */
@Controller
@RequestMapping(value = ProfileController.REST_API_METHOD_PREFIX)
public class ProfileController {

    public static final String REST_API_METHOD_PREFIX = "/rest/test";

    // Relative URLs for the exposed REST methods.
    public static final String COMPLETE_PROFILE_RESOURCE = "/profile/{id}/{name}";
    public static final String PROFILE_RESOURCE = "/profile";
    public static final String BAD_REQUEST_RESOURCE = "/bad-request";
    public static final String UNSUPPORTED_RESOURCE = "/unsupported";

    @RequestMapping(COMPLETE_PROFILE_RESOURCE)
    @ResponseBody
    public Profile getProfile(@PathVariable("id") int id, @PathVariable("name") String name) {
        return new Profile(id, name);
    }

    @RequestMapping(value = PROFILE_RESOURCE, method = RequestMethod.POST)
    @ResponseBody
    public Profile upgradeProfile(@RequestBody Profile profile) {
        return new Profile(profile.getId() * 2, profile.getName() + profile.getName());
    }

    @RequestMapping(value = BAD_REQUEST_RESOURCE)
    public Profile badRequest() {
        throw new IllegalArgumentException();
    }

    @RequestMapping(value = UNSUPPORTED_RESOURCE)
    public Profile unsupportedResource() {
        throw new UnsupportedOperationException();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public void handleIllegalArgumentException(HttpServletResponse response) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public void handleUnsupportedOperationException(HttpServletResponse response) {
        response.setStatus(HttpStatus.NOT_IMPLEMENTED.value());
    }
}
