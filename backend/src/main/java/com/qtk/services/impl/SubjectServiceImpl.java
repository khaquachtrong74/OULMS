/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.services.impl;

import com.qtk.pojo.Subject;
import com.qtk.repositories.SubjectRepository;
import com.qtk.services.SubjectService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author ngodo
 */
public class SubjectServiceImpl implements SubjectService{
    
    @Autowired
    private SubjectRepository subRepo;

    @Override
    public List<Subject> getSubject(Map<String, String> params) {
        return this.subRepo.getSubject(params);
    }

    @Override
    public void addOrUpdateSubject(Subject s) {
        this.subRepo.addOrUpdateSubject(s);
    }

    @Override
    public Subject getSubjectById(int id) {
        return this.subRepo.getSubjectById(id);
    }

    @Override
    public void deleteSubject(int id) {
        this.subRepo.deleteSubject(id);
    }
    
}
