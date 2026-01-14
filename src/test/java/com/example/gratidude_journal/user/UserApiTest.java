package com.example.gratidude_journal.user;

import com.example.gratidude_journal.TestcontainersConfiguration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

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
				.expectBody(User.class)
				.value(user -> {
					org.junit.jupiter.api.Assertions.assertEquals(userName, user.getUserName());
					org.junit.jupiter.api.Assertions.assertEquals(firstName, user.getFirstName());
					org.junit.jupiter.api.Assertions.assertEquals(lastName, user.getLastName());
					org.junit.jupiter.api.Assertions.assertNotNull(user.getUserId());
				});
	}

	ResponseSpec requestDeleteUser(String userName) {
		return restTestClient.delete()
				.uri("http://localhost:%d/user/%s".formatted(port, userName))
				.exchange();
	}

	ResponseSpec requestPostUser(String userName, String firstName, String lastName) {
		UserDTO newUser = new UserDTO(userName, firstName, lastName);
		return restTestClient.post().uri("/user")
				.body(newUser)
				.exchange();
	}

	void requestAndValidatePostUser(String userName, String firstName, String lastName) {
		requestPostUser(userName, firstName, lastName)
				.expectStatus().isCreated()
				.expectBody(User.class)
				.value(user -> {
					Assertions.assertEquals(userName, user.getUserName());
					Assertions.assertEquals(firstName, user.getFirstName());
					Assertions.assertEquals(lastName, user.getLastName());
					Assertions.assertNotNull(user.getUserId());
				});
	}

	ResponseSpec requestPutUser(String userName, String firstName, String lastName) {
		UserDTO updatedUser = new UserDTO(userName, firstName, lastName);
		return restTestClient.put().uri("/user")
				.body(updatedUser)
				.exchange();
	}

	void requestAndValidatePutUser(String userName, String firstName, String lastName) {
		requestPutUser(userName, firstName, lastName)
				.expectStatus().isOk()
				.expectBody(User.class)
				.value(user -> {
					Assertions.assertEquals(userName, user.getUserName());
					Assertions.assertEquals(firstName, user.getFirstName());
					Assertions.assertEquals(lastName, user.getLastName());
					Assertions.assertNotNull(user.getUserId());
				});
	}

	@Test
	void getUserThatDoesExist() {
		requestAndValidateGetUser("test1UserName", "test1FirstName", "test1LastName");
	}

	@Test
	void getUserThatDoesNotExist() {
		requestGetUser("test4UserName")
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
				.expectStatus().isOk();

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
		requestDeleteUser("t")
				.expectStatus().isBadRequest();
	}

	@Test
	void createUserThatDoesNotExist() {
		requestGetUser("newUserTestuserName")
				.expectStatus().isNotFound();

		requestAndValidatePostUser("newUserTestuserName", "newUserTestfirstName", "newUserTestlastName");

		requestAndValidateGetUser("newUserTestuserName", "newUserTestfirstName", "newUserTestlastName");
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
		requestPutUser("", "firstName", "lastName")
				.expectStatus().isBadRequest();
		requestPutUser("test2UserName", "", "lastName")
				.expectStatus().isBadRequest();
		requestPutUser("test2UserName", "firstName", "")
				.expectStatus().isBadRequest();
	}
}