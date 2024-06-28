package com.taekyoung.planner.domain.plan.comment.dto

import com.taekyoung.planner.domain.plan.comment.model.Comment
import java.time.LocalDateTime

data class CommentResponse(
    val planId: Long,
    val commentId: Long,
    val comment: String,
    val writer: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
) {
}

fun Comment.toResponse() = CommentResponse(
    planId = plan.id!!,
    commentId = id!!,
    comment = contents,
    writer = member.name,
    createdAt = createdAt,
    updatedAt = updatedAt,
)