/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.qtk.services;

import com.qtk.pojo.Level;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ngodo
 */
public interface LevelService {
    List<Level> getLevel(Map<String, String> params);
}
