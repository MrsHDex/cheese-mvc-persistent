package org.launchcode.controllers;

import org.launchcode.models.Category;
import org.launchcode.models.Cheese;
import org.launchcode.models.data.CategoryDAO;
import org.launchcode.models.data.CheeseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping("cheese")
public class CheeseController {

    @Autowired
    private CheeseDao cheeseDao;

    @Autowired
    private CategoryDAO categoryDAO;

    // Request path: /cheese
    @RequestMapping(value = "")
    public String index(Model template) {

        template.addAttribute("cheeses", cheeseDao.findAll());
        template.addAttribute("title", "My Cheeses");

        return "cheese/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddCheeseForm(Model template) {
        template.addAttribute("title", "Add Cheese");
        template.addAttribute(new Cheese());
        template.addAttribute("categories", categoryDAO.findAll());
        return "cheese/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddCheeseForm(@ModelAttribute  @Valid Cheese newCheese,
                                       Errors errors, @RequestParam int categoryId, Model template) {

        if (errors.hasErrors()) {
            template.addAttribute("title", "Add Cheese");
            template.addAttribute("categories", categoryDAO.findAll());
            return "cheese/add";
        }

        Category cat = categoryDAO.findOne(categoryId);
        newCheese.setCategory(cat);
        cheeseDao.save(newCheese);
        return "redirect:";
    }

    @RequestMapping(value = "remove", method = RequestMethod.GET)
    public String displayRemoveCheeseForm(Model template) {
        template.addAttribute("cheeses", cheeseDao.findAll());
        template.addAttribute("title", "Remove Cheese");
        return "cheese/remove";
    }

    @RequestMapping(value = "remove", method = RequestMethod.POST)
    public String processRemoveCheeseForm(@RequestParam int[] cheeseIds) {

        for (int cheeseId : cheeseIds) {
            cheeseDao.delete(cheeseId);
        }

        return "redirect:";
    }

    @RequestMapping(value = "category", method = RequestMethod.GET)
    public String category(Model template, @RequestParam int id) {
        Category cat = categoryDAO.findOne(id);
        List<Cheese> cheeses = cat.getCheeses();
        template.addAttribute("cheeses", cheeses);
        template.addAttribute("title", "Cheeses in Category: " + cat.getName());
        return "cheese/index";
    }

}
