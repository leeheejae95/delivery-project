package org.delivery.db.userordermenu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.delivery.db.BaseEntity;
import org.delivery.db.storemenu.StoreMenuEntity;
import org.delivery.db.userorder.UserOrderEntity;
import org.delivery.db.userordermenu.enums.UserOrderMenuStatus;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user_order_menu")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserOrderMenuEntity extends BaseEntity {

    @JoinColumn(nullable = false, name = "user_order_id")
    @ManyToOne
    private UserOrderEntity userOrder; // 1:n

    @JoinColumn(nullable = false) //, name = "store_menu_id")
    @ManyToOne
    private StoreMenuEntity storeMenu; // n:1

    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    private UserOrderMenuStatus status;
}
