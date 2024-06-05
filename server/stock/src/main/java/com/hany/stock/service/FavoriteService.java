package com.hany.stock.service;

import com.hany.stock.domain.entity.User;
import com.hany.stock.domain.entity.stock.FavoriteStock;
import com.hany.stock.domain.entity.stock.Stocks;
import com.hany.stock.domain.entity.theme.FavoriteTheme;
import com.hany.stock.domain.entity.theme.Themes;
import com.hany.stock.repository.UserRepository;
import com.hany.stock.repository.stock.FavoriteStockRepository;
import com.hany.stock.repository.theme.FavoriteThemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteStockRepository favoriteStockRepository;
    private final FavoriteThemeRepository favoriteThemeRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addStockFavorite(String loginId, Stocks stock) {
        User loginUser = userRepository.findByLoginId(loginId).get();
        favoriteStockRepository.save(FavoriteStock.builder()
                        .user(loginUser)
                        .stock(stock)
                        .build());
    }
    @Transactional
    public void addThemeFavorite(String loginId, Themes theme) {
        User loginUser = userRepository.findByLoginId(loginId).get();
        favoriteThemeRepository.save(FavoriteTheme.builder()
                .user(loginUser)
                .theme(theme)
                .build());
    }
    @Transactional
    public void deleteStockFavorite(String loginId,Stocks stock) {
        User loginUser = userRepository.findByLoginId(loginId).get();
        favoriteStockRepository.deleteByUserLoginIdAndStock(loginId, stock);
    }

    @Transactional
    public void deleteThemeFavorite(String loginId,Themes theme) {
        User loginUser = userRepository.findByLoginId(loginId).get();
        favoriteThemeRepository.deleteByUserLoginIdAndTheme(loginId, theme);
    }

    public Boolean checkStockFavorite(String loginId,Stocks stock) {
        return favoriteStockRepository.existsByUserLoginIdAndStock(loginId, stock);
    }

    public Boolean checkThemeFavorite(String loginId,Themes theme) {
        return favoriteThemeRepository.existsByUserLoginIdAndTheme(loginId, theme);
    }

    public List<FavoriteStock> getFavoriteStockAll(String loginId){
        return favoriteStockRepository.findAllByUserLoginId(loginId);
    }
    public List<FavoriteTheme> getFavoriteThemeAll(String loginId){
        return favoriteThemeRepository.findAllByUserLoginId(loginId);
    }
}
