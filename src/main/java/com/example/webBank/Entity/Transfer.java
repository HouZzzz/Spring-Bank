package com.example.webBank.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String senderFullname, getterFullname;

    private int summSended;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSenderFullname() {
        return senderFullname;
    }

    public void setSenderFullname(String senderFullname) {
        this.senderFullname = senderFullname;
    }

    public String getGetterFullname() {
        return getterFullname;
    }

    public void setGetterFullname(String getterFullname) {
        this.getterFullname = getterFullname;
    }

    public int getSummSended() {
        return summSended;
    }

    public void setSummSended(int summSended) {
        this.summSended = summSended;
    }
}
