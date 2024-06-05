package com.hany.stock.domain.entity.stock;

import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Table(name = "stock_info")
@Getter
@NoArgsConstructor
public class StockInfo {

  @Column(name = "id")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @Column(name = "bps")
  private Double bps;
  @Column(name = "company_description")
  private String companyDescription;
  @Column(name = "divided")
  private Double divided;
  @Column(name = "divided_rate")
  private Double dividedRate;
  @Column(name = "eps")
  private Double eps;
  @Column(name = "market")
  private String market;
  @Column(name = "market_cap")
  private Double marketCap;
  @Column(name = "pbr")
  private Double pbr;
  @Column(name = "per")
  private Double per;
  @Column(name = "sector")
  private String sector;
  @ManyToOne
  @JoinColumn(name = "stock_id", referencedColumnName = "id", nullable = false)
  private Stocks stock;

}
