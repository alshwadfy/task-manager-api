package com.projects.Tasks.repositories;

import com.projects.Tasks.domain.entities.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepo extends JpaRepository<Task, UUID> {

    Page<Task> findByTaskListId(UUID taskListId, Pageable pageable);

    Optional<Task> findByTaskListIdAndId(UUID taskListId,UUID id);
    void deleteByTaskListIdAndId(UUID taskListId,UUID id);
}
