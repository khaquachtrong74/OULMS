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
@Table(name = "comment")
public class Comment extends BaseEntity{
    
//    user_id FK(USER)
//    document_id FK(Document)
    @Basic(optional = false)
    @Column(name = "content", length = 150)
    private String content;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User userId;
    
    @JoinColumn(name = "document_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Document documentId;
    
    public Comment() {
    }
    
    
}
