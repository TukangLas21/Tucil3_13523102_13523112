package com.tuciltiga.controller;

public enum Algorithm {
    GREEDY_BFS("Greedy Best-First Search"),
    UNIFORM_COST("Uniform Cost Search"),
    A_STAR("A* Search");

    private final String displayName;

    Algorithm(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
