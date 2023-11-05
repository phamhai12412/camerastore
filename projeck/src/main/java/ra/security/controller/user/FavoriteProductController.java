package ra.security.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.security.exception.ProductException;
import ra.security.model.domain.FavoriteProduct;
import ra.security.model.dto.response.FavoriteProductResponseDto;
import ra.security.service.IFavoriteProductService;
import ra.security.service.impl.usersevice.UserServiceOnline;

import java.util.List;

@RestController
@RequestMapping("/api/use/favorites")
@CrossOrigin("*")
public class FavoriteProductController {
    @Autowired
    private UserServiceOnline userServiceOnline;
    @Autowired
    private IFavoriteProductService favoriteProductService;

    @GetMapping()
    public ResponseEntity<List<FavoriteProductResponseDto>> getUserFavoriteProducts() throws ProductException {
        List<FavoriteProductResponseDto> favoriteProducts = favoriteProductService.getUserFavoriteProducts(userServiceOnline.getCurrentUsername());
        return ResponseEntity.ok(favoriteProducts);
    }
    @PostMapping("/add/productId/{productId}")
    public ResponseEntity<String> addProductToFavorites(
            @PathVariable Long productId
    ) throws ProductException {
        favoriteProductService.addProductToFavorites(userServiceOnline.getCurrentUsername(), productId);
        return ResponseEntity.ok("Product added to favorites successfully");
    }
    @DeleteMapping("/remove/productId/{productId}")
    public ResponseEntity<String> removeProductFromFavorites(

            @PathVariable Long productId
    ) throws ProductException {
        favoriteProductService.removeProductFromFavorites(userServiceOnline.getCurrentUsername(), productId);
        return ResponseEntity.ok("Product removed from favorites successfully");
    }
}
