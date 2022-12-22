package org.example.auslanderbehorde.sessionfinder.model;

import java.util.Objects;

public class SessionInfo {
    private  String dswid;
    private  String dsrid;
    private String requestId;

    public SessionInfo(String dswid, String dsrid) {
        this.dswid = dswid;
        this.dsrid = dsrid;
    }

    public SessionInfo(String dswid, String dsrid, String requestId) {
        this.dswid = dswid;
        this.dsrid = dsrid;
        this.requestId = requestId;
    }

    public SessionInfo() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessionInfo sessionInfo = (SessionInfo) o;
        return Objects.equals(dswid, sessionInfo.dswid) && Objects.equals(dsrid, sessionInfo.dsrid) && Objects.equals(requestId, sessionInfo.requestId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dswid, dsrid, requestId);
    }

    @Override
    public String toString() {
        return "Session{" +
                "dswid='" + dswid + '\'' +
                ", dsrid='" + dsrid + '\'' +
                ", requestId='" + requestId + '\'' +
                '}';
    }
}
