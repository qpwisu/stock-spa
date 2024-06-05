package com.hany.stock.domain.entity.aggregate;

import com.hany.stock.domain.entity.theme.Themes;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Table(name = "themeMentionIncrements")
@Getter
@NoArgsConstructor
public class ThemeMentionIncrements {

  @Column(name = "id")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @Column(name = "increase")
  private long increase;
  @ManyToOne
  @JoinColumn(name = "theme_id", referencedColumnName = "id", nullable = false)
  private Themes theme;

}
