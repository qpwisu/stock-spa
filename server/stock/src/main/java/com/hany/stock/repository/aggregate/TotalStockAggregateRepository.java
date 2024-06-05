package com.hany.stock.repository.aggregate;

import com.hany.stock.domain.entity.aggregate.TotalStockAggregate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TotalStockAggregateRepository extends JpaRepository<TotalStockAggregate, Long> {
}
