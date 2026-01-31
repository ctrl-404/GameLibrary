package de.htwsaar.Berta;

import java.sql.SQLException;

import de.htwsaar.Berta.servicelayer.Application;

public class Main {

    public static void main(String[] args) {

        Application application = null;
        try {
            application = new Application();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }

        application.run();
    }

}
