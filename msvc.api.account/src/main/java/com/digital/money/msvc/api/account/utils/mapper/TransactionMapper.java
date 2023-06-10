package com.digital.money.msvc.api.account.utils.mapper;

import com.digital.money.msvc.api.account.model.Transaction;
import com.digital.money.msvc.api.account.model.dto.CardTransactionGetDTO;
import com.digital.money.msvc.api.account.model.dto.CardTransactionPostDTO;
import com.digital.money.msvc.api.account.model.dto.TransactionGetDto;
import com.digital.money.msvc.api.account.model.dto.TransactionPostDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class TransactionMapper {

    public abstract Transaction toTransaction(TransactionPostDto transactionPostDto);

    public abstract TransactionGetDto toTransactionGetDto(Transaction transaction);

    public abstract Transaction cTPDTOToTransaction(CardTransactionPostDTO cardTransactionPostDTO);

    public abstract CardTransactionGetDTO transactionToCardTransactionGetDTO(Transaction transaction);

//    @Mappings({
//            @Mapping(target = "email", expression="java(clientPostUpdateDto.getEmail().toLowerCase())"),
//            @Mapping(target = "deleted", source="deleted"),
//            @Mapping(target = "state", source="state"),
//    })
//    public abstract Account toClientUpdate(ClientPostDto clientPostUpdateDto, ClientState state, boolean deleted);

}
