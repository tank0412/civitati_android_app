package com.app.civitati.ui.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class NeedyHelp {
    @SerializedName("ID")
    @Expose
    private Integer id;
    @SerializedName("ASSISTANT_NICKNAME")
    @Expose
    private String assistantNickname;
    @SerializedName("NEEDY_ID")
    @Expose
    private String needyID;
    @SerializedName("HELP_INFO")
    @Expose
    private String helpInfo;
    @SerializedName("HELP_DATE")
    @Expose
    private Date helpDate;
    @SerializedName("SUBMIT_DATE")
    @Expose
    private Date submitDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAssistantNickname() {
        return assistantNickname;
    }

    public void setAssistantNickname(String assistantNickname) {
        this.assistantNickname = assistantNickname;
    }

    public String getNeedyID() {
        return needyID;
    }

    public void setNeedyID(String needyID) {
        this.needyID = needyID;
    }

    public String getHelpInfo() {
        return helpInfo;
    }

    public void setHelpInfo(String helpInfo) {
        this.helpInfo = helpInfo;
    }

    public Date getHelpDate() {
        return helpDate;
    }

    public void setHelpDate(Date helpDate) {
        this.helpDate = helpDate;
    }

    public Date getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
    }

}
