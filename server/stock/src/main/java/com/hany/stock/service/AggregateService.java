package com.hany.stock.service;

import com.hany.stock.domain.entity.EachThemeAggregate;
import com.hany.stock.domain.entity.aggregate.*;
import com.hany.stock.domain.entity.stock.Stocks;
import com.hany.stock.domain.entity.theme.Themes;
import com.hany.stock.repository.aggregate.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class AggregateService {
    private final TotalThemeAggregateRepository totalThemeAggregateRepository;
    private final TotalStockAggregateRepository totalStockAggregateRepository;
    private final EachStockAggregateRepository eachStockAggregateRepository;
    private final EachThemeAggregateRepository eachThemeAggregateRepository;
    private final DateThemeAggregateRepository dateThemeAggregateRepository;
    private final DateStockAggregateRepository dateStockAggregateRepository;
    private final StockMentionIncrementsRepository stockMentionIncrementsRepository;
    private final ThemeMentionIncrementsRepository themeMentionIncrementsRepository;
    private final RelatedStockAggregateRepository relatedStockAggregateRepository;

    public List<TotalThemeAggregate> getAllTotalThemeAggregate(){
        return totalThemeAggregateRepository.findAll();
    }
    public List<TotalStockAggregate> getAllTotalStockAggregate(){
        return totalStockAggregateRepository.findAll();
    }
    public List<StockMentionIncrements> getAllStockMentionIncrements(){
        return stockMentionIncrementsRepository.findAll();
    }
    public List<ThemeMentionIncrements> getAllThemeMentionIncrements(){
        return themeMentionIncrementsRepository.findAll();
    }
    public List<EachStockAggregate> getAllEachStockAggregate(Stocks stock){
        return eachStockAggregateRepository.findAllByStockId(stock.getId());
    }
    public List<EachThemeAggregate> getAllEachThemeAggregate( Themes theme){
        return eachThemeAggregateRepository.findAllByThemeId(theme.getId());
    }

    public List<DateStockAggregate> getAllDateStockAggregate(Stocks stock){
        return dateStockAggregateRepository.findAllByStockId(stock.getId());
    }
    public List<DateThemeAggregate> getAllDateThemeAggregate( Themes theme){
        return dateThemeAggregateRepository.findAllByThemeId(theme.getId());
    }

    public  List<RelatedStockAggregate> getAllRelatedStockAggregate(Stocks stock){
        return relatedStockAggregateRepository.findAllBySearchStock(stock);
    }
}
