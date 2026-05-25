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
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Table(name = "user")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.id = :id"),
    @NamedQuery(name = "User.findByFirstName", query = "SELECT u FROM User u WHERE u.firstName = :firstName"),
    @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
    @NamedQuery(name = "User.findByPassword", query = "SELECT u FROM User u WHERE u.password = :password"),
    @NamedQuery(name = "User.findByRole", query = "SELECT u FROM User u WHERE u.role = :role"),
    @NamedQuery(name = "User.findByCreatedAt", query = "SELECT u FROM User u WHERE u.createdAt = :createdAt"),
    @NamedQuery(name = "User.findByUpdatedAt", query = "SELECT u FROM User u WHERE u.updatedAt = :updatedAt"),
    @NamedQuery(name = "User.findByLastName", query = "SELECT u FROM User u WHERE u.lastName = :lastName"),
    @NamedQuery(name = "User.findByAvatar", query = "SELECT u FROM User u WHERE u.avatar = :avatar")})
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "first_name")
    private String firstName;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "password")
    private String password;
    @Size(max = 8)
    @Column(name = "role")
    private String role;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "last_name")
    private String lastName;
    @Size(max = 100)
    @Column(name = "avatar")
    private String avatar;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Set<ForumPost> forumPostSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "uploadedBy")
    private Set<Document> documentSet;
    @OneToMany(mappedBy = "studentId")
    private Set<StudentAnswer> studentAnswerSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "studentId")
    private Set<QuizzesResult> quizzesResultSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "receiverId")
    private Set<Chat> chatSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "senderId")
    private Set<Chat> chatSet1;
    @OneToMany(mappedBy = "lecturerId")
    private Set<Course> courseSet;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "userId")
    private Progress progress;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Set<Comment> commentSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Set<ForumComment> forumCommentSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Set<Transaction> transactionSet;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "studentId")
    private CourseStudent courseStudent;

    public User() {
    }

    public User(Integer id) {
        this.id = id;
    }

    public User(Integer id, String firstName, String email, String password, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.email = email;
        this.password = password;
        this.lastName = lastName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @XmlTransient
    public Set<ForumPost> getForumPostSet() {
        return forumPostSet;
    }

    public void setForumPostSet(Set<ForumPost> forumPostSet) {
        this.forumPostSet = forumPostSet;
    }

    @XmlTransient
    public Set<Document> getDocumentSet() {
        return documentSet;
    }

    public void setDocumentSet(Set<Document> documentSet) {
        this.documentSet = documentSet;
    }

    @XmlTransient
    public Set<StudentAnswer> getStudentAnswerSet() {
        return studentAnswerSet;
    }

    public void setStudentAnswerSet(Set<StudentAnswer> studentAnswerSet) {
        this.studentAnswerSet = studentAnswerSet;
    }

    @XmlTransient
    public Set<QuizzesResult> getQuizzesResultSet() {
        return quizzesResultSet;
    }

    public void setQuizzesResultSet(Set<QuizzesResult> quizzesResultSet) {
        this.quizzesResultSet = quizzesResultSet;
    }

    @XmlTransient
    public Set<Chat> getChatSet() {
        return chatSet;
    }

    public void setChatSet(Set<Chat> chatSet) {
        this.chatSet = chatSet;
    }

    @XmlTransient
    public Set<Chat> getChatSet1() {
        return chatSet1;
    }

    public void setChatSet1(Set<Chat> chatSet1) {
        this.chatSet1 = chatSet1;
    }

    @XmlTransient
    public Set<Course> getCourseSet() {
        return courseSet;
    }

    public void setCourseSet(Set<Course> courseSet) {
        this.courseSet = courseSet;
    }

    public Progress getProgress() {
        return progress;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }

    @XmlTransient
    public Set<Comment> getCommentSet() {
        return commentSet;
    }

    public void setCommentSet(Set<Comment> commentSet) {
        this.commentSet = commentSet;
    }

    @XmlTransient
    public Set<ForumComment> getForumCommentSet() {
        return forumCommentSet;
    }

    public void setForumCommentSet(Set<ForumComment> forumCommentSet) {
        this.forumCommentSet = forumCommentSet;
    }

    @XmlTransient
    public Set<Transaction> getTransactionSet() {
        return transactionSet;
    }

    public void setTransactionSet(Set<Transaction> transactionSet) {
        this.transactionSet = transactionSet;
    }

    public CourseStudent getCourseStudent() {
        return courseStudent;
    }

    public void setCourseStudent(CourseStudent courseStudent) {
        this.courseStudent = courseStudent;
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
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.qtk.pojo.User[ id=" + id + " ]";
    }
    
}
