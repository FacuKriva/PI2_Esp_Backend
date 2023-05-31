package com.digital.money.msvc.api.account.utils.mapper;

import com.digital.money.msvc.api.account.model.dto.CardGetDTO;
import com.digital.money.msvc.api.account.model.dto.CardPostDTO;
import com.digital.money.msvc.api.account.model.Card;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class CardMapper {

    public abstract Card toCard(CardPostDTO cardPostDTO);

    public abstract CardGetDTO toCardGetDTO(Card card);
}
