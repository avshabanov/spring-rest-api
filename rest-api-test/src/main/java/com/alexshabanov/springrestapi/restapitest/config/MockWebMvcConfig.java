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

package com.alexshabanov.springrestapi.restapitest.config;

import com.alexshabanov.springrestapi.restapitest.DefaultRestTestSupport;
import com.alexshabanov.springrestapi.restapitest.RestOperationsTestClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 * Holds base Spring MVC configuration for testing purposes.
 * For alternative XML-based configuration take a look on ClientServerTest-context.xml in the test/resources folder.
 * <p/>
 * The prospective user only need:
 * <ul>
 *     <li>To add all the required message converters by overriding {@link WebMvcConfigurerAdapter#configureMessageConverters(java.util.List)}</li>
 *     <li>To add controller beans</li>
 * </ul>to add controllers to the resultant configuration class.
 */
@EnableWebMvc
public class MockWebMvcConfig extends WebMvcConfigurerAdapter {

    @Inject
    private ApplicationContext applicationContext;

    private final MockServletContext mockServletContext = new MockServletContext();

    /**
     * @return Mocked servlet context.
     */
    public MockServletContext getMockServletContext() {
        return mockServletContext;
    }

    @PostConstruct
    public void initServletContext() {
        final Map<String, ServletContextAware> servletContextAwareMap = applicationContext.getBeansOfType(ServletContextAware.class);
        for (final ServletContextAware servletContextAware : servletContextAwareMap.values()) {
            servletContextAware.setServletContext(getMockServletContext());
        }
    }

    @Bean
    public DefaultRestTestSupport restTestSupport() {
        return new DefaultRestTestSupport();
    }

    @Bean
    public RestOperationsTestClient testClient() {
        return new RestOperationsTestClient();
    }
}
