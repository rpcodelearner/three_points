package com.github.rpcodelearner.three_points;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;


class ThreePointsPanel extends JPanel {
    int xSize = 500;
    int ySize = 500;
    private final ThreePointsModel model;
    private final RangerXY rangerXY = new RangerXY(
            new Dimension(xSize, ySize),
            new PlanePoint(-1, -1),
            new PlanePoint(1, 1)
            );

    ThreePointsPanel(ThreePointsModel model) {
        super();
        this.setPreferredSize(new Dimension(xSize, ySize));
        this.model = model;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        updatePanelSize();
        recenterPanel();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, null, 0f));
        paintBands(g2d);
        paintFocuses(g2d);
    }

    private void updatePanelSize() {
        xSize = getWidth();
        ySize = getHeight();
    }

    private void recenterPanel() {
        rangerXY.setMathCenterToPixel(new Pixel(xSize/2, ySize/2));
    }

    private void paintBands(Graphics2D g2d) {
        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                if (model.isPlot(rangerXY.toMath(new Pixel(x, y)))) {
                    g2d.setColor(Color.BLACK);
                    g2d.fillRect(x, y, 1, 1);
                }
            }
        }
    }

    private void paintFocuses(Graphics2D g2d) {
        final int half_size = 2;
        g2d.setColor(Color.RED);

        List<Pixel> foci;
        foci = model.getFoci().stream().map(rangerXY::toPixel).collect(Collectors.toList());
        foci.forEach(pixel -> g2d.fillOval(pixel.x - half_size, pixel.y -half_size, 2*half_size, 2*half_size));
    }

}
