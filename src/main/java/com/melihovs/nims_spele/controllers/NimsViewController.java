package com.melihovs.nims_spele.controllers;

import com.melihovs.nims_spele.miniMax.MiniMax;
import com.melihovs.nims_spele.services.GraphBuildService;
import com.melihovs.nims_spele.services.GraphNode;
import com.melihovs.nims_spele.services.Level;
import org.springframework.stereotype.Controller;


@Controller
public class NimsViewController {

    private final GraphBuildService service;
    private final MiniMax miniMax;
    private GraphNode root;

    public NimsViewController(GraphBuildService service, MiniMax miniMax) {
        this.service = service;
        this.miniMax = miniMax;
    }

    public void setParameters(int amountOfMatches, Enum<Level> level) {
        this.service.setAmountOfMatches(amountOfMatches);
        this.service.setStartingLevel(level);
    }

    public void generateGraph() {
        root = this.service.generateGraph();
        this.miniMax.setRoot(root);
        root = miniMax.evaluateNodes();
    }

    public GraphNode getRoot() {
        return root;
    }
}
