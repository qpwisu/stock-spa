package com.hany.stock.domain.dto;

import com.hany.stock.domain.entity.stock.Stocks;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
@AllArgsConstructor
public class TopNowStockPriceDto {
    private Stocks stock;
    private double changeRate;
}
