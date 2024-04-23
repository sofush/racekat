package com.example.racekat;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.*;
import com.example.racekat.entity.Role;
import com.example.racekat.entity.User;
import com.example.racekat.repository.UserRepository;
import com.example.racekat.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MainTests {
	@Test
	public void testAddValidUserSucceeds() {
		User validUser = new User(
			"valid-user",
			"password123",
			Role.USER,
			"",
			"",
			List.of()
		);

		UserRepository mockRepo = Mockito.mock(UserRepository.class);
		UserService service = new UserService(mockRepo);

		Mockito
			.when(mockRepo.findUserByUsername(validUser.getUsername()))
			.thenReturn(validUser);

		service.addUser(
			validUser.getUsername(),
			validUser.getPassword(),
			validUser.getName(),
			validUser.getAbout()
		);

		User ret = service.findUserByUsername(validUser.getUsername());
		assertThat(validUser).isEqualTo(ret);
	}

	@Test
	public void testAddInvalidUserFails() {
		User invalidUser = new User(
			"short-password-user",
			"pw",
			Role.USER,
			"",
			"",
			List.of()
		);

		UserRepository mockRepo = Mockito.mock(UserRepository.class);
		UserService service = new UserService(mockRepo);

		Exception e = assertThrows(IllegalArgumentException.class, () -> service.addUser(
            invalidUser.getUsername(),
            invalidUser.getPassword(),
            invalidUser.getName(),
            invalidUser.getAbout()
        ));
		assertThat(e.getMessage().toLowerCase()).contains("password");
	}
}
