package com.shopme.client.service.impl;

import com.shopme.client.dto.request.CartItemRequest;
import com.shopme.client.dto.response.CartItemResponse;
import com.shopme.client.dto.response.ProductPriceResponse;
import com.shopme.client.exception.type.ProductNotFoundException;
import com.shopme.client.mapper.CartItemMapper;
import com.shopme.client.repository.CartItemRepository;
import com.shopme.client.repository.ProductRepository;
import com.shopme.client.service.AuthenticationService;
import com.shopme.client.service.CustomerContextService;
import com.shopme.client.service.FileUploadService;
import com.shopme.client.service.ShoppingCartService;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final CustomerContextService customerContextService;
    private final CartItemMapper cartItemMapper;
    private final FileUploadService fileUploadService;

    private String getProductMainImage(Product product) {
        return fileUploadService.getProductMainImageUrl(product.getId(), product.getMainImage());
    }

    public List<CartItemResponse> getAll() {
        Customer customer = customerContextService.getCurrentCustomer();
        List<CartItem> cartItems = cartItemRepository.findAllByCustomer(customer);
        return cartItems.stream().map(c -> {
            CartItemResponse cartItemResponse = cartItemMapper.toCartItemResponse(c);
            cartItemResponse.setMainImage(getProductMainImage(c.getProduct()));
            return cartItemResponse;
        }).toList();
    }

    public CartItemResponse addProductToCart(CartItemRequest request) {
        Customer Customer = customerContextService.getCurrentCustomer();
        CartItem cartItem = cartItemRepository.findByCustomerAndProduct_Id(Customer, request.getProductId());
        if (cartItem == null) {
            Product product = productRepository.findById(request.getProductId())
                    .orElseThrow(ProductNotFoundException::new);
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCustomer(Customer);
            cartItem.setQuantity(request.getQuantity());
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        }
        cartItemRepository.save(cartItem);
        CartItemResponse cartItemResponse = cartItemMapper.toCartItemResponse(cartItem);
        cartItemResponse.setMainImage(getProductMainImage(cartItem.getProduct()));
        return cartItemResponse;
    }

    public CartItemResponse updateQuantity(CartItemRequest request) {
        Customer Customer = customerContextService.getCurrentCustomer();
        CartItem cartItem = cartItemRepository.findByCustomerAndProduct_Id(Customer, request.getProductId());
        cartItem.setQuantity(request.getQuantity());
        cartItemRepository.save(cartItem);
        CartItemResponse cartItemResponse = cartItemMapper.toCartItemResponse(cartItem);
        cartItemResponse.setMainImage(getProductMainImage(cartItem.getProduct()));
        return cartItemResponse;
    }

    public void deleteCartItem(Integer id) {
        cartItemRepository.deleteById(id);
    }
}
