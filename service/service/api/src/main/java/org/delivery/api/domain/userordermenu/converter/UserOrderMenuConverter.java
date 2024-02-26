package org.delivery.api.domain.userordermenu.converter;

import org.delivery.common.annotation.Converter;
import org.delivery.db.storemenu.StoreMenuEntity;
import org.delivery.db.userorder.UserOrderEntity;
import org.delivery.db.userordermenu.UserOrderMenuEntity;

@Converter
public class UserOrderMenuConverter {

    /**
     * 주문했을 때 테이블에 들어가야 되는게 어떠한 메뉴인지 그리고 사용자의 주문이 들어가야 된다.
     */
    public UserOrderMenuEntity toEntity(UserOrderEntity userOrderEntity, StoreMenuEntity storeMenuEntity) {
        return UserOrderMenuEntity.builder()
                .userOrder(userOrderEntity)
                .storeMenu(storeMenuEntity)
                .build();
    }
}
