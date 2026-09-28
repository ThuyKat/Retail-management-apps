package com.AllInSmall.demo.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@Tag(name = "Dashboard", description = "Dashboard APIs")
public class DashboardController {

	@Operation(summary = "View user management features")
    @GetMapping("/manageUser")
    public String manageUser(Model model, Principal principal) {
        addUsernameToModel(principal, model);
        return "manageUser";
    }

	@Operation(summary = "View category management features")
    @GetMapping("/manageCategory")
    public String manageCategory(Model model, Principal principal) {
        addUsernameToModel(principal, model);
        return "manageCategory";
    }

	@Operation(summary = "View product management features")
    @GetMapping("/manageProduct")
    public String manageProduct(Model model, Principal principal) {
        addUsernameToModel(principal, model);
        return "manageProduct";
    }

	@Operation(summary = "View order management features")
    @GetMapping("/manageOrder")
    public String manageOrder(Model model, Principal principal) {
        addUsernameToModel(principal, model);
        return "manageOrder";
    }

	@Operation(summary = "View report management features")
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
