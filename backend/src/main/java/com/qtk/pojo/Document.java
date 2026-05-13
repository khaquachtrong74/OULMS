/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.pojo;

import jakarta.persistence.Basic;
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
@Table(name = "document")
public class Document extends BaseEntity{

    @Basic(optional = false)
    @Column(name = "title", length = 50)
    private String title;
    
    @Column(name = "description", length = 100)
    private String description;

    @Column(name = "url", length = 100)
    private String url;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private DocumentType type;
    
    
    //    topic_id
    @JoinColumn(name = "topic_id", referencedColumnName = "id")
    // INNER JOIN vi biet chac chan 100% co data o bang phu
    // toc do se nhanh hon so voi LEFT OUTER JOIN tren cac he MySQL, PostgreSQL
    @ManyToOne(optional = false) 
    private Topic topicId;
//    subject_id
    @JoinColumn(name = "subject_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Subject subjectId;
//    level_id
    
    @JoinColumn(name = "level_id", referencedColumnName = "id")
    @ManyToOne(optional = false) 
    private Level levelId;
//    upload_by FK(User)
    
    @JoinColumn(name = "uploaded_by", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User uploadedBy;

    public Document() {
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the type
     */
    public DocumentType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(DocumentType type) {
        this.type = type;
    }

    /**
     * @return the topicId
     */
    public Topic getTopicId() {
        return topicId;
    }

    /**
     * @param topicId the topicId to set
     */
    public void setTopicId(Topic topicId) {
        this.topicId = topicId;
    }

    /**
     * @return the subjectId
     */
    public Subject getSubjectId() {
        return subjectId;
    }

    /**
     * @param subjectId the subjectId to set
     */
    public void setSubjectId(Subject subjectId) {
        this.subjectId = subjectId;
    }

    /**
     * @return the levelId
     */
    public Level getLevelId() {
        return levelId;
    }

    /**
     * @param levelId the levelId to set
     */
    public void setLevelId(Level levelId) {
        this.levelId = levelId;
    }

    /**
     * @return the uploadedBy
     */
    public User getUploadedBy() {
        return uploadedBy;
    }

    /**
     * @param uploadedBy the uploadedBy to set
     */
    public void setUploadedBy(User uploadedBy) {
        this.uploadedBy = uploadedBy;
    }
    
    
}
