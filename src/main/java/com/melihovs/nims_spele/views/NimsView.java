package com.melihovs.nims_spele.views;

import com.melihovs.nims_spele.controllers.NimsViewController;
import com.melihovs.nims_spele.services.GraphNode;
import com.melihovs.nims_spele.services.Level;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Route(value = "")
@PageTitle("Nims Spele")
public class NimsView extends VerticalLayout {

    Logger logger = LoggerFactory.getLogger(NimsView.class);

    private final NimsViewController controller;
    private IntegerField input = new IntegerField();
    private Button start = new Button();
    private Button restart = new Button("Restart");
    private Select<String> selector = new Select<>();
    private GraphNode current;
    private Map<Button, GraphNode> but_node = new HashMap<>();
    private Map<GraphNode, Button> node_but = new HashMap<>();
    private Enum<Level> gameLeader;

    public NimsView(NimsViewController controller) {
        setAlignItems(Alignment.CENTER);
        this.controller = controller;
        generateLayout();
    }


    private void generateLayout() {
        HorizontalLayout vl = new HorizontalLayout();
        HorizontalLayout vl2 = new HorizontalLayout();

        H1 naming = new H1("NIMS SPĒLE");

        //input matches button
        input.setMin(6);
        input.setMax(14);
        input.setValue(7);
        input.setStepButtonsVisible(true);
        input.setHelperText("No 6 līdz 14");

        //start button
        start.setText("Start");
        start.addClickListener(e -> {
           startGame();
        });

        //restart
        restart.addClickListener(e -> {
            UI.getCurrent().getPage().reload();
        });

        //selector
        selector.setEmptySelectionAllowed(false);
        selector.setLabel("Izvēlēties, kurš spēlēs pirmais");
        selector.setItems(Arrays.asList("Sakšu pirmais","Sakšu otrais"));
        selector.setValue("Sakšu pirmais");

        //add to Vertical Layout
        vl.add(input, start, restart);
        vl2.add(selector);

        add(naming, vl, vl2);
    }

    private void startGame() {
        String starter = selector.getValue();
        Enum<Level> level = starter.equalsIgnoreCase("Sakšu pirmais") ? Level.MIN : Level.MAX;

        if (input.getValue() > 14 || input.getValue() < 6) {
            Notification.show("Ievadiet skaitļi no 6 līdz 14", 3000, Notification.Position.TOP_START)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }
        controller.setParameters(input.getValue(), level);
        controller.generateGraph();
        GraphNode root = controller.getRoot();

        //layout
        HorizontalLayout hl = new HorizontalLayout();
        hl.setAlignItems(Alignment.CENTER);
        hl.setSpacing(true);
        Button origin = new Button(root.getPile().toString());
        hl.add(origin);
        add(hl);

        gameLeader = root.getLevel();
        generateLineWithButton((ArrayList<GraphNode>) root.getChildren());
        start.setEnabled(false);

        logger.info("GAME STARTED");
    }

    private void generateLineWithButton(ArrayList<GraphNode> list) {
        HorizontalLayout hl = new HorizontalLayout();
        hl.setAlignItems(Alignment.CENTER);
        hl.setSpacing(true);
        for (GraphNode node: list) {
            String values = node.getPile().toString();
            hl.add(addClickListener(new Button(values), node));
        }
        add(new Text(gameLeader.equals(Level.MAX) ? "DATORA GĀJIENS" : "JŪSU GĀJIENS"));
        add(hl);
        if (gameLeader == Level.MAX) {
            boolean generated = false;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getGrade() == 1) {
                    node_but.get(list.get(i)).addThemeVariants(ButtonVariant.LUMO_CONTRAST);
                    gameLeader = list.get(i).getLevel();
                    node_but.get(list.get(i)).click();
                    generated = true;
                }
            }
            if (!generated) {
                node_but.get(list.get(0)).addThemeVariants(ButtonVariant.LUMO_CONTRAST);
                gameLeader = list.get(0).getLevel();
                node_but.get(list.get(0)).click();
            }
        }
    }

    private void endGame(GraphNode node) {
        Notification not = new Notification();
        if (node.getLevel() == Level.MAX) {
            not.setText("Jūs uzvarējāt!");
            not.setDuration(5000);
            not.setPosition(Notification.Position.MIDDLE);
            not.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } else {
            not.setText("Dators uzvarējā!");
            not.setDuration(5000);
            not.setPosition(Notification.Position.MIDDLE);
            not.addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
        not.open();
    }


    private Button addClickListener(Button button, GraphNode node) {
        but_node.put(button, node);
        node_but.put(node, button);
        button.addClickListener(e -> {
            current = but_node.get(button);
            button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            gameLeader = current.getLevel();
            but_node.remove(button);
            for (Map.Entry<Button, GraphNode> entry: but_node.entrySet()) {
                entry.getKey().setEnabled(false);
            }
            but_node.clear();
            node_but.clear();
            ArrayList<GraphNode> children = (ArrayList<GraphNode>) current.getChildren();
            if (children.size() == 0) {
                endGame(current);
            } else {
                generateLineWithButton(children);
            }
        });
        return button;
    }

}
