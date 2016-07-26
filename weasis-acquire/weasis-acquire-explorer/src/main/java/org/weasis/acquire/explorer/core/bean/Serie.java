package org.weasis.acquire.explorer.core.bean;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.dcm4che3.data.Tag;
import org.weasis.core.api.media.data.TagUtil;
import org.weasis.core.api.media.data.TagW;
import org.weasis.dicom.codec.TagD;

public class Serie extends AbstractTagable implements Comparable<Serie> {
    public enum Type {
        NONE, DATE, NAME;
    }

    private final Type type;
    private String name;
    private LocalDateTime date;

    public static final Serie DEFAULT_SERIE = new Serie();
    public static final Serie DATE_SERIE = new Serie(Type.DATE);
    public static final String DEFAULT_SERIE_NAME = "Other";

    public Serie() {
        this(Type.NONE);
    }

    private Serie(Type type) {
        this.type = type;
        init();
    }

    public Serie(String name) {
        this.type = Type.NAME;
        this.name = name;
        init();
    }

    public Serie(LocalDateTime date) {
        this.type = Type.DATE;
        this.date = date;
        init();
    }

    private void init() {
        // Default Modality if not overridden
        TagW modality = TagD.get(Tag.Modality);
        tags.put(modality, "XC");
    }

    public Type getType() {
        return type;
    }

    public String getDisplayName() {
        switch (type) {
            case NAME:
                return name;
            case DATE:
                return TagUtil.formatDateTime(date);
            default:
                return DEFAULT_SERIE_NAME;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Serie other = (Serie) obj;
        if (date == null) {
            if (other.date != null)
                return false;
        } else {
            if (other.date == null) {
                return false;
            } else {
                if (!date.atZone(ZoneId.systemDefault()).equals(other.date.atZone(ZoneId.systemDefault())))
                    return false;
            }
        }

        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (type != other.type)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return getDisplayName();
    }

    @Override
    public int compareTo(Serie that) {
        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;

        // this optimization is usually worthwhile, and can
        // always be added
        if (this == that)
            return EQUAL;

        // Check Type
        if (this.type.equals(Type.NONE) && !that.type.equals(Type.NONE))
            return BEFORE;
        if (this.type.equals(Type.DATE) && that.type.equals(Type.NONE))
            return AFTER;
        if (this.type.equals(Type.DATE) && that.type.equals(Type.NAME))
            return BEFORE;

        // Check Dates
        if (this.date != null && that.date == null)
            return BEFORE;
        if (this.date == null && that.date != null)
            return AFTER;
        if (this.date != null && that.date != null) {
            int comp = this.date.compareTo(that.date);
            if (comp != EQUAL)
                return comp;
        }

        // Check Names
        if (this.name != null && that.name == null)
            return BEFORE;
        if (this.name == null && that.name != null)
            return AFTER;
        if (this.name != null && that.name != null) {
            int comp = this.name.compareTo(that.name);
            if (comp != EQUAL)
                return comp;
        }

        // Check equals
        assert this.equals(that) : "compareTo inconsistent with equals.";

        return EQUAL;
    }

}
