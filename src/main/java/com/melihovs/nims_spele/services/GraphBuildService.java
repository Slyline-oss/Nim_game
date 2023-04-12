package com.melihovs.nims_spele.services;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GraphBuildService {

    private int amountOfMatches;
    private Enum<Level> startingLevel;


    public GraphBuildService() {
    }

    public void setAmountOfMatches(int amountOfMatches) {
        this.amountOfMatches = amountOfMatches;
    }

    public void setStartingLevel(Enum<Level> startingLevel) {
        this.startingLevel = startingLevel;
    }

    public GraphNode generateGraph() {
        GraphNode root = new GraphNode();
        root.setLevel(startingLevel);
        ArrayList<Integer> pile = new ArrayList<>(1);
        pile.add(amountOfMatches);
        root.setPile(pile);

        Queue<GraphNode> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            GraphNode curr = queue.poll();
            List<GraphNode> list = generateChildren(curr, queue);
            queue.addAll(curr.getChildren());
        }

        return root;
    }

    private List<GraphNode> generateChildren(GraphNode node, Queue<GraphNode> queue) {
        ArrayList<Integer> pile = node.getPile();
        ArrayList<ArrayList<Integer>> combinations = new ArrayList<>();
        List<GraphNode> list = new ArrayList<>();

        for (int i = 0; i < pile.size(); i++) {
            if (pile.get(i) <= 2) continue;
            int len = pile.get(i) % 2 == 0 ? pile.get(i) / 2 - 1 : pile.get(i) / 2;
            ArrayList<Integer> combin;
            for (int j = 1; j <= len; j++) {
                combin = new ArrayList<>(pile);
                combin.remove(pile.get(i));
                combin.add(pile.get(i) - j);
                combin.add(j);
                combinations.add(combin);
            }
        }

        if (combinations.size() == 0) {
            node.setGrade(node.getLevel() == Level.MAX ? -1 : 1);
            return null;
        }

        for (ArrayList<Integer> comb : combinations) {
            GraphNode child = searchForExisting(queue, comb);
            if (child == null) {
                child = new GraphNode();
                child.setPile(comb);
                list.add(child);
            }
            child.addAncestor(node);
            child.setLevel(node.getLevel() == Level.MAX ? Level.MIN : Level.MAX);
            node.addChild(child);
        }

        return list;
    }

    private GraphNode searchForExisting(Queue<GraphNode> queue, ArrayList<Integer> comb) {
        Collections.sort(comb);

        for (GraphNode node: queue) {
            ArrayList<Integer> piles = node.getPile();
            Collections.sort(piles);
            if (comb.equals(piles)) return node;
        }
        return null;
    }

}
