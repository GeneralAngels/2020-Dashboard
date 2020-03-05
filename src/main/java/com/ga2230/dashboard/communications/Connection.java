package com.ga2230.dashboard.communications;

import com.ga2230.dashboard.configuration.Configuration;
import jdk.vm.ci.code.site.Call;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

public class Connection {

    private static final int PORT = 5800;

    private ConnectionType connectionType = ConnectionType.QueuedExecution;

    // Pending command queue
    private Queue<Command> commandQueue;
    private Command currentCommand;

    // I/O section
    private BufferedWriter writer;
    private BufferedReader reader;
    private Socket socket;

    private boolean connected = false;

    private double refreshRate;
    private int teamNumber;

    private Timer timer;

    public Connection(int teamNumber, double refreshRate, ConnectionType connectionType) {
        this.connectionType = connectionType;
        this.commandQueue = new ArrayDeque<>();
        this.currentCommand = null;
        this.teamNumber = teamNumber;
        this.refreshRate = refreshRate;

        this.timer = new Timer();

        // Clear queues
        clear();

        // Add to communicator
        Communicator.register(this);
    }

    public static Connection openConnection(double refreshRate, ConnectionType connectionType) {
        Connection connection = new Connection(Configuration.load().getTeam(), refreshRate, connectionType);
        connection.open();
        return connection;
    }

    public boolean open() {
        try {
            connected = false;
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

    public void clear() {
        commandQueue.clear();
        currentCommand = null;
    }

    public void send(Command command) {
        if (connectionType == ConnectionType.QueuedExecution)
            commandQueue.add(command);
        else
            currentCommand = command;
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
            } catch (Exception ignored) {
            } finally {
                timer = new Timer();
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

    private void next() {
        if (connectionType == ConnectionType.QueuedExecution)
            currentCommand = commandQueue.remove();
    }

    private void loop() throws IOException {
        if (connected) {
            next();
            if (currentCommand != null) {
                // Write command
                writer.write(currentCommand.getFunction());
                writer.newLine();
                writer.flush();
                // Wait for result
                // Parse result
                String result = reader.readLine();
                String[] resultSplit = result.split(":", 2);
                // Make sure we have all of the results
                if (resultSplit.length == 2) {
                    // Make sure callback is not null
                    if (currentCommand.getCallback() != null) {
                        currentCommand.getCallback().callback(Boolean.parseBoolean(resultSplit[0]), resultSplit[1]);
                    }
                }
            }
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public enum ConnectionType {
        QueuedExecution,
        PeriodicExecution
    }

    public interface Callback {
        void callback(boolean finished, String result);
    }

    public static class Command {
        private String function;
        private Callback callback;

        public Command(String function, Callback callback) {
            this.function = function;
            this.callback = callback;
        }

        public String getFunction() {
            return function;
        }

        public Callback getCallback() {
            return callback;
        }
    }
}
