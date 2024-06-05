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
  private long stockId;
  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  @JoinColumn(name = "stock_id", referencedColumnName = "id")
  private Stocks stock;
  @Column(name = "date")
  private LocalDateTime date;
  @Column(name = "open")
  private Double open;
  @Column(name = "high")
  private Double high;
  @Column(name = "low")
  private Double low;
  @Column(name = "close")
  private Double close;
  @Column(name = "volume")
  private Double volume;
  @Column(name = "change_rate")
  private Double changeRate;
}
