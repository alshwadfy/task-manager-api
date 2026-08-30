package com.projects.Tasks.repositories;

import com.projects.Tasks.domain.entities.Task;
import com.projects.Tasks.domain.entities.TaskList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskListRepo extends JpaRepository<TaskList, UUID> {
    Page<TaskList> findByUserUserId(UUID userId, Pageable pageable);
    boolean existsByUserUserIdAndTitle(UUID userId, String title);

}
