package de.htwsaar.Berta;

import de.htwsaar.Berta.servicelayer.Application;

/**
 * Eine Bootstrap-Klasse, die sicherstellt, dass die Anwendung unter macOS
 * mit dem JVM-Parameter -XstartOnFirstThread gestartet wird.
 * Dies ist für Bibliotheken wie Raylib (Jaylib) oder LWJGL notwendig,
 * da macOS verlangt, dass grafische Oberflächen auf dem ersten Thread
 * des Prozesses initialisiert werden.
 */
public class MacBootstrap {
    /**
     * Der Einstiegspunkt der Bootstrap-Anwendung.
     * * @param args Die Befehlszeilenargumente, die an die Hauptanwendung weitergegeben werden.
     */
    public static void main(String[] args) {
        String os = System.getProperty("os.name").toLowerCase();
        boolean isMac = os.contains("mac");
        boolean isRestarted = "true".equals(System.getProperty("restarted"));

        if (isMac && !isRestarted) {
            try {
                String jarPath = MacBootstrap.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

                ProcessBuilder pb = new ProcessBuilder(
                        "java",
                        "-XstartOnFirstThread",
                        "-Drestarted=true",
                        "-cp", jarPath,
                        "de.htwsaar.Berta.Main"
                );
                pb.inheritIO();
                pb.start();
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Application application = new Application();
            application.run();
        }
    }
}
