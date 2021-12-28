package dz.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

public class Server {

    public static void main(String[] args) {
        ServerSocket listener = null;
        String line;
        BufferedReader is;
        BufferedWriter os;
        Socket socketOfServer;

        try {
            listener = new ServerSocket(9999);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }

        try {
            System.out.println("Le serveur attend d'accepter l'utilisateur...");
            socketOfServer = listener.accept();
            // Response 1
            System.out.println("Serveur à l'écoute sur le port Numéro : " + listener.getLocalPort());

            is = new BufferedReader(new InputStreamReader(socketOfServer.getInputStream()));
            os = new BufferedWriter(new OutputStreamWriter(socketOfServer.getOutputStream()));

            while (true) {
                line = is.readLine();

                os.write(">> " + line);
                os.newLine();
                os.flush();
                //
                if (line.equalsIgnoreCase("List")) {
                    // Response 2.1
                    File directoryPath = new File(is.readLine());
                    try {
                        // List of all files and directories
                        String contents[] = directoryPath.list();
                        System.out.println("List of files and directories in the specified directory:");

                        for(int i=0; i<contents.length; i++) {
                            System.out.println(contents[i]);
                        }
                    } catch (Exception e){
                        System.out.println("ERROR: File/directory does not exist");
                    }

                }else if (line.equalsIgnoreCase("Get")) {
                    // Response 2.2
                    try {
                        URL monURL = new URL(is.readLine());
                        URLConnection connexion = monURL.openConnection();
                        InputStream flux = connexion.getInputStream();
                        int donneesALire = connexion.getContentLength();
                        for (; donneesALire != 0; donneesALire--)
                            System.out.print((char) flux.read());
                        flux.close();
                    } catch (Exception e) {
                        System.out.println("ERROR: File/directory does not exist");
                    }
                }else if (line.equalsIgnoreCase("QUIT")) {
                    // Response 2.1
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }
        System.out.println("Sever stopped!");
    }
}