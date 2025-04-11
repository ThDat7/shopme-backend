package com.shopme.client.service.impl;

import com.shopme.client.dto.request.CartItemRequest;
import com.shopme.client.dto.response.CartItemResponse;
import com.shopme.client.dto.response.ProductPriceResponse;
import com.shopme.client.exception.type.ProductNotFoundException;
import com.shopme.client.mapper.CartItemMapper;
import com.shopme.client.repository.CartItemRepository;
import com.shopme.client.repository.ProductRepository;
import com.shopme.client.service.*;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ProductPriceService productPriceService;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final CustomerContextService customerContextService;
    private final CartItemMapper cartItemMapper;
    private final FileUploadService fileUploadService;

    private String getProductMainImage(Product product) {
        return fileUploadService.getProductMainImageUrl(product.getId(), product.getMainImage());
    }

    @Override
    public List<CartItemResponse> getAll() {
        Customer customer = customerContextService.getCurrentCustomer();
        List<CartItem> cartItems = cartItemRepository.findAllByCustomer(customer);
        return cartItems.stream().map(c -> {
            CartItemResponse cartItemResponse = cartItemMapper.toCartItemResponse(c);
            cartItemResponse.setMainImage(getProductMainImage(c.getProduct()));

            ProductPriceResponse productPriceResponse =
                    productPriceService.calculateProductPriceResponse(c.getProduct().getId());
            cartItemResponse.setPrice(productPriceResponse.getPrice());
            cartItemResponse.setDiscountPercent(productPriceResponse.getDiscountPercent());
            cartItemResponse.setDiscountPrice(productPriceResponse.getDiscountPrice());

            return cartItemResponse;
        }).toList();
    }

    @Override
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

    @Override
    public CartItemResponse updateQuantity(CartItemRequest request) {
        Customer Customer = customerContextService.getCurrentCustomer();
        CartItem cartItem = cartItemRepository.findByCustomerAndProduct_Id(Customer, request.getProductId());
        cartItem.setQuantity(request.getQuantity());
        cartItemRepository.save(cartItem);
        CartItemResponse cartItemResponse = cartItemMapper.toCartItemResponse(cartItem);
        cartItemResponse.setMainImage(getProductMainImage(cartItem.getProduct()));
        return cartItemResponse;
    }

    @Override
    public void deleteCartItem(Integer id) {
        cartItemRepository.deleteById(id);
    }

    @Override
    public List<CartItemResponse> syncCartItems(List<CartItemRequest> localCartItems) {
        if (localCartItems == null || localCartItems.isEmpty()) {
            return getAll();
        }

        Customer customer = customerContextService.getCurrentCustomer();
        List<CartItem> existingCartItems = cartItemRepository.findAllByCustomer(customer);

        for (CartItemRequest localItem : localCartItems) {
            boolean itemExists = false;

            Product product = productRepository.findById(localItem.getProductId())
                    .orElse(null);

            if (product == null)
                continue;


            for (CartItem existingItem : existingCartItems) {
                if (existingItem.getProduct().getId().equals(localItem.getProductId())) {
                    int newQuantity = Math.max(existingItem.getQuantity(), localItem.getQuantity());
                    existingItem.setQuantity(newQuantity);
                    cartItemRepository.save(existingItem);
                    itemExists = true;
                    break;
                }
            }

            if (!itemExists) {
                CartItem newCartItem = new CartItem();
                newCartItem.setProduct(product);
                newCartItem.setCustomer(customer);
                newCartItem.setQuantity(localItem.getQuantity());
                cartItemRepository.save(newCartItem);
            }
        }

        return getAll();
    }
}
