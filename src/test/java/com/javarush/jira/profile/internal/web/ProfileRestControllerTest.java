package com.javarush.jira.profile.internal.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.jira.profile.ContactTo;
import com.javarush.jira.profile.ProfileTo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.anyLong;
import java.util.Set;

@WebMvcTest(ProfileRestController.class)
class ProfileRestControllerTest {

    private static final String REST_URL = "/api/profile";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfileRestController profileRestController;


    @Test
    @WithMockUser(username = "user", roles = "USER")
    void getProfile_Success() throws Exception {
        Set<String> mailNotifications = Set.of("email", "sms");
        Set<ContactTo> contacts = Set.of(new ContactTo("El Misterio", "misterio@gmail.com"));
        ProfileTo profile = new ProfileTo(1L, mailNotifications, contacts);

        when(profileRestController.get(anyLong())).thenReturn(profile);

        mockMvc.perform(get(REST_URL))
            .andExpect(status().isOk());
        }

    @Test
    void getProfile_Unauthorized() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void updateProfile_Success() throws Exception {
        Set<String> mailNotifications = Set.of("email");
        Set<ContactTo> contacts = Set.of(new ContactTo("skype", "userSkype"));
        ProfileTo updateRequest = new ProfileTo(1L, mailNotifications, contacts);

        mockMvc.perform(put(REST_URL).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @WithAnonymousUser
    void updateProfile_Unauthorized() throws Exception {
        Set<String> mailNotifications = Set.of("email");
        Set<ContactTo> contacts = Set.of(new ContactTo("Changed Name", "changed@gmail.com"));
        ProfileTo updateRequest = new ProfileTo(1L, mailNotifications, contacts);

        mockMvc.perform(put(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateRequest))
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }
}