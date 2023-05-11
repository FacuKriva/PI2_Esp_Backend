package com.digital.money.msvc.api.users.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 30, nullable = false)
    private String name;

    @Column(name = "last_name", length = 40, nullable = false)
    private String lastName;

    @Column(name = "cvu", unique = true, nullable = false)
    @Length(min = 22, max = 50)
    private String cvu;

    @Column(name = "dni", unique = true, length = 10, nullable = false)
    private Long dni;

    @Column(name = "email", length = 60, unique = true, nullable = false)
    private String email;

    @Column(name = "password", length = 120, nullable = false)
    private String password;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Column(name = "attempts", nullable = false)
    private int attempts;

    @Column(name = "phone", length = 10, nullable = false)
    private Integer phone;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

}
