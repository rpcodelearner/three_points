package com.github.rpcodelearner.three_points;

import com.github.rpcodelearner.three_points.PlaneScreenCoordinates.Pixel;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;


class ThreePointsPanel extends JPanel {
    static final int XSIZE = 500;
    static final int YSIZE = 500;
    private final ThreePointsModel model;
    private final PlaneScreenCoordinates coords = new PlaneScreenCoordinates(XSIZE, YSIZE);

    ThreePointsPanel(ThreePointsModel model) {
        super();
        this.setPreferredSize(new Dimension(XSIZE, YSIZE));
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
        for (int x = 0; x < XSIZE; x++) {
            for (int y = 0; y < YSIZE; y++) {
                if (model.isPlot(coords.screenToPlanePoint(x, y))) {
                    g2d.setColor(Color.BLACK);
                    g2d.fillRect(x, y, 1, 1);
                }
            }
        }
    }

    private void paintFocuses(Graphics2D g2d) {
        final int size = 4;
        g2d.setColor(Color.RED);

        List<Pixel> foci;
        foci = model.getPoints().stream().map(coords::planePointToPixel).collect(Collectors.toList());
        foci.forEach(pixel -> g2d.fillOval(pixel.x, pixel.y, size, size));
    }

}
