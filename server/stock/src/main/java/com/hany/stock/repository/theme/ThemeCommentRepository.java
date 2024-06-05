package com.hany.stock.repository.theme;

import com.hany.stock.domain.entity.theme.ThemeComment;
import com.hany.stock.domain.entity.theme.Themes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThemeCommentRepository extends JpaRepository<ThemeComment,Long> {
    List<ThemeComment> findAllByTheme(Themes Theme);
}
