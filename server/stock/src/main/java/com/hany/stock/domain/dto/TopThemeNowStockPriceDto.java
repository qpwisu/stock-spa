package com.hany.stock.domain.dto;

import com.hany.stock.domain.entity.stock.Stocks;
import com.hany.stock.domain.entity.theme.Themes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
@AllArgsConstructor
public class TopThemeNowStockPriceDto {
    private Themes theme;
    private double changeRate;
}
