package com.hany.stock.domain.entity.stock;

import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Table(name = "stock_price")
@Getter
@NoArgsConstructor
public class StockPrice {

  @Column(name = "id")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "date")
  private java.sql.Date date;
  @Column(name = "open")
  private Double open;
  @Column(name = "high")
  private Double high;
  @Column(name = "low")
  private Double low;
  @Column(name = "close")
  private Double close;
  @Column(name = "volume")
  private Long volume;
  @Column(name = "change_rate")
  private Double changeRate;
  @ManyToOne
  @JoinColumn(name = "stock_id", referencedColumnName = "id", nullable = false)
  private Stocks stock;

}
