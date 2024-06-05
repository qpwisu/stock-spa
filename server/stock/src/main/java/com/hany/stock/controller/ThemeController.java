package com.hany.stock.controller;

//import com.hany.stock.domain.entity.DateCategoryAggregate;
//import com.hany.stock.domain.entity.EachStockAggregate;
import com.hany.stock.domain.dto.CategoryCommentCreateRequest;
import com.hany.stock.domain.entity.EachThemeAggregate;
import com.hany.stock.domain.entity.aggregate.DateThemeAggregate;
import com.hany.stock.domain.entity.stock.StockBlog;
import com.hany.stock.domain.entity.theme.ThemeBlog;
import com.hany.stock.domain.entity.theme.Themes;
import com.hany.stock.service.AggregateService;
import com.hany.stock.service.CommentService;
import com.hany.stock.service.FavoriteService;
import com.hany.stock.service.Theme.ThemeService;
import com.hany.stock.service.stock.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/theme")
@RequiredArgsConstructor
public class ThemeController {
    private final AggregateService aggregateService;
    private final CommentService commentService;
    private final ThemeService themeService;

    private final FavoriteService favoriteService;

    @GetMapping("")
    public String themePage(@RequestParam Themes theme,@RequestParam(defaultValue = "0") int page, Model model,@RequestParam(defaultValue = "10") int size, Authentication auth) {
        if (auth != null) {
            model.addAttribute("loginUserLoginId", auth.getName());
            model.addAttribute("favoriteCheck", favoriteService.checkThemeFavorite(auth.getName(),theme));
        }
        model.addAttribute("theme", theme);
        model.addAttribute("themeStock", themeService.getThemeStockByTheme(theme));
        model.addAttribute("eachThemeAggregates", aggregateService.getAllEachThemeAggregate(theme));
        model.addAttribute("dateThemeAggregates", aggregateService.getAllDateThemeAggregate(theme));
        model.addAttribute("categoryCommentCreateRequest", new CategoryCommentCreateRequest());
        model.addAttribute("commentList", commentService.findAllThemeComment(theme));

        Page<ThemeBlog> themeBlogs = themeService.getThemeBlogByStock(theme, page,size);
        model.addAttribute("themeBlogs", themeBlogs);
        return "category/theme";
    }
}