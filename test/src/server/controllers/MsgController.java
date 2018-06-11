package server.controllers;

import server.Console;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/message/new")
public class MsgController {
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String messageHandler(@FormParam("userName") String userName,@FormParam("message") String message) {
        Console.log(userName+message);
        return "You have chosen " + userName + "!"+ message;
    }
}
