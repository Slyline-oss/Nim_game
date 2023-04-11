package com.melihovs.nims_spele.services;

import java.util.ArrayList;
import java.util.List;

public class GraphNode {

    private ArrayList<Integer> pile;
    private List<GraphNode> children;
    private List<GraphNode> ancestor;
    private Enum<Level> level;
    private int grade;

    public GraphNode() {
        this.children = new ArrayList<>();
        this.ancestor = new ArrayList<>();
    }

    public void addAncestor(GraphNode node) {
        for (GraphNode curr: this.ancestor) {
            if (curr == node) return;
        }
        this.ancestor.add(node);
    }

    public void addChild(GraphNode node) {
        for (GraphNode curr: this.children) {
            if (curr == node) return;
        }
        this.children.add(node);
    }

    public ArrayList<Integer> getPile() {
        return pile;
    }

    public void setPile(ArrayList<Integer> pile) {
        this.pile = pile;
    }

    public List<GraphNode> getChildren() {
        return children;
    }

    public Enum<Level> getLevel() {
        return level;
    }

    public void setLevel(Enum<Level> level) {
        this.level = level;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "GraphNode{" +
                "pile=" + pile +
                " grade=" + grade
                ;
    }
}
