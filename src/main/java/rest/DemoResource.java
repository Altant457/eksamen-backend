package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dtos.DinnerEventDTO;
import dtos.DinnerEventDTOs;
import entities.DinnerEvent;
import entities.Member;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import facades.MemberFacade;
import utils.EMF_Creator;


/**
 * @author lam@cphbusiness.dk
 */
@Path("info")
@DeclareRoles({"member", "admin"})
public class DemoResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    private static final MemberFacade FACADE = MemberFacade.getUserFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"msg\":\"Hello anonymous\"}";
    }

    //Just to verify if the database is set up
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("all")
    public String allUsers() {

        EntityManager em = EMF.createEntityManager();
        try {
            TypedQuery<Member> query = em.createQuery("select m from Member m", Member.class);
            List<Member> members = query.getResultList();
            return "[" + members.size() + "]";
        } finally {
            em.close();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("member")
    @RolesAllowed({"member"})
    public String getFromUser() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to Member: " + thisuser + "\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("admin")
    @RolesAllowed({"admin"})
    public String getFromAdmin() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to (admin) Member: " + thisuser + "\"}";
    }

    @POST
    @Path("signup")
    @Consumes("application/json")
    @Produces("application/json")
    @RolesAllowed({"admin"})
    public String createUser(String userJSON) { // input is the body of the request, generated in the frontend
        JsonObject json = JsonParser.parseString(userJSON).getAsJsonObject();
        String membername = json.get("memberName").getAsString();
        String password = json.get("userPass").getAsString();
        String address = json.get("street").getAsString();
        String phone = json.get("phone").getAsString();
        String email = json.get("email").getAsString();
        int birthYear = json.get("birthYear").getAsInt();
        Member member = new Member(membername, password, address, phone, email, birthYear);
        Member createdMember = FACADE.createMember(member);

        return GSON.toJson(createdMember);
    }

    @GET
    @Path("events")
    @Produces("application/json")
    @RolesAllowed({"admin", "member"})
    public String getAllEvents() {
        List<DinnerEvent> dinnerEvents = FACADE.getAllEvents();
        return GSON.toJson(new DinnerEventDTOs(dinnerEvents));
    }

    @POST
    @Path("event/add")
    @Produces("application/json")
    @Consumes("application/json")
    @RolesAllowed({"admin"})
    public String createEvent(String eventJSON) {
        JsonObject json = JsonParser.parseString(eventJSON).getAsJsonObject();
        String time = json.get("time").getAsString();
        String location = json.get("location").getAsString();
        String dish = json.get("dish").getAsString();
        double pricePerPerson = json.get("pricePerPerson").getAsDouble();
        DinnerEvent newEvent = FACADE.createEvent(new DinnerEvent(Timestamp.valueOf(time), location, dish, new BigDecimal(pricePerPerson)));
        return GSON.toJson(new DinnerEventDTO(newEvent));
    }

}