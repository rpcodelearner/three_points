package com.github.rpcodelearner.three_points;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ThreePointsModelTest {
    private static final double EPS = 1e-10;
    private static final double RADIUS = 2.0 / 3.0;
    private ThreePointsUserChoices userChoices;
    private ThreePointsModel model;

    @BeforeEach
    void setUp() {
        userChoices = new ThreePointsUserChoices();
        model = new ThreePointsModel(userChoices);
    }

    @Test
    void getFociOnePoint() {
        userChoices.setNumPts(1);
        model.computePoints();
        assertEquals(1, model.getFoci().size());
        assertEquals(0.0, model.getFoci().get(0).x, EPS);
        assertEquals(0.0, model.getFoci().get(0).y, EPS);
    }

    @Test
    void defaultFoci() {
        assertEquals(3, model.getFoci().size());

        assertEquals(0.0, model.getFoci().get(0).x, EPS);
        assertEquals(-RADIUS, model.getFoci().get(0).y, EPS);

        // WARNING: don't get confused by the fact that x,y and sin,cos look "strangely swapped",
        // it is just a coincidence of this test
        assertEquals(Math.sin(Math.PI/3)*RADIUS, model.getFoci().get(1).x, EPS);
        assertEquals(Math.cos(Math.PI/3)*RADIUS, model.getFoci().get(1).y, EPS);

        // WARNING: don't get confused by the fact that x,y and sin,cos look "strangely swapped",
        // it is just a coincidence of this test
        assertEquals(-Math.sin(Math.PI/3)*RADIUS, model.getFoci().get(2).x, EPS);
        assertEquals(Math.cos(Math.PI/3)*RADIUS, model.getFoci().get(2).y, EPS);
    }

    @Test
    void alignedFoci() {
        userChoices.setNumPts(5); // easy test, so we also try more points
        userChoices.setFociPattern("Aligned");
        model.computePoints();

        assertEquals(5, model.getFoci().size());

        assertEquals(-RADIUS * 1.0, model.getFoci().get(0).x, EPS);
        assertEquals(-RADIUS * 0.5, model.getFoci().get(1).x, EPS);
        assertEquals(RADIUS * 0.0, model.getFoci().get(2).x, EPS);
        assertEquals(RADIUS * 0.5, model.getFoci().get(3).x, EPS);
        assertEquals(RADIUS * 1.0, model.getFoci().get(4).x, EPS);

        for (PlanePoint pt : model.getFoci()) {
            assertEquals(0.0, pt.y, EPS);
        }
    }

    @Test
    void randomFoci() {
        // as random is unpredictable, we "test" that none of
        // the points coordinates matches "special" values
        userChoices.setFociPattern("Random");
        model.computePoints();
        for (PlanePoint pt : model.getFoci()) {
            assertNotEquals(0.0, pt.x, EPS);
            assertNotEquals(0.0, pt.y, EPS);

            assertNotEquals(RADIUS, pt.x, EPS);
            assertNotEquals(RADIUS, pt.y, EPS);

            assertNotEquals(-RADIUS, pt.x, EPS);
            assertNotEquals(-RADIUS, pt.y, EPS);
        }
    }

    @Test
    void repeatedRandomFoci() {
        // as random is unpredictable, we "test" that successive
        // calls to model.computePoints() give different sets of foci
        userChoices.setFociPattern("Random");
        model.computePoints();
        List<PlanePoint> firstRunFoci = model.getFoci();
        model.computePoints();
        List<PlanePoint> secondRunFoci = model.getFoci();
        preTestPlanePointComparison();
        doAllComparisons(firstRunFoci, secondRunFoci);
    }

    private void preTestPlanePointComparison() {
        final PlanePoint ppA = new PlanePoint(0.1, 2.3); // NB same values as ppB below
        assertNotEquals( null, ppA);
        assertNotEquals(ppA, new Object());
        final PlanePoint ppB = new PlanePoint(0.1, 2.3); // NB same values as ppA above
        assertEquals(ppA, ppB);
        assertEquals(ppA.hashCode(), ppB.hashCode());
        final PlanePoint ppC = new PlanePoint(4.5, 6.7);
        assertNotEquals(ppA, ppC);
        assertNotEquals(ppA.hashCode(), ppC.hashCode());
    }

    private static void doAllComparisons(List<PlanePoint> firstRunFoci, List<PlanePoint> secondRunFoci) {
        for (PlanePoint firstRunFocus : firstRunFoci) {
            for (PlanePoint secondRunFocus : secondRunFoci) {
                assertNotEquals(firstRunFocus, secondRunFocus);
            }
        }
    }

    @Test
    void isWithinBand() {
        // we test that bands looks circular for a single point
        userChoices.setNumPts(1);
        userChoices.setDrawingStyle("Medium");
        model.computePoints();

        for (double radius = 0.1 ; radius < RADIUS ; radius += 0.1) {
            PlanePoint referencePoint = new PlanePoint(0.0, radius);
            boolean isWithinBand = model.isPlot(referencePoint);
            final int TEST_POINTS_AT_CURR_RADIUS = 10;

            for (double angle = 0.0; angle < Math.PI; angle += Math.PI / TEST_POINTS_AT_CURR_RADIUS) {
                PlanePoint pt = new PlanePoint(radius * Math.cos(angle), radius * Math.sin(angle));
                assertEquals(isWithinBand, model.isPlot(pt));
            }
        }
    }

    @Test
    void precisionPlotInvocation() {
        // we test that a circle is drawn if there is one focus point
        userChoices.setNumPts(1);
        userChoices.setDrawingStyle("Precision");
        model.computePoints();

        double radius = findRadiusOfPaintedCircle(model);
        final int TEST_POINTS_AT_CURR_RADIUS = 1000; // for precision plot we want to try lots of points
        final double angleLimit = Math.PI / 2.0;
        final double angleStep = angleLimit / TEST_POINTS_AT_CURR_RADIUS;
        for (double angle = 0.0; angle < angleLimit; angle += angleStep) {
            PlanePoint pt = new PlanePoint(radius * Math.cos(angle), radius * Math.sin(angle));
            assertTrue(model.isPlot(pt));
        }
    }

    private double findRadiusOfPaintedCircle(ThreePointsModel model) {
        for (double r = RADIUS; r > 0.0; r -= 0.001) {
            PlanePoint pt = new PlanePoint(0, r);
            if (model.isPlot(pt)) {
                return r;
            }
        }
        return Double.NaN;
    }

    @Test
    void exceptionTest() {
        // to force an unexpected result we fake a "malfunctioning" ThreePointsUserChoices
        assertThrows(RuntimeException.class, () -> new ThreePointsModel(new ThreePointsUserChoices() {
            @Override
            public String getFociPattern() {
                return "WrongChoiceForTestingPurposes";
            }
        }));
    }

}