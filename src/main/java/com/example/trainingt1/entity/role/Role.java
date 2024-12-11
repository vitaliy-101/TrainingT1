package com.example.trainingt1.entity.role;

import com.example.trainingt1.entity.role.RoleEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "role")
@Getter
@Setter
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private RoleEnum name;

    public Role(RoleEnum roleEnum) {
        name = roleEnum;
    }
}
