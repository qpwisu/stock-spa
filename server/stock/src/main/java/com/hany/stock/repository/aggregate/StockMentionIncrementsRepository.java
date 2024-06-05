package com.hany.stock.repository.aggregate;

import com.hany.stock.domain.entity.aggregate.StockMentionIncrements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockMentionIncrementsRepository extends JpaRepository<StockMentionIncrements, Long> {
}
