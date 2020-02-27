package com.app.civitati.ui.dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;
import java.util.Date;

public class Needy {
    @SerializedName("ID")
    @Expose
    private Integer id;
    @SerializedName("NAME")
    @Expose
    private String name;

    @SerializedName("HELP_REASON")
    @Expose
    private String helpReason;

    @SerializedName("ADDRESS")
    @Expose
    private String adress;

    @SerializedName("TELEPHONE")
    @Expose
    private BigInteger telephone;

    @SerializedName("SUBMIT_DATE")
    @Expose
    private Date submitDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHelpReason() {
        return helpReason;
    }

    public void setHelpReason(String helpReason) {
        this.helpReason = helpReason;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public BigInteger getTelephone() {
        return telephone;
    }

    public void setTelephone(BigInteger telephone) {
        this.telephone = telephone;
    }

    public Date getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
    }



}
