package com.example.gratitude_journal.user;

import com.example.gratitude_journal.user.dto.NewUserDTO;
import com.example.gratitude_journal.user.dto.UpdateUserDTO;
import com.example.gratitude_journal.user.dto.ReturnUserDTO;
import com.example.gratitude_journal.TestcontainersConfiguration;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.client.RestTestClient.ResponseSpec;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
class UserApiTest {

	@LocalServerPort
	private int port;

	@Autowired
	private RestTestClient restTestClient;

	ResponseSpec requestGetUser(String userName) {
		return restTestClient.get()
				.uri("http://localhost:%d/user/%s".formatted(port, userName))
				.exchange();
	}

	void requestAndValidateGetUser(String userName, String firstName, String lastName) {
		requestGetUser(userName)
				.expectStatus().isOk()
				.expectBody(ReturnUserDTO.class)
				.value(returnUserDTO -> {
					org.junit.jupiter.api.Assertions.assertEquals(userName, returnUserDTO.userName());
					org.junit.jupiter.api.Assertions.assertEquals(firstName, returnUserDTO.firstName());
					org.junit.jupiter.api.Assertions.assertEquals(lastName, returnUserDTO.lastName());
					org.junit.jupiter.api.Assertions.assertNotNull(returnUserDTO.userId());
				});
	}

	ResponseSpec requestDeleteUser(String userName) {
		return restTestClient.delete()
				.uri("http://localhost:%d/user/%s".formatted(port, userName))
				.exchange();
	}

	ResponseSpec requestPutUser(String userName, String firstName, String lastName) {
		UpdateUserDTO updateNewUserDTO = new UpdateUserDTO(firstName, lastName);
		return restTestClient.put().uri("http://localhost:%d/user/%s".formatted(port, userName))
				.body(updateNewUserDTO)
				.exchange();
	}

	void requestAndValidatePutUser(String userName, String firstName, String lastName) {
		requestPutUser(userName, firstName, lastName)
				.expectStatus().isOk()
				.expectBody(ReturnUserDTO.class)
				.value(returnUserDTO -> {
					assertEquals(userName, returnUserDTO.userName());
					assertEquals(firstName, returnUserDTO.firstName());
					assertEquals(lastName, returnUserDTO.lastName());
					assertNotNull(returnUserDTO.userId());
				});
	}

	ResponseSpec requestPostUser(String userName, String firstName, String lastName) {
		NewUserDTO newUserDTO = new NewUserDTO(userName, firstName, lastName);
		return restTestClient.post().uri("/user")
				.body(newUserDTO)
				.exchange();
	}

	void requestAndValidatePostUser(String userName, String firstName, String lastName) {
		requestPostUser(userName, firstName, lastName)
				.expectStatus().isCreated()
				.expectBody(ReturnUserDTO.class)
				.value(returnUserDTO -> {
					assertEquals(userName, returnUserDTO.userName());
					assertEquals(firstName, returnUserDTO.firstName());
					assertEquals(lastName, returnUserDTO.lastName());
					assertNotNull(returnUserDTO.userId());
				});
	}

	@Test
	void getUserThatDoesExist() {
		requestAndValidateGetUser("test1UserName", "test1FirstName", "test1LastName");
	}

	@Test
	void getUserThatDoesNotExist() {
		requestGetUser("thisUserDoesNotExist")
				.expectStatus().isNotFound();
	}

	@Test
	void getUserWithInvalidName() {
		requestGetUser("t")
				.expectStatus().isBadRequest();
	}

	@Test
	void deleteUser() {
		requestDeleteUser("test3UserName")
				.expectStatus().isNoContent();

		requestGetUser("test3UserName")
				.expectStatus().isNotFound();
	}

	@Test
	void deleteUserThatDoesNotExist() {
		requestDeleteUser("test4UserName")
				.expectStatus().isNotFound();
	}

	@Test
	void deleteUserWithInvalidName() {
		requestDeleteUser("I")
				.expectStatus().isBadRequest();
	}

	@Test
	void updateUserThatDoesExist() {
		requestAndValidateGetUser("test2UserName", "test2FirstName", "test2LastName");

		requestAndValidatePutUser("test2UserName", "firstNameUpdated", "lastNameUpdated");

		requestAndValidateGetUser("test2UserName", "firstNameUpdated", "lastNameUpdated");
	}

	@Test
	void updateUserThatDoesNotExist() {
		requestPutUser("IDoNotExist", "firstName", "lastName")
				.expectStatus().isNotFound();
	}

	@Test
	void updateUserWithInvalidName() {
		requestPutUser("I", "firstName", "lastName")
				.expectStatus().isBadRequest();
		requestPutUser("test2UserName", "", "lastName")
				.expectStatus().isBadRequest();
		requestPutUser("test2UserName", "firstName", "")
				.expectStatus().isBadRequest();
	}

	@Test
	void createUserThatDoesNotExist() {
		requestGetUser("newUserDTOTestuserName")
				.expectStatus().isNotFound();

		requestAndValidatePostUser("newUserDTOTestuserName", "newUserDTOTestfirstName", "newUserDTOTestlastName");

		requestAndValidateGetUser("newUserDTOTestuserName", "newUserDTOTestfirstName", "newUserDTOTestlastName");
	}

	@Test
	void createUserThatDoesExist() {
		requestPostUser("test1UserName", "firstName", "lastName")
				.expectStatus().isForbidden();
	}

	@Test
	void createUserWithInvalidName() {
		requestPostUser("", "firstName", "lastName")
				.expectStatus().isBadRequest();
		requestPostUser("userName", "", "lastName")
				.expectStatus().isBadRequest();
		requestPostUser("userName", "firstName", "")
				.expectStatus().isBadRequest();
	}
}