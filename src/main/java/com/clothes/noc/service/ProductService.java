package com.clothes.noc.service;

import com.clothes.noc.dto.request.SearchProductRequest;
import com.clothes.noc.dto.response.*;
import com.clothes.noc.entity.Product;
import com.clothes.noc.entity.ProductVariant;
import com.clothes.noc.exception.AppException;
import com.clothes.noc.exception.ErrorCode;
import com.clothes.noc.mapper.ProductMapper;
import com.clothes.noc.mapper.SizeMapper;
import com.clothes.noc.mapper.VariantMapper;
import com.clothes.noc.repository.ColorRepository;
import com.clothes.noc.repository.ProductRepository;
import com.clothes.noc.repository.SizeRepository;
import com.clothes.noc.repository.spec.ProductSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final SizeRepository sizeRepository;
    private final ColorRepository colorRepository;
    private final ProductMapper productMapper;
    private final VariantMapper variantMapper;
    private final SizeMapper sizeMapper;

    public ProductWithVariantResponse getByPath(String path) {
        Product product = productRepository.findByPath(path)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXIST));
        ProductFullResponse productFullResponse = productMapper.toProductFullResponse(product);
        productFullResponse.setColors(getColorResponses(productFullResponse.getId()));
        productFullResponse.setSizes(getSizeResponses(productFullResponse.getId()));
        Set<String> imgs = new HashSet<>(Arrays.asList(product.getImg(), product.getHoverImg()));
        List<ProductVariant> variants = product.getProductVariants();
        variants.forEach(productVariant ->
                imgs.add(productVariant.getImg()));
        return ProductWithVariantResponse.builder()
                .product(productFullResponse)
                .variants(variants
                        .stream()
                        .map(variantMapper::toProductVariantResponse)
                        .toList())
                .imgs(imgs.stream().toList())
                .build();
    }

    public Page<ProductResponse> getHotProducts(Pageable pageable) {
        return productRepository.findHotProducts(pageable)
                .map(productMapper::toProductResponse);
    }

    public Page<ProductResponse> search(SearchProductRequest request, Pageable pageable) {
        return productRepository
                .findAll(ProductSpecifications.multipleFieldsSearch(request), pageable)
                .map(productMapper::toProductResponse)
                .map(product -> {
                    // Gắn danh sách màu vào product response
                    List<ColorResponse> colors = getColorResponses(product.getId());
                    List<SizeResponse> sizes = getSizeResponses(product.getId());
                    product.setColors(colors);
                    product.setSizes(sizes);
                    return product;
                });
    }

    private List<SizeResponse> getSizeResponses(String id) {
        return productRepository
                .findAllSizesOfAProduct(id)
                .stream()
                .map(size -> SizeResponse.builder()
                        .name(size.getName())
                        .build())
                .toList();
    }

    private List<ColorResponse> getColorResponses(String id) {
        return productRepository
                .findAllColorsOfAProduct(id)
                .stream()
                .map(color -> ColorResponse.builder()
                        .name(color.getName())
                        .code(color.getCode())
                        .build())
                .toList();
    }

    public ColorsAndSizeResponse getColorsAndSizes() {
        return ColorsAndSizeResponse.builder()
                .colors(colorRepository.findAll().stream()
                        .map(color -> ColorResponse.builder()
                                .code(color.getCode())
                                .name(color.getName())
                                .build()).toList())
                .sizes(sizeRepository.findAll().stream()
                        .map(sizeMapper::toSizeResponse)
                        .toList())
                .build();
    }

    public ColorsAndSizeResponse getColorsAndSizesOfAType(String type) {
        return ColorsAndSizeResponse.builder()
                .colors(productRepository.findAllColorsOfAType(type).stream()
                        .map(color -> ColorResponse.builder()
                                .code(color.getCode())
                                .name(color.getName())
                                .build())
                        .toList())
                .sizes(productRepository.findAllSizesOfAType(type).stream()
                        .map(sizeMapper::toSizeResponse)
                        .toList())
                .build();

    }

    public ColorsAndSizeResponse getColorsAndSizesOfASubtype(String subtype) {
        return ColorsAndSizeResponse.builder()
                .colors(productRepository.findAllColorsOfASubType(subtype).stream()
                        .map(color -> ColorResponse.builder()
                                .code(color.getCode())
                                .name(color.getName())
                                .build())
                        .toList())
                .sizes(productRepository.findAllSizesOfASubType(subtype).stream()
                        .map(sizeMapper::toSizeResponse)
                        .toList())
                .build();

    }
}
