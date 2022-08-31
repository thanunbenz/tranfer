import java.net.*;
import java.util.Scanner;
import java.io.*;

public class Client {
 

  public static void main(String[] args) throws Exception {
    try {
      Socket socket = new Socket("localhost", 9999);
      DataInputStream inStream = new DataInputStream(socket.getInputStream());
      DataOutputStream outStream = new DataOutputStream(socket.getOutputStream());
      // BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      Scanner sc = new Scanner(System.in);
      String clientMessage = "", serverMessage = "", fn = "";
      boolean check;
      while (!clientMessage.equals("bye")) {
        /* start 1 cilent get from server */
        System.out.println("[ ================================================================ ]");
        serverMessage = inStream.readUTF(); // probleam
        System.out.println(serverMessage);
        System.out.println("[ ================================================================ ]");
        /* end 1 cilent get from server */

        /* strat 2 cilent send to server */
        System.out.print("Clinet input : ");
        clientMessage = sc.nextLine(); // for input form keyboard
        outStream.writeUTF(clientMessage);
        if (clientMessage.equals("1")) {
          fn = "BLACKPINK";
        } else if (clientMessage.equals("2")) {
          fn = "JAPAN";
        } else if (clientMessage.equals("3")) {
          fn = "GAME";
        } else {
          fn = clientMessage;
        }
        System.out.println(clientMessage);
        outStream.flush();
        /* end 2 cilent send to server */

        /* strat 3 cilent get to server */
        serverMessage = inStream.readUTF();
        check = inStream.readBoolean();
        System.out.println("SERVER REPLY : " + serverMessage + " Status : " + check);
        /* end 3 cilent get to server */

        /* 4 download file form server */

        if (check == true) {
          System.out.print("REPLY yes OR no: ");
          clientMessage = sc.nextLine();
          outStream.writeUTF(clientMessage);
          if (clientMessage.equalsIgnoreCase("yes")) {
            System.out.println("FILE NEAME : " + fn);
            downdloadThread DL_file = new downdloadThread(socket, fn);
            for (int i = 0; i < 10; ++i) {
              String str = i + "";
              Thread tr = new Thread(DL_file, str);
              tr.start();
              tr.join();
            }
          } else {
            continue;
          }
        }
        /* 4 download file form server */
      }
      inStream.close();
      outStream.close();
      socket.close();
    } catch (IOException e) {
      System.out.println(e);
    } finally {
      System.out.println("is closed");
    }
  }
}

class downdloadThread implements Runnable {
  Socket socket;
  String file_name;

  downdloadThread(Socket socket, String file_name) {
    this.socket = socket;
    this.file_name = file_name;
  }

  public void run() {
    try {
      System.out.println("Clinet : dowdloading. . . . " + file_name + " with thread " + Thread.currentThread().getName());
      InputStream is = socket.getInputStream();
      int bufferSize = socket.getReceiveBufferSize();
      FileOutputStream fos = new FileOutputStream("C:/Users/aumra/OneDrive/เดสก์ท็อป/psc/keep_client_file/" + file_name + ".exe");///* */
      BufferedOutputStream bos = new BufferedOutputStream(fos);
      byte[] bytes = new byte[bufferSize];
      int count = 0;
      while ((count = is.read(bytes)) >= 0) {
        bos.write(bytes, 0, count);
      }
      bos.close();
      is.close();

    } catch (Exception e) {
      System.out.println(e);
    }
  }

}