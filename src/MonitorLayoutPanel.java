import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

public class MonitorLayoutPanel extends JPanel {
    private int width;
    private int height;
    private ArrayList<Monitor> monitors = new ArrayList<Monitor>();

    public MonitorLayoutPanel(int width, int height, Monitor[] monitors) {
        // super(false);
        super();
        // setSize(width, height);
        for (Monitor m : monitors) {
            m.setParent(this);
        }

        this.setWidth(width);
        this.setHeight(height);
        this.setMonitors(monitors);
    }

    public int getWidth() {
        return this.width;
    }
    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }
    public void setHeight(int height) {
        this.height = height;
    }

    public int getMonitorCount() {
        return this.monitors.size();
    }
    public Monitor[] getMonitors() {
        Monitor[] monitors = new Monitor[this.getMonitorCount()];
        return this.monitors.toArray(monitors);
    }
    public void addMonitor(Monitor monitor) {
        this.monitors.add(monitor);
    }
    public void addMonitor(Monitor[] monitors) {
        for (Monitor monitor : monitors) {
            this.addMonitor(monitor);
        }
    }
    public void clearMonitors() {
        this.monitors.clear();
    }
    public void setMonitors(Monitor[] monitors) {
        this.clearMonitors();
        this.addMonitor(monitors);
    }

    public HashMap<String, Integer> getBoundingBox() {
        HashMap<String, Integer> boundingBox = new HashMap<String, Integer>();
        boundingBox.put("width", this.width);
        boundingBox.put("height", this.height);
        return boundingBox;
    }
}
