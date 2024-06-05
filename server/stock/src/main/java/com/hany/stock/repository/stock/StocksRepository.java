package com.hany.stock.repository.stock;

import com.hany.stock.domain.entity.stock.Stocks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StocksRepository extends JpaRepository<Stocks,Long> {
    Optional<Stocks> findByCompanyName(String companyName);
    Optional<Stocks>  findByTicker(String ticker);

    @Query("SELECT s FROM Stocks s WHERE s.ticker LIKE %:keyword%")
    List<Stocks> searchByTicker(@Param("keyword") String keyword);

    @Query("SELECT s FROM Stocks s WHERE s.companyName LIKE %:keyword%")
    List<Stocks> searchByCompanyName(@Param("keyword") String keyword);
}
