package com.hany.stock.domain.entity.stock;

import com.hany.stock.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.sql.ConnectionBuilder;

@Entity
@Table(name = "favorite_stock")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteStock {

  @Column(name = "id")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @ManyToOne(fetch = FetchType.LAZY)
  private User user;      // 작성자

  @ManyToOne
  @JoinColumn(name = "stock_id", referencedColumnName = "id", nullable = false)
  private Stocks stock;

}
