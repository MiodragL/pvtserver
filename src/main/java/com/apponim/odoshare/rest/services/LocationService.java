package com.apponim.odoshare.rest.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apponim.odoshare.rest.entities.Document;
import com.apponim.odoshare.rest.entities.Location;

@Service
public class LocationService {

	@PersistenceContext
	private EntityManager em;
	
	/**
	 * Retrieves all of the locations from database.
	 * @return List of locations
	 */
	@Transactional
	public List<Location> getAllLocations(){
		
		List<Location> locations = new ArrayList<>();
		
		try{
			locations = em.createQuery("SELECT loc FROM Location loc", Location.class)
							.getResultList();
		}catch(NoResultException e){
			e.printStackTrace();
		}
		
		return locations;
	}
	
	/**
	 * Retrieves all of the locations in area selected with 4 coordinates.
	 * @param latMin Minimum latitude
	 * @param latMax Maximum latitude
	 * @param lonMin Minimum longitude
	 * @param lonMax Maximum longitude
	 * @return List of Locations
	 */
	@Transactional
	public List<Location> getAllLocations(double latMin, double latMax, double lonMin, double lonMax){

		List<Location> locations = new ArrayList<>();
		
		try{
			locations = em.createQuery("SELECT loc FROM Location loc"
			+ " WHERE loc.latitude BETWEEN :latMax AND :latMin"
			+ " AND loc.longitude BETWEEN :lonMax AND :lonMin"
			,Location.class)
			.setParameter("latMin", latMin)
			.setParameter("latMax", latMax)
			.setParameter("lonMin", lonMin)
			.setParameter("lonMax", lonMax)
			.getResultList();
		}catch(NoResultException e){
			e.printStackTrace();
		}
		
		return locations;
	}
	
	/**
	 * Persists Location into database.
	 * @param location Location to be added
	 * @return Location with persisted ID
	 */
	@Transactional
	public Location addLocation(Location location){
		em.persist(location);
		return location;
	}
	
	/**
	 * Retrieves locations with certain id from database or returns null if
	 * location does not exist.
	 * 
	 * @param id Location id
	 * @return Location
	 */
	@Transactional
	public Location getLocation(int id){
		return em.find(Location.class, id);
	}
	
	/**
	 * Adds document to a location with selected id.
	 * @param locationId Id of location
	 * @param document Document to be added
	 * @return True if document is added else returns False
	 */
	@Transactional
	public boolean addDocument(int locationId, Document document){
		
		try{
			Location location = em.createQuery("SELECT loc FROM Location loc"
									+ " LEFT JOIN FETCH loc.documents"
									+ " WHERE loc.id=:location_id",Location.class)
									.setParameter("location_id", locationId)
									.getSingleResult();
			
			location.getDocuments().add(document);
			em.merge(location);
			
			return true;
			
		}catch(NoResultException e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Adds document to a selected location. Uses {@link #addDocument(int, Document)} method
	 * @param location Selected location
	 * @param document Document to be added
	 * @return True if document is added else returns False
	 * 
	 * @see #addDocument(int, Document)
	 */
	public boolean addDocument(Location location, Document document){
		return location.getId()>0 ? addDocument(location.getId(), document) : false;
	}

	/**
	 * Retrieves collection of documents for selected location.
	 * @param locationId Location id
	 * @return Collection of locations Documents
	 */
	@Transactional
	public Set<Document> getDocuments(int locationId){

		try{
			Location location = em.createQuery("SELECT loc FROM Location loc"
									+ " LEFT JOIN FETCH loc.documents"
									+ " WHERE loc.id=:location_id",Location.class)
									.setParameter("location_id", locationId)
									.getSingleResult();

			return location.getDocuments();
			
		}catch(NoResultException e){
			e.printStackTrace();
		}
		
		return new HashSet<>();
	}
	
	/**
	 * Retrieves collection of documents for selected location.
	 * @param location Selected location
	 * @return Collection of locations documents
	 */
	public Set<Document> getDocuments(Location location){
		return getDocuments(location.getId());
	}
}
