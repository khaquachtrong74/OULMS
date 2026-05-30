/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.controllers;

import com.qtk.pojo.Topic;
import com.qtk.services.TopicService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author ngodo
 */
@Controller
@RequestMapping("/admin")
public class TopicController {
    
    @Autowired
    private TopicService topicService;
    
    @RequestMapping("/topics")
    public String topicView(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("topics", topicService.getTopic(params));
        return "topic";
    }
    
    @GetMapping("/topic")
    public String createView(Model model) {
        model.addAttribute("topic", new Topic());
        return "topicCreate";
    }
    
    @PostMapping("/topic")
    public String create(@ModelAttribute(value = "topic") Topic t) {
        this.topicService.addOrUpdateTopic(t);
        return "redirect:/admin/topics";
    }
    
    @GetMapping("/topics/{topicId}")
    public String updateView(Model model, @PathVariable(value = "topicId") int id) {
        model.addAttribute("topic", topicService.getTopicById(id));
        return "topicCreate";
    }
}
