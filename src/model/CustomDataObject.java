package model;

import java.util.Objects;
import java.util.UUID;

public class CustomDataObject {
    private UUID id;
    private String value;
    private int start;
    private int end;

    public CustomDataObject(UUID id, String value, int start, int end) {
        this.id = id;
        this.value = value;
        this.start = start;
        this.end = end;
    }

    public UUID getId() { return id; }
    public String getValue() { return value; }

    public void setId(UUID id) { this.id = id; }
    public void setValue(String value) { this.value = value; }
    public int getStart() { return start; }
    public void setStart(int start) { this.start = start; }
    public int getEnd() { return end; }
    public void setEnd(int end) { this.end = end; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomDataObject that = (CustomDataObject) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Value: " + value+", Position: " + start;
    }
}
