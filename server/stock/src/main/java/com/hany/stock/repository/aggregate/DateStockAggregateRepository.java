package com.hany.stock.repository.aggregate;

import com.hany.stock.domain.entity.stock.Stocks;
import com.hany.stock.domain.entity.aggregate.DateStockAggregate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DateStockAggregateRepository extends JpaRepository<DateStockAggregate, Long> {
    List<DateStockAggregate> findAllByStock(Stocks stock);
    List<DateStockAggregate> findAllByStockId(Long stockId);
}
