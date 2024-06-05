package com.hany.stock.repository.stock;

import com.hany.stock.domain.entity.stock.StockComment;
import com.hany.stock.domain.entity.stock.Stocks;
import com.hany.stock.domain.entity.theme.ThemeComment;
import com.hany.stock.domain.entity.theme.Themes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockCommentRepository extends JpaRepository<StockComment,Long> {
    List<StockComment> findAllByStock(Stocks stock);

}
