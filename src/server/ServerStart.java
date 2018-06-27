package server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import server.models.Message;

import java.util.ArrayList;

public class ServerStart {

    public static void main(String[] args) {
        DatabaseConnection.open("MessageBoard.db");
        ResourceConfig config = new ResourceConfig();
        config.packages("server");
        ServletHolder servlet = new ServletHolder(new ServletContainer(config));

        Server server = new Server(80);
        ServletContextHandler context = new ServletContextHandler(server, "/*");
        context.addServlet(servlet, "/*");

        try {
            server.start();
            Console.log("Server successfully started.");
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            server.destroy();
            DatabaseConnection.close();
        }
    }

}

