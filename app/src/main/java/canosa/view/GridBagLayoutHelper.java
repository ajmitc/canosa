package canosa.view;

import javax.swing.*;
import java.awt.*;

public class GridBagLayoutHelper {
    private GridBagConstraints constraints = new GridBagConstraints();
    private Container container;

    public GridBagLayoutHelper(Container container){
        this(container, false);
    }

    public GridBagLayoutHelper(Container container, boolean useWrapper){
        if (useWrapper){
            this.container = new JPanel();
            container.add(this.container);
        }
        else {
            this.container = container;
        }
        this.container.setLayout(new GridBagLayout());

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.ipadx = 0;
        constraints.ipady = 0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(0, 0, 0, 0);
    }

    public GridBagLayoutHelper add(Component component){
        container.add(component, constraints);
        constraints.gridx += 1;
        return this;
    }

    public GridBagLayoutHelper nextRow(){
        constraints.gridy += 1;
        constraints.gridx = 0;
        return this;
    }

    public GridBagLayoutHelper setGridWidth(int width){
        constraints.gridwidth = width;
        return this;
    }

    public GridBagLayoutHelper resetGridWidth(){
        constraints.gridwidth = 1;
        return this;
    }

    public GridBagLayoutHelper setPadding(int x, int y){
        constraints.ipadx = x;
        constraints.ipady = y;
        return this;
    }

    public GridBagLayoutHelper setExternalPadding(int top, int left, int bottom, int right){
        constraints.insets = new Insets(top, left, bottom, right);
        return this;
    }

    public GridBagLayoutHelper setAnchor(int anchor){
        constraints.anchor = anchor;
        return this;
    }

    public GridBagLayoutHelper setFill(int fill){
        constraints.fill = fill;
        return this;
    }

    public GridBagLayoutHelper setWeightX(double weightX){
        constraints.weightx = weightX;
        return this;
    }

    public GridBagLayoutHelper setWeightY(double weightY){
        constraints.weighty = weightY;
        return this;
    }
}
