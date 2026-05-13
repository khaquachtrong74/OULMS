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
@Table(name = "question")
public class Question extends BaseEntity {
    
//    quizzes_id FK(quizzes)
    @Basic(optional = false)
    @Column(name = "content", length = 100)
    private String content;
    
    @Basic(optional = false)
    @Column(name = "correct_answer", length = 1)
    private Character correctAnswer;
    
    @JoinColumn(name = "quizzes_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Quizzes quizzesId;
    
    
}
