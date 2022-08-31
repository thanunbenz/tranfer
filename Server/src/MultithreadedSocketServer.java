
import java.net.*;
import java.io.*;

public class MultithreadedSocketServer {
    public static void main(String[] args) throws Exception {
        try {
            ServerSocket server = new ServerSocket(9999);
            int counter = 0;
            System.out.println("Server Started ....");
            while (true) {
                counter++;
                Socket serverClient = server.accept(); // server accept the client connection request
                System.out.println(" >> " + "Client No:" + counter + " started!");
                new Thread(new ServerClientThread(serverClient, counter)).start(); // thread get client

            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

class ServerClientThread implements Runnable {
    Socket serverClient;
    int clientNo;

    ServerClientThread(Socket inSocket, int counter) {
        serverClient = inSocket;
        clientNo = counter;

    }

    public void run() {
        try {
            String clientMessage = "", serverMessage = "", fn = "";
            boolean check = false; // HERE EDIT
            DataInputStream inStream = inStream = new DataInputStream(serverClient.getInputStream());
            DataOutputStream outStream = new DataOutputStream(serverClient.getOutputStream());
            while (!clientMessage.equals("bye")) {
                /* start 1 server send from client */
                serverMessage = "          Hello Client we have 3 files for download  \n--->        1.BLACKPINK\n--->        2.JAPAN\n--->        3.GAME\n";
                serverMessage += "         PRESS 1 2 3 OR type name file";
                outStream.writeUTF(serverMessage); // probleam
                outStream.flush();
                /* end 1 server send from client */

                /* start 2 server send to client */
                clientMessage = inStream.readUTF();
                System.out.println("Client no Message - " + clientNo + " : " + clientMessage);
                /* end 2 server get to client */
                /*find something what client want */
                String filename[] = { "BLACKPINK", "JAPAN", "GAME" };
                String number[] = { "1", "2", "3" };
                for (int i = 0; i < 3; ++i) {
                    if (clientMessage.equalsIgnoreCase(filename[i]) || clientMessage.equalsIgnoreCase(number[i])) {
                        serverMessage = "this "+ filename[i]+" flie that you want download yes OR no";
                        fn = filename[i];
                        System.out.println("SERVER : SEND FILE " + fn);
                        check = true;
                        break;
                    }
                }
                /*find something what client want */
                /* start 3 server get to client */
                outStream.writeUTF(serverMessage);
                outStream.writeBoolean(check);
                outStream.flush();
                /* end 3 server send to client */

                /* 4 send file to client */
                if (check == true) {
                    clientMessage = inStream.readUTF();
                    if (clientMessage.equalsIgnoreCase("yes")) {
                        String file_name = fn.toUpperCase();
                        File myfile = new File("../" + "Install VALORANT" + ".exe"); //////////////////////
                        byte brr[] = new byte[(int) myfile.length()];
                        System.out.println("SERVER :file size : " + brr.length + " of " + file_name);
                        FileInputStream fis = new FileInputStream(myfile);
                        BufferedInputStream bis = new BufferedInputStream(fis);
                        BufferedOutputStream out = new BufferedOutputStream(serverClient.getOutputStream());
                        int count;
                        while ((count = bis.read(brr)) > 0) {
                            out.write(brr, 0, count);
                        }
                        fis.close();
                        bis.close();
                        out.close();
                        System.out.println("SERVER :sending file to client . . .");
                    }
                }
                /* 4 send file to client */
            }
            inStream.close();
            outStream.close();
            serverClient.close();
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            System.out.println("Client -" + clientNo + " exit!! ");
        }
    }
}
