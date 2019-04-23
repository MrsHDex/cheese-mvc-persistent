package org.launchcode.controllers;

import org.launchcode.models.Category;
import org.launchcode.models.data.CategoryDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryDAO categoryDAO;

    @RequestMapping(value = "")
    public String index(Model template) {
        template.addAttribute("categories", categoryDAO.findAll());
        template.addAttribute("title", "Categories");
        return "category/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddCategoryForm(Model template) {
        template.addAttribute("title", "Add Category");
        template.addAttribute(new Category());
        return "category/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddCategoryForm(@ModelAttribute  @Valid Category newCategory,
                                       Errors errors, Model template) {

        if (errors.hasErrors()) {
            template.addAttribute("title", "Add Category");
            return "category/add";
        }

        categoryDAO.save(newCategory);
        return "redirect:";
    }
}
