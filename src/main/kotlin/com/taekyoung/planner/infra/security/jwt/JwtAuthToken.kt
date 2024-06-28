package com.taekyoung.planner.infra.security.jwt

import com.taekyoung.planner.infra.security.MemberPrincipal
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.web.authentication.WebAuthenticationDetails

class JwtAuthToken(
    private val principal: MemberPrincipal,
    details: WebAuthenticationDetails
) : AbstractAuthenticationToken(principal.authorities) {

    init {
        super.setAuthenticated(true)
        super.setDetails(details)
    }

    override fun getCredentials() = null

    override fun getPrincipal() = principal

    override fun isAuthenticated() = true

}