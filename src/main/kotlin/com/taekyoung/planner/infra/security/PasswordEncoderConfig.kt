package com.taekyoung.planner.infra.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class PasswordEncoderConfig {

    @Bean
    fun passwordEncoder() : PasswordEncoder {
        return BCryptPasswordEncoder()
    }
    fun checkPasswordRule(password: String) {
        if (!password.matches("^[a-zA-Z0-9!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]{8,15}\$".toRegex())) {
            throw IllegalArgumentException("Invalid Password.")
        }
    }
}