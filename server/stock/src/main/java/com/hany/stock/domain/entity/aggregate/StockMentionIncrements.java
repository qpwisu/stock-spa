package com.hany.stock.domain.entity.aggregate;

import com.hany.stock.domain.entity.stock.Stocks;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Table(name = "stockMentionIncrements")
@Getter
@NoArgsConstructor
public class StockMentionIncrements {

  @Column(name = "id")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @ManyToOne
  @JoinColumn(name = "stock_id", referencedColumnName = "id", nullable = false)
  private Stocks stock;

  @Column(name = "increase")
  private long increase;

}
