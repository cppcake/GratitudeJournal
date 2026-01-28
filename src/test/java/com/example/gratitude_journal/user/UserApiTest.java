package com.example.gratitude_journal.user;

import com.example.gratitude_journal.user.dto.SimpleUserDTO;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.client.RestTestClient.ResponseSpec;

/**
 * Unit-Tests for the endpoints in
 * {@link com.example.gratitude_journal.user.UserController}, testing the
 * presentation layer of the User-API.
 * 
 * @author Afeef Neiroukh
 */
@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
@ActiveProfiles("test")
class UserApiTest {
	/**
	 * Port of the testing service.
	 */
	@LocalServerPort
	private int port;

	/**
	 * HTTP-Client to interact with the presentation layer.
	 */
	@Autowired
	private RestTestClient restTestClient;

	/**
	 * Helper method to perform a GET-Request for a user.
	 * 
	 * @param userName The user name of the user to retrieve.
	 * @return {@link ResponseSpec} of the request.
	 */
	ResponseSpec requestGetUser(String userName) {
		return restTestClient.get()
				.uri("http://localhost:%d/user/%s".formatted(port, userName))
				.exchange();
	}

	/**
	 * Helper method to perform a GET-Request for a user and validate if the
	 * response is as expected.
	 * 
	 * @param userName  The user name of the user to retrieve and validate.
	 * @param firstName The expected first name.
	 * @param lastName  The expected last name.
	 */
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

	/**
	 * Helper method to perform a DELETE-Request for a user.
	 * 
	 * @param userName The user name of the user to delete.
	 * @return {@link ResponseSpec} of the request.
	 */
	ResponseSpec requestDeleteUser(String userName) {
		return restTestClient.delete()
				.uri("http://localhost:%d/user/%s".formatted(port, userName))
				.exchange();
	}

	/**
	 * Helper method to perform a PUT-Request for a user.
	 * 
	 * @param userName  The user name of the user to update.
	 * @param firstName The new first name to apply on the user.
	 * @param lastName  The new last name to apply on the user.
	 * @return {@link ResponseSpec} of the request.
	 */
	ResponseSpec requestPutUser(String userName, String firstName, String lastName) {
		SimpleUserDTO simpleUserDTO = new SimpleUserDTO(firstName, lastName);
		return restTestClient.put().uri("http://localhost:%d/user/%s".formatted(port, userName))
				.body(simpleUserDTO)
				.exchange();
	}

	/**
	 * Helper method to perform a PUT-Request for a user and validate if the
	 * response is as expected.
	 * 
	 * @param userName  The user name of the user to update and validate.
	 * @param firstName The new first name to apply and validate.
	 * @param lastName  The new last name to apply and validate.
	 */
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

	/**
	 * Helper method to perform a POST-Request for a user.
	 * 
	 * @param userName  The user name of the user to create.
	 * @param firstName The first name of the user to create.
	 * @param lastName  The last name of the user to create.
	 * @return {@link ResponseSpec} of the request.
	 */
	ResponseSpec requestPostUser(String userName, String firstName, String lastName) {
		SimpleUserDTO simpleUserDTO = new SimpleUserDTO(firstName, lastName);
		return restTestClient.post().uri("http://localhost:%d/user/%s".formatted(port, userName))
				.body(simpleUserDTO)
				.exchange();
	}

	/**
	 * Helper method to perform a POST-Request and validate if the created user is
	 * as expected.
	 * 
	 * @param userName  The user name of the user to create and validate.
	 * @param firstName The first name of the user to create and validate.
	 * @param lastName  The last name of the user to create and validate.
	 */
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

	/**
	 * Unit-Test for GET-Request on a user that exists.
	 */
	@Test
	void getUserThatDoesExist() {
		requestAndValidateGetUser("test1UserName", "test1FirstName", "test1LastName");
	}

	/**
	 * Unit-Test for GET-Request on a user that does not exist.
	 */
	@Test
	void getUserThatDoesNotExist() {
		requestGetUser("thisUserDoesNotExist")
				.expectStatus().isNotFound();
	}

	/**
	 * Unit-Test for GET-Request on a user with an invalid name according to
	 * {@link com.example.gratitude_journal.user.User#validateName(String)}.
	 */
	@Test
	void getUserWithInvalidName() {
		requestGetUser("t")
				.expectStatus().isBadRequest();
	}

	/**
	 * Unit-Test for DELETE-Request on a user that exists.
	 */
	@Test
	void deleteUserThatDoesExist() {
		requestDeleteUser("test3UserName")
				.expectStatus().isNoContent();

		requestGetUser("test3UserName")
				.expectStatus().isNotFound();
	}

	/**
	 * Unit-Test for DELETE-Request on a user that doesn't exist.
	 */
	@Test
	void deleteUserThatDoesNotExist() {
		requestDeleteUser("test4UserName")
				.expectStatus().isNotFound();
	}

	/**
	 * Unit-Test for DELETE-Request on a user with an invalid name according to
	 * {@link com.example.gratitude_journal.user.User#validateName(String)}.
	 */
	@Test
	void deleteUserWithInvalidName() {
		requestDeleteUser("I")
				.expectStatus().isBadRequest();
	}

	/**
	 * Unit-Test for PUT-Request on a user that exists.
	 */
	@Test
	void updateUserThatDoesExist() {
		requestAndValidateGetUser("test2UserName", "test2FirstName", "test2LastName");

		requestAndValidatePutUser("test2UserName", "firstNameUpdated", "lastNameUpdated");

		requestAndValidateGetUser("test2UserName", "firstNameUpdated", "lastNameUpdated");
	}

	/**
	 * Unit-Test for PUT-Request on a user that doesn't exist.
	 */
	@Test
	void updateUserThatDoesNotExist() {
		requestPutUser("IDoNotExist", "firstName", "lastName")
				.expectStatus().isNotFound();
	}

	/**
	 * Unit-Test for PUT-Request on a user with an invalid name according to
	 * {@link com.example.gratitude_journal.user.User#validateName(String)}.
	 */
	@Test
	void updateUserWithInvalidName() {
		requestPutUser("I", "firstName", "lastName")
				.expectStatus().isBadRequest();
		requestPutUser("test2UserName", "I", "lastName")
				.expectStatus().isBadRequest();
		requestPutUser("test2UserName", "firstName", "I")
				.expectStatus().isBadRequest();
	}

	/**
	 * Unit-Test for POST-Request on a user that doesn't exist.
	 */
	@Test
	void createUserThatDoesNotExist() {
		requestGetUser("simpleUserDTOTestuserName")
				.expectStatus().isNotFound();

		requestAndValidatePostUser("simpleUserDTOTestuserName", "simpleUserDTOTestfirstName",
				"simpleUserDTOTestlastName");

		requestAndValidateGetUser("simpleUserDTOTestuserName", "simpleUserDTOTestfirstName",
				"simpleUserDTOTestlastName");
	}

	/**
	 * Unit-Test for POST-Request on a user that exists.
	 */
	@Test
	void createUserThatDoesExist() {
		requestPostUser("test1UserName", "firstName", "lastName")
				.expectStatus().isEqualTo(409);
	}

	/**
	 * Unit-Test for POST-Request on a user with an invalid name according to
	 * {@link com.example.gratitude_journal.user.User#validateName(String)}.
	 */
	@Test
	void createUserWithInvalidName() {
		requestPostUser("I", "firstName", "lastName")
				.expectStatus().isBadRequest();
		requestPostUser("userName", "I", "lastName")
				.expectStatus().isBadRequest();
		requestPostUser("userName", "firstName", "I")
				.expectStatus().isBadRequest();
	}
}