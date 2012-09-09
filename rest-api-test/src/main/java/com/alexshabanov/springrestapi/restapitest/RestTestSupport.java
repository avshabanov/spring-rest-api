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

package com.alexshabanov.springrestapi.restapitest;

import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * Provides interface for REST API testing support.
 */
public interface RestTestSupport {

    /**
     * Encapsulates handling of the HTTP servlet request.
     * <p/>
     * Implementation delegates request processing to the spring MVC's {@link org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter}.
     *
     * @param request Incoming HTTP servlet request, usually an instance of {@link org.springframework.mock.web.MockHttpServletRequest}.
     * @return Outcoming HTTP servlet response.
     */
    MockHttpServletResponse handle(HttpServletRequest request);
}
