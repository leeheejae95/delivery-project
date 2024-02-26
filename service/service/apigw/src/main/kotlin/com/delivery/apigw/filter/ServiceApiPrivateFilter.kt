package com.delivery.apigw.filter

import com.delivery.apigw.account.model.TokenDto
import com.delivery.apigw.account.model.TokenValidationRequest
import com.delivery.apigw.account.model.TokenValidationResponse
import com.delivery.apigw.common.Log
import org.delivery.common.error.ErrorCode
import org.delivery.common.error.TokenErrorCode
import org.delivery.common.exception.ApiException
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono

/**
 * 인증이 필요한 필터
 */
@Component
class ServiceApiPrivateFilter: AbstractGatewayFilterFactory<ServiceApiPrivateFilter.Config>(Config::class.java) {

    companion object: Log

    class Config

    override fun apply(config: Config): GatewayFilter {

        return GatewayFilter { exchange, chain ->

            val uri = exchange.request.uri
            log.info("service api private filter route uri : {}",uri)

            // account server를 통한 인증실행
            // 1. 토큰의 유무 체크
            val headers = exchange.request.headers["authorization-token"]?: listOf() // 반드시 값이 들어오게 셋팅

            val token = if(headers.isEmpty()) {
                throw ApiException(TokenErrorCode.AUTHORIZATION_TOKEN_NOT_FOUND)
            } else{
                headers.get(0)
            }
            log.info("authorization token : {}", token)

            // 2. 토큰의 유효성 체크
            val accountApiUrl = UriComponentsBuilder
                .fromUriString("http://localhost")
                .port(8082) // account서버로 보내기
                .path("/internal-api/token/validation")
                .build()
                .encode()
                .toUriString()
            val webClient = WebClient.builder().baseUrl(accountApiUrl).build()

            val request = TokenValidationRequest(
                tokenDto = TokenDto(
                    token = token,
                )
            )

            webClient
                .post()
                .body(Mono.just(request), object : ParameterizedTypeReference<TokenValidationRequest>(){})
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(
                    {
                        status: HttpStatus -> status.isError // status가 error이면 밑에 response로 떨어짐
                    },
                    {
                        response: ClientResponse -> response.bodyToMono( object: ParameterizedTypeReference<Any>(){})
                        .flatMap { error ->
                            log.error("", error)
                            Mono.error(ApiException(TokenErrorCode.TOKEN_EXCEPTION))
                        }
                    }
                )
                .bodyToMono(object: ParameterizedTypeReference<TokenValidationResponse>(){})
                .flatMap { response ->
                    // 응답이 왔을때
                    log.info("response : {}", response)

                    // 3. 사용자 정보 추가
                    val userId = response.userId?.toString()
                    val proxyRequest = exchange.request.mutate()
                        .header("x-user-id", userId).build()
                    val requestBuild = exchange.mutate().request(proxyRequest).build()


                    val mono = chain.filter(requestBuild)
                    mono
                }
                .onErrorMap { e ->
                    log.error("",e)
                    e
                }
        }
    }
}