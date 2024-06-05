package com.hany.stock.service.stock;

import com.hany.stock.domain.entity.stock.StockBlog;
import com.hany.stock.domain.entity.stock.StockInfo;
import com.hany.stock.domain.entity.stock.Stocks;
import com.hany.stock.domain.entity.theme.ThemeBlog;
import com.hany.stock.domain.entity.theme.ThemeStock;
import com.hany.stock.domain.entity.theme.Themes;
import com.hany.stock.repository.stock.StockBlogRepository;
import com.hany.stock.repository.stock.StockInfoRepository;
import com.hany.stock.repository.stock.StocksRepository;
import com.hany.stock.repository.theme.ThemeBlogRepository;
import com.hany.stock.repository.theme.ThemeStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockService {

    public final StocksRepository stocksRepository;
    public final StockInfoRepository stockInfoRepository;
    private final ThemeStockRepository themeStockRepository;
    private final StockBlogRepository stockBlogRepository;
    public Stocks getStockByTicker(String ticker){
        return stocksRepository.findByTicker(ticker)
                .orElse(null);  // Stock 객체가 없으면 null 반환
    }

    public Stocks getStockByCompanyName(String companyName){
        return stocksRepository.findByCompanyName(companyName)
                .orElse(null);  // Stock 객체가 없으면 null 반환
    }

    public Optional<Stocks> getStockIdById(Long Id){
        return stocksRepository.findById(Id);
 // Stock 객체가 없으면 null 반환
    }

    public Optional<StockInfo> getStockInfoByStock(Stocks stock){
        return stockInfoRepository.findByStock(stock);
        // Stock 객체가 없으면 null 반환
    }

    public List<Stocks> searchStocksByCompanyName(String companyName) {
        return stocksRepository.searchByCompanyName(companyName);
    }

    public List<Stocks> searchStocksByTicker(String ticker) {
        return stocksRepository.searchByTicker(ticker);
    }

    public List<ThemeStock> getThemeStockByStock(Stocks stock){
        return themeStockRepository.findAllByStock(stock);
    }

    public Page<StockBlog> getStockBlogByStock(Stocks stock, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<StockBlog> stockBlogPage = stockBlogRepository.findAllByStockOrderByDateDesc(stock, pageable);
        return stockBlogPage;
    }
}
