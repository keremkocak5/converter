package com.giftandgo.converter.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "CONVERSION_LOG")
@Getter
@EqualsAndHashCode
public class ConversionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;

    @Column(name = "CREATION_DATE", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime creationDate;

    @Column(name = "URI", nullable = false, updatable = false)
    private String uri;

    @Column(name = "HTTP_RESPONSE_CODE")
    private Integer httpResponseCode;

    @Column(name = "IP_ADDRESS")
    private String ipAddress;

    @Column(name = "COUNTRY_CODE")
    private String countryCode;

    @Column(name = "ISP")
    private String isp;

    @Column(name = "TIME_LAPSED")
    private Integer timeLapsed;

    public ConversionLog(String uri) {
        this.uri = uri;
    }

}
