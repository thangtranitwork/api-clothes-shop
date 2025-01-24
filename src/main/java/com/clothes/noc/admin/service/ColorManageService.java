package com.clothes.noc.admin.service;

import com.clothes.noc.dto.request.ColorRequest;
import com.clothes.noc.dto.response.ColorResponse;
import com.clothes.noc.entity.Color;
import com.clothes.noc.exception.AppException;
import com.clothes.noc.exception.ErrorCode;
import com.clothes.noc.mapper.ColorMapper;
import com.clothes.noc.repository.ColorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ColorManageService {
    private final ColorRepository colorRepository;
    private final ColorMapper colorMapper;

    public ColorResponse add(ColorRequest request) {
        request.setCode(request.getCode().toUpperCase());
        if (colorRepository.existsByCodeOrName(request.getCode(), request.getName()))
            throw new AppException(ErrorCode.DUPLICATE_COLOR);

        return colorMapper.toColorResponse(
                colorRepository.save(colorMapper.toColor(request)));
    }

    public ColorResponse get(String code) {
        return colorMapper.toColorResponse(
                colorRepository.findById(code)
                        .orElseThrow(() -> new AppException(ErrorCode.COLOR_NOT_FOUND)));
    }

    public Color getEntity(String code) {
        return colorRepository.findById(code)
                .orElseThrow(() -> new AppException(ErrorCode.COLOR_NOT_FOUND));
    }

    public List<ColorResponse> getAll() {
        return colorRepository.findAll().stream()
                .map(colorMapper::toColorResponse).toList();
    }

    public void delete(String code) {
        if (colorRepository.countVariantByCode(code) > 0) {
            throw new AppException(ErrorCode.COLOR_DELETE_FAILED);
        }
        colorRepository.deleteById(code);
    }
}
