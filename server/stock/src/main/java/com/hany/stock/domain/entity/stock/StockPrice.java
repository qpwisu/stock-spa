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
  private long id;
  @Column(name = "date")
  private java.sql.Date date;
  @Column(name = "open")
  private double open;
  @Column(name = "high")
  private double high;
  @Column(name = "low")
  private double low;
  @Column(name = "close")
  private double close;
  @Column(name = "volume")
  private long volume;
  @Column(name = "change_rate")
  private double changeRate;
  @ManyToOne
  @JoinColumn(name = "stock_id", referencedColumnName = "id", nullable = false)
  private Stocks stock;

}
