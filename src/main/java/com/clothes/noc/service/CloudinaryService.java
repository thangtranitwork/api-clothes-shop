package com.clothes.noc.service;

import com.clothes.noc.exception.AppException;
import com.clothes.noc.exception.ErrorCode;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public String upload(MultipartFile file, Map<String, Serializable> options) {
        // Kiểm tra file có phải là ảnh không
        if (!isImageFile(file)) {
            throw new AppException(ErrorCode.ONLY_IMAGE_FILES_ARE_ALLOWED);
        }

        try {
            var uploadResult = this.cloudinary.uploader().upload(file.getBytes(), options);
            return uploadResult.get("url").toString();
        } catch (IOException io) {
            throw new AppException(ErrorCode.UPLOAD_IMAGE_FAILED);
        }
    }

    private boolean isImageFile(MultipartFile file) {
        // Kiểm tra content type
        String contentType = file.getContentType();
        if (contentType == null) {
            return false;
        }

        // Danh sách các content type cho phép
        return contentType.startsWith("image/")
                && Arrays.asList(
                "image/jpeg",
                "image/png",
                "image/gif",
                "image/bmp",
                "image/webp"
        ).contains(contentType.toLowerCase());
    }

    public void deleteImage(String url) {
        try {
            String publicId = extractPublicId(url);
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private String extractPublicId(String url) {
        String publicIdWithExtension = url.substring(url.lastIndexOf('/') + 1);
        return publicIdWithExtension.split("\\.")[0];
    }
}
