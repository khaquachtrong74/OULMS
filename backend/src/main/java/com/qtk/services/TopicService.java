/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.qtk.services;

import com.qtk.pojo.Topic;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ngodo
 */
public interface TopicService {
    List<Topic> getTopic(Map<String, String> params);
    void addOrUpdateTopic(Topic t);
    Topic getTopicById(int id);
    void deleteTopic(int id);
}
