package com.digital.money.msvc.api.account.utils.mapper;

import com.digital.money.msvc.api.account.model.dto.CardGetDTO;
import com.digital.money.msvc.api.account.model.dto.CardPostDTO;
import com.digital.money.msvc.api.account.model.Card;
import org.mapstruct.Mapper;

import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public abstract class CardMapper {

    public abstract Card toCard(CardPostDTO cardPostDTO);

    // We create a method toCardGetDTO that takes a Card object and returns a CardGetDTO object AND
    // hides the card number except for the last 4 digits
    public CardGetDTO toCardGetDTO(Card card) {
        CardGetDTO cardGetDTO = new CardGetDTO();
        cardGetDTO.setCardId(card.getCardId());
        cardGetDTO.setAlias(card.getAlias());
        cardGetDTO.setCardNumber(hideCardNumber(card.getCardNumber()));
        cardGetDTO.setCardHolder(card.getCardHolder());
        cardGetDTO.setBank(card.getBank());
        cardGetDTO.setCardNetwork(card.getCardNetwork());
        return cardGetDTO;
    }
    //public abstract CardGetDTO toCardGetDTO(Card card);

    public DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");

    public String hideCardNumber(Long cardNumber) {
        String cardNumberString = cardNumber.toString();
        String lastFourDigits = cardNumberString.substring(cardNumberString.length() - 4);
        return "**** " + lastFourDigits;
    }
}
