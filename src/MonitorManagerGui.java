import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;

import java.awt.*;
import java.io.*;
import java.util.prefs.*;
import java.util.ArrayList;

public class MonitorManagerGui extends JFrame {
    public MonitorManagerGui() throws IOException {
        super("monitorman");
        setSize(600, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // get current state
        GraphicsEnvironment graphicsEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] screens = graphicsEnv.getScreenDevices();

        // get preferences
        Preferences prefs = Preferences.userNodeForPackage(MonitorManagerGui.class);
        // check for user's preferred unit in prefs
        final String pkUnit = "pk_unit";
        String prefsUnit = prefs.get(pkUnit, "in");
        
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
                        
            if (prefsUnit != "" && prefsSize != "" && prefsAspectRatio != "") {
                int prefsSizeInt;
                try {
                    prefsSizeInt = Integer.parseInt(prefsSize);
                    monitor = new Monitor(screenId, width, height, prefsSizeInt, prefsUnit, prefsAspectRatio);
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

        // manip GUI
        // JPanel monitorLayoutPanel = new JPanel(new BorderLayout());
        int nextMonitorXCoord = 0;
        for (Monitor m : monitors) {
            MonitorElement mElem = new MonitorElement(monitorLayoutPanel, m);
            monitorLayoutPanel.add(mElem);
            mElem.setAlignmentX(nextMonitorXCoord);
            // need to figure out how to tell it WHERE to add the component
            nextMonitorXCoord = nextMonitorXCoord + m.getViewportWidth();
        }

        // getContentPane().add(monitorLayoutPanel, BorderLayout.CENTER);
        add(monitorLayoutPanel, BorderLayout.CENTER);

        SettingsPanel settingsPanel = new SettingsPanel();
        add(settingsPanel, BorderLayout.NORTH);
    }

    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(() -> {
            try {
                new MonitorManagerGui().setVisible(true);
                for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (IOException e) {
                System.err.println("IOException: " + e);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                e.getStackTrace();
            }
        });
    }
}