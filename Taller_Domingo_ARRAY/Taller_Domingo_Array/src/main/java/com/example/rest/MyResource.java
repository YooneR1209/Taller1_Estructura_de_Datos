package com.example.rest;

import java.util.HashMap;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;  // Import correcto para Response


/**
 * Root resource (exposed at "myresource" path)
 */

@Path("myresource")
public class MyResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getIt() {
    
    HashMap mapa = new HashMap<>();
    String aux = "";

        mapa.put("msg", "Ok");
        mapa.put("data", "test "+aux);
        
        // Construir la respuesta correctamente
        return Response.ok(mapa).build();
    }
}
