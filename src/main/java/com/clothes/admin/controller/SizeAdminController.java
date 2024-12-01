package com.clothes.admin.controller;

import com.clothes.admin.service.SizeAdminService;
import com.clothes.noc.dto.response.ApiResponse;
import com.clothes.noc.dto.response.ColorResponse;
import com.clothes.noc.dto.response.SizeResponse;
import com.clothes.noc.entity.Color;
import com.clothes.noc.entity.Size;
import com.clothes.noc.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("admin/sizes")
public class SizeAdminController {
    private final SizeAdminService sizeService;
    private final ProductMapper productMapper;

    @Autowired
    public SizeAdminController(SizeAdminService sizeService, ProductMapper productMapper) {
        this.sizeService = sizeService;
        this.productMapper = productMapper;
    }

    @PostMapping
    public ApiResponse<SizeResponse> createSize(@RequestBody Size size) {
        Size savedSize = sizeService.save(size);
        return ApiResponse.<SizeResponse>builder()
                .message("Size created successfully")
                .body(productMapper.toSizeResponse(savedSize))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<SizeResponse> getSize(@PathVariable String id) {
        Size size = sizeService.findById(id);
        return ApiResponse.<SizeResponse>builder()
                .message("Size found")
                .body(productMapper.toSizeResponse(size))
                .build();
    }

    @GetMapping
    public ApiResponse<List<SizeResponse>> getAllSizes() {
        List<Size> sizes = sizeService.findAll();
        List<SizeResponse> responses = sizes.stream()
                .map(productMapper::toSizeResponse)
                .collect(Collectors.toList());
        return ApiResponse.<List<SizeResponse>>builder()
                .message("Sizes retrieved")
                .body(responses)
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<SizeResponse> updateSize(@PathVariable String id, @RequestBody Size size) {
        Size updatedSize = sizeService.update(id, size);
        return ApiResponse.<SizeResponse>builder()
                .message("Size updated")
                .body(productMapper.toSizeResponse(updatedSize))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteSize(@PathVariable String id) {
        sizeService.deleteById(id);
        return ApiResponse.<Void>builder()
                .message("Size deleted successfully")
                .body(null)
                .build();
    }

    @GetMapping("/variant/{id}")
    public ApiResponse<SizeResponse> getSizeByProductVariantId(@PathVariable String id) {
        Size size = sizeService.getSizeByProductVariantId(id);
        return ApiResponse.<SizeResponse>builder()
                .message("Color updated")
                .body(productMapper.toSizeResponse(size))
                .build();
    }
}
