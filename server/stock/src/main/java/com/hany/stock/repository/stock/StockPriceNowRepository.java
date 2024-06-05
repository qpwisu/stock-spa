package com.hany.stock.repository.stock;

import com.hany.stock.domain.dto.TopNowStockPriceDto;
import com.hany.stock.domain.dto.TopThemeNowStockPriceDto;
import com.hany.stock.domain.entity.stock.StockPriceNow;
import com.hany.stock.domain.entity.stock.Stocks;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockPriceNowRepository extends JpaRepository<StockPriceNow, Long> {

    @Query("SELECT s FROM StockPriceNow s ORDER BY s.changeRate DESC")
    List<StockPriceNow> findTopByOrderByChangeRateDesc(Pageable pageable);


    @Query("SELECT new com.hany.stock.domain.dto.TopThemeNowStockPriceDto(ts.theme, AVG(spn.changeRate)) " +
            "FROM ThemeStock ts " +
            "JOIN ts.stock s " +
            "JOIN StockPriceNow spn ON spn.stock = s " +
            "GROUP BY ts.theme " +
            "ORDER BY AVG(spn.changeRate) DESC")
    List<TopThemeNowStockPriceDto> findTopChangeRateThemes(Pageable pageable);


    Optional<StockPriceNow> findAllByStock(Stocks stock);
}
