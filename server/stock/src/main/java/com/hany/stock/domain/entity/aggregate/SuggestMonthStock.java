package com.hany.stock.domain.entity.aggregate;

import com.hany.stock.domain.entity.stock.Stocks;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "suggest_month_stock")
@Getter
@NoArgsConstructor
public class SuggestMonthStock {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "stock_id", referencedColumnName = "id", nullable = false)
    private Stocks stock;

    @Column(name = "cnt")
    private long cnt;

    @Column(name = "month")
    private long month;
}
