package backend.server;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

public class ArcadeGamesApp {

    public static void main(String[] args) {
        try {
            Tomcat tomcat = new Tomcat();
            tomcat.getConnector().setPort(8080);
            tomcat.getHost().setAppBase(".");
            tomcat.addWebapp("/", ".");
            tomcat.start();
            tomcat.getServer().await();
        } catch (LifecycleException e) {
            System.out.println(e.getMessage());
        }
    }
}
