package com.vedikunnel.web.rest;

import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class BackofficeResource {

    private static String checkins = "[{\"placeId\":\"d8bcfcb51b84e7b4fb871a63ff81768a878e82a6\"" +
            ",\"comment\":\"good\"" +
            ",\"userName\":\"Jojo\"" +
            ",\"name\":\"Edo sushi\"" +
            ",\"icon\":\"https://maps.gstatic.com/mapfiles/place_api/icons/restaurant-71.png\"" +
            ",\"vicinity\":\"210 Rue de Charenton, Paris\"" +
            ",\"key\":1451766933710" +
            ",\"id\":1}]";

    /**
     * /rest/checkins
     * Recherche des checkins
     */
    @GET
    @Path("/checkins")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCheckins() {
        return Response.ok(checkins).build();
    }

    /**
     * /rest/checkins
     * Ajout d'un nouveau checkin
     */
    @POST
    @Path("/checkins")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addCheckin(String newCheckin) {
        JSONArray jsonArray = new JSONArray(checkins);
        int lastId = 0;
        for (Object checkinObject : jsonArray) {
            JSONObject checkin = (JSONObject) checkinObject;
            lastId = checkin.has("id") && checkin.getInt("id") > lastId ? checkin.getInt("id") : lastId;
        }
        for (Object checkinObject : jsonArray) {
            // save remaining checkins
            JSONObject checkin = (JSONObject) checkinObject;
            if (!checkin.has("id")) {
                checkin.put("id", ++lastId);
            }
        }
        JSONObject jsonObject = new JSONObject(newCheckin);
        int newId = ++lastId;
        jsonObject.put("id", newId);
        jsonArray.put(jsonObject);
        checkins = jsonArray.toCompactString();
        return Response.status(Response.Status.CREATED).entity("{id: " + newId + "}").build();
    }

    /**
     * /rest/checkins/{id}
     * Recheche d'un checkin particulier
     */
    @GET
    @Path("/checkins/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCheckin(@PathParam("id") String checkinId) {
        JSONArray jsonArray = new JSONArray(checkins);
        for (Object checkinObject : jsonArray) {
            JSONObject checkin = (JSONObject) checkinObject;
            if (checkin.has("id") && checkin.getInt("id") == Integer.valueOf(checkinId)) {
                return Response.ok(checkin.toCompactString()).build();
            }
        }
        return Response.status(Response.Status.MOVED_PERMANENTLY).build();
    }
}
