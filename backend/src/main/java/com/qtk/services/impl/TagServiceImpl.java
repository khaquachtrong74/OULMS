/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.services.impl;

import com.qtk.pojo.Tag;
import com.qtk.repositories.TagRepository;
import com.qtk.services.TagService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author lossrunner
 */

@Service
public class TagServiceImpl implements TagService{

     
    @Autowired
    private TagRepository tagRepo;
    @Override
    public List<Tag> getTag(Map<String, String> params) {
        return this.tagRepo.getTag(params);
    }

    @Override
    public void addOrUpdateTag(Tag t) {
        this.tagRepo.addOrUpdateTag(t);
    }

    @Override
    public Tag getTagById(int id) {
        return this.tagRepo.getTagById(id);
    }

    @Override
    public void deleteTag(int id) {
        this.tagRepo.deleteTag(id);
    }
    
}
