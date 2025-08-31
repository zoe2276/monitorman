import javax.swing.*;
import java.awt.GridLayout;
import java.util.prefs.*;

public class SettingsPanel extends JPanel {

    public SettingsPanel() {
        super(new GridLayout(2, 3));
        
        // get preferences
        Preferences prefs = Preferences.userNodeForPackage(MonitorManagerGui.class);
        final String pkUnit = "pk_unit";

        // unit selector
        String[] units = { "in", "cm" };
        
        JComboBox<String> inputUnit = new JComboBox<String>(units);
        inputUnit.addActionListener(unit -> {
            prefs.put(pkUnit, unit.toString());
            // also need to inform the sizes to change
        });
        add(inputUnit);
    }
}

class BinButton extends JToggleButton {
    private String label;
    private Action action;

    BinButton(String label, Action action) {
        super(action );
    }
}