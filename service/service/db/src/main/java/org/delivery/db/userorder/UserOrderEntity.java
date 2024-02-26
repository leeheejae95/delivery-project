/*
package org.delivery.db.userorder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.delivery.db.BaseEntity;
import org.delivery.db.store.StoreEntity;
import org.delivery.db.userorder.enums.UserOrderStatus;
import org.delivery.db.userordermenu.UserOrderMenuEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

*/
/**
 * 사용자가 주문한 내역 Entity
 *//*

@Entity
@Table(name = "user_order")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserOrderEntity extends BaseEntity {

    @Column(nullable = false)
    private Long userId;

    @JoinColumn(nullable = false, name = "store_id")
    @ManyToOne
    private StoreEntity store;

    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    private UserOrderStatus status;

    @Column(precision = 11, scale = 4, nullable = false)
    private BigDecimal amount;

    private LocalDateTime orderedAt;

    private LocalDateTime acceptedAt;

    private LocalDateTime cookingStartedAt;

    private LocalDateTime deliveryStartedAt;

    private LocalDateTime receivedAt;

    @OneToMany(mappedBy = "userOrder")
    @ToString.Exclude
    @JsonIgnore // 연관관계시 재귀호출 하지 못하도록 꼭 해줘야됨
    private List<UserOrderMenuEntity> userOrderMenuList;
}
*/
