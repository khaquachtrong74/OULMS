/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.pojo;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author ngodo
 */
@Entity
@Table(name = "progress")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Progress.findAll", query = "SELECT p FROM Progress p"),
    @NamedQuery(name = "Progress.findById", query = "SELECT p FROM Progress p WHERE p.id = :id"),
    @NamedQuery(name = "Progress.findByStatus", query = "SELECT p FROM Progress p WHERE p.status = :status"),
    @NamedQuery(name = "Progress.findByTimeSpent", query = "SELECT p FROM Progress p WHERE p.timeSpent = :timeSpent"),
    @NamedQuery(name = "Progress.findByCreatedAt", query = "SELECT p FROM Progress p WHERE p.createdAt = :createdAt"),
    @NamedQuery(name = "Progress.findByUpdatedAt", query = "SELECT p FROM Progress p WHERE p.updatedAt = :updatedAt")})
public class Progress implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 11)
    @Column(name = "status")
    private String status;
    @Column(name = "time_spent")
    private Integer timeSpent;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @JoinColumn(name = "document_id", referencedColumnName = "id")
    @OneToOne(optional = false)
    private Document documentId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @OneToOne(optional = false)
    private User userId;

    public Progress() {
    }

    public Progress(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(Integer timeSpent) {
        this.timeSpent = timeSpent;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Document getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Document documentId) {
        this.documentId = documentId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Progress)) {
            return false;
        }
        Progress other = (Progress) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qtk.pojo.Progress[ id=" + id + " ]";
    }
    
}
