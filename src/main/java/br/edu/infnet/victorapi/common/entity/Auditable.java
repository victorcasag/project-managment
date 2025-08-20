package br.edu.infnet.victorapi.common.entity;

public interface Auditable {
    
    String getAuditInfo();
    
    boolean isRecentlyModified();
    
    String getAuditIdentifier();
}
