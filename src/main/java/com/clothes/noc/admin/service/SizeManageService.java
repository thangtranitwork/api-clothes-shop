package com.clothes.noc.admin.service;

import com.clothes.noc.dto.request.SizeCreationRequest;
import com.clothes.noc.dto.response.SizeResponse;
import com.clothes.noc.entity.Size;
import com.clothes.noc.exception.AppException;
import com.clothes.noc.exception.ErrorCode;
import com.clothes.noc.mapper.SizeMapper;
import com.clothes.noc.repository.SizeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SizeManageService {
    private final SizeRepository sizeRepository;
    private final SizeMapper sizeMapper;

    public Size getEntity(String id) {
        return sizeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SIZE_NOT_FOUND));

    }

    public SizeResponse add(SizeCreationRequest request) {
        if (sizeRepository.findById(request.getName()).isPresent())
            throw new AppException(ErrorCode.DUPLICATE_SIZE);
        return sizeMapper.toSizeResponse(sizeRepository.save(sizeMapper.toSize(request)));
    }

    public SizeResponse get(String id) {
        return sizeMapper.toSizeResponse(
                sizeRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.SIZE_NOT_FOUND)));
    }

    public List<SizeResponse> getAll() {
        return sizeRepository.findAll().stream()
                .map(sizeMapper::toSizeResponse).toList();
    }

    public void delete(String name) {
        if (sizeRepository.countVariantByName(name) > 0) {
            throw new AppException(ErrorCode.SIZE_DELETE_FAILED);
        }
        sizeRepository.deleteById(name);
    }
}
