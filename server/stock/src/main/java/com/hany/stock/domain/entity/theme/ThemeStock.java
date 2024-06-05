package com.hany.stock.domain.entity.theme;

import com.hany.stock.domain.entity.stock.Stocks;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Table(name = "theme_stock")
@Getter
@NoArgsConstructor
public class ThemeStock {

  @Column(name = "id")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @ManyToOne
  @JoinColumn(name = "stock_id", referencedColumnName = "id", nullable = false)
  private Stocks stock;
  @ManyToOne
  @JoinColumn(name = "theme_id", referencedColumnName = "id", nullable = false)
  private Themes theme;

}
