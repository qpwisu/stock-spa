package com.hany.stock.repository.aggregate;

import com.hany.stock.domain.entity.aggregate.SuggestMonthStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuggestMonthStockRepository extends JpaRepository<SuggestMonthStock,Long> {
}
