package com.projects.Tasks.mappers;

import com.projects.Tasks.domain.dto.UserDto;
import com.projects.Tasks.domain.entities.User;

public interface UserMapper {


    UserDto toDto(User user);



}
