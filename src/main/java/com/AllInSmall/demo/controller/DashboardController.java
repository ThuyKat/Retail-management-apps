package com.AllInSmall.demo.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

    @GetMapping("/manageUser")
    public String manageUser(Model model, Principal principal) {
        addUsernameToModel(principal, model);
        return "manageUser";
    }

    @GetMapping("/manageCategory")
    public String manageCategory(Model model, Principal principal) {
        addUsernameToModel(principal, model);
        return "manageCategory";
    }

    @GetMapping("/manageProduct")
    public String manageProduct(Model model, Principal principal) {
        addUsernameToModel(principal, model);
        return "manageProduct";
    }

    @GetMapping("/manageOrder")
    public String manageOrder(Model model, Principal principal) {
        addUsernameToModel(principal, model);
        return "manageOrder";
    }

    @GetMapping("/viewReport")
    public String viewReport(Model model, Principal principal) {
        addUsernameToModel(principal, model);
        return "viewReport";
    }

    private void addUsernameToModel(Principal principal, Model model) {
        if (principal != null) {
            model.addAttribute("username", principal.getName());
        }
    }
}
