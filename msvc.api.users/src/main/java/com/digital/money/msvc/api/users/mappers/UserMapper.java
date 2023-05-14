package com.digital.money.msvc.api.users.mappers;

import com.digital.money.msvc.api.users.controllers.requestDto.UserRequestDTO;
import com.digital.money.msvc.api.users.dtos.UserDTO;
import com.digital.money.msvc.api.users.entities.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserMapper {

    private final ObjectMapper objectMapper;

    @Autowired
    public UserMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Entity ->  mapToDto
     */
    public UserDTO mapToDto(User userEntity) {

        log.info("*** userEntity {}", userEntity);
        UserDTO dto = objectMapper.convertValue(userEntity, UserDTO.class);
        log.info("*** UserDTO {}", dto);

        return dto;
    }

    /**
     * mapToDto -> Entity
     */
    public User mapToEntity(UserRequestDTO userRequestDTO) {

        log.info("*** UserDTO Dto {}", userRequestDTO);
        User entity = objectMapper.convertValue(userRequestDTO, User.class);
        log.info("*** user Entity {}", entity);

        return entity;
    }
}
