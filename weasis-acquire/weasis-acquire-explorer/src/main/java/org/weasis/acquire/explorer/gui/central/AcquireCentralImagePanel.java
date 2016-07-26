package org.weasis.acquire.explorer.gui.central;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.weasis.acquire.explorer.AcquireImageInfo;
import org.weasis.acquire.explorer.AcquireManager;
import org.weasis.acquire.explorer.core.bean.Serie;
import org.weasis.acquire.explorer.gui.central.tumbnail.AcquireCentralTumbnailPane;
import org.weasis.base.explorer.list.IThumbnailModel;
import org.weasis.core.api.media.data.ImageElement;

public class AcquireCentralImagePanel extends JPanel implements ListSelectionListener {
    private static final long serialVersionUID = 1270219114006046523L;

    private final AcquireCentralTumbnailPane<ImageElement> imageListPane;
    private AcquireCentralInfoPanel imageInfo;

    public AcquireCentralImagePanel(AcquireTabPanel acquireTabPanel) {
        this(acquireTabPanel, null, new ArrayList<AcquireImageInfo>());
    }

    public AcquireCentralImagePanel(AcquireTabPanel acquireTabPanel, Serie serie, List<AcquireImageInfo> imageInfos) {
        setLayout(new BorderLayout());

        imageInfo = new AcquireCentralInfoPanel(serie);

        imageListPane = new AcquireCentralTumbnailPane<>(toImageElement(imageInfos));
        imageListPane.setAcquireTabPanel(acquireTabPanel);
        imageListPane.addListSelectionListener(this);

        add(imageListPane, BorderLayout.CENTER);
        add(imageInfo, BorderLayout.SOUTH);
    }

    private List<ImageElement> toImageElement(List<AcquireImageInfo> list) {
        return list.stream().map(e -> e.getImage()).collect(Collectors.toList());
    }

    public void updateList(List<AcquireImageInfo> imageInfos) {
        imageListPane.setList(toImageElement(imageInfos));
    }

    public IThumbnailModel<ImageElement> getFileListModel() {
        return imageListPane.getFileListModel();
    }

    public void removeElements(List<ImageElement> medias) {
        IThumbnailModel<ImageElement> model = getFileListModel();
        medias.forEach(m -> {
            model.removeElement(m);
            AcquireManager.remove(m);
        });
    }

    public boolean isEmpty() {
        return getFileListModel().isEmpty();
    }

    public void refreshSerieMeta() {
        imageInfo.refreshSerieMeta();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        List<ImageElement> images = imageListPane.getSelectedValuesList();
        if (images.size() == 1) {
            imageInfo.setImage(images.get(0));
        } else {
            imageInfo.setImage(null);
        }
    }
}
