package com.hany.stock.repository.theme;

import com.hany.stock.domain.entity.stock.FavoriteStock;
import com.hany.stock.domain.entity.stock.Stocks;
import com.hany.stock.domain.entity.theme.FavoriteTheme;
import com.hany.stock.domain.entity.theme.Themes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteThemeRepository extends JpaRepository<FavoriteTheme,Long> {
    void deleteByUserLoginIdAndTheme(String loginId, Themes theme);
    Boolean existsByUserLoginIdAndTheme(String loginId,  Themes theme);
    List<FavoriteTheme> findAllByUserLoginId(String loginId);
}
