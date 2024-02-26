package org.delivery.storeadmin.domain.userorder.business;

import lombok.RequiredArgsConstructor;
import org.delivery.common.message.model.UserOrderMessage;
import org.delivery.db.userordermenu.UserOrderMenuRepository;
import org.delivery.storeadmin.domain.sse.connection.SseConnectionPool;
import org.delivery.storeadmin.domain.storemenu.converter.StoreMenuConverter;
import org.delivery.storeadmin.domain.storemenu.service.StoreMenuService;
import org.delivery.storeadmin.domain.userorder.controller.model.UserOrderDetailResponse;
import org.delivery.storeadmin.domain.userorder.converter.UserOrderConverter;
import org.delivery.storeadmin.domain.userorder.service.UserOrderService;
import org.delivery.storeadmin.domain.userordermenu.service.UserOrderMenuService;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserOrderBusiness {

    private final UserOrderService userOrderService;
    private final UserOrderConverter userOrderConverter;

    private final UserOrderMenuService userOrderMenuService;

    private final StoreMenuService storeMenuService;
    private final StoreMenuConverter storeMenuConverter;

    private final SseConnectionPool sseConnectionPool;

    /**
     * 1. 주문
     * 2. 주문내역 찾기
     * 3. 스토어찾기
     * 4. 연결된 세션 찾기
     */
    public void pushUserOrder(UserOrderMessage userOrderMessage) {

        // 주문내역 찾아오기
        // user order entity(사용자 주문이 들어옴)
        var userOrderEntity = userOrderService.getUserOrder(userOrderMessage.getUserOrderId())
                .orElseThrow(() -> new RuntimeException("사용자 주문내역없음"));

        // user order menu (사용자 주문이 들어오면 메뉴를 뽑을수 있음)
        var userOrderMenuList = userOrderMenuService.getUserOrderMenuList(userOrderEntity.getId());

        // user order menu -> store menu (뽑은 메뉴로 스토어 메뉴를 뽑음)
        var storeMenuReponseList = userOrderMenuList.stream()
                .map(userOrderMenuEntity -> {
                    return storeMenuService.getStoreMenuWithThrow(userOrderMenuEntity.getStoreMenu().getId());
                })
                .map(storeMenuEntity -> {
                    // response (메뉴를 response에 담아서)
                    return storeMenuConverter.toResponse(storeMenuEntity);
                })
                .collect(Collectors.toList());
        // response
        var userOrderResponse = userOrderConverter.toResponse(userOrderEntity);

        // push로 보내기
        var push = UserOrderDetailResponse.builder()
                .userOrderResponse(userOrderResponse)
                .storeMenuResponseList(storeMenuReponseList)
                .build();

        // 스토어 찾기
        var userConnection = sseConnectionPool.getSession(userOrderEntity.getStore().getId().toString());

        // 사용자에게 push
        userConnection.sendMessage(push);

    }
}
