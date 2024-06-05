package com.hany.stock.repository.aggregate;

import com.hany.stock.domain.entity.aggregate.ThemeMentionIncrements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThemeMentionIncrementsRepository extends JpaRepository<ThemeMentionIncrements, Long> {
}
