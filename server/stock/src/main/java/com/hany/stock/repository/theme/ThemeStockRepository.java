package com.hany.stock.repository.theme;

import com.hany.stock.domain.entity.stock.Stocks;
import com.hany.stock.domain.entity.theme.ThemeStock;
import com.hany.stock.domain.entity.theme.Themes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThemeStockRepository extends JpaRepository<ThemeStock,Long> {
    List<ThemeStock> findAllByStock(Stocks stock);
    List<ThemeStock> findAllByTheme(Themes theme);

}
