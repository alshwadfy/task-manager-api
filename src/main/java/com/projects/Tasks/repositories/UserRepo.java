package com.projects.Tasks.repositories;

import com.projects.Tasks.domain.dto.TaskListDto;
import com.projects.Tasks.domain.entities.TaskList;
import com.projects.Tasks.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepo extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);



}
