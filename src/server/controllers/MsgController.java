package server.controllers;

import org.eclipse.jetty.server.Server;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import server.Console;
import server.models.Message;
import server.models.services.MessageService;

import java.util.Date;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;


@Path("/message")
public class MsgController {
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("new")
    public String messageHandler(@FormParam("userName") String userName,@FormParam("message") String message) {
        //Console.log(userName+message);
        int id = Message.nextID();
        Message newM = new Message(id,userName,message, new Date().toString());
        Message.messages.add(newM);
        Console.log(newM.toString());
        return getMessageList();
    }
    @POST
    @Path("new")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    //Message(int id, String messageText, String author, String date)
    public String newMessage(@FormParam("message") String messageText,
                             @FormParam("userName") String messageAuthor ) {

        Console.log("/message/new - Posted by " + messageAuthor);
        MessageService.selectAllInto(Message.messages);
        int messageId = Message.nextID();
        String messageDate = new Date().toString();
        Message newMessage = new Message(messageId, messageText, messageDate, messageAuthor);
        return MessageService.insert(newMessage);
    }
    @SuppressWarnings("unchecked")
    public String getMessageList() {
        JSONArray messageSummary = new JSONArray();

        for (Message m: Message.messages) {
            messageSummary.add(m.toJSON());
        }
        return messageSummary.toString();
    }
    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("unchecked")
    public String listMessages() {
        Console.log("/message/list - Getting all messages from database");
        String status = MessageService.selectAllInto(Message.messages);
        if (status.equals("OK")) {
            return getMessageList();
        } else {
            JSONObject response = new JSONObject();
            response.put("error", status);
            return response.toString();
        }
    }
    @POST
    @Path("delete")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteMessage(@FormParam("messageId") int messageId) {
        Console.log("/message/delete - Message " + messageId);
        Message message = MessageService.selectById(messageId);
        if (message == null) {
            return "That message doesn't appear to exist";
        } else {
            return MessageService.deleteById(messageId);
        }
    }
    @POST
    @Path("edit")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String editMessage(@FormParam("messageId") int messageId, @FormParam("messageText") String messageText) {
        Console.log("/message/edit - Message " + messageId);
        Message message = MessageService.selectById(messageId);
        if (message == null) {
            return "That message doesn't appear to exist";
        } else {
            String messageDate = new Date().toString();
            message.setMessageText(messageText);
            message.setDate(messageDate);
            return MessageService.update(message);
        }
    }


    @GET
    @Path("hello")
    @Produces(MediaType.APPLICATION_JSON)
    public String sayHello() {
        return "{\"text\":\"Hello world!\"}";
    }

}
