package org.delivery.api.domain.storemenu.controller;

import lombok.RequiredArgsConstructor;
import org.delivery.common.api.Api;
import org.delivery.api.domain.storemenu.business.StoreMenuBusiness;
import org.delivery.api.domain.storemenu.controller.model.StoreMenuResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 로그인한 사용자가 메뉴를 눌렀을 떄 메뉴 정보조회
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/store-menu")
public class StoreMenuApiController {

    private final StoreMenuBusiness storeMenuBusiness;

    @GetMapping("/search")
    public Api<List<StoreMenuResponse>> search(@RequestParam Long storeId) {
        var response = storeMenuBusiness.search(storeId);
        return Api.OK(response);
    }
}
