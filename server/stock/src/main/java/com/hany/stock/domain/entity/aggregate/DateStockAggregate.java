package com.hany.stock.domain.entity.aggregate;

import com.hany.stock.domain.entity.stock.Stocks;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Table(name = "date_stock_aggregate")
@Getter
@NoArgsConstructor
public class DateStockAggregate {

  @Column(name = "id")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @Column(name = "date")
  private java.sql.Date date;
  @Column(name = "cnt")
  private long cnt;
  @ManyToOne
  @JoinColumn(name = "stock_id", referencedColumnName = "id", nullable = false)
  private Stocks stock;

}
