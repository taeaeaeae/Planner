package com.taekyoung.planner.infra.s3

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.ObjectMetadata
import io.jsonwebtoken.io.IOException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class FileUploadService(
    private val amazonS3Client: AmazonS3Client
) {
    @Value("\${AWS_BUCKET}")
    private lateinit var bucket: String

    fun uploadFile(file: MultipartFile): String? {
        val image = listOf("jpg", "jpeg", "png", "gif", "bmp")
        val fileName = file.originalFilename ?: throw IOException("File name is empty")
        val exception = fileName.substringAfterLast('.')
        if (!image.contains(exception)) return null
        val metadata = ObjectMetadata().apply {
            contentType = "image/${exception}"
            contentLength = file.size
        }
        //파일 저장하는부분
        amazonS3Client.putObject(bucket, fileName, file.inputStream, metadata)
        return amazonS3Client.getUrl(bucket, fileName).toString()
    }
}