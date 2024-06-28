package com.taekyoung.planner.domain.member.dto

import com.taekyoung.planner.domain.member.model.Member

data class MemberResponse(
    val memberId: Long,
    val email: String,
    val name: String,
    val imageUrl: String?,
) {
}


fun Member.toResponse(): MemberResponse {
    return MemberResponse(
        memberId = id!!,
        email = email,
        name = name,
        imageUrl = imageUrl,
    )
}