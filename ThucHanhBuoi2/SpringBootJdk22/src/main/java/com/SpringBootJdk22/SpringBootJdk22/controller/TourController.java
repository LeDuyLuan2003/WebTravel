package com.SpringBootJdk22.SpringBootJdk22.controller;

import com.SpringBootJdk22.SpringBootJdk22.model.Image;
import com.SpringBootJdk22.SpringBootJdk22.model.ItemCategory;
import com.SpringBootJdk22.SpringBootJdk22.model.Tour;
import com.SpringBootJdk22.SpringBootJdk22.service.CategoryService;
import com.SpringBootJdk22.SpringBootJdk22.service.ItemCategoryService;
import com.SpringBootJdk22.SpringBootJdk22.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("admin/tour")
public class TourController {
    @Autowired
    private TourService tourService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ItemCategoryService itemCategoryService;

    @GetMapping()
    public String showProductList(Model model) {
        model.addAttribute("tours", tourService.getAllProducts());
        return "/admin/tours/tour-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("tour", new Tour());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "/admin/tours/add-tour";
    }

    @PostMapping("/add")
    public String addProduct(@Valid @ModelAttribute Tour tour,
                             BindingResult result,
                             @RequestParam("avatarFile") MultipartFile avatarFile,
                             @RequestParam("imageFiles") MultipartFile[] imageFiles,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "/admin/tours/add-tour";
        }

        try {
            if (!avatarFile.isEmpty()) {
                String avatarFileName = saveFile(avatarFile);
                tour.setAvatar(avatarFileName);
            }

            Tour savedTour = tourService.addProduct(tour);

            for (MultipartFile imageFile : imageFiles) {
                if (!imageFile.isEmpty()) {
                    String imageFileName = saveFile(imageFile);
                    Image image = new Image();
                    image.setUrl(imageFileName);
                    image.setTour(savedTour);
                    tourService.addImage(image);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/admin/tour";
    }

    @GetMapping("/update/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Tour tour = tourService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tour Id:" + id));
        model.addAttribute("tour", tour);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "/admin/tours/update-tour";
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id,
                                @Valid @ModelAttribute Tour tour,
                                BindingResult result,
                                @RequestParam("avatarFile") MultipartFile avatarFile,
                                @RequestParam("imageFiles") MultipartFile[] imageFiles,
                                Model model) {
        if (result.hasErrors()) {
            tour.setId(id);
            model.addAttribute("categories", categoryService.getAllCategories());
            return "admin/tours/update-tour";
        }

        try {
            Tour existingTour = tourService.getProductById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid tour Id:" + id));

            if (!avatarFile.isEmpty()) {
                String avatarFileName = saveFile(avatarFile);
                existingTour.setAvatar(avatarFileName);
            }

            existingTour.setName(tour.getName());
            existingTour.setPrice(tour.getPrice());
            existingTour.setDescription(tour.getDescription());
            existingTour.setItemCategory(tour.getItemCategory());

            Tour updatedTour = tourService.updateProduct(existingTour);

            for (MultipartFile imageFile : imageFiles) {
                if (!imageFile.isEmpty()) {
                    String imageFileName = saveFile(imageFile);
                    Image image = new Image();
                    image.setUrl(imageFileName);
                    image.setTour(updatedTour);
                    tourService.addImage(image);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/admin/tour";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        tourService.deleteProductById(id);
        return "redirect:/admin/tour";
    }

    @GetMapping("/detail/{id}")
    public String showProductDetail(@PathVariable Long id, Model model) {
        Tour tour = tourService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tour Id:" + id));
        model.addAttribute("tour", tour);
        return "/admin/tours/tour-detail";
    }

    @GetMapping("/search")
    public String searchProductsByName(@RequestParam("name") String name, Model model) {
        List<Tour> searchResults = tourService.findProductsByName(name);
        model.addAttribute("products", searchResults);
        return "/admin/tours/tour-list";
    }


    private String saveFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get("uploads", fileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());
        return fileName;
    }

}
