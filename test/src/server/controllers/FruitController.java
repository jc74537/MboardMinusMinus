package server.controllers;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
//import javax.ws.rs.core.MediaType;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;


@Path("/fruit")
public class FruitController {
    @POST
    @Produces(value = TEXT_PLAIN)
    public String fruitHandler(@FormParam("chosenFruit") String chosenFruit) {
        if (chosenFruit.contains("coffee")) {
            return ("Nice choice! You chose: "+ chosenFruit);
        } else {
            return "You have chosen " + chosenFruit + "!";
        }
    }
}
