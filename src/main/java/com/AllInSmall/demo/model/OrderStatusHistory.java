package com.AllInSmall.demo.model;

import java.time.LocalDateTime;

import com.AllInSmall.demo.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_status_history")
public class OrderStatusHistory {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by_user_id")
    @JsonBackReference
    private User updatedBy;
}
