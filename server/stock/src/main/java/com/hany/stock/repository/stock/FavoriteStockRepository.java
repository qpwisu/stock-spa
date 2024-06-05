package com.hany.stock.repository.stock;

import com.hany.stock.domain.entity.stock.FavoriteStock;
import com.hany.stock.domain.entity.stock.Stocks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteStockRepository extends JpaRepository<FavoriteStock,Long> {
    void deleteByUserLoginIdAndStock(String loginId, Stocks stock);
    Boolean existsByUserLoginIdAndStock(String loginId,  Stocks stock);
    List<FavoriteStock> findAllByUserLoginId(String loginId);
}
