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
@Table(name = "chat")
public class Chat extends BaseEntity{
    
//    sender_id FK(USER)
//    receiver_id FK (USER)
    
    @Basic(optional = false)
    @Column(name = "message", length = 100)
    private String message;
 
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User senderId;
    
    @JoinColumn(name = "receiver_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User receiverId;
    public Chat() {
    }
    
    
    
}
