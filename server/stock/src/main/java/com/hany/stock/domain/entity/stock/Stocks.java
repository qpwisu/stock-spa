package com.hany.stock.domain.entity.stock;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stocks")
@Getter
@NoArgsConstructor
public class Stocks {

  @Column(name = "id")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @Column(name = "ticker")
  private String ticker;
  @Column(name = "company_name")
  private String companyName;
  @OneToOne(mappedBy = "stock", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
  @JsonIgnore //이 방법은 특정 필드를 JSON 직렬화 과정에서 제외시키는 방법으로, 순환 참조를 간단하게 해결할 수 있습니다.
  private StockPriceNow stockPriceNow;  // 양방향 매핑을 위한 참조 추가


}
