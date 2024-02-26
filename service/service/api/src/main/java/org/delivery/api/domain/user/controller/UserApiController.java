package org.delivery.api.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.delivery.common.annotation.UserSession;
import org.delivery.common.api.Api;
import org.delivery.api.domain.user.business.UserBusiness;
import org.delivery.api.domain.user.controller.model.UserResponse;
import org.delivery.api.domain.user.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserApiController {

    private final UserBusiness userBusiness;

    /**
     * 사용자가 로그인 했을때 나의 정보를 가져가는 API
     */
    @GetMapping("/me")
    public Api<UserResponse> me(
            @UserSession User user
    ) {
        var response = userBusiness.me(user);
        return Api.OK(response);
    }
}
