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

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.util.Assert;
import org.springframework.web.client.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URI;
import java.util.Collection;
import java.util.List;

/**
 * Delegates HTTP requests to the REST API test support by adapting the RestTemplate's HTTP objects to
 * the servlet api's domain model and vice versa.
 */
public final class RestOperationsTestClient extends RestTemplate {

    @Inject
    private DefaultRestTestSupport testSupport;

    @Override
    protected <T> T doExecute(URI url,
                              HttpMethod method,
                              RequestCallback requestCallback,
                              ResponseExtractor<T> responseExtractor) throws RestClientException {
        try {
            final ClientHttpRequest clientHttpRequest = createRequest(url, method);

            if (requestCallback != null) {
                requestCallback.doWithRequest(clientHttpRequest);
            }

            final MockHttpServletResponse mockHttpServletResponse = testSupport.handle(
                    toMockHttpServletRequest(url, method, clientHttpRequest));

            final ClientHttpResponse clientHttpResponse = new ClientHttpResponseAdapter(mockHttpServletResponse);

            // translate error statuses
            if (getErrorHandler().hasError(clientHttpResponse)) {
                getErrorHandler().handleError(clientHttpResponse);
            }

            return responseExtractor == null ? null : responseExtractor.extractData(clientHttpResponse);
        } catch (IOException e) {
            throw new ResourceAccessException("Can't access the resource provided: " + url, e);
        }
    }

    // converts RestTemplate's ClientHttpRequest to MockHttpServletRequest
    private MockHttpServletRequest toMockHttpServletRequest(URI url,
                                                            HttpMethod method,
                                                            ClientHttpRequest clientHttpRequest) throws IOException {
        final MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest(
                method.name(),
                url.getPath()
        );

        // copy headers
        final HttpHeaders headers = clientHttpRequest.getHeaders();
        for (final String headerKey : headers.toSingleValueMap().keySet()) {
            final List<String> headerValues = headers.get(headerKey);
            for (final String headerValue : headerValues) {
                mockHttpServletRequest.addHeader(headerKey, headerValue);
            }
        }

        // copy query parameters
        final String query = clientHttpRequest.getURI().getQuery();
        if (query != null) {
            mockHttpServletRequest.setQueryString(query);
            final String[] queryParameters = query.split("&");
            for (String keyValueParam : queryParameters) {
                final String[] components = keyValueParam.split("=");
                if (components.length == 1) {
                    continue; // optional parameter
                }

                Assert.isTrue(components.length == 2,
                        "Can't split query parameters " + keyValueParam + " by key-value pair");
                mockHttpServletRequest.setParameter(components[0], components[1]);
            }
        }

        // copy request body
        // TODO: another byte copying approach here
        // TODO: for now we rely to the fact that request body is always presented as byte array output stream
        final OutputStream requestBodyStream = clientHttpRequest.getBody();
        if (requestBodyStream instanceof ByteArrayOutputStream) {
            mockHttpServletRequest.setContent(((ByteArrayOutputStream) requestBodyStream).toByteArray());
        } else {
            throw new AssertionError("Ooops, client http request has non-ByteArrayOutputStream body");
        }

        return mockHttpServletRequest;
    }

    /**
     * Adapts {@link MockHttpServletResponse} object to the {@link ClientHttpResponse} object for further processing
     * by the {@link RestTemplate} data extractor.
     */
    private static final class ClientHttpResponseAdapter implements ClientHttpResponse {

        private final MockHttpServletResponse mockResponse;

        private ClientHttpResponseAdapter(MockHttpServletResponse response) {
            Assert.notNull(response);
            this.mockResponse = response;
        }

        @Override
        public HttpStatus getStatusCode() throws IOException {
            return HttpStatus.valueOf(mockResponse.getStatus());
        }

        @Override
        public int getRawStatusCode() throws IOException {
            return mockResponse.getStatus();
        }

        @Override
        public String getStatusText() throws IOException {
            return getStatusCode().getReasonPhrase();
        }

        @Override
        public void close() {
            // forbid to use response any longer
            mockResponse.setCommitted(true);
        }

        @Override
        public InputStream getBody() throws IOException {
            return new ByteArrayInputStream(mockResponse.getContentAsByteArray());
        }

        @Override
        public HttpHeaders getHeaders() {
            final HttpHeaders headers = new HttpHeaders();
            final Collection<String> names = mockResponse.getHeaderNames();
            for (final String name : names) {
                headers.set(name, mockResponse.getHeader(name));
            }
            return headers;
        }
    }
}
