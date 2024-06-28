package com.taekyoung.planner.domain.member.service

import com.taekyoung.planner.domain.member.dto.*
import com.taekyoung.planner.domain.member.model.Member
import com.taekyoung.planner.domain.member.model.MemberRole
import com.taekyoung.planner.domain.member.repository.MemberRepository
import com.taekyoung.planner.infra.security.jwt.TokenProvider
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val tokenProvider: TokenProvider
) {

    @Transactional
    fun signUp(request: SignupRequest): MemberResponse {
        if (memberRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("enmail: '${request.email}' already exists.")
        }

        val member = Member.of(
            email = request.email,
            name = request.name,
            password = passwordEncoder.encode(request.password),
            role = request.role,
            imageUrl = request.imageUrl
        )
        return memberRepository.save(member).toResponse()
    }

    fun signIn(request: SigninRequest): SigninResponse {
        val member = memberRepository.findByEmail(request.email)
            ?.takeIf { passwordEncoder.matches(request.password, it.password) }
            ?: throw IllegalArgumentException("Invalid Credential.")

        return SigninResponse(
            accessToken = tokenProvider.generateAccessToken(
                member.id!!.toString(),
                member.email,
                member.role
            )
        )
    }

//    fun update()

}