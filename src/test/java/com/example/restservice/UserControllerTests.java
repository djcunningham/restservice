package com.example.restservice;

import com.example.business.IUserService;
import com.example.business.models.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private IUserService userService;

    private UriComponentsBuilder uriComponentsBuilder;
    private UserModel notAdminModel =
            new UserModel("d.c", "deric", "cunningham", List.of("NotAdminRole"));
    private UserModel adminModel =
            new UserModel("djcunningham", "deric", "cunningham", List.of("Admin"));

    @BeforeEach
    public void SetUp() {
        uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl("http://localhost/user")
                .port(port);
        when(userService.findUserByUserName(adminModel.getUserName())).thenReturn(Optional.of(adminModel));
        when(userService.findUser(1)).thenReturn(Optional.of(adminModel));
        when(userService.findUserByUserName(notAdminModel.getUserName())).thenReturn(Optional.of(notAdminModel));
        when(userService.findUser(2)).thenReturn(Optional.of(notAdminModel));

    }

    @Test
    public void UserReturnsUnauthorizedResponseForUnauthenticatedUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>(headers);
        assertThat(restTemplate.exchange(uriComponentsBuilder.toUriString(), HttpMethod.GET, entity, UserModel.class).getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void UserReturnsForbiddenResponseForUserWithIncorrectRoles() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        uriComponentsBuilder.queryParam("id", 1);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        assertThat(restTemplate
                .withBasicAuth(notAdminModel.getUserName(), "test1234")
                .exchange(
                        uriComponentsBuilder.toUriString(),
                        HttpMethod.GET,
                        entity,
                        UserModel.class)
                .getStatusCode())
                .isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void UserReturnsCorrectModelForAdminUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        uriComponentsBuilder.queryParam("id", 2);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        UserModel responseModel = restTemplate
                .withBasicAuth("djcunningham", "test1234")
                .getForObject(uriComponentsBuilder.build().toUri(), UserModel.class);

        assertThat(responseModel.getFirstName()).isEqualTo(notAdminModel.getFirstName());
        assertThat(responseModel.getLastName()).isEqualTo(notAdminModel.getLastName());
        assertThat(responseModel.getUserName()).isEqualTo(notAdminModel.getUserName());
    }

}
