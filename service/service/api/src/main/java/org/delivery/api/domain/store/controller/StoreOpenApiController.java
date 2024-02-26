package org.delivery.api.domain.store.controller;

import lombok.RequiredArgsConstructor;
import org.delivery.common.api.Api;
import org.delivery.api.domain.store.business.StoreBusiness;
import org.delivery.api.domain.store.controller.model.StoreRegisterRequest;
import org.delivery.api.domain.store.controller.model.StoreResponse;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 로그인한 사용자가 등록하는게 아니라 가맹점 직원들이 등록
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/open-api/store")
public class StoreOpenApiController {

    private final StoreBusiness storeBusiness;

    @PostMapping("/register")
    public Api<StoreResponse> register(
            @Valid
            @RequestBody Api<StoreRegisterRequest> request
    ) {
        var response = storeBusiness.register(request.getBody());

        return Api.OK(response);
    }
}
