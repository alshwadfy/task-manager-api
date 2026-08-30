package com.projects.Tasks;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.projects.Tasks.domain.dto.RegisterRequest;
import com.projects.Tasks.mappers.TaskListMapper;
import com.projects.Tasks.repositories.TaskListRepo;
import com.projects.Tasks.repositories.UserRepo;
import com.projects.Tasks.security.JwtService;
import com.projects.Tasks.services.impl.UserServiceImpl;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class TasksApplicationTests {
	@Mock
	private TaskListRepo taskListRepo;
	@Mock
	private UserRepo userRepo;
	@Mock
	private TaskListMapper taskListMapper;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private JwtService jwtService;
	@InjectMocks
	private UserServiceImpl userServiceImpl;

	


@Test
public void createUser(){

}
}



