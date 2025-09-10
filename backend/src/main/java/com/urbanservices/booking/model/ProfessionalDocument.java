package com.urbanservices.booking.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "professional_documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfessionalDocument extends BaseEntity {
    
    public enum DocumentType {
        ID_PROOF,           // Government issued ID
        ADDRESS_PROOF,      // Proof of address
        QUALIFICATION,      // Educational certificates
        EXPERIENCE,         // Experience certificates
        POLICE_VERIFICATION,
        OTHER
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professional_id", nullable = false)
    private Professional professional;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentType documentType;
    
    @Column(nullable = false)
    private String documentName;
    
    @Column(nullable = false)
    private String documentUrl;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "is_verified")
    private boolean isVerified = false;
    
    @Column(name = "verified_by")
    private String verifiedBy; // Admin username who verified this document
    
    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;
    
    @Column(columnDefinition = "TEXT")
    private String verificationNotes;
    
    public ProfessionalDocument(Professional professional, DocumentType documentType, String documentName, String documentUrl) {
        this.professional = professional;
        this.documentType = documentType;
        this.documentName = documentName;
        this.documentUrl = documentUrl;
    }
}
