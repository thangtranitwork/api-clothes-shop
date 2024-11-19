package com.clothes.noc.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryService {
    public static final Map SQUARE_100 = ObjectUtils.asMap(
            "transformation", new Transformation().width(100).height(100).crop("fill")
    );

    private final Cloudinary cloudinary;

    public String upload(MultipartFile file, Map options) {
        try {
            Map uploadResult = this.cloudinary.uploader().upload(file.getBytes(), options);
            return uploadResult.get("url").toString();
        } catch (IOException io) {
            throw new RuntimeException("Image upload fail", io);
        }
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
        // https://res.cloudinary.com/{cloud_name}/image/upload/{public_id}.{format}
        String publicIdWithExtension = url.substring(url.lastIndexOf('/') + 1);
        return publicIdWithExtension.split("\\.")[0];
    }
}
