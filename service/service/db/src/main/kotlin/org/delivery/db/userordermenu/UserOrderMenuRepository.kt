package org.delivery.db.userordermenu

import org.delivery.db.userordermenu.enums.UserOrderMenuStatus
import org.springframework.data.jpa.repository.JpaRepository

interface UserOrderMenuRepository : JpaRepository<UserOrderMenuEntity, Long> {

    // 어떠한 주문을 했으면 그 주문 아이디가 있을 거고 그 주문 아이디에 해당되는 등록된 모든 메뉴릐 리스트를 리턴하는 메소드
    // select * from user_order_menu where user_order_id = ? and status = ?
    fun findAllByUserOrderIdAndStatus(userOrderId: Long?, status: UserOrderMenuStatus?): List<UserOrderMenuEntity>

}