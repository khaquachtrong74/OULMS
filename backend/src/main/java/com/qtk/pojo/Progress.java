/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 *
 * @author lossrunner
 */

@Entity
@Table(name = "progress")
public class Progress extends BaseEntity{
    
//    user_id
//    document_id
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProgressStatus status;

    @Column(name = "time_spent")
    private Integer timeSpent;
    
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User userId;
    
    @JoinColumn(name = "document_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Document documentId;
    
    public Progress() {
    }
    
    
}
