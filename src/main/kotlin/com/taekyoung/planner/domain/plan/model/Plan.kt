package com.taekyoung.planner.domain.plan.model

import com.taekyoung.planner.domain.member.model.Member
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "plans")
class Plan(
    @Column(name = "title", nullable = false)
    var title: String,

    @Column(name = "content", nullable = false)
    var content: String,

    @ManyToOne(fetch = FetchType.LAZY)
    var member: Member,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: PlanStatus,

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
        fun of(title: String, content: String, member: Member): Plan {
            return Plan(
                title = title,
                content = content,
                member = member,
                status = PlanStatus.READY,
                createdAt = LocalDateTime.now()
            )
        }
    }

    fun updatePlan(title: String, content: String, status: PlanStatus) {
        this.title = title
        this.content = content
        this.status = status
        this.updatedAt = LocalDateTime.now()
    }

    fun deletePlan() {
        this.deletedAt = LocalDateTime.now()
    }
}