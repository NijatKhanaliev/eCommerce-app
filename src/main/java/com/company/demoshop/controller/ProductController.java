package com.company.demoshop.controller;

import com.company.demoshop.dto.ProductDto;
import com.company.demoshop.exceptions.AlreadyExistsException;
import com.company.demoshop.exceptions.ResourceNotFoundException;
import com.company.demoshop.model.Product;
import com.company.demoshop.request.AddProductRequest;
import com.company.demoshop.request.UpdateProductRequest;
import com.company.demoshop.response.ApiResponse;
import com.company.demoshop.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private final IProductService productService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProduct() {
        List<Product> allProduct = productService.getAllProducts();
        List<ProductDto> convertedAllProduct = productService.convertAllToDto(allProduct);

        return ResponseEntity.ok(new ApiResponse("Success!", convertedAllProduct));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
        try {
            Product product = productService.getProductById(productId);
            ProductDto convertedProduct = productService.convertToDto(product);

            return ResponseEntity.ok(new ApiResponse("Found!", convertedProduct));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product) {
        try {
            Product theProduct = productService.addProduct(product);
            return ResponseEntity.ok(new ApiResponse("Success!", theProduct));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/product/{productId}/update")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody UpdateProductRequest product, @PathVariable Long productId) {
        try {
            Product theProduct = productService.updateProduct(product, productId);
            return ResponseEntity.ok(new ApiResponse("Update success!", theProduct));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/product/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) {
        try {
            productService.deleteProductById(productId);
            return ResponseEntity.ok(new ApiResponse("Delete success!", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/by/name/{productName}")
    public ResponseEntity<ApiResponse> getProductByName(@PathVariable String productName) {
        try {
            List<Product> allProduct = productService.getProductsByName(productName);
            if (allProduct.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No product found!", null));
            }
            List<ProductDto> convertedAllProduct = productService.convertAllToDto(allProduct);

            return ResponseEntity.ok(new ApiResponse("Success!", convertedAllProduct));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/by/brand/{brand}")
    public ResponseEntity<ApiResponse> getProductByBrand(@PathVariable String brand) {
        try {
            List<Product> allProduct = productService.getProductsByBrand(brand);
            if (allProduct.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No product found!", null));
            }
            List<ProductDto> convertedAllProduct = productService.convertAllToDto(allProduct);

            return ResponseEntity.ok(new ApiResponse("Success!", convertedAllProduct));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/by/category/{category}")
    public ResponseEntity<ApiResponse> getProductByCategory(@PathVariable String category) {
        try {
            List<Product> allProduct = productService.getProductsByCategory(category);
            if (allProduct.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No Product found!", null));
            }
            List<ProductDto> convertedAllProduct = productService.convertAllToDto(allProduct);

            return ResponseEntity.ok(new ApiResponse("Success!", convertedAllProduct));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/by/brand-and-productName")
    public ResponseEntity<ApiResponse> getProductByBrandAndName(@RequestParam String productName, @RequestParam String brand) {
        try {
            List<Product> allProduct = productService.getProductsByBrandAndName(brand, productName);
            if (allProduct.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No product found!", null));
            }
            List<ProductDto> convertedAllProduct = productService.convertAllToDto(allProduct);

            return ResponseEntity.ok(new ApiResponse("Success!", convertedAllProduct));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("by/category-and-brand")
    public ResponseEntity<ApiResponse> getProductByCategoryAndBrand(@RequestParam String category, @RequestParam String brand) {
        try {
            List<Product> allProduct = productService.getProductsByCategoryAndBrand(category, brand);
            if (allProduct.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No product found!", null));
            }
            List<ProductDto> convertedAllProduct = productService.convertAllToDto(allProduct);

            return ResponseEntity.ok(new ApiResponse("Success!", convertedAllProduct));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/count/by-brand/and-name")
    public ResponseEntity<ApiResponse> getCountProductByBrandAndName(@RequestParam String brand,@RequestParam String name){
        try {
            var productCount = productService.countProductsByBrandAndName(brand,name);
            return ResponseEntity.ok(new ApiResponse("Product count!",productCount));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }

}
