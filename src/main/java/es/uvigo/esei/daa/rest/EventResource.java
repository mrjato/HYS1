package es.uvigo.esei.daa.rest;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.uvigo.esei.daa.bean.EventFilterBean;
import es.uvigo.esei.daa.bean.PagBean;
import es.uvigo.esei.daa.entities.Event.EventStatus;
import es.uvigo.esei.daa.entities.Location;
import es.uvigo.esei.daa.services.FacadeException;
import es.uvigo.esei.daa.services.PublicFacade;
import es.uvigo.esei.daa.services.pojo.AllEventPojo;
import es.uvigo.esei.daa.services.pojo.AllEventPojoPag;
import es.uvigo.esei.daa.services.pojo.PublicEventPojo;
import es.uvigo.esei.daa.services.pojo.PublicEventPojoPag;

@Component
@Path("/event/{categoryId}")
@Produces(MediaType.APPLICATION_JSON)
public class EventResource {
	private final static Logger LOG = Logger.getLogger(EventResource.class
			.getSimpleName());
	
	private final int NUM_ELEMENTS_PER_PAG = 15;

	@Autowired
	private PublicFacade facade;

	public EventResource() {

	}

	@GET
	public Response list(@CookieParam("token") String token,
			@PathParam("categoryId") String categoryId,
			@QueryParam("latitude") String latitude,
			@QueryParam("longitude") String longitude,
			@QueryParam("text") String text, @QueryParam("page") String page,
			@QueryParam("filters") List<String> filters) {
		try {
			// Location
			Location srcLocation = getParsedLocation(latitude, longitude);

			// Filter
			EventFilterBean eventFilter = new EventFilterBean();
			Integer categoryIdNumber = null;
			try {
				categoryIdNumber = Integer.parseInt(categoryId);
			} catch (NumberFormatException ex) {
				categoryIdNumber = 0;
				LOG.info("None category specified. Show all...");
			}
			if (categoryIdNumber > 0) {
				eventFilter.getFilters().add(
						Restrictions.eq("category.id",
								Integer.parseInt(categoryId)));
			}
			if (text != null) {
				eventFilter.getFilters().add(
						Restrictions.or(
								Restrictions.like("title", "%" + text + "%"),
								Restrictions.like("description", "%" + text
										+ "%")));
			}
			
			if (filters != null) {
				Disjunction c = Restrictions.or();
				for (String filter:filters) {
					c.add(Restrictions.eq("status", EventStatus.valueOf(filter.toUpperCase())));
				}
				eventFilter.getFilters().add(c);
			}

			PagBean pagBean = new PagBean();

			try {
				pagBean.setNumPag(Integer.parseInt(page) - 1);
			} catch (NumberFormatException e) {
				pagBean.setNumPag(0);
			}

			pagBean.setNumElemPag(NUM_ELEMENTS_PER_PAG);

			//List<Object> toret = new ArrayList<Object>();
			
			if (facade.checkToken(token) == null) {
				PublicEventPojoPag publicEvents = new PublicEventPojoPag();
				
				List<PublicEventPojo> list = this.facade.getPublicEventList(
						srcLocation, eventFilter, pagBean);

				publicEvents.setListEvents(list);
				publicEvents.setPageBean(pagBean);
				
				return Response.ok(publicEvents, MediaType.APPLICATION_JSON).build();
			} else {
				AllEventPojoPag privateEvents = new AllEventPojoPag();
				
				List<AllEventPojo> list = this.facade.getAllEventList(
						srcLocation, eventFilter, pagBean);

				privateEvents.setListEvents(list);
				privateEvents.setPageBean(pagBean);

				return Response.ok(privateEvents, MediaType.APPLICATION_JSON).build();
			}

		} catch (FacadeException e) {
			LOG.log(Level.SEVERE, "Error listing events", e);
			return Response.serverError().entity(e.getMessage()).build();
		}
	}

	private Location getParsedLocation(String latitude, String longitude) {
		Location srcLocation = null;
		try {
			Location locationTmp = new Location();
			locationTmp.setLatitude(Double.parseDouble(latitude));
			locationTmp.setLongitude(Double.parseDouble(longitude));
			srcLocation = locationTmp;
		} catch (NumberFormatException | NullPointerException e) {
			LOG.info("None location specified. Show all...");
		}
		return srcLocation;
	}
}
