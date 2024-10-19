package com.company.demoshop.service.product;

import com.company.demoshop.dto.ImageDto;
import com.company.demoshop.dto.ProductDto;
import com.company.demoshop.exceptions.AlreadyExistsException;
import com.company.demoshop.exceptions.ResourceNotFoundException;
import com.company.demoshop.model.Category;
import com.company.demoshop.model.Image;
import com.company.demoshop.model.Product;
import com.company.demoshop.repository.CategoryRepository;
import com.company.demoshop.repository.ImageRepository;
import com.company.demoshop.repository.ProductRepository;
import com.company.demoshop.request.AddProductRequest;
import com.company.demoshop.request.UpdateProductRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;

    @Override
    public Product addProduct(AddProductRequest request) {
        // first we must check if category is exist and we must use it
        // if it is not exist we must create new one and set it in product

        if(isProductExists(request.getName(),request.getBrand())){
            throw new AlreadyExistsException(request.getBrand() + " " + request.getName() + " is already exists!");
        }

       Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
               .orElseGet(()->{
                   Category newCategory = new Category(request.getCategory().getName());
                   return categoryRepository.save(newCategory);
               });
            request.setCategory(category);
       return productRepository.save(createProduct(request,category));
    }

    private boolean isProductExists(String name, String brand){
        return productRepository.existsByNameAndBrand(name,brand);
    }

    private Product createProduct(AddProductRequest request, Category category){
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("product not found!"));
    }

    @Override
    public void deleteProductById(Long id) {
         productRepository.findById(id)
                 .ifPresentOrElse(productRepository::delete,
                         ()->{ throw new ResourceNotFoundException("product not found!"); });
    }

    @Override
    public Product updateProduct(UpdateProductRequest request, Long productId) {
        return productRepository.findById(productId)
                .map(existingProduct->updateExistingProduct(existingProduct,request))
                .map(productRepository :: save)
                .orElseThrow(()->new ResourceNotFoundException("product not found!"));
    }

    private Product updateExistingProduct(Product existingProduct,UpdateProductRequest request){
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());

       Category category = categoryRepository.findByName(request.getCategory().getName());
       existingProduct.setCategory(category);

       return existingProduct;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category,brand);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand,name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand,name) ;
    }

    @Override
    public List<ProductDto> convertAllToDto(List<Product> allProduct){
        return allProduct.stream().map(this::convertToDto).toList();
    }

    @Override
    public ProductDto convertToDto(Product product){
       ProductDto productDto = modelMapper.map(product,ProductDto.class);

       List<Image> allImage = imageRepository.findByProductId(product.getId());
       List<ImageDto> allImageDto = allImage.stream().map(image->modelMapper.map(image, ImageDto.class)).toList();
       productDto.setImages(allImageDto);

       return productDto;
    }

}
