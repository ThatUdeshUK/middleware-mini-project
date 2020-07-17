package com.studentx.frontend.controllers;

import com.studentx.frontend.models.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
public class StudentController {

    private final String baseUrl;
    private final RestTemplate restTemplate;

    public StudentController(@Value("${properties.base-url}") String baseUrl, RestTemplate restTemplate) {
        this.baseUrl = baseUrl;
        this.restTemplate = restTemplate;
    }

    @GetMapping()
    public String listing(Model model) {
        Student[] students = restTemplate.getForObject(baseUrl + "/students", Student[].class);

        model.addAttribute("students", students);
        return "listing";
    }

}
