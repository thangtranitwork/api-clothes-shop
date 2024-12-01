package com.clothes.admin.controller;

import com.clothes.admin.service.ColorAdminService;
import com.clothes.noc.dto.response.ApiResponse;
import com.clothes.noc.dto.response.ColorResponse;
import com.clothes.noc.entity.Color;
import com.clothes.noc.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("admin/colors")
@RequiredArgsConstructor
public class ColorAdminController {
    private final ColorAdminService colorService;
    private final ProductMapper productMapper;

    @PostMapping
    public ApiResponse<ColorResponse> createColor(@RequestBody Color color) {
        Color savedColor = colorService.save(color);
        return ApiResponse.<ColorResponse>builder()
                .message("Color created successfully")
                .body(productMapper.toColorResponse(savedColor))
                .build();
    }

    @GetMapping("/{code}")
    public ApiResponse<ColorResponse> getColor(@PathVariable String code) {
        Color color = colorService.findByCode(code);
        return ApiResponse.<ColorResponse>builder()
                .message("Color found")
                .body(productMapper.toColorResponse(color))
                .build();
    }

    @GetMapping
    public ApiResponse<List<ColorResponse>> getAllColors() {
        List<Color> colors = colorService.findAll();
        List<ColorResponse> responses = colors.stream()
                .map(productMapper::toColorResponse)
                .collect(Collectors.toList());
        return ApiResponse.<List<ColorResponse>>builder()
                .message("Colors retrieved")
                .body(responses)
                .build();
    }

    @PutMapping("/{code}")
    public ApiResponse<ColorResponse> updateColor(@PathVariable String code, @RequestBody Color color) {
        Color updatedColor = colorService.update(code, color);
        return ApiResponse.<ColorResponse>builder()
                .message("Color updated")
                .body(productMapper.toColorResponse(updatedColor))
                .build();
    }

    @DeleteMapping("/{code}")
    public ApiResponse<Void> deleteColor(@PathVariable String code) {
        colorService.deleteByCode(code);
        return ApiResponse.<Void>builder()
                .message("Color deleted successfully")
                .body(null)
                .build();
    }

    @GetMapping("/variant/{id}")
    public ApiResponse<ColorResponse> getColorByProductVariantId(@PathVariable String id) {
        Color color = colorService.getColorByProductVariantId(id);
        return ApiResponse.<ColorResponse>builder()
                .message("Color updated")
                .body(productMapper.toColorResponse(color))
                .build();
    }
}
