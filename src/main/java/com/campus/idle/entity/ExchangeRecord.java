package com.campus.idle.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "exchange_record")
public class ExchangeRecord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false, unique = true)
    private ExchangeRequest request;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private IdleItem item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_user_id", nullable = false)
    private SysUser ownerUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_user_id", nullable = false)
    private SysUser requesterUser;

    @Column(nullable = false)
    private LocalDateTime exchangeTime = LocalDateTime.now();

    @Column(length = 100)
    private String exchangeLocation;

    @Column(length = 255)
    private String note;

    @Column(nullable = false)
    private Integer status = 1;

    @Column(nullable = false)
    private Integer ownerConfirmed = 0;

    @Column(nullable = false)
    private Integer requesterConfirmed = 0;
}