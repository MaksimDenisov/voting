package ru.denisovmaksim.voting.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
public class TestUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

    @Autowired
    private MockMvc mockMvc;

    public RequestPostProcessor getUser() {
        return httpBasic("user@mail.com", "user");
    }

    public RequestPostProcessor getAdmin() {
        return httpBasic("admin@mail.com", "admin");
    }

    public ResultActions perform(final MockHttpServletRequestBuilder request) throws Exception {
        return mockMvc.perform(request);
    }
/*

    public ResultActions performByUser(final MockHttpServletRequestBuilder request, final String byUser)
            throws Exception {
        final String token = jwtHelper.expiring(Map.of("username", byUser));
        request.header(AUTHORIZATION, token);
        return perform(request);
    }
*/

    public void checkNotAuthorizedRequestIsForbidden(final MockHttpServletRequestBuilder request) throws Exception {
        perform(request).andExpect(status().isForbidden())
                .andReturn()
                .getResponse();
    }

    public static String asJson(final Object object) throws JsonProcessingException {
        return MAPPER.writeValueAsString(object);
    }

    public static <T> T fromJson(final String json, final TypeReference<T> to) throws JsonProcessingException {
        return MAPPER.readValue(json, to);
    }
}
