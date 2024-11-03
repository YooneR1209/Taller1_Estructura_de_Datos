package com.example.rest;

import controller.Dao.servicies.FamiliaServicies;
import models.Censo;
import models.Familia;
import models.Generador;
import models.HistorialTransacciones;
import models.Transacciones;

import java.util.HashMap;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("familia")
public class FamiliaApi {
    private HistorialTransacciones historialTransacciones = new HistorialTransacciones(100);
    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPersons() {
        HashMap map = new HashMap<>();
        FamiliaServicies ps = new FamiliaServicies();
        map.put("msg", "Ok");
        map.put("data", ps.arrayAll());
        if (ps.listAll().isEmpty()) {
            map.put("data", new Object[]{});
           
        }
        return Response.ok(map).build();
    }

    @Path("/get/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFamilia(@PathParam("id") Integer id) {
        HashMap<String, Object> map = new HashMap<>();
        Censo censo = new Censo(50); // Creamos una instancia de Censo con capacidad inicial de 50 familias
    
        try {
            
            Familia familia = censo.getFamiliaById(id); // Obtenemos la familia por su ID
    
            if (familia == null) {
                map.put("msg", "Error");
                map.put("data", "No existe la familia con ese identificador");
                return Response.status(Status.BAD_REQUEST).entity(map).build();
            } else {
                map.put("msg", "Ok");
                map.put("data", familia);
                return Response.ok(map).build();
            }
    
        } catch (Exception e) {
            map.put("msg", "Error");
            map.put("data", e.toString());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(map).build();
        }
    }
    
        
    @Path("/save")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(HashMap<String, Object> map) {
        HashMap<String, Object> res = new HashMap<>();
        Censo censo = new Censo(50); 
        
        try {
            
            Familia nuevaFamilia = new Familia(); // Creamos una nueva instancia de Familia con sus datos desde el mapa
            nuevaFamilia.setCanton(map.get("canton").toString());
            nuevaFamilia.setApellidoPaterno(map.get("apellidoPaterno").toString());
            nuevaFamilia.setApellidoMaterno(map.get("apellidoMaterno").toString());
            nuevaFamilia.setIntegrantes(Integer.parseInt(map.get("integrantes").toString()));
            nuevaFamilia.setTieneGenerador(Boolean.parseBoolean(map.get("tieneGenerador").toString()));

            
            if (nuevaFamilia.getTieneGenerador()) { // Si la familia tiene generador, asignamos los valores del generador
                Generador nuevoGenerador = new Generador();
                nuevoGenerador.setCosto(Float.parseFloat(map.get("costo").toString()));
                nuevoGenerador.setConsumoXHora(Float.parseFloat(map.get("consumoXHora").toString()));
                nuevoGenerador.setEnergiaGenerada(Float.parseFloat(map.get("energiaGenerada").toString()));
                nuevoGenerador.setUso(map.get("uso").toString());

                
                nuevaFamilia.setGenerador(nuevoGenerador); // Asignamos el generador a la familia
            }

            
            censo.registrarFamilia(nuevaFamilia);  // Añadimos la familia al arreglo y guardamos los datos en el archivo JSON

            Transacciones transaccion = new Transacciones("GUARDAR FAMILIA", nuevaFamilia.getId()); // Registramos la transacción
            historialTransacciones.agregarTransaccion(transaccion);

            res.put("msg", "Ok");
            res.put("data", "Guardado correctamente");
            return Response.ok(res).build();

        } catch (Exception e) {
            res.put("msg", "Error");
            res.put("data", e.toString());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }


    @Path("/update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(HashMap<String, Object> map) {
        HashMap<String, String> res = new HashMap<>();
        try {
            Censo censo = new Censo(50); 
            int id = Integer.parseInt(map.get("id").toString());
            String canton = map.get("canton").toString();
            String apellidoPaterno = map.get("apellidoPaterno").toString();
            String apellidoMaterno = map.get("apellidoMaterno").toString();
            int integrantes = Integer.parseInt(map.get("integrantes").toString());
            boolean tieneGenerador = Boolean.parseBoolean(map.get("tieneGenerador").toString());
    
            
            Familia nuevaFamilia = new Familia(id, canton, apellidoPaterno, apellidoMaterno, integrantes, tieneGenerador); 
    
            
            if (tieneGenerador && nuevaFamilia.getGenerador() != null) { // Asignamos los valores del generador si tieneGenerador es true
                float costo = Float.parseFloat(map.get("costo").toString());
                float consumoXHora = Float.parseFloat(map.get("consumoXHora").toString());
                float energiaGenerada = Float.parseFloat(map.get("energiaGenerada").toString());
                String uso = map.get("uso").toString();
    
                nuevaFamilia.getGenerador().setCosto(costo);
                nuevaFamilia.getGenerador().setConsumoXHora(consumoXHora);
                nuevaFamilia.getGenerador().setEnergiaGenerada(energiaGenerada);
                nuevaFamilia.getGenerador().setUso(uso);
            }


            if (censo.updateFamilia(id, nuevaFamilia)) { // Llamamos al método updateFamilia de Censo para actualizar la familia

                Transacciones transaccion = new Transacciones("ACTUALIZAR FAMILIA", nuevaFamilia.getId()); // Registramos la transacción
                historialTransacciones.agregarTransaccion(transaccion);

                res.put("msg", "Ok");
                res.put("data", "Actualización realizada correctamente");
                return Response.ok(res).build();
            } else {
                res.put("msg", "Error");
                res.put("data", "Familia no encontrada");
                return Response.status(Status.NOT_FOUND).entity(res).build();
            }
    
        } catch (Exception e) {
            res.put("msg", "Error");
            res.put("data", e.toString());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }
    
    
    // @Path("/listType")

    // @GET
    // @Produces(MediaType.APPLICATION_JSON)
    // public Response getType() {
    //     HashMap map = new HashMap<>();
    //     FamiliaServicies ps = new FamiliaServicies();
    //     map.put("msg", "Ok");
    //     map.put("data", ps.getFamilia());
    //     return Response.ok(map).build();
    // }

    @Path("/delete/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteFamilia(@PathParam("id") int id) {
        HashMap<String, Object> res = new HashMap<>();
        Censo censo = new Censo(50);
        
        try {

            boolean familiaDeleted = censo.deleteFamilia(id); // Eliminamos a la familia por su ID

            Transacciones transaccion = new Transacciones("ELIMINAR FAMILIA, GENERADOR", id); // Registramos la transacción
            historialTransacciones.agregarTransaccion(transaccion);

            if (familiaDeleted) {
                res.put("message", "Familia y Generador eliminados exitosamente");
                return Response.ok(res).build();
            } else {
                
                res.put("message", "Familia no encontrada o no eliminada"); // Si no se eliminó, enviamos un error 404
                return Response.status(Response.Status.NOT_FOUND).entity(res).build();

            }
        } catch (Exception e) {

            res.put("message", "Error al intentar eliminar la familia"); // En caso de error, devolvemos una respuesta de error interno
            res.put("error", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(res).build(); // Util para que soluciones el error
        }
    }
    

    @Path("/contadorGeneradores")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response contarFamiliasConGenerador() {
        FamiliaServicies fs = new FamiliaServicies();
        int totalFamiliasConGenerador = fs.contarFamiliasConGenerador();

        HashMap<String, Object> response = new HashMap<>();
        response.put("msg", "Ok");
        response.put("familiasConGenerador", totalFamiliasConGenerador);
        
        return Response.ok(response).build();
    }


}
