package com.example.medicare.data;

import java.io.Serializable;
import java.util.Date;



public class PatientDetailLines implements Serializable {

    Date joinDate;
    String comments;

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
