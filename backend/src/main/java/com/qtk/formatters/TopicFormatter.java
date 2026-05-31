/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.formatters;

import com.qtk.pojo.Topic;
import java.text.ParseException;
import java.util.Locale;
import org.springframework.format.Formatter;

/**
 *
 * @author ngodo
 */
public class TopicFormatter implements Formatter<Topic>{

    @Override
    public String print(Topic topic, Locale locale) {
        return String.valueOf(topic.getId());
    }

    @Override
    public Topic parse(String topicId, Locale locale) throws ParseException {
        Topic t = new Topic();
        t.setId(Integer.valueOf(topicId));
        return t;
    }
    
}
