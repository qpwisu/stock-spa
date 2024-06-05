package com.hany.stock.repository.stock;

import com.hany.stock.domain.entity.stock.StockInfo;
import com.hany.stock.domain.entity.stock.Stocks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockInfoRepository extends JpaRepository<StockInfo,Long> {

    Optional<StockInfo> findByStock(Stocks stock);
}
