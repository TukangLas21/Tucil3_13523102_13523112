package com.tuciltiga.controller;

public enum Algorithm {
    GBFS("Greedy Best-First Search"),
    UCS("Uniform Cost Search"),
    ASTAR("A* Search"),
    IDA("Iterative Deepening A* Search");

    private final String displayName;

    Algorithm(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
