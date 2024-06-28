package com.taekyoung.planner.domain.member.dto

import com.taekyoung.planner.domain.member.model.MemberRole
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern

data class SignupRequest (
    @field:Email
    val email: String,
    @field:Pattern(regexp = "^[a-zA-Z0-9!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]{8,15}\$", message = "Invalid Password")
    val password: String,
    val name: String,
    val role: MemberRole,
    val imageUrl: String?,
)

