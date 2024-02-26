package org.delivery.api.domain.userorder.controller.model

import org.delivery.api.domain.store.controller.model.StoreResponse
import org.delivery.api.domain.storemenu.controller.model.StoreMenuResponse


data class UserOrderDetailResponse (
    val userOrderResponse: UserOrderResponse? = null, // 사용자가 주문한 건에 대한 정보
    val storeResponse: StoreResponse? = null, // 그 가게가 어디인지 스토어 정보
    val storeMenuResponsesList: List<StoreMenuResponse>? = null // 어떠한 메뉴를 주문했는지
)
