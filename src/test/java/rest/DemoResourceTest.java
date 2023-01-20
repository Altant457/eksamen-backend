package rest;

import entities.Assignment;
import entities.DinnerEvent;
import entities.Role;
import entities.Member;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import java.math.BigDecimal;
import java.net.URI;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.isOneOf;
import static org.hamcrest.core.IsEqual.equalTo;

class DemoResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;
    Role userR, adminR;
    Member m1, m2, m3;
    DinnerEvent d1, d2, d3;
    Assignment a1, a2, a3;
    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();

        httpServer.shutdownNow();
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            userR = new Role("member");
            adminR = new Role("admin");
            m1 = new Member("member", "test1", "Test street 1", "+45 12 34 56 78", "member1@mail.dk", 1993);
            m2 = new Member("admin", "test2", "Test street 2", "+45 21 43 65 87", "member2@mail.dk", 1997);
            m3 = new Member("user_admin", "test3", "Test street 3", "+45 87 65 43 21", "member3@mail.dk", 2001);
            m1.addRole(userR);
            m2.addRole(adminR);
            m3.addRole(userR);
            m3.addRole(adminR);
            d1 = new DinnerEvent(new Timestamp(System.currentTimeMillis()), "Roma", "Pizza", new BigDecimal(200));
            d2 = new DinnerEvent(new Timestamp(System.currentTimeMillis()), "Rønne", "Pasta", new BigDecimal(150));
            d3 = new DinnerEvent(new Timestamp(System.currentTimeMillis()), "Aakirkeby", "Beans", new BigDecimal(100));
            a1 = new Assignment(m1, "fname", String.format("Email: %s\nTlf nr: %s", m1.getEmail(), m1.getPhone()));
            a2 = new Assignment(m2, "fname", String.format("Email: %s\nTlf nr: %s", m2.getEmail(), m2.getPhone()));
            a3 = new Assignment(m3, "fname", String.format("Email: %s\nTlf nr: %s", m3.getEmail(), m3.getPhone()));
            a1.setDinnerEvent(d1);
            a2.setDinnerEvent(d2);
            a3.setDinnerEvent(d3);
            a1.addMember(m2);
            a2.addMember(m1);

            em.getTransaction().begin();
            // Delete old data
            em.createQuery("DELETE FROM Assignment").executeUpdate();
            em.createQuery("DELETE FROM Member").executeUpdate();
            em.createQuery("DELETE FROM Role").executeUpdate();
            em.createQuery("DELETE FROM DinnerEvent").executeUpdate();
            // Start with fresh data
            em.persist(userR);
            em.persist(adminR);
            em.persist(m1);
            em.persist(m2);
            em.persist(m3);
            em.persist(d1);
            em.persist(d2);
            em.persist(d3);
            em.persist(a1);
            em.persist(a2);
            em.persist(a3);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    private static String securityToken;

    private static void login(String username, String password) {
        String json = String.format("{username: \"%s\", password: \"%s\"}", username, password);
        securityToken = given()
                .contentType("application/json")
                .body(json)
                .when().post("/login")
                .then()
                .extract().path("token");
    }

    private void logOut() {
        securityToken = null;
    }

    @Test
    void createUser() {
        String json = "{\"memberName\": \"test\", \"userPass\": \"1234\", \"street\": \"test street\", \"phone\": \"+45 11 11 11 11\", \"email\": \"test@mail.dk\", \"birthYear\": 1980}";
        login("admin", "test2");
        given()
                .contentType("application/json")
                .accept("application/json")
                .header("x-access-token", securityToken)
                .body(json)
                .when().post("/info/signup")
                .then()
                .assertThat()
                .statusCode(200)
                .body("memberName", equalTo("test"))
                .body("roleList", hasItems(hasEntry("roleName", "member")));
    }

    @Test
    void getAllEvents() {
        login("member", "test1");
        Response res = given()
                .contentType("application/json")
                .accept("application/json")
                .header("x-access-token", securityToken)
                .when().get("/info/events");
        res.then().assertThat().statusCode(200);
        List<Object> jList = res.jsonPath().getList("all");
        jList.forEach(event -> {
            assertThat(((LinkedHashMap<?, ?>) event).get("location"), isOneOf(d1.getLocation(), d2.getLocation(), d3.getLocation()));
        });
    }

    @Test
    void createEvent() {
        String json = "{\"time\": \"2023-01-23 18:00:00\", \"location\": \"testLoc\", \"dish\": \"testDish\", \"pricePerPerson\": 59.95}";
        login("admin", "test2");
        given()
                .contentType("application/json")
                .accept("application/json")
                .header("x-access-token", securityToken)
                .body(json)
                .when().post("/info/event/add")
                .then()
                .assertThat()
                .statusCode(200)
                .body("location", equalTo("testLoc"))
                .body("assignments", equalTo(0));

    }
}