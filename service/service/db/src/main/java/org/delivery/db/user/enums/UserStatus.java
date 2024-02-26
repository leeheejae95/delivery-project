package org.delivery.db.user.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserStatus {

    REGISTERED("등록"),
    UNREGISTERED("해제")
    ;

    private final String description;
}
