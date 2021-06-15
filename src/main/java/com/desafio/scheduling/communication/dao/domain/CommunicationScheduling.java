package com.desafio.scheduling.communication.dao.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Entity
@Table(name = "COMMUNICATION_SCHEDULING", uniqueConstraints = @UniqueConstraint(columnNames = {"ID"}))
@ToString
@NoArgsConstructor
public class CommunicationScheduling implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    
    
    public CommunicationScheduling(LocalDateTime creationDate, LocalDateTime scheduleDate, String phoneNumber,
			String email, String sendType, String message, String status, String statusDescription) {
		super();
		this.creationDate = creationDate;
		this.scheduleDate = scheduleDate;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.sendType = sendType;
		this.message = message;
		this.status = status;
		this.statusDescription = statusDescription;
	}

	@Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CREATION_DATE")
	private LocalDateTime creationDate;

    @Column(name = "SCHEDULE_DATE")
	private LocalDateTime scheduleDate;

    @Column(name = "PHONE_NUMBER")
	private String phoneNumber;

    @Column(name = "EMAIL")
	private String email;

    @Column(name = "SEND_TYPE")
	private String sendType;

    @Column(name = "MESSAGE")
	private String message;

    @Column(name = "STATUS")
	private String status;

    @Column(name = "STATUS_DESCRIPTION")
	private String statusDescription;
	
}