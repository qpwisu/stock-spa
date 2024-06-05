package com.hany.stock.repository.theme;

import com.hany.stock.domain.entity.stock.StockBlog;
import com.hany.stock.domain.entity.stock.Stocks;
import com.hany.stock.domain.entity.theme.ThemeBlog;
import com.hany.stock.domain.entity.theme.Themes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThemeBlogRepository extends JpaRepository<ThemeBlog,Long> {
    Page<ThemeBlog> findAllByThemeOrderByDateDesc(Themes theme, Pageable pageable);
}
