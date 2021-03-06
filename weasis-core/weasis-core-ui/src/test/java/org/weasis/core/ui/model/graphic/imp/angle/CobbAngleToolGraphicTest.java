package org.weasis.core.ui.model.graphic.imp.angle;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;

import org.weasis.core.api.service.WProperties;
import org.weasis.core.ui.model.graphic.Graphic;
import org.weasis.core.ui.test.testers.GraphicTester;

public class CobbAngleToolGraphicTest extends GraphicTester<CobbAngleToolGraphic> {
    private static final String XML_0 = "/graphic/cobbAngle/cobbAngle.graphic.0.xml"; //$NON-NLS-1$
    private static final String XML_1 = "/graphic/cobbAngle/cobbAngle.graphic.1.xml"; //$NON-NLS-1$

    public static final String BASIC_TPL = "<cobbAngle fill=\"%s\" showLabel=\"%s\" thickness=\"%s\" uuid=\"%s\">" //$NON-NLS-1$
        + "<paint rgb=\"%s\"/>" //$NON-NLS-1$
        + "<pts/>" //$NON-NLS-1$
        + "</cobbAngle>"; //$NON-NLS-1$

    public static final CobbAngleToolGraphic COMPLETE_OBJECT = new CobbAngleToolGraphic();
    static {
        COMPLETE_OBJECT.setUuid(GRAPHIC_UUID_1);

        List<Point2D.Double> pts = Arrays.asList(new Point2D.Double(1770.5, 1435.0), new Point2D.Double(1765.5, 1445.0),
            new Point2D.Double(1611.5, 1590.0), new Point2D.Double(1575.5, 1589.0),
            new Point2D.Double(1637.1255967238944, 1500.8183122461598));
        COMPLETE_OBJECT.setPts(pts);
    }

    @Override
    public String getTemplate() {
        return BASIC_TPL;
    }

    @Override
    public Object[] getParameters() {
        return new Object[] { Graphic.DEFAULT_FILLED, Graphic.DEFAULT_LABEL_VISISIBLE, Graphic.DEFAULT_LINE_THICKNESS,
            getGraphicUuid(), WProperties.color2Hexadecimal(Graphic.DEFAULT_COLOR, true) };
    }

    @Override
    public String getXmlFilePathCase0() {
        return XML_0;
    }

    @Override
    public String getXmlFilePathCase1() {
        return XML_1;
    }

    @Override
    public CobbAngleToolGraphic getExpectedDeserializeCompleteGraphic() {
        return COMPLETE_OBJECT;
    }
}
