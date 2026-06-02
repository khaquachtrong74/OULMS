/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.qtk.pojo;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 *
 * @author ngodo
 */
@Entity
@Table(name = "quizzes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Quizzes.findAll", query = "SELECT q FROM Quizzes q"),
    @NamedQuery(name = "Quizzes.findById", query = "SELECT q FROM Quizzes q WHERE q.id = :id"),
    @NamedQuery(name = "Quizzes.findByTitle", query = "SELECT q FROM Quizzes q WHERE q.title = :title"),
    @NamedQuery(name = "Quizzes.findByCreatedAt", query = "SELECT q FROM Quizzes q WHERE q.createdAt = :createdAt"),
    @NamedQuery(name = "Quizzes.findByUpdatedAt", query = "SELECT q FROM Quizzes q WHERE q.updatedAt = :updatedAt")})
public class Quizzes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "title")
    private String title;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "quizzesId")
    private Set<Question> questionSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "quizzesId")
    private Set<QuizzesResult> quizzesResultSet;
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Course courseId;

    public Quizzes() {
    }

    public Quizzes(Integer id) {
        this.id = id;
    }

    public Quizzes(Integer id, String title) {
        this.id = id;
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    @XmlTransient
    public Set<Question> getQuestionSet() {
        return questionSet;
    }

    public void setQuestionSet(Set<Question> questionSet) {
        this.questionSet = questionSet;
    }

    @XmlTransient
    public Set<QuizzesResult> getQuizzesResultSet() {
        return quizzesResultSet;
    }

    public void setQuizzesResultSet(Set<QuizzesResult> quizzesResultSet) {
        this.quizzesResultSet = quizzesResultSet;
    }

    public Course getCourseId() {
        return courseId;
    }

    public void setCourseId(Course courseId) {
        this.courseId = courseId;
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
        if (!(object instanceof Quizzes)) {
            return false;
        }
        Quizzes other = (Quizzes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qtk.pojo.Quizzes[ id=" + id + " ]";
    }
    
}
