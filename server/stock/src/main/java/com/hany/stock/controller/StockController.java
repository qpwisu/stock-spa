package com.hany.stock.controller;

import com.hany.stock.domain.dto.CategoryCommentCreateRequest;
import com.hany.stock.domain.entity.EachThemeAggregate;
import com.hany.stock.domain.entity.aggregate.EachStockAggregate;
import com.hany.stock.domain.entity.stock.*;
import com.hany.stock.service.*;
import com.hany.stock.service.stock.StockPriceService;
import com.hany.stock.service.stock.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.standard.expression.Each;

import java.util.List;
import java.util.Optional;
//
//1. 연관 테마
//2. 기간별 주식과 가장 많이 언급된 테마 탑10 : `each_stock_aggregate`
//3. 날짜별 주식 언급량 : `date_stock_aggregate`
//4. 관련주- 주식과 한달간 가장많이 언급된 주식 : `related_stock_aggregate`
// 즐겨찾기, 댓글
@Controller
@RequestMapping("/stock")
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;
    private final StockPriceService stockPriceService;
    private final AggregateService aggregateService;

//    private final StockPriceService stockPriceService;
    private final CommentService commentService;
    private final FavoriteService favoriteService;


    @GetMapping("")
    public String stockPage(@RequestParam Stocks stock,@RequestParam(defaultValue = "0") int page, Model model,@RequestParam(defaultValue = "10") int size,  Authentication auth){
        if (auth != null) {
            model.addAttribute("loginUserLoginId", auth.getName());
            model.addAttribute("favoriteCheck", favoriteService.checkStockFavorite(auth.getName(),stock));
        }
        model.addAttribute("stock", stock);
        Optional<StockPriceNow> nowStockPrice = stockPriceService.getNowStockPrice(stock);
        model.addAttribute("nowStockPrice", nowStockPrice.get());
        List<StockPrice> stockPrice = stockPriceService.getStockPriceAll(stock);
        System.out.println(stockPrice.toString());
        model.addAttribute("stockPrices", stockPrice);
        Optional<StockInfo> stockInfo = stockService.getStockInfoByStock(stock);
        model.addAttribute("stockInfo", stockInfo.get());
        List<EachStockAggregate> eachStockAggregates = aggregateService.getAllEachStockAggregate(stock);
        model.addAttribute("eachStockAggregates", eachStockAggregates);
        model.addAttribute("themeStock", stockService.getThemeStockByStock(stock));
        model.addAttribute("relatedStockAggregate",aggregateService.getAllRelatedStockAggregate(stock));
        model.addAttribute("dateStockAggregates",aggregateService.getAllDateStockAggregate(stock));

        model.addAttribute("categoryCommentCreateRequest", new CategoryCommentCreateRequest());
        model.addAttribute("commentList", commentService.findAllStockComment(stock));
        Page<StockBlog> stockBlogs = stockService.getStockBlogByStock(stock, page,size);
        model.addAttribute("stockBlogs", stockBlogs);
        return "category/stock";}
}
