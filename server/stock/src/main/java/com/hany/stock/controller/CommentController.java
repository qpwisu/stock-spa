package com.hany.stock.controller;

//import com.hany.stock.domain.dto.CategoryCommentCreateRequest;
import com.hany.stock.domain.dto.CategoryCommentCreateRequest;
import com.hany.stock.domain.dto.CommentCreateRequest;
import com.hany.stock.domain.entity.stock.Stocks;
import com.hany.stock.domain.entity.theme.Themes;
import com.hany.stock.service.BoardService;
import com.hany.stock.service.CommentService;
import com.hany.stock.service.Theme.ThemeService;
import com.hany.stock.service.stock.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final BoardService boardService;

    private final StockService stockService;
    private final ThemeService themeService;


    @PostMapping("/{boardId}")
    public String addComments(@PathVariable Long boardId, @ModelAttribute CommentCreateRequest req,
                                     Authentication auth, Model model) {
        commentService.writeComment(boardId, req, auth.getName());

        model.addAttribute("message", "댓글이 추가되었습니다.");
        model.addAttribute("nextUrl", "/boards/" + boardService.getCategory(boardId) + "/" + boardId);
        return "printMessage";
    }

    @PostMapping("/{commentId}/edit")
    public String editComment(@PathVariable Long commentId, @ModelAttribute CommentCreateRequest req,
                              Authentication auth, Model model) {
        Long boardId = commentService.editComment(commentId, req.getBody(), auth.getName());
        model.addAttribute("message", boardId == null? "잘못된 요청입니다." : "댓글이 수정 되었습니다.");
        model.addAttribute("nextUrl", "/boards/" + boardService.getCategory(boardId) + "/" + boardId);
        return "printMessage";
    }

    @GetMapping("/{commentId}/delete")
    public String deleteComment(@PathVariable Long commentId, Authentication auth, Model model) {
        Long boardId = commentService.deleteComment(commentId, auth.getName());
        model.addAttribute("message", boardId == null? "작성자만 삭제 가능합니다." : "댓글이 삭제 되었습니다.");
        model.addAttribute("nextUrl", "/boards/" + boardService.getCategory(boardId) + "/" + boardId);
        return "printMessage";
    }

    @PostMapping("/{category}/{id}")
    public String addCategoryComments(@PathVariable String category,@PathVariable Long id, @ModelAttribute CategoryCommentCreateRequest req,
                                      Authentication auth, Model model) {

        if("stock".equals(category)){
            Optional<Stocks> stock = stockService.getStockIdById(id);
            commentService.writeStockComment(stock.get(), req, auth.getName());
            model.addAttribute("message", "댓글이 추가되었습니다.");
            model.addAttribute("nextUrl", "/search?category=companyName"+ "&keyword=" + stock.get().getCompanyName());
        }

        if("theme".equals(category)) {
            Optional<Themes> theme = themeService.getThemeById(id);
            commentService.writeThemeComment(theme.get(), req, auth.getName());
            model.addAttribute("message", "댓글이 추가되었습니다.");
            model.addAttribute("nextUrl", "/search?category=theme"+ "&keyword=" + theme.get().getThemeName());
        }
        return "printMessage";
    }

    @PostMapping("/{category}/{id}/{commentId}/edit")
    public String editCategoryComment(@PathVariable String category,@PathVariable Long id,@PathVariable Long commentId, @ModelAttribute CategoryCommentCreateRequest req,
                              Authentication auth, Model model) {

        if("stock".equals(category)){
            Optional<Stocks> stock = stockService.getStockIdById(id);
            Long cid = commentService.editStockComment(commentId, req.getBody(), auth.getName());
            model.addAttribute("message", cid == null? "잘못된 요청입니다." : "댓글이 수정 되었습니다.");
            model.addAttribute("nextUrl", "/search?category=companyName"+ "&keyword=" + stock.get().getCompanyName());
        }

        if("theme".equals(category)){
            Optional<Themes> theme = themeService.getThemeById(id);
            Long cid = commentService.editThemeComment(commentId, req.getBody(), auth.getName());
            model.addAttribute("message", cid == null? "잘못된 요청입니다." : "댓글이 수정 되었습니다.");
            model.addAttribute("nextUrl", "/search?category=theme"+ "&keyword=" + theme.get().getThemeName());
        }
        return "printMessage";
    }

    @GetMapping("/{category}/{id}/{commentId}/delete")
    public String deleteCategoryComment(@PathVariable String category,@PathVariable Long id,@PathVariable Long commentId, Authentication auth, Model model) {

        if("stock".equals(category)){
            Optional<Stocks> stock = stockService.getStockIdById(id);
            Long cid = commentService.deleteStockComment(commentId, auth.getName());
            model.addAttribute("message", cid == null? "작성자만 삭제 가능합니다." : "댓글이 삭제 되었습니다.");
            model.addAttribute("nextUrl", "/search?category=companyName"+ "&keyword=" + stock.get().getCompanyName());
        }

        if("theme".equals(category)){
            Optional<Themes> theme = themeService.getThemeById(id);
            Long cid = commentService.deleteThemeComment(commentId, auth.getName());
            model.addAttribute("message", cid == null? "작성자만 삭제 가능합니다." : "댓글이 삭제 되었습니다.");
            model.addAttribute("nextUrl", "/search?category=theme"+ "&keyword=" + theme.get().getThemeName());
        }
        return "printMessage";
    }

}
