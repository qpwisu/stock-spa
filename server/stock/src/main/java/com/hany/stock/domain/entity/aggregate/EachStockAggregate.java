package com.hany.stock.domain.entity.aggregate;

import com.hany.stock.domain.entity.stock.Stocks;
import com.hany.stock.domain.entity.theme.Themes;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Table(name = "each_stock_aggregate")
@Getter
@NoArgsConstructor
public class EachStockAggregate {

  @Column(name = "id")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @Column(name = "cnt")
  private long cnt;
  @Column(name = "ranking")
  private long ranking;
  @Column(name = "period")
  private long period;
  @ManyToOne
  @JoinColumn(name = "stock_id", referencedColumnName = "id", nullable = false)
  private Stocks stock;
  @ManyToOne
  @JoinColumn(name = "theme_id", referencedColumnName = "id", nullable = false)
  private Themes theme;

}
