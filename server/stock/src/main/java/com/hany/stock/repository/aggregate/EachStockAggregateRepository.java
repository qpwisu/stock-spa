package com.hany.stock.repository.aggregate;

import com.hany.stock.domain.entity.aggregate.EachStockAggregate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EachStockAggregateRepository extends JpaRepository<EachStockAggregate, Long> {
    List<EachStockAggregate> findAllByStockId(Long stockId);

}
