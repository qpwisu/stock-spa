package com.hany.stock.domain.entity.theme;

import com.hany.stock.domain.entity.theme.Themes;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "theme_average_change_rate")
@Getter
@NoArgsConstructor
public class ThemeAverageChangeRate {

  @Id
  @Column(name = "theme_id")
  private Long themeId;

  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  @JoinColumn(name = "theme_id")
  private Themes theme;

  @Column(name = "average_change_rate")
  private double averageChangeRate;
  @Column(name = "cnt")
  private long cnt;

}
