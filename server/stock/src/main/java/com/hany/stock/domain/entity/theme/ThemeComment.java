package com.hany.stock.domain.entity.theme;

import com.hany.stock.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "theme_comment")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThemeComment {

  @Column(name = "id")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @CreatedDate
  @Column(name = "created_at",updatable = false)
  private LocalDateTime createdAt;
  @LastModifiedDate
  @Column(name = "last_modified_at")
  private LocalDateTime lastModifiedAt;
  @Column(name = "body")
  private String body;
  @ManyToOne(fetch = FetchType.LAZY)
  private User user;      // 작성자
  @ManyToOne
  @JoinColumn(name = "theme_id", referencedColumnName = "id", nullable = false)
  private Themes theme;
  public void update(String newBody) {
    this.body = newBody;
  }
}
