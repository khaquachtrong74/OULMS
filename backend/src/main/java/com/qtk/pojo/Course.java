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
@Table(name = "course")
public class Course extends BaseEntity{
    
//    subject_id FK(subject)
//    lecturer_id FK(User)
    
    @Basic(optional = false)
    @Column(name = "name", length = 50)
    private String name;
        
    @Column(name = "description", length = 100)
    private String description;

    @Basic(optional = false)
    @Column(name = "price")
    private Float price;
    
    @JoinColumn(name = "subject_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Subject subjectId;
    
    @JoinColumn(name = "lecturer_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User lecturerId;
}
