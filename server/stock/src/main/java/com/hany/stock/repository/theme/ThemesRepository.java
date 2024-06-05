package com.hany.stock.repository.theme;

import com.hany.stock.domain.entity.theme.Themes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ThemesRepository extends JpaRepository<Themes,Long> {
    Optional<Themes> findByThemeName(String ThemeName);
    @Query("SELECT e FROM Themes e WHERE e.themeName LIKE %:keyword%")
    List<Themes> searchByThema(@Param("keyword") String keyword);
}
