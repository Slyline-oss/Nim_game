package com.melihovs.nims_spele.miniMax;

import com.melihovs.nims_spele.services.GraphNode;
import com.melihovs.nims_spele.services.Level;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MiniMax {

    private GraphNode root;

    public MiniMax() {

    }

    public GraphNode evaluateNodes() {
        GraphNode root = this.root;

        List<List<GraphNode>> levels = traverseGraphByLevels(root);
        int n = levels.size();

        for (int i = n - 1; i >=0; i--) {
            ArrayList<GraphNode> list  = (ArrayList<GraphNode>) levels.get(i);
            for (int j = 0; j < list.size(); j++) {
                GraphNode curr = list.get(j);
                if (curr.getChildren().size() == 0) continue;
                if (curr.getChildren().size() == 1) {
                    GraphNode child = curr.getChildren().get(0);
                    curr.setGrade(child.getGrade());
                }
                if (curr.getChildren().size() > 1) {
                    Enum<Level> levelEnum = curr.getLevel();
                    ArrayList<GraphNode> children = (ArrayList<GraphNode>) curr.getChildren();
                    if (levelEnum.equals(Level.MAX)) {
                        int max = -2;
                        for (GraphNode child : children) {
                            if (child.getGrade() > max) max = child.getGrade();
                        }
                        curr.setGrade(max);
                    } else {
                        int min = 2;
                        for (GraphNode child : children) {
                            if (child.getGrade() < min) min = child.getGrade();
                        }
                        curr.setGrade(min);
                    }
                }
            }
        }
        return root;
    }

    private List<List<GraphNode>> traverseGraphByLevels(GraphNode root) {
        List<List<GraphNode>> levels = new ArrayList<>();

        Queue<GraphNode> queue = new LinkedList<>();
        queue.add(root);

        int level = 0;
        while (!queue.isEmpty()) {
            levels.add(new ArrayList<>());

            int level_length = queue.size();
            Set<GraphNode> set = new HashSet<>();
            for (int i = 0; i < level_length; ++i) {
                GraphNode node = queue.poll();
                levels.get(level).add(node);
                if (node != null) {
                    set.addAll(node.getChildren());
                }
            }
            queue.addAll(set);
            set.clear();
            level++;
        }
        return levels;
    }

    public GraphNode getRoot() {
        return root;
    }

    public void setRoot(GraphNode root) {
        this.root = root;
    }
}
