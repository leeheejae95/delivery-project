package org.delivery.api.domain.store.controller;

import lombok.RequiredArgsConstructor;
import org.delivery.common.api.Api;
import org.delivery.api.domain.store.business.StoreBusiness;
import org.delivery.api.domain.store.controller.model.StoreResponse;
import org.delivery.db.store.enums.StoreCategory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 가맹점 조회 같은 경우 로그인된 사용자만 가능
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/store")
public class StoreApiController {

    private final StoreBusiness storeBusiness;

    @GetMapping("")
    public Api<List<StoreResponse>> search(
            @RequestParam(required = false)
            StoreCategory storeCategory
    ) {
        var response = storeBusiness.searchCategory(storeCategory);

        return Api.OK(response);
    }
}
