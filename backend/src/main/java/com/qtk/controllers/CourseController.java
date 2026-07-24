package com.qtk.controllers;

import com.qtk.dto.CourseRequestDTO;
import com.qtk.pojo.Course;
import com.qtk.services.CourseService;
import com.qtk.services.SubjectService;
import com.qtk.services.UserService;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @Autowired
    private SubjectService subjectService;

    @GetMapping("/admin/courses")
    public String listCourses(Model model, @RequestParam Map<String, String> params) {
        int currentPage = Integer.parseInt(params.getOrDefault("page", "1"));
        int pageSize = this.courseService.getCoursePageSize();

        long totalCourses = this.courseService.countCourses(params);
        int totalPages = (int) Math.ceil((double) totalCourses / pageSize);

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("kw", params.get("kw")); 

        model.addAttribute("courses", this.courseService.getCourses(params));
        return "course";
    }

    @GetMapping("/admin/course")
    public String showCreateForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("lecturers", this.userService.getLecturers());
        return "courseCreate";
    }

    @GetMapping("/admin/courses/{id}")
    public String showUpdateForm(Model model, @PathVariable(value = "id") int id) {
        Course c = this.courseService.getCourseById(id);

        CourseRequestDTO dto = new CourseRequestDTO();
        dto.setId(c.getId());
        dto.setName(c.getName());
        dto.setDescription(c.getDescription());
        dto.setPrice(c.getPrice());

        if (c.getSubjectId() != null) {
            dto.setSubjectId(c.getSubjectId().getId());
        }
        if (c.getLecturerId() != null) {
            dto.setLecturerId(c.getLecturerId().getId());
        }

        model.addAttribute("course", dto);

        model.addAttribute("subjects", this.subjectService.getSubject(null));
        model.addAttribute("lecturers", this.userService.getLecturers());

        return "courseCreate";
    }

    @PostMapping("/admin/course")
    public String addOrUpdateCourse(@Valid @ModelAttribute(value = "course") CourseRequestDTO dto,
            BindingResult rs,
            Model model) {

        if (rs.hasErrors()) {
            model.addAttribute("subjects", this.subjectService.getSubject(null));
            model.addAttribute("lecturers", this.userService.getLecturers());

            return "courseCreate";
        }

        this.courseService.addOrUpdateCourse(dto);

        return "redirect:/admin/courses";
    }

}
