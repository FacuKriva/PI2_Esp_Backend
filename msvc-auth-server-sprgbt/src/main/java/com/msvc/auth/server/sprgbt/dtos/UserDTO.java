package com.msvc.auth.server.sprgbt.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDTO {

    private Long id;

    @JsonProperty("user_id")
    private Long userId;

    private String name;

    private String email;

    private String password;

    private boolean enabled;

    private int attempts;

    @JsonProperty("date_created")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime dateCreated;

    @JsonProperty("last_update")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime lastUpdate;

    //@JsonInclude(JsonInclude.Include.NON_NULL)
    private RoleDTO role;
}
