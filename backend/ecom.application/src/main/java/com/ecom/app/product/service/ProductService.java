package com.ecom.app.product.service;

import com.ecom.app.product.dto.ProductRequest;
import com.ecom.app.product.dto.ProductResponse;
import com.ecom.app.product.entity.Product;
import com.ecom.app.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductService
{
    private final ProductRepository productRepository;

    public ProductResponse addProduct(ProductRequest productRequest)
    {
        Product product = new Product();
        updateProductFromRequest(product, productRequest);

        Product savedProduct = productRepository.save(product);

        return mapToProductResponse(savedProduct);
    }

    private void updateProductFromRequest(Product product, ProductRequest productRequest)
    {
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setCategory(productRequest.getCategory());
        product.setPrice(productRequest.getPrice());
        product.setImageUrl(productRequest.getImageUrl());
        product.setStockQuantity(productRequest.getStockQuantity());

    }

    private ProductResponse mapToProductResponse(Product product)
    {
        ProductResponse response = new ProductResponse();

        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setActive(product.getActive());
        response.setCategory(product.getCategory());
        response.setPrice(product.getPrice());
        response.setImageUrl(product.getImageUrl());
        response.setStockQuantity(product.getStockQuantity());

        return response;
    }

    public Optional<ProductResponse> updateProduct(Long id, ProductRequest productRequest)
    {
        return productRepository.findById(id)
                .map(existingProduct ->
                    {
                        updateProductFromRequest(existingProduct,productRequest);
                        Product updatedProduct = productRepository.save(existingProduct);
                        return mapToProductResponse(updatedProduct);
                    }
                );
    }

    public List<ProductResponse> getAllProducts()
    {
        return productRepository.findByActiveTrue().stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    public boolean deleteProduct(Long id)
    {
        return productRepository.findById(id)
                .map( product ->
                {
                    product.setActive(false);
                    productRepository.save(product);
                    return true;
                }).orElse(false);


        
    }

    public List<ProductResponse> searchProducts(String keyword)
    {
        return productRepository.searchProducts(keyword)
                .stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }
}
