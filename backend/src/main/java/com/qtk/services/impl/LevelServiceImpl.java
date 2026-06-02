/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.services.impl;

import com.qtk.pojo.Level;
import com.qtk.repositories.LevelRepository;
import com.qtk.services.LevelService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ngodo
 */
@Service
public class LevelServiceImpl implements LevelService{
    
    @Autowired
    private LevelRepository levelRepo;

    @Override
    public List<Level> getLevel(Map<String, String> params) {
        return this.levelRepo.getLevel(params);
    }

    @Override
    public Level getLevelById(int id) {
        return this.levelRepo.getLevelById(id);
    }
    
}
