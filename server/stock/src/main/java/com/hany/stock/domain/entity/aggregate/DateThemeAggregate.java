package com.hany.stock.domain.entity.aggregate;

import com.hany.stock.domain.entity.theme.Themes;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Table(name = "date_theme_aggregate")
@Getter
@NoArgsConstructor
public class DateThemeAggregate {

  @Column(name = "id")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @Column(name = "date")
  private java.sql.Date date;
  @Column(name = "cnt")
  private long cnt;
  @ManyToOne
  @JoinColumn(name = "theme_id", referencedColumnName = "id", nullable = false)
  private Themes theme;


}
