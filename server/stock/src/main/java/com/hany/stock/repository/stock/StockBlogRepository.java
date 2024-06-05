package com.hany.stock.repository.stock;

import com.hany.stock.domain.entity.stock.StockBlog;
import com.hany.stock.domain.entity.stock.Stocks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockBlogRepository extends JpaRepository<StockBlog,Long> {
    Page<StockBlog> findAllByStockOrderByDateDesc(Stocks stock, Pageable pageable);
}
