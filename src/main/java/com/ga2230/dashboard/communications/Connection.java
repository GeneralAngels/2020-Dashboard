package com.ga2230.dashboard.communications;

import com.ga2230.dashboard.configuration.Configuration;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

public class Connection {

    private static final int PORT = 5800;

    // Pending command queue
    private Queue<String> commandQueue;
    private Queue<Callback> callbackQueue;

    // I/O section
    private BufferedWriter writer;
    private BufferedReader reader;
    private Socket socket;

    private boolean connected = false;

    private boolean loop = true;
    private boolean queue = true;
    private double refreshRate;
    private int teamNumber;

    private Timer timer;

    public Connection(int teamNumber, double refreshRate, boolean queue) {
        this.commandQueue = new ArrayDeque<>();
        this.callbackQueue = new ArrayDeque<>();
        this.teamNumber = teamNumber;
        this.refreshRate = refreshRate;
        this.queue = queue;

        this.timer = new Timer();

        // Clear queues
        clear();

        // Add to communicator
        Communicator.register(this);
    }

    public static Connection openConnection(double refreshRate, boolean queue) {
        Connection connection = new Connection(Configuration.load().getTeam(), refreshRate, queue);
        connection.open();
        return connection;
    }

    public boolean open() {
        try {
            connected = false;
            prepare();
            connect();
            connected = true;
        } catch (IOException e) {
            connected = false;
            Communicator.disconnected();
        }
        return connected;
    }

    public void close() throws IOException {
        connected = false;
        socket.close();
    }

    private void connect() throws IOException {
        // Determine the address
        String address = "10." + (teamNumber / 100) + "." + (teamNumber % 100) + ".2";
        // Create a new socket
        socket = new Socket();
        // Open the socket
        socket.connect(new InetSocketAddress(address, PORT), 1000);
        // Connect I/O
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        // Start the loop
        if (refreshRate > 0) {
            try {
                timer.cancel();
                timer = new Timer();
            } catch (Exception ignored) {
            } finally {
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            loop();
                        } catch (IOException e) {
                            connected = false;
                            Communicator.disconnected();
                        }
                    }
                }, 0, (long) (1000.0 / refreshRate));
            }
        }
    }

    public void clear() {
        commandQueue.clear();
        callbackQueue.clear();
        // Change loop
        prepare();
    }

    public void prepare() {
        // Change loop
        loop = true;
    }

    public void send(String command, Callback callback) {
        commandQueue.add(command);
        callbackQueue.add(callback);
    }

    private void loop() throws IOException {
        if (connected) {
            if (!commandQueue.isEmpty() || !callbackQueue.isEmpty()) {
                if (loop) {
                    writer.write(commandQueue.peek());
                    writer.newLine();
                    writer.flush();
                    // Pop
                    if (this.queue)
                        commandQueue.remove();
                } else {
                    // Read result
                    String result = reader.readLine();
                    // Split result
                    String[] split = result.split(":", 2);
                    // Call callback
                    Callback callback = callbackQueue.peek();
                    if (callback != null && split.length == 2)
                        callback.callback(Boolean.parseBoolean(split[0]), split[1]);
                    // Pop
                    if (this.queue)
                        callbackQueue.remove();
                }
                // Switch handler
                loop = !loop;
            }
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public interface Callback {
        void callback(boolean finished, String result);
    }
}
