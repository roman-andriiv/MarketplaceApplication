package com.andriiv.MarketplaceApplication.controller;

import com.andriiv.MarketplaceApplication.exception.ResourceNotFoundException;
import com.andriiv.MarketplaceApplication.model.Product;
import com.andriiv.MarketplaceApplication.model.User;
import com.andriiv.MarketplaceApplication.repository.ProductRepository;
import com.andriiv.MarketplaceApplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

/**
 * @author Roman_Andriiv
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Autowired
    public UserController(UserRepository userRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    //get users
    @GetMapping("/getAll")
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    //get user by id
    @GetMapping("/get/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this ID :: " + userId));
        return ResponseEntity.ok().body(user);
    }

    //save user
    @PostMapping("/save")
    public User createUser(@Valid @RequestBody User user) {
        return this.userRepository.save(user);
    }

    //update user
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable(value = "id") Long userId,
                                           @Valid @RequestBody User userDetails) throws ResourceNotFoundException {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this ID :: " + userId));

        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setAmountOfMoney(userDetails.getAmountOfMoney());

        return ResponseEntity.ok(this.userRepository.save(user));
    }

    //delete user
    @DeleteMapping("/delete/{id}")
    public Map<String, Boolean> deleteUser(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this ID :: " + userId));

        this.userRepository.delete(user);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);

        return response;
    }

    @PutMapping("/{id_user}/buyProduct/{id_product}")
    public ResponseEntity<HttpStatus> buyProductById(@PathVariable(value = "id_user") Long userId,
                                                     @PathVariable(value = "id_product") Long productId) throws ResourceNotFoundException {

        Product productToBeBought = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found for this ID :: " + productId));

        User userWhoBuy = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this ID ::" + userId));


        if (userWhoBuy.getAmountOfMoney() < productToBeBought.getPrice()) {
            throw new ResourceNotFoundException("User with this ID hasn't enough money ");
        } else {
            userWhoBuy.setAmountOfMoney(userWhoBuy.getAmountOfMoney() - productToBeBought.getPrice());
            productToBeBought.getProductOwner().add(userWhoBuy);
            userWhoBuy.getUserProducts().add(productToBeBought);

            productRepository.save(productToBeBought);
            userRepository.save(userWhoBuy);

            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
    }

    @GetMapping("/getUsersWhoBought/{product_id}")
    public ResponseEntity<Set<User>> getUsersWhoBoughtProductByProductId(@PathVariable(value = "product_id") Long productId){

        Optional<Product> products =  productRepository.findById(productId);
        if (products.isPresent()) {
            return ResponseEntity.ok().body(products.get().getProductOwner());
        }
        return ResponseEntity.ok().body(Set.of());
    }

}
