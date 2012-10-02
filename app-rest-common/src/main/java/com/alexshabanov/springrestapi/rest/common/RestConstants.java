package com.alexshabanov.springrestapi.rest.common;

/**
 * Defines REST constants being used by both client and server.
 */
public final class RestConstants {
    private RestConstants() {}

    //
    // User service URIs
    //

    public static final String REGISTER_USER_URI = "/user/register";

    public static final String FIND_USER_BY_ID_URI = "/user/{id}";

    public static final String FIND_ALL_USERS_URI = "/users";

    //
    // Bank account service URIs
    //

    public static final String UPDATE_BANK_ACCOUNT_URI = "/user/{id}/account";

    public static final String REGISTER_BANK_ACCOUNT_URI = "/account";
}
