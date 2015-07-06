package com.apprevolution.resource;

import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.apprevolution.citycabs.EOTrackLocation;
import com.apprevolution.citycabs.EntityController;
import com.apprevolution.citycabs.GEOLocation;
import com.google.gson.Gson;

@Path("/server")
public class Location {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayHello(){
		return "Testing Successfull.";
	}

	@GET
	@Path("/update_geoloc")
	@Produces(MediaType.APPLICATION_JSON)
	public String updateGeoLoc(@QueryParam("cabUid") int cabUid,
			@QueryParam("latitude") double latitude,
			@QueryParam("longitude") double longitude,
			@QueryParam("location") String location){
		GEOLocation geoLoc = null;
		if(cabUid > 0){
			geoLoc = new GEOLocation();
			geoLoc.setCabUid(cabUid);
			geoLoc.setLatitude(latitude);
			geoLoc.setLongitude(longitude);
			geoLoc.setLocation(location);
		}
		EntityController.getInstance().updateLocation(geoLoc);
		return "{\"data\":\"Geo Location Updated\"}";
	}

	@GET
	@Path("/get_geoloc")
	@Produces(MediaType.APPLICATION_JSON)
	public String getGeoLog(@QueryParam("cabUid") int cabUid){
		GEOLocation location = EntityController.getInstance().getLocation(cabUid);
		return new Gson().toJson(location);
	}
	
	@POST
	@Path("/trackmyway")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON }) 
	public String updateMyWay(EOTrackLocation location){
		
		System.out.println("called-->>  "+ location.getImeiNumber()+"   "+location.getTime());
		
		if(location!=null){
			location.setUpdateTime(new Date());
			Session session = EntityController.getInstance().sessionFactory.openSession();
			Transaction transaction = session.beginTransaction();
			session.save(location);
			transaction.commit();
			session.close();
		}
		
		return "\"1\"";
	}
	
	@POST
	@Path("/offlinetrack")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON }) 
	public String insertOfflineData(EOTrackLocation location){
		
		System.out.println("offlinetrack"+ location.toString());
		
		/*if(location!=null){
			location.setUpdateTime(new Date());
			Session session = EntityController.getInstance().sessionFactory.openSession();
			Transaction transaction = session.beginTransaction();
			session.saveOrUpdate(location);
			transaction.commit();
			session.close();
		}*/
		
		return "{\"responseData\":\"Sent\"}";
	}

}
