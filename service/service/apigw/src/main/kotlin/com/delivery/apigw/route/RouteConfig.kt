package com.delivery.apigw.route

import com.delivery.apigw.filter.ServiceApiPrivateFilter
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * 서버가 10개이상 또는 5개, 6개일 경우 yaml파일에 관리하면 코드가 너무 길어져 코드로 따로 관리해야됨
 * yaml파일에 설정한 것과 동일하게 동작
 */
@Configuration
class RouteConfig(
    private val serviceApiPrivateFilter: ServiceApiPrivateFilter
) {

    @Bean
    fun gatewayRoutes(builder: RouteLocatorBuilder): RouteLocator {
        return builder.routes()
            .route {spec ->
                spec.order(-1) // 우선순위
                spec.path(
                    "service-api/api/**" // 매칭할 주소
                ).filters {filterSpec ->
                    filterSpec.filter(serviceApiPrivateFilter.apply(ServiceApiPrivateFilter.Config())) // 필터지정
                    filterSpec.rewritePath("/service-api(?<segment>/?.*)", "\${segment}")
                }.uri(
                    "http://localhost:8080" // 라우팅할 주소
                )
            }
            .build()
    }
}