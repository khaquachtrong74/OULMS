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
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 *
 * @author ngodo
 */
@Entity
@Table(name = "student_answer")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StudentAnswer.findAll", query = "SELECT s FROM StudentAnswer s"),
    @NamedQuery(name = "StudentAnswer.findById", query = "SELECT s FROM StudentAnswer s WHERE s.id = :id"),
    @NamedQuery(name = "StudentAnswer.findByAnswer", query = "SELECT s FROM StudentAnswer s WHERE s.answer = :answer"),
    @NamedQuery(name = "StudentAnswer.findByIsCorrect", query = "SELECT s FROM StudentAnswer s WHERE s.isCorrect = :isCorrect"),
    @NamedQuery(name = "StudentAnswer.findByCreatedAt", query = "SELECT s FROM StudentAnswer s WHERE s.createdAt = :createdAt"),
    @NamedQuery(name = "StudentAnswer.findByUpdatedAt", query = "SELECT s FROM StudentAnswer s WHERE s.updatedAt = :updatedAt")})
public class StudentAnswer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "answer")
    private Character answer;
    @Basic(optional = false)
    @NotNull
    @Column(name = "is_correct")
    private short isCorrect;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    @ManyToOne
    private Question questionId;
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    @ManyToOne
    private User studentId;

    public StudentAnswer() {
    }

    public StudentAnswer(Integer id) {
        this.id = id;
    }

    public StudentAnswer(Integer id, Character answer, short isCorrect) {
        this.id = id;
        this.answer = answer;
        this.isCorrect = isCorrect;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Character getAnswer() {
        return answer;
    }

    public void setAnswer(Character answer) {
        this.answer = answer;
    }

    public short getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(short isCorrect) {
        this.isCorrect = isCorrect;
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

    public Question getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Question questionId) {
        this.questionId = questionId;
    }

    public User getStudentId() {
        return studentId;
    }

    public void setStudentId(User studentId) {
        this.studentId = studentId;
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
        if (!(object instanceof StudentAnswer)) {
            return false;
        }
        StudentAnswer other = (StudentAnswer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qtk.pojo.StudentAnswer[ id=" + id + " ]";
    }
    
}
