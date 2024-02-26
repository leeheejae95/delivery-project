package org.delivery.api.domain.userorder.controller.model;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class UserOderRequest {

    @NotNull
    private Long storeId;

    // 주문
    // 특정사용자가 특정 메뉴를 주문
    // 특정사용자 = 로그인된 세션에 들어있는 사용자
    // 특정 메뉴 id만 있으면됨
    @NotNull
    private List<Long> storeMenuIdList;

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public List<Long> getStoreMenuIdList() {
        return storeMenuIdList;
    }

    public void setStoreMenuIdList(List<Long> storeMenuIdList) {
        this.storeMenuIdList = storeMenuIdList;
    }
}
