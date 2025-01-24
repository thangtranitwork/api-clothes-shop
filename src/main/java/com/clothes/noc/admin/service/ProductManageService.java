package com.clothes.noc.admin.service;

import com.clothes.noc.dto.request.ProductCreationRequest;
import com.clothes.noc.dto.request.UpdatePriceRequest;
import com.clothes.noc.dto.response.AdminProductResponse;
import com.clothes.noc.dto.response.ProductCreationResponse;
import com.clothes.noc.entity.Product;
import com.clothes.noc.entity.ProductType;
import com.clothes.noc.exception.AppException;
import com.clothes.noc.exception.ErrorCode;
import com.clothes.noc.mapper.ProductMapper;
import com.clothes.noc.repository.ProductRepository;
import com.clothes.noc.service.CloudinaryService;
import com.cloudinary.Transformation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductManageService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CloudinaryService cloudinaryService;
    private final ProductTypeManageService productTypeManageService;

    @Transactional
    public ProductCreationResponse add(ProductCreationRequest request) {
        Product product = productMapper.toProduct(request);
        ProductType productType = productTypeManageService.get(request.getType());

        // Upload images
        product.setImg(uploadImage(request.getImage(), product.getPath(), "product_img"));
        product.setHoverImg(uploadImage(request.getHoverImage(), product.getPath(), "product_hover_img"));
        product.setType(productType);

        // Save and return response
        productRepository.save(product);
        return productMapper.toProductCreationResponse(product);
    }


    private String uploadImage(MultipartFile image, String path, String prefix) {
        if (image == null || image.isEmpty()) {
            return null;
        }
        return cloudinaryService.upload(image,
                Map.of("public_id", path,
                        "public_id_prefix", prefix,
                        "format", "webp",
                        "transformation", new Transformation<>().width(1024).height(1024).crop("fill")
                ));
    }

    public Product getEntity(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXIST));
    }

    public AdminProductResponse get(String path) {
        AdminProductResponse response = productMapper.toAdminProductResponse(productRepository.findByPath(path)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXIST)));
        HashSet<String> imgs = new HashSet<>(Arrays.asList(response.getImg(), response.getHoverImg()));
        response.getVariants()
                .forEach(variant ->
                        imgs.add(variant.getImg()));
        response.setImgs(imgs);
        return response;
    }

    @Transactional
    public void updatePrice(String id, UpdatePriceRequest request) {
        productRepository.findById(id).ifPresentOrElse(product -> {
            product.setPrice(request.price());
            productRepository.save(product);
        }, () -> {
            throw new AppException(ErrorCode.PRODUCT_NOT_EXIST);
        });
    }

    @Transactional
    public void delete(String id) {
        productRepository.findById(id).ifPresentOrElse(product -> {
            if (!product.getProductVariants().isEmpty()) {
                throw new AppException(ErrorCode.PRODUCT_DELETE_FAILED);
            }
            productRepository.delete(product);
            cloudinaryService.deleteImage(product.getImg());
            cloudinaryService.deleteImage(product.getHoverImg());
        }, () -> {
            throw new AppException(ErrorCode.PRODUCT_NOT_EXIST);
        });
    }
}
