package com.example.gratidude_journal;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
class HttpRequestTest {

	@LocalServerPort
	private int port;

	@Autowired
	private RestTestClient restTestClient;

	@Test
	void getapitest() {
		restTestClient.get()
				.uri("http://localhost:%d/user/test1UserName".formatted(port))
				.exchange()
				.expectStatus().isOk()
				.expectBody(User.class)
				.value(user -> {
					org.junit.jupiter.api.Assertions.assertEquals("test1UserName", user.getUserName());
					org.junit.jupiter.api.Assertions.assertEquals("test1FirstName", user.getFirstName());
					org.junit.jupiter.api.Assertions.assertEquals("test1LastName", user.getLastName());
					org.junit.jupiter.api.Assertions.assertNotNull(user.getUserId());
				});
	}
}