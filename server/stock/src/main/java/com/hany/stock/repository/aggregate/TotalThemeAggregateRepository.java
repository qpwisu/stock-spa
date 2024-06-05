package com.hany.stock.repository.aggregate;

import com.hany.stock.domain.entity.aggregate.TotalThemeAggregate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TotalThemeAggregateRepository extends JpaRepository<TotalThemeAggregate, Long> {
}
