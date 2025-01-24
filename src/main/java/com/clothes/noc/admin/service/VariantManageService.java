package com.clothes.noc.admin.service;

import com.clothes.noc.dto.request.UpdateVariantQuantityRequest;
import com.clothes.noc.dto.request.VariantCreationRequest;
import com.clothes.noc.dto.response.ProductVariantResponse;
import com.clothes.noc.entity.Color;
import com.clothes.noc.entity.Product;
import com.clothes.noc.entity.ProductVariant;
import com.clothes.noc.entity.Size;
import com.clothes.noc.exception.AppException;
import com.clothes.noc.exception.ErrorCode;
import com.clothes.noc.mapper.VariantMapper;
import com.clothes.noc.repository.ProductVariantRepository;
import com.clothes.noc.service.CloudinaryService;
import com.cloudinary.Transformation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class VariantManageService {
    private final VariantMapper variantMapper;
    private final ColorManageService colorManageService;
    private final SizeManageService sizeManageService;
    private final ProductManageService productManageService;
    private final ProductVariantRepository productVariantRepository;
    private final CloudinaryService cloudinaryService;

    @Transactional
    public ProductVariantResponse addVariant(String productId, VariantCreationRequest request) {
        ProductVariant productVariant = new ProductVariant();
        if (productVariantRepository.existsByProductIdAndColorCodeAndSizeName(productId, request.getColor(), request.getSize())) {
            throw new AppException(ErrorCode.DUPLICATE_PRODUCT_VARIANT);
        }
        Product product = productManageService.getEntity(productId);
        productVariant.setProduct(product);
        Color color = colorManageService.getEntity(request.getColor());
        productVariant.setColor(color);
        Size size = sizeManageService.getEntity(request.getSize());
        productVariant.setSize(size);
        productVariant.setQuantity(request.getQuantity());
        if (!request.getExistedImageUrl().isEmpty()) {
            productVariant.setImg(request.getExistedImageUrl());
        } else {
            String publicId = String.format("%s-%s-%s",
                    product.getPath(),
                    color.getName().replace(" ", "-").toLowerCase(),
                    size.getName().replace(" ", "-").toLowerCase());
            productVariant.setImg(cloudinaryService.upload(request.getImg(), Map.of(
                    "public_id", publicId,
                    "public_id_prefix", "variant_img",
                    "format", "webp",
                    "transformation", new Transformation<>().width(1024).height(1024).crop("fill")
            )));
        }
        return variantMapper.toProductVariantResponse(productVariantRepository.save(productVariant));
    }
    public void updateQuantity(String id, UpdateVariantQuantityRequest request) {
        productVariantRepository.findById(id).ifPresentOrElse(variant -> {
            if(!variant.getCartItems().isEmpty()) {
                throw new AppException(ErrorCode.PRODUCT_DELETE_FAILED);
            }
            variant.setQuantity(request.quantity());
            productVariantRepository.save(variant);
        }, () -> {
            throw new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND);
        });
    }
    public void delete(String id) {
        productVariantRepository.findById(id).ifPresentOrElse(variant -> {
            if(!variant.getCartItems().isEmpty()) {
                throw new AppException(ErrorCode.PRODUCT_DELETE_FAILED);
            }
            productVariantRepository.delete(variant);
        }, () -> {
            throw new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND);
        });
    }
}
