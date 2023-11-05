package ra.security.service;

import ra.security.exception.ProductException;
import ra.security.model.domain.FavoriteProduct;
import ra.security.model.dto.response.FavoriteProductResponseDto;

import java.util.List;

public interface IFavoriteProductService {

    List<FavoriteProductResponseDto> getUserFavoriteProducts(String usename) throws ProductException;

    void addProductToFavorites(String username, Long productId) throws ProductException;

    void removeProductFromFavorites(String username, Long productId) throws ProductException;
}
