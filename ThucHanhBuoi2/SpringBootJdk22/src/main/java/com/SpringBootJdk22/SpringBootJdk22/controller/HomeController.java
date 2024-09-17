package com.SpringBootJdk22.SpringBootJdk22.controller;

import com.SpringBootJdk22.SpringBootJdk22.model.Tour;
import com.SpringBootJdk22.SpringBootJdk22.service.CategoryService;
import com.SpringBootJdk22.SpringBootJdk22.service.TourService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    private TourService tourService;
    @Autowired
    private CategoryService categoryService; // Đảm bảo bạn đã inject CategoryService

    @ModelAttribute
    public void addCategoriesToModel(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
    }

    // Display a list of all products
    @GetMapping
    public String showProductList(Model model) {
        model.addAttribute("tours", tourService.getAllProducts());
        return "/users/home";
    }
    @GetMapping("/search")
    public String searchProductsByName(@RequestParam("name") String name, Model model) {
        List<Tour> searchResults = tourService.findProductsByName(name);
        model.addAttribute("tours", searchResults);
        return "/users/home"; // Template dùng cho người dùng
    }
    @GetMapping("/companyIntroduction")
    public String companyIntroduction(Model model) {

        return "/users/home"; // Template dùng cho người dùng
    }
    @RequestMapping("/403")
    public String accessDenied() {
        return "/403"; // Trả về tên của file HTML chứa nội dung lỗi 403
    }

}
