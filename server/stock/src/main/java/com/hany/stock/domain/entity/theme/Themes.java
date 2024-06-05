package com.hany.stock.domain.entity.theme;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hany.stock.domain.entity.stock.StockPriceNow;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "themes")
@Getter
@NoArgsConstructor
public class Themes {

  @Column(name = "id")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @Column(name = "theme_name")
  private String themeName;
  @OneToOne(mappedBy = "theme", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
  @JsonIgnore // 순환 참조를 방지하기 위해 사용
  private ThemeAverageChangeRate themeAverageChangeRate;
}
