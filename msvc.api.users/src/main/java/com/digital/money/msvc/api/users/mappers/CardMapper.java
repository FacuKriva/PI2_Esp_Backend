package com.digital.money.msvc.api.users.mappers;

import com.digital.money.msvc.api.users.controllers.requestDto.CardRequestDTO;
import com.digital.money.msvc.api.users.dtos.CardDTO;
import com.digital.money.msvc.api.users.entities.Card;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class CardMapper {

    private final ObjectMapper objectMapper;

    @Autowired
    public CardMapper(ObjectMapper objectMapper) {this.objectMapper = objectMapper;}

    /**
     * Entity ->  mapToDto
     */
    public CardDTO mapToDto(Card cardEntity) {
        log.info("*** cardEntity {}", cardEntity);
        CardDTO dto = objectMapper.convertValue(cardEntity, CardDTO.class);
        log.info("*** CardDTO {}", dto);
        return dto;
    }

    /**
     * mapToDto -> Entity
     */
    public Card mapToEntity(CardDTO cardDTO) {
        log.info("*** CardDTO Dto {}", cardDTO);
        Card entity = objectMapper.convertValue(cardDTO, Card.class);
        log.info("*** card Entity {}", entity);
        return entity;
    }

    /**
     * mapToDto -> Entity
     */
    public Card mapRequestToEntity(CardRequestDTO cardRequestDTO) {
        log.info("*** CardRequestDTO Dto {}", cardRequestDTO);
        Card entity = objectMapper.convertValue(cardRequestDTO, Card.class);
        log.info("*** card Entity {}", entity);
        return entity;
    }

}
