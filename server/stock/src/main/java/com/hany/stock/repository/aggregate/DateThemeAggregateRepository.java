package com.hany.stock.repository.aggregate;

import com.hany.stock.domain.entity.theme.Themes;
import com.hany.stock.domain.entity.aggregate.DateThemeAggregate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface    DateThemeAggregateRepository extends JpaRepository<DateThemeAggregate, Long> {
    // 테마 객체로 모든 관련 DateThemeAggregate 엔트리 찾기
    List<DateThemeAggregate> findAllByTheme(Themes theme);

    // 테마 ID로 모든 관련 DateThemeAggregate 엔트리 찾기
    List<DateThemeAggregate> findAllByThemeId(Long themeId);
}
