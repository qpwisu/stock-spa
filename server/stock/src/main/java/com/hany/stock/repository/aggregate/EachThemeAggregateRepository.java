package com.hany.stock.repository.aggregate;

import com.hany.stock.domain.entity.EachThemeAggregate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EachThemeAggregateRepository extends JpaRepository<EachThemeAggregate, Long> {
    List<EachThemeAggregate> findAllByThemeId(Long themeId);

}
