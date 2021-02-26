package nl.jeremiedevos.dea.services;

import nl.jeremiedevos.dea.services.dto.ItemDTO;
import nl.jeremiedevos.dea.services.exceptions.IdAlreadyInUseException;
import nl.jeremiedevos.dea.services.exceptions.ItemNotAvailableException;

import javax.ejb.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

@Singleton
@Path("/items")
public class ItemResource {

    private ItemService itemService = new ItemService();

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getItemsASPlainText(){
        return Response.ok("[\"bread\", \"butter\"]").build();
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItemsASJson(){
        return Response.ok("[\"bread\", \"butter\"]").build();
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllItemsAsJson(){
        return Response.ok(itemService.getAll()).build();
    }

    @GET
    @Path("/{itemId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItemByIdAsJson(@PathParam("itemId") int id){

        ItemDTO item;
        try {
            item = itemService.getItem(id);
        }catch (ItemNotAvailableException e){
            return Response.status(404).build();
        }

        return Response.ok(item).build();
    }

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postItemAsJson(ItemDTO itemDTO){

        try {
            itemService.addItem(itemDTO);
        }catch (IdAlreadyInUseException e){
            return Response.status(409).build();
        }

        final URI itemDTOIdUri = UriBuilder.fromResource(ItemResource.class).path("items/{id}").build(itemDTO.getId());
        return Response.created(itemDTOIdUri).build();
    }

    @DELETE
    @Path("/{itemId}")
    public Response deleteItem(@PathParam("itemId") Integer itemId){

        try{
            itemService.deleteItem(itemId);
        }catch(ItemNotAvailableException e){
            return Response.status(404).build();
        }
        return Response.ok().build();
    }
}
