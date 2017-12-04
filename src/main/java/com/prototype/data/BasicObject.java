package com.prototype.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "basic_object", schema = "main_schema")
public class BasicObject implements Serializable {

	private static final long serialVersionUID = -6591204544891248160L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	@JsonIgnore
	private long id;
	
	@Column(name = "user_id")
	@Size(max = 8, message = "The length can not be more than 8 characters")
	@NotNull(message = "This values is mandatory")
	private String userId;
	
	@Column(name = "personal_info")
	@Size(max = 64, message = "The length can not be more than 64 characters")
	@NotNull(message = "This values is mandatory")
	private String personalInfo;

	@JsonIgnore
	@NotNull(message = "This values is mandatory")
	private boolean isValid;
}
