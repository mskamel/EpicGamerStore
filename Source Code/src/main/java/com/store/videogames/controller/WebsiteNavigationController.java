package com.store.videogames.controller;

import com.store.videogames.service.VideogameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class WebsiteNavigationController
{
    @Autowired
    VideogameService videogameService;

    @GetMapping("/")
    public String getHomePage(Model model)
    {
        model.addAttribute("videogames",videogameService.retriveAllVideogames());
        return "index";
    }

}