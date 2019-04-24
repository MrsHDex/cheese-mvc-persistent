package org.launchcode.controllers;

import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDAO;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@RequestMapping("menu")
public class MenuController {

    @Autowired
    MenuDAO menuDAO;

    @Autowired
    CheeseDao cheeseDao;

    @RequestMapping(value = "")
    public String index(Model template) {
        template.addAttribute("cheeses", menuDAO.findAll());
        template.addAttribute("title", "Menus");
        return "menu/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String addMenuForm(Model template) {
        template.addAttribute(new Menu());
        template.addAttribute("title", "Add Menu");
        return "menu/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddMenuForm(Model template, @ModelAttribute @Valid Menu newMenu, Errors errors) {
        if (errors.hasErrors()) {
            template.addAttribute("title", "Add Menu");
            return "menu/add";
        }
        menuDAO.save(newMenu);
        return "redirect:view/" + newMenu.getId();
    }

    @RequestMapping(value = "view/{menuId}", method = RequestMethod.GET)
    public String viewMenuForm(Model template, @PathVariable int menuId) {
        Menu menu = menuDAO.findOne(menuId);
        template.addAttribute("title", menu.getName());
        template.addAttribute("cheeses", menu.getCheeses());
        template.addAttribute("menuId", menu.getId());
        return "menu/view";
    }

    @RequestMapping(value = "add-item/{menuId}", method = RequestMethod.GET)
    public String addItemForm(Model template, @PathVariable int menuId) {
        Menu menu = menuDAO.findOne(menuId);
        AddMenuItemForm form = new AddMenuItemForm(cheeseDao.findAll(), menu);
        template.addAttribute("title", "Add Item to Menu: " + menu.getName());
        template.addAttribute("form", form);
        return "menu/add-item";
    }

    @RequestMapping(value = "add-item", method = RequestMethod.POST)
    public String processaddItemForm(Model template, @ModelAttribute @Valid AddMenuItemForm form, Errors errors) {
        if (errors.hasErrors()) {
            template.addAttribute("form", form);
            return "menu/add-item";
        }
        Cheese theCheese = cheeseDao.findOne(form.getCheeseId());
        Menu theMenu = menuDAO.findOne(form.getMenuId());
        theMenu.addItem(theCheese);
        menuDAO.save(theMenu);
        return "redirect:/menu/view/" + theMenu.getId();
    }
}
