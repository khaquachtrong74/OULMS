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

/**
 *
 * @author ngodo
 */
@Entity
@Table(name = "question")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Question.findAll", query = "SELECT q FROM Question q"),
    @NamedQuery(name = "Question.findById", query = "SELECT q FROM Question q WHERE q.id = :id"),
    @NamedQuery(name = "Question.findByContent", query = "SELECT q FROM Question q WHERE q.content = :content"),
    @NamedQuery(name = "Question.findByCorrectAnswer", query = "SELECT q FROM Question q WHERE q.correctAnswer = :correctAnswer"),
    @NamedQuery(name = "Question.findByCreatedAt", query = "SELECT q FROM Question q WHERE q.createdAt = :createdAt"),
    @NamedQuery(name = "Question.findByUpdatedAt", query = "SELECT q FROM Question q WHERE q.updatedAt = :updatedAt")})
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "content")
    private String content;
    @Basic(optional = false)
    @NotNull
    @Column(name = "correct_answer")
    private Character correctAnswer;
    @Column(name = "created-at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @JoinColumn(name = "quizzes_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Quizzes quizzesId;
    @OneToMany(mappedBy = "questionId")
    private Set<StudentAnswer> studentAnswerSet;

    public Question() {
    }

    public Question(Integer id) {
        this.id = id;
    }

    public Question(Integer id, String content, Character correctAnswer) {
        this.id = id;
        this.content = content;
        this.correctAnswer = correctAnswer;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Character getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(Character correctAnswer) {
        this.correctAnswer = correctAnswer;
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

    public Quizzes getQuizzesId() {
        return quizzesId;
    }

    public void setQuizzesId(Quizzes quizzesId) {
        this.quizzesId = quizzesId;
    }

    @XmlTransient
    public Set<StudentAnswer> getStudentAnswerSet() {
        return studentAnswerSet;
    }

    public void setStudentAnswerSet(Set<StudentAnswer> studentAnswerSet) {
        this.studentAnswerSet = studentAnswerSet;
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
        if (!(object instanceof Question)) {
            return false;
        }
        Question other = (Question) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qtk.pojo.Question[ id=" + id + " ]";
    }
    
}
