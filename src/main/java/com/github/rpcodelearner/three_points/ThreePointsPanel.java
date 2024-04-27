package com.github.rpcodelearner.three_points;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;


class ThreePointsPanel extends JPanel {
    static final int X_SIZE = 500;
    static final int Y_SIZE = 500;
    private final ThreePointsModel model;
    private final Ranger xRanger = new Ranger(0, X_SIZE, -1, 1);
    private final Ranger yRanger = new Ranger(0, Y_SIZE, -1, 1);

    ThreePointsPanel(ThreePointsModel model) {
        super();
        this.setPreferredSize(new Dimension(X_SIZE, Y_SIZE));
        this.model = model;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, null, 0f));
        paintBands(g2d);
        paintFocuses(g2d);
    }

    private void paintBands(Graphics2D g2d) {
        for (int x = 0; x < X_SIZE; x++) {
            for (int y = 0; y < Y_SIZE; y++) {
                if (model.isPlot(screenToPlanePoint(x, y))) {
                    g2d.setColor(Color.BLACK);
                    g2d.fillRect(x, y, 1, 1);
                }
            }
        }
    }

    private PlanePoint screenToPlanePoint(int x, int y) {
        return new PlanePoint(xRanger.toMath(x), yRanger.toMath(y));
    }

    private void paintFocuses(Graphics2D g2d) {
        final int half_size = 2;
        g2d.setColor(Color.RED);

        List<Pixel> foci;
        foci = model.getFoci().stream().map(this::planePointToPixel).collect(Collectors.toList());
        foci.forEach(pixel -> g2d.fillOval(pixel.x - half_size, pixel.y -half_size, 2*half_size, 2*half_size));
    }

    private Pixel planePointToPixel(PlanePoint planePoint) {
        return new Pixel(xRanger.toPixel(planePoint.x), yRanger.toPixel(planePoint.y));
    }

}
