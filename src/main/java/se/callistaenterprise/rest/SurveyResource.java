package se.callistaenterprise.rest;

import java.util.Collection;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;

import se.callistaenterprise.domain.Links;
import se.callistaenterprise.domain.Survey;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

@Controller
@Path("/indicators")
public class SurveyResource {

    @Context private UriInfo uriInfo;
    private MongoTemplate mongoTemplate;

    @Autowired
    public SurveyResource(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @GET
    @Produces({ "application/json;charset=UTF-8;qs=3.5",
            "application/vnd-riv.orgmaster.hsa.v1+json;charset=UTF-8;qs=3.2", "application/xml;charset=UTF-8;qs=2.5",
            "application/vnd-riv.orgmaster.hsa.v1+xml;charset=UTF-8;qs=2.2", "application/csv;charset=UTF-8;qs=1.5",
            "application/vnd-riv.orgmaster.hsa.v1+csv;charset=UTF-8;qs=1.2" })
    @SuppressWarnings("unchecked")
    public Links findAll() {
        List<Integer> years = mongoTemplate.getCollection(Survey.class.getSimpleName().toLowerCase()).distinct("year");
        Links uris = new Links(years.size());
        for (Integer year : years) {
            uris.add(uriInfo.getAbsolutePathBuilder().path(year.toString()).build());
        }
        return uris.sort();
    }

    @GET
    @Path("/{year}")
    @Produces({ "application/json;charset=UTF-8;qs=3.5",
            "application/vnd-riv.orgmaster.hsa.v1+json;charset=UTF-8;qs=3.2", "application/xml;charset=UTF-8;qs=2.5",
            "application/vnd-riv.orgmaster.hsa.v1+xml;charset=UTF-8;qs=2.2", "application/csv;charset=UTF-8;qs=1.5",
            "application/vnd-riv.orgmaster.hsa.v1+csv;charset=UTF-8;qs=1.2" })
    @SuppressWarnings("unchecked")
    public Links getIndicators(@PathParam("year") Integer year) {
        String collectionName = Survey.class.getSimpleName().toLowerCase();
        DBCollection collection = mongoTemplate.getCollection(collectionName);

        List<String> counties = collection.distinct("county", new BasicDBObject("year", year));
        Links uris = new Links(counties.size());
        for (String county : counties) {
            uris.add(uriInfo.getAbsolutePathBuilder().path(county).build());
        }
        return uris.sort();
    }

    @GET
    @Path("/{year}/{county}")
    @Produces({ "application/json;charset=UTF-8;qs=3.5",
            "application/vnd-riv.orgmaster.hsa.v1+json;charset=UTF-8;qs=3.2", "application/xml;charset=UTF-8;qs=2.5",
            "application/vnd-riv.orgmaster.hsa.v1+xml;charset=UTF-8;qs=2.2", "application/csv;charset=UTF-8;qs=1.5",
            "application/vnd-riv.orgmaster.hsa.v1+csv;charset=UTF-8;qs=1.2" })
    public Collection<Survey> getIndicators(@PathParam("year") Integer year, @PathParam("county") String county) {
        Criteria c = Criteria.where("year").is(year).and("county").is(county);
        return mongoTemplate.find(new Query(c), Survey.class);
    }

    @DELETE
    public Response deleteAllIndicators() {
        mongoTemplate.dropCollection(Survey.class);
        return Response.ok().build();
    }

    @POST
    @Consumes("application/csv;charset=iso-8859-1")
    public Response loadIndicators(Collection<Survey> surveys) {
        mongoTemplate.dropCollection(Survey.class);
        for (Survey survey : surveys) {
            mongoTemplate.save(survey);
        }
        return Response.ok().build();
    }
}
