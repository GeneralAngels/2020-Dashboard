package com.ga2230.dashboard.communications;

import com.ga2230.dashboard.telemetry.TelemetryParser;
import org.json.JSONObject;

public abstract class Global {

    public static final BroadcastConnection TelemetryConnection = Communicator.openBroadcastConnection(20);
    public static final Connection ActionConnection = Communicator.openConnection(10, Connection.ConnectionType.QueuedExecution);

    static {
        Global.TelemetryConnection.send(new Connection.Command("robot telemetry", null));
        Global.TelemetryConnection.register((finished, result) -> TelemetryParser.update(new JSONObject(result)));
    }

}
