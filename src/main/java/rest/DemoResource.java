package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import entities.Member;

import java.util.List;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
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

    @POST
    @Path("signup")
    @Consumes("application/json")
    @Produces("application/json")
    public String createUser(String userJSON) { // input is the body of the request, generated in the frontend
        JsonObject json = JsonParser.parseString(userJSON).getAsJsonObject();
        String username = json.get("userName").getAsString();
        String password = json.get("userPass").getAsString();
        Member member = new Member(username, password);
        Member createdMember = FACADE.createMember(member);

        return GSON.toJson(createdMember);
    }

}