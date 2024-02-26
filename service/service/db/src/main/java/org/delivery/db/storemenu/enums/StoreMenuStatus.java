package org.delivery.db.storemenu.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum StoreMenuStatus {

    REGISTERED("등록"),
    UNREGISTERED("해제")
    ;

    private String description;
}
