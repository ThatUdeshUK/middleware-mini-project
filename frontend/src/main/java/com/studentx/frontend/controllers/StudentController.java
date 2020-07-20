package com.studentx.frontend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studentx.frontend.models.ApiResponse;
import com.studentx.frontend.models.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping(path = "/create")
    public String createFrom(Model model) {
        model.addAttribute("student", new Student());

        return "create";
    }

    @PostMapping(path = "/create")
    public String createAction(@ModelAttribute Student student, Model model) {
        ApiResponse response = restTemplate.postForObject(baseUrl + "/students", student, ApiResponse.class);

        model.addAttribute("student", new Student());
        model.addAttribute("response", response);

        return "create";
    }

    @GetMapping(path = "/update")
    public String updateFrom(@RequestParam() Integer studentId, Model model) {
        Student student = restTemplate.getForObject(baseUrl + "/students/" + studentId, Student.class);
        model.addAttribute("student", student);

        return "update";
    }

    @PostMapping(path = "/update")
    public String updateAction(@ModelAttribute Student student, Model model) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonBody = objectMapper.writeValueAsString(student);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

            ApiResponse response = restTemplate.exchange(
                    baseUrl + "/students/" + student.getId(),
                    HttpMethod.PUT,
                    entity,
                    ApiResponse.class
            ).getBody();

            model.addAttribute("student", student);
            model.addAttribute("response", response);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            ApiResponse failed = new ApiResponse();
            failed.setStatus(500);
            failed.setMessage("Failed parsing JSON");
            model.addAttribute("response", failed);
            model.addAttribute("student", student);
        }

        return "update";
    }

    @GetMapping(path = "/delete")
    public String deleteFrom(@RequestParam() Integer studentId, Model model) {
        Student student = restTemplate.getForObject(baseUrl + "/students/" + studentId, Student.class);
        model.addAttribute("student", student);

        return "delete";
    }

    @PostMapping(path = "/delete")
    public String deleteAction(@ModelAttribute Student student, Model model) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ApiResponse response = restTemplate.exchange(
                baseUrl + "/students/" + student.getId(),
                HttpMethod.DELETE,
                entity,
                ApiResponse.class
        ).getBody();

        model.addAttribute("student", student);
        model.addAttribute("response", response);

        return "delete";
    }

}
