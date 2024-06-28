package com.taekyoung.planner.domain.member.controller

import com.taekyoung.planner.domain.member.dto.MemberResponse
import com.taekyoung.planner.domain.member.dto.SigninRequest
import com.taekyoung.planner.domain.member.dto.SigninResponse
import com.taekyoung.planner.domain.member.dto.SignupRequest
import com.taekyoung.planner.domain.member.service.MemberService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/auth")
@RestController
class MemberController(
    private val memberService: MemberService
) {

    @PostMapping("/signup")
    fun signup(@RequestBody request: SignupRequest) : ResponseEntity<MemberResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.signUp(request))
    }

    @PostMapping("/signin")
    fun signin(@RequestBody request: SigninRequest) : ResponseEntity<SigninResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.signIn(request))
    }


}