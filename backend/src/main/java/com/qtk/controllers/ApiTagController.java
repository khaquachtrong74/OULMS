package com.qtk.controllers;

import com.qtk.pojo.Tag;
import com.qtk.pojo.Topic;
import com.qtk.services.TagService;
import com.qtk.services.TopicService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author lossrunner
 */

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:3000", "https://quietly-untrashed-darby.ngrok-free.dev"}, allowCredentials = "true")
public class ApiTagController {
        
    @Autowired
    private TagService tagService;
    
    @GetMapping("/tags")
    public ResponseEntity<List<Tag>> list(@RequestParam Map<String, String> params) {
        return new ResponseEntity<>(this.tagService.getTag(params), HttpStatus.OK);
    }
    
    @DeleteMapping("/tags/{tagId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "tagId") int id) {
        this.tagService.deleteTag(id);
    }
}
