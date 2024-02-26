/*
package org.delivery.api.domain.userorder.business;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.delivery.api.domain.userorder.controller.model.UserOrderResponse;
import org.delivery.common.annotation.Business;
import org.delivery.api.domain.store.converter.StoreConverter;
import org.delivery.api.domain.store.service.StoreService;
import org.delivery.api.domain.storemenu.converter.StoreMenuConverter;
import org.delivery.api.domain.storemenu.service.StoreMenuService;
import org.delivery.api.domain.user.model.User;
import org.delivery.api.domain.userorder.controller.model.UserOderRequest;
import org.delivery.api.domain.userorder.controller.model.UserOrderDetailResponse;
import org.delivery.api.domain.userorder.converter.UserOrderConverter;
import org.delivery.api.domain.userorder.producer.UserOrderProducer;
import org.delivery.api.domain.userorder.service.UserOrderService;
import org.delivery.api.domain.userordermenu.converter.UserOrderMenuConverter;
import org.delivery.api.domain.userordermenu.service.UserOrderMenuService;
import org.delivery.db.userordermenu.enums.UserOrderMenuStatus;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Business
public class UserOrderBusiness {

    private final UserOrderService userOrderService;
    private final UserOrderConverter userOrderConverter;

    private final UserOrderMenuConverter userOrderMenuConverter;
    private final UserOrderMenuService userOrderMenuService;

    private final StoreMenuService storeMenuService; // 유효한 메뉴인지 체크
    private final StoreMenuConverter storeMenuConverter;

    private final StoreService storeService;
    private final StoreConverter storeConverter;

    private final UserOrderProducer userOrderProducer;

    private final ObjectMapper objectMapper;

    // 1. 사용자 , 메뉴 Id 받기
    // 2. userOrder 생성 (주문만들기)
    // 3. userOrderMenu 생성
    // 4. 응답 생성
    public UserOrderResponse order(User user, UserOderRequest body) {

        var storeEntity = storeService.getStoreWithThrow(body.getStoreId());

        // 메뉴가 유효한지 체크
        var storeMenuEntityList = body.getStoreMenuIdList().stream() // 가게메뉴 : 아이스아메리카노(1), 카페라떼(2)
                .map(it -> storeMenuService.getStoreMenuWithThrow(it))
                .collect(Collectors.toList());

        var userOrderEntity = userOrderConverter.toEntity(user, storeEntity, storeMenuEntityList);

        // 주문
        var newUserOrderEntity = userOrderService.order(userOrderEntity);

        // 맵핑, 메뉴별로 만들어줘야됨
        // 사용자의 주문 메뉴 리스트
        var userOrderMenuEntityList = storeMenuEntityList.stream()
                .map(it -> {
                    // 메뉴 + userOrder
                    var userOrderMenuEntity = userOrderMenuConverter.toEntity(newUserOrderEntity, it);
                    return userOrderMenuEntity;
                })
                .collect(Collectors.toList());

        // 주문내역 기록 남기기
        userOrderMenuEntityList.forEach(it -> {
            userOrderMenuService.order(it);
        });

        // 비동기로 가맹점에 주문 알리기
        userOrderProducer.sendOrder(newUserOrderEntity);

        // response
        return userOrderConverter.toResponse(newUserOrderEntity);
    }

    public List<UserOrderDetailResponse> current(User user) {
        var userOrderEntityList = userOrderService.current(user.getId()); // 현재 사용자가 주문한 내역

        // 주문 1건씩 처리
        var userOrderDetailResponseList = userOrderEntityList.stream()
                .map(userOrderEntity -> {
                    // 사용자가 주문한 메뉴 -> List로 여러건 나옴
                    //var userOrderMenuEntityList = userOrderMenuService.getUserOrderMenu(it.getId());

                    var userOrderMenuEntityList = userOrderEntity.getUserOrderMenuList()
                            .stream()
                            .filter(it -> it.getStatus().equals(UserOrderMenuStatus.REGISTERED))
                            .collect(Collectors.toList());

                    // 어떠한 메뉴들을 주문했는지 나옴
                    var storeMenuEntityList = userOrderMenuEntityList.stream()
                            .map(userOrderMenuEntity ->{
                                // var storeMenuEntity = storeMenuService.getStoreMenuWithThrow(userOrderMenuEntity.getStoreMenu().getId());
                                return userOrderMenuEntity.getStoreMenu();
                            })
                            .collect(Collectors.toList());
                    // 사용자가 주문한 스토어 가져오기
                    //var storeEntity = storeService.getStoreWithThrow(storeMenuEntityList.stream().findFirst().get().getStore().getId());
                    var storeEntity = userOrderEntity.getStore();

                    return UserOrderDetailResponse.builder()
                            .userOrderResponse(userOrderConverter.toResponse(userOrderEntity)) // 사용자의 주문건
                            .storeMenuResponsesList(storeMenuConverter.toResponse(storeMenuEntityList)) // 주문한 메뉴
                            .storeResponse(storeConverter.toResponse(storeEntity)) // 주문한 가게
                            .build();
                }).collect(Collectors.toList());
        return userOrderDetailResponseList;
    }

    public List<UserOrderDetailResponse> history(User user) {

        var userOrderEntityList = userOrderService.history(user.getId()); // 현재 사용자가 주문한 내역

        // 주문 1건씩 처리
        var userOrderDetailResponseList = userOrderEntityList.stream()
                .map(userOrderEntity -> {
                    // 사용자가 주문한 메뉴 -> List로 여러건 나옴
                    //var userOrderMenuEntityList = userOrderMenuService.getUserOrderMenu(it.getId());
                    var userOrderMenuEntityList = userOrderEntity.getUserOrderMenuList()
                            .stream()
                            .filter(it -> it.getStatus().equals(UserOrderMenuStatus.REGISTERED))
                            .collect(Collectors.toList());
                    // 어떠한 메뉴들을 주문했는지 나옴
                    var storeMenuEntityList = userOrderMenuEntityList.stream()
                            .map(userOrderMenuEntity -> userOrderMenuEntity.getStoreMenu()
                                //var storeMenuEntity = storeMenuService.getStoreMenuWithThrow(userOrderMenuEntity.getStoreMenu().getId());
                            )
                            .collect(Collectors.toList());
                    // 사용자가 주문한 스토어 가져오기
                    //var storeEntity = storeService.getStoreWithThrow(storeMenuEntityList.stream().findFirst().get().getStore().getId());
                    var storeEntity = userOrderEntity.getStore();

                    return UserOrderDetailResponse.builder()
                            .userOrderResponse(userOrderConverter.toResponse(userOrderEntity)) // 사용자의 주문건
                            .storeMenuResponsesList(storeMenuConverter.toResponse(storeMenuEntityList)) // 주문한 메뉴
                            .storeResponse(storeConverter.toResponse(storeEntity)) // 주문한 가게
                            .build();
                }).collect(Collectors.toList());
        return userOrderDetailResponseList;
    }

    public UserOrderDetailResponse read(User user, Long orderId) {

        var userOrderEntity = userOrderService.getUserOrderWithOutStatusWithThrow(orderId, user.getId());

        // 사용자가 주문한 메뉴 -> List로 여러건 나옴
        //var userOrderMenuEntityList = userOrderMenuService.getUserOrderMenu(userOrderEntity.getId());
        var userOrderMenuEntityList = userOrderEntity.getUserOrderMenuList()
                .stream()
                .filter(it -> it.getStatus().equals(UserOrderMenuStatus.REGISTERED))
                .collect(Collectors.toList());
        // 어떠한 메뉴들을 주문했는지 나옴
        var storeMenuEntityList = userOrderMenuEntityList.stream()
                .map(userOrderMenuEntity -> userOrderMenuEntity.getStoreMenu())
                .collect(Collectors.toList());

        // 사용자가 주문한 스토어 가져오기
        //var storeEntity = storeService.getStoreWithThrow(storeMenuEntityList.stream().findFirst().get().getStore().getId());
        var storeEntity = userOrderEntity.getStore();

        return UserOrderDetailResponse.builder()
                .userOrderResponse(userOrderConverter.toResponse(userOrderEntity)) // 사용자의 주문건
                .storeMenuResponsesList(storeMenuConverter.toResponse(storeMenuEntityList)) // 주문한 메뉴
                .storeResponse(storeConverter.toResponse(storeEntity)) // 주문한 가게
                .build();
    }
}
*/
