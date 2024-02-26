package org.delivery.api.domain.storemenu.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreMenuRegisterRequest {

    @NotNull
    private Long storeId; // 내가 어떠한 상점에다 등록 하겠다

    @NotBlank
    private String name;

    @NotNull
    private BigDecimal amount;

    @NotBlank
    private String thumbnailUrl;
}