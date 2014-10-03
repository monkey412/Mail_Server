package vn.aptech.mail.Entities;
// Generated Oct 3, 2014 11:46:33 AM by Hibernate Tools 3.6.0


import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Events generated by hbm2java
 */
@Entity
@Table(name="Events"
    ,schema="dbo"
    ,catalog="MAIL_SERVER"
)
public class Events  implements java.io.Serializable {


     private int eventId;
     private String title;
     private String content;
     private String description;
     private Date createDate;
     private String picture;

    public Events() {
    }

	
    public Events(int eventId, String title, String content, Date createDate) {
        this.eventId = eventId;
        this.title = title;
        this.content = content;
        this.createDate = createDate;
    }
    public Events(int eventId, String title, String content, String description, Date createDate, String picture) {
       this.eventId = eventId;
       this.title = title;
       this.content = content;
       this.description = description;
       this.createDate = createDate;
       this.picture = picture;
    }
   
     @Id 

    
    @Column(name="EventId", unique=true, nullable=false)
    public int getEventId() {
        return this.eventId;
    }
    
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    
    @Column(name="Title", nullable=false)
    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    
    @Column(name="Content", nullable=false)
    public String getContent() {
        return this.content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }

    
    @Column(name="Description")
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="CreateDate", nullable=false, length=23)
    public Date getCreateDate() {
        return this.createDate;
    }
    
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    
    @Column(name="Picture", length=60)
    public String getPicture() {
        return this.picture;
    }
    
    public void setPicture(String picture) {
        this.picture = picture;
    }




}


