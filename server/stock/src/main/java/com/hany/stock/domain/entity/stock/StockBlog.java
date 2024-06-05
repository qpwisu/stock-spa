package com.hany.stock.domain.entity.stock;

import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_blog")
@Getter
@NoArgsConstructor
public class StockBlog {

  @Column(name = "id")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @Column(name = "title")
  private String title;
  @Column(name = "header")
  private String header;
  @Column(name = "href")
  private String href;
  @Column(name = "date")
  private LocalDateTime date;
  @ManyToOne
  @JoinColumn(name = "stock_id", referencedColumnName = "id", nullable = false)
  private Stocks stock;

}
