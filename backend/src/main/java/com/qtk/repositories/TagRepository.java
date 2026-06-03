/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.repositories;

import com.qtk.pojo.Tag;
import java.util.List;
import java.util.Map;

/**
 *
 * @author lossrunner
 */
public interface TagRepository {
    public List<Tag> getTag(Map<String, String> params);
    public void addOrUpdateTag(Tag t);
    public Tag getTagById(int id);
    public void deleteTag(int id);
}
