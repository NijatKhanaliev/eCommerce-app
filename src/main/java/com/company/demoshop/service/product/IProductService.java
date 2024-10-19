package com.company.demoshop.service.product;

import com.company.demoshop.dto.ProductDto;
import com.company.demoshop.model.Product;
import com.company.demoshop.request.AddProductRequest;
import com.company.demoshop.request.UpdateProductRequest;

import java.util.List;

public interface IProductService {
    Product addProduct(AddProductRequest product);
    Product getProductById(Long id);
    void deleteProductById(Long id);
    Product updateProduct(UpdateProductRequest product, Long productId);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByName(String name);
    List<Product> getProductsByCategoryAndBrand(String category,String brand);
    List<Product> getProductsByBrandAndName(String brand,String name);
    Long countProductsByBrandAndName(String brand,String name);

    List<ProductDto> convertAllToDto(List<Product> allProduct);

    ProductDto convertToDto(Product product);
}
