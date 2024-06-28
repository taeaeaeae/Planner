package com.taekyoung.planner.domain.plan.comment.model

import com.taekyoung.planner.domain.member.model.Member
import com.taekyoung.planner.domain.plan.model.Plan
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "comment")
class Comment(

    @Column(nullable = false)
    var contents: String,

    @ManyToOne(fetch = FetchType.LAZY)
    val plan: Plan,

    @ManyToOne(fetch = FetchType.LAZY)
    var member: Member,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null,

    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime? = null,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    companion object {
        fun of(content: String, member: Member, plan: Plan): Comment {
            return Comment(
                contents = content,
                member = member,
                plan = plan,
                createdAt = LocalDateTime.now()
            )
        }
    }

    fun updateComment(content: String) {
        this.contents = content
        this.updatedAt = LocalDateTime.now()
    }

    fun deleteComment() {
        this.deletedAt = LocalDateTime.now()
    }
}