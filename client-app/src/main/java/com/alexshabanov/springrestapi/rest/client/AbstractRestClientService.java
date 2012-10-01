package com.alexshabanov.springrestapi.rest.client;

import org.springframework.web.client.RestOperations;

/**
 * Provides base class for REST client.
 */
public abstract class AbstractRestClientService {

    private final RestOperations restOperations;
    private final String baseUrl;

    protected AbstractRestClientService(String baseUrl, RestOperations restOperations) {
        this.baseUrl = baseUrl;
        this.restOperations = restOperations;
    }

    public RestOperations getRestOperations() {
        return restOperations;
    }

    /**
     * Returns absolute method URI.
     *
     * @param methodUriScheme relative path with optional placeholders.
     * @return absolute method URI
     */
    protected String getMethodUri(String methodUriScheme) {
        return baseUrl + methodUriScheme;
    }
}
