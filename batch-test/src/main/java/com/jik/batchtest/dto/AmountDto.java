package com.jik.batchtest.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AmountDto {
    private int index;
    private String name;
    private int amount;
}
