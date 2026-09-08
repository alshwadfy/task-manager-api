package com.projects.Tasks.Services.impl;

import com.projects.Tasks.services.impl.EmailServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceImplTest {

    @Mock
    private JavaMailSender javaMailSender;

    @Test
    public void sendVerificationCode_ShouldSendEmailWithCorrectContent() {
        EmailServiceImpl emailService = new EmailServiceImpl(javaMailSender, "test@example.com");

        emailService.sendVerificationCode("user@example.com", "123456");

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(javaMailSender, times(1)).send(captor.capture());

        SimpleMailMessage sentMessage = captor.getValue();
        assertEquals("test@example.com", sentMessage.getFrom());
        assertArrayEquals(new String[]{"user@example.com"}, sentMessage.getTo());
        assertEquals("TaskFlow - Verify Your Email", sentMessage.getSubject());
        assertTrue(Objects.requireNonNull(sentMessage.getText()).contains("123456"));
    }

    @Test
    public void sendVerificationCode_ShouldThrowRuntimeException_WhenSenderFails() {
        doThrow(new RuntimeException("Mail server down")).when(javaMailSender).send(any(SimpleMailMessage.class));

        EmailServiceImpl emailService = new EmailServiceImpl(javaMailSender, "test@example.com");

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> emailService.sendVerificationCode("user@example.com", "123456"));

        assertEquals("Failed to send verification email", exception.getMessage());
    }
}
