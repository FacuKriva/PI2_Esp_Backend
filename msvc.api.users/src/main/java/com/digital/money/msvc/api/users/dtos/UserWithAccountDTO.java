package com.digital.money.msvc.api.users.dtos;

import com.digital.money.msvc.api.users.clients.dtos.AccountDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserWithAccountDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserDTO userDto;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AccountDTO accountId;

}
