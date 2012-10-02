package com.alexshabanov.springrestapi.rest.client;

import com.alexshabanov.springrestapi.domain.User;
import com.alexshabanov.springrestapi.rest.common.InlineInt;
import com.alexshabanov.springrestapi.rest.common.InlineString;
import com.alexshabanov.springrestapi.rest.common.RestConstants;
import com.alexshabanov.springrestapi.service.UserService;
import org.springframework.web.client.RestOperations;

import java.util.List;

/**
 * Communicates with the exposed UserService.
 */
public final class UserRestClientService extends AbstractRestClientService implements UserService {

    public UserRestClientService(String baseUrl, RestOperations restOperations) {
        super(baseUrl, restOperations);
    }

    @Override
    public int register(String name) {
        return getRestOperations().postForObject(
                getMethodUri(RestConstants.REGISTER_USER_URI),
                new InlineString(name),
                InlineInt.class)
                .getValue();
    }

    @Override
    public User findById(int id) {
        return getRestOperations().getForObject(getMethodUri(RestConstants.FIND_USER_URI), User.class, id);
    }

    @Override
    public List<User> findUsers() {
        throw new UnsupportedOperationException();
    }
}
