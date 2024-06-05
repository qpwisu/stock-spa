package com.hany.stock.repository.aggregate;

import com.hany.stock.domain.entity.aggregate.RelatedStockAggregate;
import com.hany.stock.domain.entity.stock.Stocks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelatedStockAggregateRepository extends JpaRepository<RelatedStockAggregate, Long> {
    List<RelatedStockAggregate> findAllBySearchStock(Stocks stock);
}
