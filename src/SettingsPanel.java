import javax.swing.*;
import java.awt.GridLayout;
import java.util.prefs.*;

public class SettingsPanel extends JPanel {
    private MonitorManagerGui mainframe; // i love the funny

    public SettingsPanel(MonitorManagerGui mainframe) {
        super(new GridLayout(2, 3));

        setMainframe(mainframe);
        
        // get preferences
        Preferences prefs = Preferences.userNodeForPackage(MonitorManagerGui.class);
        final String pkUnit = "pk_unit";

        // unit selector
        String[] units = { "in", "cm" };
        
        JComboBox<String> inputUnit = new JComboBox<String>(units);
        inputUnit.addActionListener(event -> {
            // System.out.println(event.toString());
            String newUnit = (String) inputUnit.getSelectedItem();
            prefs.put(pkUnit, newUnit);
            
            // inform the parent to refresh its children's labels and computed sizes
            mainframe.refreshUnit();
        });
        add(inputUnit);
    }

    //g/s
    
    public MonitorManagerGui getMainframe() {
        return this.mainframe;
    }
    public void setMainframe(MonitorManagerGui mainframe) {
        this.mainframe = mainframe;
    }
}

class BinButton extends JToggleButton {
    private String label;
    private Action action;

    BinButton(String label, Action action) {
        super(action);

        setLabel(label);
        setAction(action);
    }

    //g/s

    public String getLabel() {
        return this.label;
    }
    public void setLabel(String label) {
        this.label = label;
    }

    public Action getAction() {
        return this.action;
    }
    public void setAction(Action action) {
        this.action = action;
    }
}