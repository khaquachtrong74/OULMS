/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.services;

import com.qtk.pojo.Subject;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ngodo
 */
public interface SubjectService {
    List<Subject> getSubject(Map<String, String> params);
    void addOrUpdateSubject(Subject s);
    Subject getSubjectById(int id);
    void deleteSubject(int id);
}
