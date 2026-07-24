/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.services.impl;

import com.qtk.pojo.Topic;
import com.qtk.repositories.TopicRepository;
import com.qtk.services.TopicService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ngodo
 */
@Service
public class TopicServiceImpl implements TopicService{
    
    @Autowired
    private TopicRepository topicRepo;

    @Override
    public List<Topic> getTopic(Map<String, String> params) {
        return this.topicRepo.getTopic(params);
    }

    @Override
    public void addOrUpdateTopic(Topic t) {
        this.topicRepo.addOrUpdateTopic(t);
    }

    @Override
    public Topic getTopicById(int id) {
        return this.topicRepo.getTopicById(id);
    }

    @Override
    public void deleteTopic(int id) {
        this.topicRepo.deleteTopic(id);
    }
    
}
