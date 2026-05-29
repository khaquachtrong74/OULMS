/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.qtk.repositories;

import com.qtk.pojo.Topic;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ngodo
 */
public interface TopicRepository {
    List<Topic> getTopic(Map<String, String> params);
    void addOrUpdateTopic(Topic t);
    Topic getTopicById(int id);
    void deleteTopic(int id);
}
