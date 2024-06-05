package com.hany.stock.service.Theme;

import com.hany.stock.domain.dto.TopThemeNowStockPriceDto;
import com.hany.stock.domain.entity.stock.StockBlog;
import com.hany.stock.domain.entity.stock.Stocks;
import com.hany.stock.domain.entity.theme.ThemeAverageChangeRate;
import com.hany.stock.domain.entity.theme.ThemeBlog;
import com.hany.stock.domain.entity.theme.ThemeStock;
import com.hany.stock.domain.entity.theme.Themes;
import com.hany.stock.repository.theme.ThemeAverageChangeRateRepository;
import com.hany.stock.repository.theme.ThemeBlogRepository;
import com.hany.stock.repository.theme.ThemeStockRepository;
import com.hany.stock.repository.theme.ThemesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ThemeService {
    private final ThemesRepository themesRepository;
    private final ThemeStockRepository themeStockRepository;
    private final ThemeBlogRepository themeBlogRepository;

    private final ThemeAverageChangeRateRepository themeAverageChangeRateRepository;
    public Long getThemeIdByThemeName(String ThemeName){
        return themesRepository.findByThemeName(ThemeName)
                .map(Themes::getId)
                .orElse(null);  // Stock 객체가 없으면 null 반환
    }

    public Optional<Themes> getThemeById(Long id){
        return themesRepository.findById(id);
    }

    public List<Themes> searchThemesByName(String ThemeName) {
        return themesRepository.searchByThema(ThemeName);
    }
    public List<ThemeStock> getThemeStockByTheme(Themes theme){
        return themeStockRepository.findAllByTheme(theme);
    }

    public Page<ThemeBlog> getThemeBlogByStock(Themes theme, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<ThemeBlog> themeBlogPage = themeBlogRepository.findAllByThemeOrderByDateDesc(theme, pageable);
        return themeBlogPage;
    }


    //1. 실시간 주가 탑10
    public List<ThemeAverageChangeRate> getTopThemeNowStockPrice(){
        List<ThemeAverageChangeRate> price = themeAverageChangeRateRepository.findTop20ByOrderByAverageChangeRateDesc();
        return price;
    }
}
