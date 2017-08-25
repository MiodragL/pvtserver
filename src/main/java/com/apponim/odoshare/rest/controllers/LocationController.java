package com.apponim.odoshare.rest.controllers;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.apponim.odoshare.rest.entities.Document;
import com.apponim.odoshare.rest.entities.Location;
import com.apponim.odoshare.rest.services.LocationService;
import com.apponim.odoshare.rest.services.StorageService;

@Controller
@RequestMapping("/locations")
public class LocationController {

	@Autowired
	private LocationService locationService;
	
	@Autowired
	private StorageService storageService;
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Location> getAllLocations(
			@RequestParam(required=false) Double latMin,
			@RequestParam(required=false) Double latMax,
			@RequestParam(required=false) Double lonMin,
			@RequestParam(required=false) Double lonMax){
		
		if(coordinatesAreValid(latMax,latMin,lonMax,lonMin))
			return locationService.getAllLocations(latMax,latMin,lonMax,lonMin);
			
		return locationService.getAllLocations();
	}

	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public List<Location> addLocation(@RequestBody Location location){
		
		locationService.addLocation(location);
		storageService.createDirectory(location.getId());
		
		return locationService.getAllLocations();
	}

	@ResponseBody
	@RequestMapping(value = "/{location_id}", method = RequestMethod.GET)
	public Location getLocation(@PathVariable(value="location_id") int id){
		
		return locationService.getLocation(id);
	}
	
	@ResponseBody
	@RequestMapping(value = "/{location_id}/documents", method = RequestMethod.GET,
					produces = MediaType.APPLICATION_JSON_VALUE)
	public Set<Document> getDocuments(@PathVariable(value="location_id") int id){
		
		return locationService.getDocuments(id);
	}
	

	@RequestMapping(value="/storage", method = RequestMethod.GET)
	public String home(Model model) {

		return "storage-test";
	}
	
	@ResponseBody
	@RequestMapping(value = "/{location_id}/documents/upload", method = RequestMethod.POST)
	public Set<Document> uploadDocument(@PathVariable(value="location_id") int id,
										@RequestParam(value = "file") MultipartFile file, 
										HttpServletRequest request){

		System.out.println("FILE UPLOAD");
		System.out.println(file.getOriginalFilename());
		
		storageService.uploadMultipartFile(file, StorageService.DIRECTORY_PREFIX+id, file.getOriginalFilename());
		
		Document document = new Document();
		document.setName(file.getOriginalFilename());
		document.setUrl(StorageService.SERVER_URL 
				+ StorageService.DIRECTORY_PREFIX + id + "/" 
				+ file.getOriginalFilename());
		document.setFileSize(file.getSize());
		
		locationService.addDocument(id, document);
		
		return locationService.getDocuments(id);
	}
	
	@ResponseBody
	@RequestMapping(value = "/{location_id}/documents/add", method = RequestMethod.POST, 
					consumes = MediaType.APPLICATION_JSON_VALUE)
	public Set<Document> addDocument(@PathVariable(value="location_id") int id,
							  @RequestBody Document document){
		
		locationService.addDocument(id, document);
		
		return locationService.getDocuments(id);
	}
	
	/**
	 * Checks if coordinates of type {@link Double} are all of non-null value.
	 * @param coordinates Array of coordinates to be checked
	 * @return True if coordinates are not null, else returns false
	 */
	private boolean coordinatesAreValid(Double... coordinates){
		for(Double coordinate : coordinates)
			if(coordinate==null)
				return false;
		return true;
	}
}
