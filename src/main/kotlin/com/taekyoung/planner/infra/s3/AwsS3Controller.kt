package com.taekyoung.planner.infra.s3

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class AwsS3Controller(
    private val service: FileUploadService
) {

    @PostMapping("/upload")
    fun upload(@RequestParam("file") file: MultipartFile): ResponseEntity<String> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(service.uploadFile(file))
    }
}