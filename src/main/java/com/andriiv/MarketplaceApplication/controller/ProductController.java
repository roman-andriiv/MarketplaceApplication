package com.andriiv.MarketplaceApplication.controller;

import com.andriiv.MarketplaceApplication.exception.ResourceNotFoundException;
import com.andriiv.MarketplaceApplication.model.Product;
import com.andriiv.MarketplaceApplication.model.User;
import com.andriiv.MarketplaceApplication.repository.ProductRepository;
import com.andriiv.MarketplaceApplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

/**
 * @author Roman_Andriiv
 */
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProductController(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }


    @GetMapping("/getAll")
    public List<Product> getAllProducts() {
        return this.productRepository.findAll();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable(value = "id") Long productId) throws ResourceNotFoundException {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found for this ID :: " + productId));
        return ResponseEntity.ok().body(product);
    }

    @PostMapping("/save")
    public Product createProduct(@Valid @RequestBody Product product) {

        return this.productRepository.save(product);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable(value = "id") Long productId,
                                                 @Valid @RequestBody Product productDetails) throws ResourceNotFoundException {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found for this ID :: " + productId));

        product.setName(productDetails.getName());
        product.setPrice(productDetails.getPrice());

        return ResponseEntity.ok(this.productRepository.save(product));
    }

    @DeleteMapping("/delete/{id}")
    public Map<String, Boolean> deleteProduct(@PathVariable(value = "id") Long productId) throws ResourceNotFoundException {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found for this ID :: " + productId));

        this.productRepository.delete(product);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);

        return response;
    }

    @GetMapping("/getUserProducts/{user_id}")
    public ResponseEntity<Set<Product>> getUserProductsByUserId(@PathVariable(value = "user_id") Long userId) {
        Optional<User> users = userRepository.findById(userId);
        if (users.isPresent()) {
            return ResponseEntity.ok().body(users.get().getUserProducts());
        }
        return ResponseEntity.ok().body(Set.of());

    }
}
