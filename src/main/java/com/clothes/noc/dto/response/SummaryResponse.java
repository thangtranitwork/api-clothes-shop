package com.clothes.noc.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class SummaryResponse {
    List<String> labels;
    List<Integer> previous;
    List<Integer> current;
    List<Map<String, List<Integer>>> data;
}
