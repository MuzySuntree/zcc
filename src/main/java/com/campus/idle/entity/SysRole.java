package com.campus.idle.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sys_role")
public class SysRole extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String roleCode;

    @Column(nullable = false, length = 50)
    private String roleName;

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private Integer status = 1;

    @Column(nullable = false)
    private Integer deleted = 0;
}
