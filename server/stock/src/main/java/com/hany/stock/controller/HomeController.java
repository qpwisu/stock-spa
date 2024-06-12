package com.hany.stock.controller;

import com.hany.stock.domain.entity.aggregate.TotalStockAggregate;
import com.hany.stock.domain.entity.aggregate.TotalThemeAggregate;
import com.hany.stock.service.AggregateService;
import com.hany.stock.service.FavoriteService;
import com.hany.stock.service.Theme.ThemeService;
import com.hany.stock.service.stock.StockPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
//1. 실시간 주가 탑10 : sql 처리
//2. 실시간 테마 주가 탑10 : sql 처리
//3. 기간별 테마 언급량 탑10 :`total_theme_aggregate`
//4. 기간별 주식 언급량 탑10 : `total_stock_aggregate`
//5. 주식 전날 대비 언급 증가량 탑10 : `stockMentionIncrements`
//6. 테마 전날 대비 언급 증가량 탑10 : `themeMentionIncrements`
//7. 즐겨찾기
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final StockPriceService stockPriceService;
    private final ThemeService themeService;

    private final AggregateService aggregateService;
    private final FavoriteService favoriteService;

    @GetMapping(value = {"", "/"})
    public String home(Model model, Authentication auth) {
        if (auth != null) {
            model.addAttribute("loginUserLoginId", auth.getName());
            model.addAttribute("favoriteStock",favoriteService.getFavoriteStockAll(auth.getName()));
            model.addAttribute("favoriteTheme",favoriteService.getFavoriteThemeAll(auth.getName()));
        }
        model.addAttribute("top_now_stock_price", stockPriceService.getTopStocksByChangeRate());
        model.addAttribute("top_theme_now_stock_price", themeService.getTopThemeNowStockPrice());
        model.addAttribute("total_theme", aggregateService.getAllTotalThemeAggregate());
        model.addAttribute("total_stock", aggregateService.getAllTotalStockAggregate());
        model.addAttribute("stock_mention_increments",aggregateService.getAllStockMentionIncrements());
        model.addAttribute("theme_mention_increments",aggregateService.getAllThemeMentionIncrements());
        model.addAttribute("suggest_month",aggregateService.getAllSuggestMonthAggregate());

        return "home";
    }
}
