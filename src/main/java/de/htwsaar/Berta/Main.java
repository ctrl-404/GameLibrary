package de.htwsaar.Berta;

import java.sql.SQLException;

import de.htwsaar.Berta.servicelayer.Application;

public class Main {

    public static void main(String[] args) {

        Application application = null;
        application = new Application();
        application.run();
    }

}
