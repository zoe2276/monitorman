import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;

import java.awt.*;
import java.io.*;
import java.util.prefs.*;
import java.util.ArrayList;

public class MonitorManagerGui extends JFrame {
    private Preferences prefs;
    private MonitorLayoutPanel layoutPanel;

    public MonitorManagerGui() throws IOException {
        super("monitorman");
        /* maybe i will figure this out eventually
            ImageIcon icon = new ImageIcon("./res/logo2.png");
            setIconImage(icon.getImage());
            setSize(600, 800);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
        */
        // get current state
        GraphicsEnvironment graphicsEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] screens = graphicsEnv.getScreenDevices();

        // get preferences
        Preferences prefs = Preferences.userNodeForPackage(MonitorManagerGui.class);
        this.prefs = prefs;
        
        // catch the monitors
        ArrayList<Monitor> monitorList = new ArrayList<Monitor>();
        for (GraphicsDevice screen : screens) {
            String screenId = screen.getIDstring();
            int width = screen.getDisplayMode().getWidth();
            int height = screen.getDisplayMode().getHeight();

            
            // check the preferences for a size for this monitor
            final String pkSize = "pk_size_" + screenId;
            final String pkAspectRatio = "pk_aspectRatio_" + screenId;
            String prefsSize = prefs.get(pkSize, "");
            String prefsAspectRatio = prefs.get(pkAspectRatio, "");

            Monitor monitor;
                        
            if (prefsSize != "" && prefsAspectRatio != "") {
                int prefsSizeInt;
                try {
                    prefsSizeInt = Integer.parseInt(prefsSize);
                    monitor = new Monitor(screenId, width, height, prefsSizeInt, prefsAspectRatio);
                } catch (NumberFormatException e) {
                    monitor = new Monitor(screenId, width, height);
                }
            } else {
                monitor = new Monitor(screenId, width, height);
            }

            // System.out.println("dimensions of " + screenId + ": " + width + "x" + height);
            monitorList.add(monitor);
        }

        Monitor[] monitors = new Monitor[monitorList.size()];
        monitorList.toArray(monitors);
        int maxMonitorWidth = monitorList.stream().mapToInt(o -> o.getViewportWidth()).sum();
        int maxMonitorHeight = monitorList.stream().mapToInt(o -> o.getViewportHeight()).sum();
        MonitorLayoutPanel monitorLayoutPanel = new MonitorLayoutPanel(maxMonitorWidth, maxMonitorHeight, monitors);
        setLayoutPanel(monitorLayoutPanel);
        // manip GUI
        // JPanel monitorLayoutPanel = new JPanel(new BorderLayout());
        int nextMonitorXCoord = 0;
        for (Monitor m : monitors) {
            MonitorElement mElem = new MonitorElement(monitorLayoutPanel, m);
            // update the monitor's gui reference to the new monitor element
            m.setGuiElement(mElem);
            // add the gui element to the monitor layout panel
            monitorLayoutPanel.add(mElem);
            mElem.setAlignmentX(nextMonitorXCoord);
            // need to figure out how to tell it WHERE to add the component
            nextMonitorXCoord = nextMonitorXCoord + m.getViewportWidth();
        }

        // getContentPane().add(monitorLayoutPanel, BorderLayout.CENTER);
        add(monitorLayoutPanel, BorderLayout.CENTER);

        SettingsPanel settingsPanel = new SettingsPanel(this);
        add(settingsPanel, BorderLayout.NORTH);
    }

    public MonitorLayoutPanel getLayoutPanel() {
        return this.layoutPanel;
    }
    public void setLayoutPanel(MonitorLayoutPanel layoutPanel) {
        this.layoutPanel = layoutPanel;
    }

    public void refreshUnit() {
        // refresh the unit and recompute sizes
        final String pkUnit = "pk_unit";
        String newUnit = this.prefs.get(pkUnit, "in");

        MonitorLayoutPanel lp = getLayoutPanel();
        Monitor[] ms = lp.getMonitors();
        for (Monitor m : ms) {
            MonitorElement mg = m.getGuiElement();
            mg.getLabel().setText(m.getSize() + newUnit);
        }

    }
}