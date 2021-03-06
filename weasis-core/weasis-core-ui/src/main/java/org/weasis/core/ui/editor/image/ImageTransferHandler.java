/*******************************************************************************
 * Copyright (c) 2016 Weasis Team and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Nicolas Roduit - initial API and implementation
 *******************************************************************************/
package org.weasis.core.ui.editor.image;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.image.RenderedImage;

import javax.media.jai.PlanarImage;
import javax.swing.JComponent;
import javax.swing.TransferHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.weasis.core.api.gui.Image2DViewer;
import org.weasis.core.api.image.SimpleOpManager;
import org.weasis.core.api.image.ZoomOp;

public class ImageTransferHandler extends TransferHandler implements Transferable {
    private static final long serialVersionUID = 7716040872158831560L;

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageTransferHandler.class);

    private static final DataFlavor[] flavors = { DataFlavor.imageFlavor };
    private SimpleOpManager disOp;

    @Override
    public int getSourceActions(JComponent c) {
        return TransferHandler.COPY;
    }

    @Override
    public boolean canImport(JComponent comp, DataFlavor flavor[]) {
        return false;
    }

    @Override
    public Transferable createTransferable(JComponent comp) {
        // Clear
        disOp = null;
        // TODO make only one export function with a dialog to choose to disable zoom (real size), add graphics,
        // anonymize and other default remove annotations
        if (comp instanceof Image2DViewer) {
            Image2DViewer<?> view2DPane = (Image2DViewer<?>) comp;
            RenderedImage src = view2DPane.getSourceImage();
            if (src != null) {
                SimpleOpManager opManager = view2DPane.getImageLayer().getDisplayOpManager().copy();
                opManager.removeImageOperationAction(opManager.getNode(ZoomOp.OP_NAME));
                opManager.setFirstNode(src);
                disOp = opManager;
                return this;
            }
        }
        return null;
    }

    @Override
    public boolean importData(JComponent comp, Transferable t) {
        return false;
    }

    @Override
    public Object getTransferData(DataFlavor flavor) {
        if (isDataFlavorSupported(flavor)) {
            RenderedImage result = disOp.process();
            if (result instanceof PlanarImage) {
                return ((PlanarImage) result).getAsBufferedImage();
            }
        }
        return null;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return flavors;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(DataFlavor.imageFlavor);
    }

}
