package org.delivery.api.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.delivery.common.error.ErrorCode;
import org.delivery.common.error.TokenErrorCode;
import org.delivery.common.exception.ApiException;
import org.delivery.api.domain.token.business.TokenBusiness;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final TokenBusiness tokenBusiness;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("Authorization Interceptor url : {}", request.getRequestURI());

        // WEB, chrome의 경우 GET, POST라든지 API요청하기 전에 OPTION이라는 API를 요청을 해서 해당 메소드를 지원하는지 체크하는 API = 통과(pass)
        if(HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

        // js, html, png resource를 요청하는 경우 = pass
        if(handler instanceof ResourceHttpRequestHandler) {
            return true;
        }

        var userId = request.getHeader("x-user-id");
        if(userId == null) {
            throw new ApiException(ErrorCode.BAD_REQUEST, "x-user-id header 없음");
        }

        // 한가지 requset에 대해서 글로벌하게 저장할 수 있는 영역
        var requestContext = Objects.requireNonNull(RequestContextHolder.getRequestAttributes());
        requestContext.setAttribute("userId", userId, RequestAttributes.SCOPE_REQUEST);

        return true;
    }
}
