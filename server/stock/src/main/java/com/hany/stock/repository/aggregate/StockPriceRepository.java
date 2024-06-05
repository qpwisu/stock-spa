package com.hany.stock.repository.aggregate;

import com.hany.stock.domain.entity.stock.StockPrice;
import com.hany.stock.domain.entity.stock.Stocks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockPriceRepository extends JpaRepository<StockPrice,String> {
    List<StockPrice> findAllByStock(Stocks stock);

}
