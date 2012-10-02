package com.alexshabanov.springrestapi.rest.common;

/**
 * Defines REST constants being used by both client and server.
 */
public final class RestConstants {
    private RestConstants() {}

    public static final String REGISTER_USER_URI = "/user/register";

    public static final String FIND_USER_BY_ID_URI = "/user/{id}";

    public static final String FIND_ALL_USERS_URI = "/users";
}
