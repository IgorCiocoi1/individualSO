import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DriverCheckerUI {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;

    public void createAndShowGUI() {
        // Configurarea ferestrei principale cu un fundal albastru închis
        frame = new JFrame("Driver Checker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLayout(new BorderLayout());

        // Setarea fundalului ferestrei la albastru închis
        frame.getContentPane().setBackground(new Color(36, 39, 49));  // Albastru închis

        // Configurarea tabelului
        String[] columnNames = {"Device Name", "Driver Version", "Manufacturer"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 14));

        // Setarea culorii tabelului
        table.setBackground(new Color(50, 55, 65));  // Gri închis pentru tabel
        table.setForeground(Color.WHITE);  // Text alb pentru tabel
        table.setSelectionBackground(new Color(70, 130, 180));  // Albastru deschis pentru selecție
        table.setSelectionForeground(Color.WHITE); // Text alb la selecție
        table.setGridColor(new Color(100, 100, 100));  // Grilă gri deschis

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Crearea butonului pentru a verifica driverele
        JButton checkDriversButton = new JButton("Check Drivers");
        checkDriversButton.setFont(new Font("Arial", Font.BOLD, 14));
        checkDriversButton.setBackground(new Color(70, 130, 180));  // Albastru închis
        checkDriversButton.setForeground(Color.WHITE);
        checkDriversButton.setFocusPainted(false);
        checkDriversButton.setPreferredSize(new Dimension(180, 40));
        checkDriversButton.addActionListener(e -> populateTable(false));

        // Crearea butonului pentru a arăta driverele care necesită actualizare
        JButton showOutdatedDriversButton = new JButton("Show Outdated Drivers");
        showOutdatedDriversButton.setFont(new Font("Arial", Font.BOLD, 14));
        showOutdatedDriversButton.setBackground(new Color(220, 53, 69));  // Roșu subtil
        showOutdatedDriversButton.setForeground(Color.WHITE);
        showOutdatedDriversButton.setFocusPainted(false);
        showOutdatedDriversButton.setPreferredSize(new Dimension(220, 40));
        showOutdatedDriversButton.addActionListener(e -> populateTable(true));

        // Panou pentru butoane, cu un fundal albastru închis
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBackground(new Color(36, 39, 49));  // Fundal închis pentru panou
        panel.add(checkDriversButton);
        panel.add(showOutdatedDriversButton);

        // Adăugarea componentelor la fereastră
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(panel, BorderLayout.SOUTH);

        // Afișarea ferestrei
        frame.setVisible(true);
    }

    // Metodă pentru a popula tabelul
    private void populateTable(boolean showOutdated) {
        DriverCheckerLogic logic = new DriverCheckerLogic();
        List<DriverInfo> drivers;

        // Dacă se dorește doar driverele care necesită actualizare
        if (showOutdated) {
            drivers = logic.getOutdatedDrivers();
        } else {
            drivers = logic.getDriverInfo();
        }

        // Curăță tabelul
        tableModel.setRowCount(0);

        // Populează tabelul cu datele driverelor
        for (DriverInfo driver : drivers) {
            tableModel.addRow(new Object[]{driver.getDeviceName(), driver.getDriverVersion(), driver.getManufacturer()});
        }
    }
}
