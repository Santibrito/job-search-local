package com.jobsearch.localjobsearch.entity.employees;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageID;
    private String message;
    private String transmitter;
    @Column(name = "shipping_date", columnDefinition = "TIMESTAMP")
    private Timestamp shippingDate;
    private boolean read = false;

    @ManyToOne
    @JsonIgnore
    private Employees employee;

    @PrePersist
    public void initializeReadMessageAndShippingDate() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        this.shippingDate = Timestamp.valueOf(currentDateTime);
    }


}
