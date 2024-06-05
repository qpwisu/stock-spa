package com.hany.stock.domain.entity.stock;

import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_price_now")
@Getter
@NoArgsConstructor
public class StockPriceNow {

  @Id
  @Column(name = "stock_id")
  private Long stockId;
  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  @JoinColumn(name = "stock_id", referencedColumnName = "id")
  private Stocks stock;
  @Column(name = "date")
  private LocalDateTime date;
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
}
