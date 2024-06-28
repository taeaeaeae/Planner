package com.taekyoung.planner.domain.member.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

@Entity
@Table(name = "members")
class Member(
    @Column(name = "email", nullable = false, unique = true)
    val email: String,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "password", nullable = false)
    var password: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    val role: MemberRole,

    @Column(name = "image_url")
    var imageUrl: String?,

    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime? = null,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun updatePassword(newPassword: String) {
        if (!newPassword.matches("^[a-zA-Z0-9!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]{8,15}\$".toRegex())) {
            throw IllegalArgumentException("Invalid Password.")
        }
        this.password = newPassword
    }

    fun updateProfile(newName: String, newImageUrl: String) {
        this.name = newName
        this.imageUrl = newImageUrl
    }

    companion object {

        fun of(
            email: String,
            name: String,
            password: String,
            role: MemberRole,
            imageUrl: String? = null,
        ): Member {
            return Member(
                email = email, name = name, password = password, role = role, imageUrl = imageUrl
            )
        }
    }


    fun softDelete() {
        this.deletedAt = LocalDateTime.now()
    }


}