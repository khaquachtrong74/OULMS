/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.pojo;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 *
 * @author lossrunner
 */

@Entity
@Table(name = "student_answer")
public class StudentAnswer extends BaseEntity{
    
    @Basic(optional = false)
    @Column(name = "answer", length = 1)
    private Character answer;


    @Basic(optional = false)
    @Column(name = "is_correct")
    private Boolean is_correct;
    
//    question_id
//    student_id
    
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Question questionId;
    
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User studentId;
    
    
    
    
    
    
}
