package com.hany.stock.service.stock;

import com.hany.stock.domain.dto.TopNowStockPriceDto;
import com.hany.stock.domain.dto.TopThemeNowStockPriceDto;
import com.hany.stock.domain.entity.stock.StockPrice;
import com.hany.stock.domain.entity.stock.StockPriceNow;
import com.hany.stock.domain.entity.stock.Stocks;
import com.hany.stock.repository.aggregate.StockPriceRepository;
import com.hany.stock.repository.stock.StockPriceNowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockPriceService {
    private final StockPriceNowRepository stockPriceNowRepository;
    private final StockPriceRepository stockPriceRepository;

    public List<StockPriceNow> getTopStocksByChangeRate() {
        Pageable topTen = PageRequest.of(0, 20);
        return stockPriceNowRepository.findTopByOrderByChangeRateDesc(topTen);
    }



    public List<StockPrice> getStockPriceAll(Stocks stock){
        List<StockPrice> prices= stockPriceRepository.findAllByStock(stock);
        return prices;
    }
    public Optional<StockPriceNow> getNowStockPrice(Stocks stock){
        Optional<StockPriceNow> prices = stockPriceNowRepository.findAllByStock(stock);
        return prices;
    }


}
