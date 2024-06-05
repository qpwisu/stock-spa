package com.hany.stock.repository.theme;

import com.hany.stock.domain.dto.TopThemeNowStockPriceDto;
import com.hany.stock.domain.entity.theme.ThemeAverageChangeRate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.ui.context.Theme;

import java.util.List;

public interface ThemeAverageChangeRateRepository extends JpaRepository<ThemeAverageChangeRate,Long> {
    List<ThemeAverageChangeRate> findTop20ByOrderByAverageChangeRateDesc();
}
