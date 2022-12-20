package org.example.auslanderbehorde.sessionfinder.model;

public class SessionModel {
    private  String dswid;
    private  String dsrid;
    private String requestId;

    public SessionModel(String dswid, String dsrid) {
        this.dswid = dswid;
        this.dsrid = dsrid;
    }

    public SessionModel(String dswid, String dsrid, String requestId) {
        this.dswid = dswid;
        this.dsrid = dsrid;
        this.requestId = requestId;
    }

    public SessionModel() {
    }

    public String getDswid() {
        return dswid;
    }

    public void setDswid(String dswid) {
        this.dswid = dswid;
    }

    public String getDsrid() {
        return dsrid;
    }

    public void setDsrid(String dsrid) {
        this.dsrid = dsrid;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
