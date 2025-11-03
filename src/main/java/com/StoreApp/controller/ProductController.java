package com.StoreApp.controller;

import com.StoreApp.entity.Product;
import com.StoreApp.entity.ProductDto;
import com.StoreApp.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.*;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public String showProductList(Model model) {
        List<Product> products = productRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("products", products);
        return "products/index";
    }

    @GetMapping("/create")
    public String createProductForm(Model model) {
        model.addAttribute("productDto", new ProductDto());
        return "products/CreateProduct";
    }

    @PostMapping("/create")
    public String createProduct(@Valid @ModelAttribute ProductDto productDto , BindingResult bindingResult) {
        if(productDto.getImageFile().isEmpty()){
            bindingResult.addError(new FieldError("productDto","imageFile","The image file is required."));
        }
        if(bindingResult.hasErrors()){
            return "products/CreateProduct";
        }

        MultipartFile image = productDto.getImageFile();
        Date createdAt = new Date();
        String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();

        try {
            String uploadDir = "C:/Users/iamco/OneDrive/Desktop/SpringBoot/StoreApp/StoreApp/public/images/";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            try(InputStream inputStream = image.getInputStream()){
                Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

        Product product = new Product();
        product.setName(productDto.getName());
        product.setBrand(productDto.getBrand());
        product.setCategory(productDto.getCategory());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setCreatedAt(createdAt);
        product.setImageFileName(storageFileName);

        productRepository.save(product);
        return  "redirect:/products";
    }

    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam int id) {

        try {
            Product product = productRepository.findById(id).orElseThrow();

            ProductDto productDto = new ProductDto();
            productDto.setId(product.getId());
            productDto.setName(product.getName());
            productDto.setBrand(product.getBrand());
            productDto.setCategory(product.getCategory());
            productDto.setPrice(product.getPrice());
            productDto.setDescription(product.getDescription());
            productDto.setImageFileName(product.getImageFileName());
            productDto.setCreatedAt(product.getCreatedAt());

            model.addAttribute("productDto", productDto);

        } catch (Exception ex) {
            return "redirect:/products";
        }

        return "products/EditProduct";
    }

    @PostMapping("/edit")
    public String updateProduct(@RequestParam int id,
                                @Valid @ModelAttribute("productDto") ProductDto productDto,
                                BindingResult result,
                                Model model) {

        try {
            Product product = productRepository.findById(id).orElseThrow();

            if (result.hasErrors()) {
                productDto.setImageFileName(product.getImageFileName());
                return "products/EditProduct";
            }

            // if user uploaded a new image → update
            if (!productDto.getImageFile().isEmpty()) {

                String uploadDir = "C:/Users/iamco/OneDrive/Desktop/SpringBoot/StoreApp/StoreApp/public/images/";

                // delete old image
                Path oldImagePath = Paths.get(uploadDir + product.getImageFileName());
                try {
                    Files.delete(oldImagePath);
                } catch (Exception ex) {
                    System.out.println("Failed to delete old image: " + ex.getMessage());
                }

                // store new image
                MultipartFile image = productDto.getImageFile();
                Date createdAt = new Date();
                String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();

                try (InputStream inputStream = image.getInputStream()) {
                    Files.copy(inputStream,
                            Paths.get(uploadDir + storageFileName),
                            StandardCopyOption.REPLACE_EXISTING);
                }

                product.setImageFileName(storageFileName);
            }

            // update text fields
            product.setName(productDto.getName());
            product.setBrand(productDto.getBrand());
            product.setCategory(productDto.getCategory());
            product.setPrice(productDto.getPrice());
            product.setDescription(productDto.getDescription());

            productRepository.save(product);

        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

        return "redirect:/products";
    }

    @GetMapping("/delete")
    public String deleteProduct(@RequestParam int id) {

        Product product = productRepository.findById(id)
                .orElse(null);

        if (product == null) {
            System.out.println("Product not found");
            return "redirect:/products";
        }

        String uploadDir = "C:/Users/iamco/OneDrive/Desktop/SpringBoot/StoreApp/StoreApp/public/images/";
        Path imagePath = Paths.get(uploadDir + product.getImageFileName());

        try {
            // Delete image file only if exists
            if (Files.exists(imagePath)) {
                Files.delete(imagePath);
                System.out.println("Image deleted: " + imagePath);
            } else {
                System.out.println("Image file not found: " + imagePath);
            }
        } catch (Exception e) {
            System.out.println("Failed to delete image: " + e.getMessage());
        }

        productRepository.delete(product);

        return "redirect:/products";
    }

}