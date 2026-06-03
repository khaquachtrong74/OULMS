/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.services;

import com.qtk.pojo.Tag;
import java.util.List;
import java.util.Map;

/**
 *
 * @author lossrunner
 */
public interface TagService {
    List<Tag> getTag(Map<String, String> params);
    void addOrUpdateTag(Tag t);
    Tag getTagById(int id);
    void deleteTag(int id);

}
