package com.hany.stock.domain.entity.aggregate;

import com.hany.stock.domain.entity.stock.Stocks;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "related_stock_aggregate")
@Getter
@NoArgsConstructor
@ToString
public class RelatedStockAggregate {

  @Column(name = "id")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne
  @JoinColumn(name = "search_stock_id", referencedColumnName = "id", nullable = false)
  private Stocks searchStock;

  @ManyToOne
  @JoinColumn(name = "mention_stock_id", referencedColumnName = "id", nullable = false)
  private Stocks mentionStock;

  @Column(name = "cnt")
  private long cnt;

}
