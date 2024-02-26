package org.delivery.account.domain.token.service

import lombok.RequiredArgsConstructor
import org.delivery.account.domain.token.ifs.TokenHelperIfs
import org.delivery.account.domain.token.model.TokenDto
import org.springframework.stereotype.Service

@RequiredArgsConstructor
@Service
class TokenService(
    private val tokenHelperIfs: TokenHelperIfs
) {

    fun issueAccessToken(userId: Long?): TokenDto? {
        return userId?.let {
            var data = mapOf("userId" to it)
            tokenHelperIfs.issueAccessToken(data)
        }
    }

    fun issueRefreshToken(userId: Long?): TokenDto? {
        requireNotNull(userId)
        var data = mapOf("userId" to userId)
        return tokenHelperIfs.issueAccessToken(data)
    }

    fun validationToken(token: String?): Long? {
        requireNotNull(token)
        val map = tokenHelperIfs.validationTokenWithThrow(token)
        var userId = map?.get("userId")
        requireNotNull(userId)

        return userId.toString().toLong()

    }

}