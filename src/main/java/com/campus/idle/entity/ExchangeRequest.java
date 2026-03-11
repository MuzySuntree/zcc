package com.campus.idle.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "exchange_request")
public class ExchangeRequest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private IdleItem item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id", nullable = false)
    private SysUser fromUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id", nullable = false)
    private SysUser toUser;

    @Column(length = 255)
    private String message;

    @Column(length = 255)
    private String offeredItemDesc;

    @Column(nullable = false)
    private Integer status = 1;

    private LocalDateTime handledTime;

    private LocalDateTime cancelledTime;

    @Column(nullable = false)
    private Integer deleted = 0;
}
