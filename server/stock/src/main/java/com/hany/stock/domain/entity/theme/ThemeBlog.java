package com.hany.stock.domain.entity.theme;

import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "theme_blog")
@Getter
@NoArgsConstructor
public class ThemeBlog {

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
  @JoinColumn(name = "theme_id", referencedColumnName = "id", nullable = false)
  private Themes theme;

}
