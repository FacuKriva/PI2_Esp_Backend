package com.msvc.auth.server.sprgbt.clients.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserRequestDTO {

    private String name;
    private String email;
    private String password;
    private boolean enabled;
    private int attempts;
}
