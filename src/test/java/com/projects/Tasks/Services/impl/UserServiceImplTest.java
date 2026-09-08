package com.projects.Tasks.Services.impl;

import com.projects.Tasks.domain.dto.RegisterRequest;
import com.projects.Tasks.domain.dto.TaskListDto;
import com.projects.Tasks.domain.entities.TaskList;
import com.projects.Tasks.domain.entities.User;
import com.projects.Tasks.mappers.TaskListMapper;
import com.projects.Tasks.repositories.TaskListRepo;
import com.projects.Tasks.repositories.UserRepo;
import com.projects.Tasks.security.JwtService;
import com.projects.Tasks.services.impl.UserServiceImpl;
import com.projects.Tasks.exceptions.ResourceNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    // Fakes (Mocks)
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

    // Real service under test with the mocks injected
    @InjectMocks
    private UserServiceImpl userService;


    @Test
    public void createUser() {
        // 1. Arrange
        RegisterRequest registerRequest = new RegisterRequest("ezz", "ezz@gmail.com", "password");
        when(userRepo.existsByEmail(registerRequest.email())).thenReturn(false);
        when(userRepo.existsByUsername(registerRequest.username())).thenReturn(false);
        when(passwordEncoder.encode(registerRequest.password())).thenReturn("Hpassword");
        
        User user = new User(
            UUID.randomUUID(),
            registerRequest.username(),
            registerRequest.email(),
            "Hpassword", 
            null,
            false
        );
        when(userRepo.save(any(User.class))).thenReturn(user);

        // 2. Act
        User result = userService.createUser(registerRequest);

        // 3. Assert
        assertNotNull(result);
        assertEquals("ezz", result.getUsername());
        assertEquals("ezz@gmail.com", result.getEmail());
        verify(userRepo, times(1)).save(any(User.class));
    }

    @Test
    public void createUser_ThrowsException_WhenEmailAlreadyExists(){
        RegisterRequest registerRequest = new RegisterRequest("ezz","ezz@gmial.com","pass");
        when(userRepo.existsByEmail(registerRequest.email())).thenReturn(true);
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> userService.createUser(registerRequest));
        assertEquals("email already registered", exception.getMessage());
        verify(userRepo,never()).save(any(User.class));
        
    }

    @Test
    public void createUser_ThrowsException_WhenUsernameAlreadyExists(){
        RegisterRequest registerRequest = new RegisterRequest("ezz","ezz@gmial.com","pass");
        when(userRepo.existsByEmail(registerRequest.email())).thenReturn(false);
        when(userRepo.existsByUsername(registerRequest.username())).thenReturn(true);
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> userService.createUser(registerRequest));
        assertEquals("username already registered", exception.getMessage());
        verify(userRepo,never()).save(any(User.class));
        
    }
    @Test
    public void createUser_ThrowsException_WhenPasswordIsNUll(){
        RegisterRequest registerRequest = new RegisterRequest("ezz","ezz@gmial.com",null);
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> userService.createUser(registerRequest));
        assertEquals("password is required", exception.getMessage());
        verify(userRepo,never()).save(any(User.class));
        
    }
    @Test
    public void createUser_ThrowsException_WhenEmailIsNUll(){
        RegisterRequest registerRequest = new RegisterRequest("ezz",null,"pass");
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> userService.createUser(registerRequest));
        assertEquals("email is required", exception.getMessage());
        verify(userRepo,never()).save(any(User.class));
        
    }
    @Test
    public void createUser_ThrowsException_WhenUsernameIsNUll(){
        RegisterRequest registerRequest = new RegisterRequest(null,"ezz@gmial.com","pass");
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> userService.createUser(registerRequest));
        assertEquals("username is required", exception.getMessage());
        verify(userRepo,never()).save(any(User.class));
        
    }

    @Test
    public void loginSucceded(){
        User user = new User(UUID.randomUUID(),"ezz","ezz@gmail.com","Hpassword",null, true);
        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pass",user.getPassword())).thenReturn(true);
        User result = userService.login(user.getEmail(),"pass");
        assertNotNull(result);
        assertEquals("ezz",result.getUsername());
        verify(userRepo,times(1)).findByEmail(user.getEmail());
        verify(passwordEncoder,times(1)).matches("pass",user.getPassword());
    }

    @Test
    public void loginFailed_WhenEmailNotVerified(){
        User user = new User(UUID.randomUUID(),"ezz","ezz@gmail.com","Hpassword",null, false);
        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pass",user.getPassword())).thenReturn(true);
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> userService.login(user.getEmail(),"pass"));
        assertEquals("Email not verified. Please verify your email before logging in.", exception.getMessage());
        verify(userRepo,times(1)).findByEmail(user.getEmail());
        verify(passwordEncoder,times(1)).matches("pass",user.getPassword());
    }

    @Test
    public void loginFailed_WhenEmailNotFoud(){
        User user = new User(UUID.randomUUID(),"ezz","ezz@gmail.com","Hpassword",null, true);
        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> userService.login(user.getEmail(),"pass"));
        assertEquals("invalid email or password", exception.getMessage());
        verify(userRepo,times(1)).findByEmail(user.getEmail());
        verify(passwordEncoder,never()).matches(anyString(),anyString());
    }

    @Test
    public void loginFailed_WhenPasswordIncorrect(){
        User user = new User(UUID.randomUUID(),"ezz","ezz@gmail.com","Hpassword",null, true);
        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pass",user.getPassword())).thenReturn(false);
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> userService.login(user.getEmail(),"pass"));
        assertEquals("invalid email or password", exception.getMessage());
        verify(userRepo,times(1)).findByEmail(user.getEmail());
        verify(passwordEncoder,times(1)).matches("pass",user.getPassword());
    }

    @Test
    public void getUserByEmail_Succeded(){
        User user = new User(UUID.randomUUID(),"ezz","ezz@gmail.com","Hpassword",null, true);
        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        User result = userService.getUserByEmail(user.getEmail());
        assertNotNull(result);
        assertEquals("ezz", result.getUsername());
        verify(userRepo,times(1)).findByEmail(user.getEmail());
    }

    @Test
    public void getUserByEmail_Failed_WhenEmailIsNull(){
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> userService.getUserByEmail(null));
        assertEquals("email cannot be null or empty", exception.getMessage());
        verify(userRepo,never()).findByEmail(anyString());
    }

    @Test
    public void getUserByEmail_Failed_WhenUserNotFound(){
        when(userRepo.findByEmail("notfound@example.com")).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class,
            () -> userService.getUserByEmail("notfound@example.com"));
        assertEquals("User with email notfound@example.com not found", exception.getMessage());
        verify(userRepo,times(1)).findByEmail("notfound@example.com");
    }

    @Test
    public void getUserById_Succeded(){
        User user = new User(UUID.randomUUID(),"ezz","ezz@gmail.com","Hpassword",null);
        when(userRepo.findById(user.getUserId())).thenReturn(Optional.of(user));
        User result = userService.getUserById(user.getUserId());
        assertNotNull(result);
        assertEquals("ezz", result.getUsername());
        verify(userRepo,times(1)).findById(user.getUserId());
    }

    @Test
    public void getUserById_Failed_WhenUserIdIsNull(){
        UUID uuid=null;
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> userService.getUserById(uuid));
            assertEquals("userId is null", exception.getMessage());
        verify(userRepo,never()).findById(uuid);
    }
    @Test
    public void getUserById_Failed_WhenUserNotFound(){
        UUID uuid = UUID.randomUUID();
        when(userRepo.findById(uuid)).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class,
            () -> userService.getUserById(uuid));
        assertEquals("userId " + uuid + " not found", exception.getMessage());
        verify(userRepo,times(1)).findById(uuid);
    }
    @Test
    public void getTaskListsByUserId_Succeded(){
        UUID uuid = UUID.randomUUID();
        List<TaskList> taskLists = new ArrayList<>();
        taskLists.add(new TaskList(UUID.randomUUID(),"ezz","description",null,null,null,null));
        when(userRepo.findById(uuid)).thenReturn(Optional.of(new User(uuid,"ezz","ezz@gmail.com","Hpassword",null)));
        when(taskListRepo.findByUserUserId(eq(uuid), any(org.springframework.data.domain.Pageable.class))).thenReturn(new org.springframework.data.domain.PageImpl<>(taskLists));
        org.springframework.data.domain.Page<TaskListDto> result = userService.getTaskListsByUserId(uuid, org.springframework.data.domain.PageRequest.of(0, 10));
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(userRepo,times(1)).findById(uuid);
        verify(taskListRepo,times(1)).findByUserUserId(eq(uuid), any(org.springframework.data.domain.Pageable.class));
    }

    @Test
    public void getTaskListsByUserId_Failed_WhenUserIdIsNull(){
        UUID uuid = null;
        Exception exception = assertThrows(IllegalArgumentException.class,
            () -> userService.getTaskListsByUserId(uuid, org.springframework.data.domain.PageRequest.of(0, 10)));
        assertEquals("userId cannot be null", exception.getMessage());
        verify(userRepo,never()).findById(uuid);
        verify(taskListRepo,never()).findByUserUserId(any(), any());
    }
    @Test
    public void getTaskListsByUserId_Failed_WhenUserNotFound(){
        UUID uuid = UUID.randomUUID();
        when(userRepo.findById(uuid)).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class,
            () -> userService.getTaskListsByUserId(uuid, org.springframework.data.domain.PageRequest.of(0, 10)));
        assertEquals("user not found", exception.getMessage());
        verify(userRepo,times(1)).findById(uuid);
        verify(taskListRepo,never()).findByUserUserId(any(), any());
    }
    
  
    



    


}
