package com.apponim.odoshare.rest.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Location {

	@Id
	@GeneratedValue
	private int id;
	
	private String name;
	
	private double latitude;
	
	private double longitude;
	
	private int filesCount;
	
	@JsonBackReference(value="documents")
	@OneToMany(cascade=CascadeType.ALL , fetch = FetchType.LAZY)
	private Set<Document> documents = new HashSet<>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public int getFilesCount() {
		return filesCount;
	}

	public void setFilesCount(int filesCount) {
		this.filesCount = filesCount;
	}

	public Set<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(Set<Document> documents) {
		this.documents = documents;
	}

	@Override
	public String toString() {
		return "Location [id=" + id + ", name=" + name + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", documents=" + documents + "]";
	}
	
	
	
}
