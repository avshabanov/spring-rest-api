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

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.OrderComparator;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Provides test support for REST API.
 */
public final class DefaultRestTestSupport implements RestTestSupport {

    @Inject
    private RequestMappingHandlerAdapter handlerAdapter;

    @Inject
    private ApplicationContext context;

    @Override
    public final MockHttpServletResponse handle(HttpServletRequest request) {
        final MockHttpServletResponse response = new MockHttpServletResponse();
        Object handler = null;

        try {
            // delegates request processing to the spring facilities
            for (HandlerMapping mapping : context.getBeansOfType(HandlerMapping.class).values()) {
                final HandlerExecutionChain chain = mapping.getHandler(request);
                if (chain == null) {
                    continue;
                }

                handler = chain.getHandler();
                if (handler != null) {
                    handlerAdapter.handle(request, response, chain.getHandler());
                    break;
                }
            }

            if (handler == null) {
                throw new AssertionError("Can't handle request in the current context");
            }
        } catch (Exception e) {
            if (handleException(e, request, response, handler)) {
                return response;
            }

            // exception mapping has not been found - propagate error outside the method boundaries
            throw new AssertionError(e);
        }

        return response;
    }

    // tries to find the exception resolver and return error payload within the response, if exception is mapped to the request
    private boolean handleException(Exception e, HttpServletRequest request, HttpServletResponse response, Object handler) {
        final Map<String, HandlerExceptionResolver> resolverMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(
                context,
                HandlerExceptionResolver.class,
                true,
                false
        );

        final List<HandlerExceptionResolver> resolvers = new ArrayList<HandlerExceptionResolver>(resolverMap.values());
        OrderComparator.sort(resolvers);

        for (HandlerExceptionResolver exceptionResolver : resolvers) {
            final Object o = exceptionResolver.resolveException(request, response, handler, e);
            if (o != null) {
                // we don't care about model-and-view in the result (this is not needed for testing)
                return true;
            }
        }

        return false;
    }
}
