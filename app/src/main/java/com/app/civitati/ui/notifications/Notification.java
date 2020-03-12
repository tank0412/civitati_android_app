package com.app.civitati.ui.notifications;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Notification {

    @SerializedName("ID")
    @Expose
    private Integer id;

    @SerializedName("USER_NICKNAME")
    @Expose
    private String USER_NICKNAME;

    @SerializedName("NEEDY_HELP_ID")
    @Expose
    private Integer NEEDY_HELP_ID;

    @SerializedName("NOTIFICATION_DATE")
    @Expose
    private Date NOTIFICATION_DATE;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUSER_NICKNAME() {
        return USER_NICKNAME;
    }

    public void setUSER_NICKNAME(String USER_NICKNAME) {
        this.USER_NICKNAME = USER_NICKNAME;
    }

    public Integer getNEEDY_HELP_ID() {
        return NEEDY_HELP_ID;
    }

    public void setNEEDY_HELP_ID(Integer NEEDY_HELP_ID) {
        this.NEEDY_HELP_ID = NEEDY_HELP_ID;
    }

    public Date getNOTIFICATION_DATE() {
        return NOTIFICATION_DATE;
    }

    public void setNOTIFICATION_DATE(Date NOTIFICATION_DATE) {
        this.NOTIFICATION_DATE = NOTIFICATION_DATE;
    }
}
