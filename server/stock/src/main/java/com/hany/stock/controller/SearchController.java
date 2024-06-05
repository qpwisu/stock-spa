package com.hany.stock.controller;

import com.hany.stock.domain.dto.SearchDto;
import com.hany.stock.domain.entity.stock.StockInfo;
import com.hany.stock.domain.entity.stock.Stocks;
import com.hany.stock.domain.entity.theme.Themes;
import com.hany.stock.service.Theme.ThemeService;
import com.hany.stock.service.stock.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.context.Theme;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final StockService stockService;
    private final ThemeService themeService;

    @GetMapping("")
    public String searchPage(@RequestParam String category, @RequestParam String keyword, RedirectAttributes attributes) {
        switch (category) {
            case "ticker":
                attributes.addAttribute("stock", stockService.getStockByTicker(keyword));
                return "redirect:/stock";
            case "companyName":
                attributes.addAttribute("stock", stockService.getStockByCompanyName(keyword));
                return "redirect:/stock";
            case "theme":
                attributes.addAttribute("theme", themeService.getThemeIdByThemeName(keyword));
                return "redirect:/theme";
            default:
                return "home";
        }
    }
    @GetMapping("/autocomplete")
    public ResponseEntity<List<SearchDto>> autocompleteSuggestions(@RequestParam String category, @RequestParam String keyword) {
        List<SearchDto> result = new ArrayList<>();

        switch (category){
            case "ticker":
                List<Stocks> suggestionsTicker = stockService.searchStocksByTicker(keyword);
                result.addAll(suggestionsTicker.stream()
                        .map(stockInfo -> new SearchDto("ticker", stockInfo.getTicker()))
                        .limit(5)
                        .collect(Collectors.toList()));
                break;
            case "companyName":
                List<Stocks> suggestionsCompanyName= stockService.searchStocksByCompanyName(keyword);
                result.addAll(suggestionsCompanyName.stream()
                        .map(stockInfo -> new SearchDto("companyName", stockInfo.getCompanyName()))
                        .limit(5)
                        .collect(Collectors.toList()));
                break;
            case "theme":
                List<Themes> suggestionsTheme = themeService.searchThemesByName(keyword);
                result.addAll(suggestionsTheme.stream()
                        .map(stockThema -> new SearchDto("theme", stockThema.getThemeName()))
                        .limit(5)
                        .collect(Collectors.toList()));
                break;
        }

        return  ResponseEntity.ok(result);
    }

}
