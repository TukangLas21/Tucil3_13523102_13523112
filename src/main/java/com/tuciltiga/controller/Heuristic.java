package com.tuciltiga.controller;

public enum Heuristic {
    DistanceToExit("Distance to Exit"),
    BlockingVehicles("Blocking Vehicles");

    private final String displayName;

    Heuristic(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
