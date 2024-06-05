package com.hany.stock.controller;

import com.hany.stock.domain.entity.stock.Stocks;
import com.hany.stock.domain.entity.theme.Themes;
import com.hany.stock.service.FavoriteService;
import com.hany.stock.service.Theme.ThemeService;
import com.hany.stock.service.stock.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/favorite")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final StockService stockService;
    private final ThemeService themeService;

    @GetMapping("/add/stock")
    public String addStockFavorite( @RequestParam Long stockId, Authentication auth) throws UnsupportedEncodingException {
        Stocks stock = stockService.getStockIdById(stockId).get();
        favoriteService.addStockFavorite(auth.getName(), stock);
        String encodedName = URLEncoder.encode(stock.getCompanyName(), StandardCharsets.UTF_8.toString());
        return "redirect:/search?category=companyName&keyword=" + encodedName;
    }

    @GetMapping("/add/theme")
    public String addThemeFavorite( @RequestParam Long themeId, Authentication auth) throws UnsupportedEncodingException  {
        Themes theme = themeService.getThemeById(themeId).get();
        favoriteService.addThemeFavorite(auth.getName(), theme);
        String encodedName = URLEncoder.encode(theme.getThemeName(), StandardCharsets.UTF_8.toString());

        return "redirect:/search?category=theme&keyword=" + encodedName;
    }

    @GetMapping("/delete/stock")
    public String deleteStockFavorite( @RequestParam Long stockId, Authentication auth) throws UnsupportedEncodingException {
        Stocks stock = stockService.getStockIdById(stockId).get();
        favoriteService.deleteStockFavorite(auth.getName(),stock);
        String encodedName = URLEncoder.encode(stock.getCompanyName(), StandardCharsets.UTF_8.toString());
        return "redirect:/search?category=companyName&keyword=" + encodedName;
    }

    @GetMapping("/delete/theme")
    public String deleteThemeFavorite( @RequestParam Long themeId, Authentication auth) throws UnsupportedEncodingException{
        Themes theme = themeService.getThemeById(themeId).get();
        favoriteService.deleteThemeFavorite(auth.getName(),theme);
        String encodedName = URLEncoder.encode(theme.getThemeName(), StandardCharsets.UTF_8.toString());
        return "redirect:/search?category=theme&keyword=" + encodedName;
    }


    @GetMapping("/delete/stock2")
    public String deleteStockFavorite2( @RequestParam Long stockId, Authentication auth)  {
        Stocks stock = stockService.getStockIdById(stockId).get();
        favoriteService.deleteStockFavorite(auth.getName(),stock);
        return "redirect:/";
    }

    @GetMapping("/delete/theme2")
    public String deleteThemeFavorite2( @RequestParam Long themeId, Authentication auth){
        Themes theme = themeService.getThemeById(themeId).get();
        favoriteService.deleteThemeFavorite(auth.getName(),theme);
        return "redirect:/";
    }
}
