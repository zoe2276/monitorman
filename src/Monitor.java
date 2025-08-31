import java.io.*;
import java.util.HashMap;
import java.util.prefs.*;

public class Monitor {
    private MonitorLayoutPanel parent;
    private String id;
    private int viewportWidth;
    private int viewportHeight;
    private int x;
    private int y;
    private float size;
    private String unit;
    private String aspectRatio;

    
    public Monitor(MonitorLayoutPanel parent, String id, int viewportWidth, int viewportHeight, float size, String unit, String aspectRatio) throws IOException {
        // Create a monitor with a known size
        this.setParent(parent);
        this.setId(id);
        this.setViewportWidth(viewportWidth);
        this.setViewportHeight(viewportHeight);
        this.setSize(size, false);
        this.setUnit(unit);
        this.setAspectRatio(aspectRatio);
    }
    
    public Monitor(MonitorLayoutPanel parent, String id, int viewportWidth, int viewportHeight) {
        // Create a monitor of unknown size
        this.setParent(parent);
        this.setId(id);
        this.setViewportWidth(viewportHeight);
        this.setViewportHeight(viewportHeight);
    }

    public Monitor(String id, int viewportWidth, int viewportHeight, float size, String unit, String aspectRatio) throws IOException {
        // Create an orphaned monitor with a known size
        this.setId(id);
        this.setViewportWidth(viewportWidth);
        this.setViewportHeight(viewportHeight);
        this.setSize(size, false);
        this.setUnit(unit);
        this.setAspectRatio(aspectRatio);
    }

    public Monitor(String id, int viewportWidth, int viewportHeight) {
        // Create an orphaned monitor of unknown size
        this.setId(id);
        this.setViewportWidth(viewportHeight);
        this.setViewportHeight(viewportHeight);
    }

    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public int getViewportWidth() {
        return this.viewportWidth;
    }
    public void setViewportWidth(int width) {
        this.viewportWidth = width;
    }

    public int getViewportHeight() {
        return this.viewportHeight;
    }
    public void setViewportHeight(int height) {
        this.viewportHeight = height;
    }
    
    public int getPositionX() {
        return this.x;
    }
    public void setPositionX(int x) {
        this.x = x;
    }
    
    public int getPositionY() {
        return this.y;
    }
    public void setPositionY(int y) {
        this.y = y;
    }

    public float getSize() {
        return this.size;
    }
    public void setSize(float size, boolean saveToPrefs) {
        this.size = size;
        if (saveToPrefs) {
            String pkSize = "pk_size_" + this.getId();
            Preferences prefs = Preferences.userNodeForPackage(MonitorManagerGui.class);
            prefs.put(pkSize, String.valueOf(size));
        }
    }

    public String getUnit() {
        return this.unit;
    }
    public void setUnit(String unit) throws IOException {
        if (unit == "cm" || unit == "in") {
            this.unit = unit;
        } else {
            throw new IOException("Provided unit does not belong to the allowable set: ['cm', 'int'].");
        }
    }

    public String getAspectRatio() {
        return this.aspectRatio;
    }
    public void setAspectRatio(String aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public MonitorLayoutPanel getParent() {
        return this.parent;
    }
    public void setParent(MonitorLayoutPanel parent) {
        this.parent = parent;
    }

    public HashMap<String, Integer> getViewportSize() {
        HashMap<String, Integer> vpSize = new HashMap<>();
        vpSize.put("width", this.getViewportWidth());
        vpSize.put("height", this.getViewportHeight());
        return vpSize;
    }
    public void setViewportSize(int width, int height) {
        this.setViewportWidth(width);
        this.setViewportHeight(height);
    }

    public HashMap<String, Integer> getPosition() {
        HashMap<String, Integer> pos = new HashMap<String, Integer>();
        pos.put("x", this.getPositionX());
        pos.put("y", this.getPositionY());
        return pos;
    }

    public void setPosition(int x, int y) throws IOException {
        // test for validity of arguments - x and y inbounds
        HashMap<String, Integer> parentBoundingBox = this.parent.getBoundingBox();
        HashMap<String, Integer> pos = this.getPosition();
        HashMap<String, Integer> vpSize = this.getViewportSize();

        if (pos.get("x") + vpSize.get("width") <= parentBoundingBox.get("width") &&
            pos.get("y") + vpSize.get("height") <= parentBoundingBox.get("height")) {
            this.setPositionX(x);
            this.setPositionY(y);
        } else {
            throw new IOException("Coordinates outside of allowed range. This parameter represents the top left corner of the display. Parent size: " + parentBoundingBox.get("width") + "x" + parentBoundingBox.get("height") + "px.");
        }
    }

    public HashMap<String, Float> getPhysicalSize() {
        // the size is the hypoteneuse
        float h = this.getSize();
        String asX, asY;
        float scaleFactor;
        String aspectRatio = this.getAspectRatio();
        String[] aspectRatioArr = aspectRatio.split(":");
        asX = aspectRatioArr[0];
        asY = aspectRatioArr[1];
        
        scaleFactor = h / Float.valueOf(String.valueOf(Math.sqrt(Math.pow(Double.valueOf(asX), Double.valueOf("2")) + Math.pow(Double.valueOf(asY), Double.valueOf("2")))));
        HashMap<String, Float> physicalSize = new HashMap<String, Float>();
        physicalSize.put("width", Float.valueOf(asX) * scaleFactor);
        physicalSize.put("height", Float.valueOf(asY) * scaleFactor);
        return physicalSize;
    }
}
