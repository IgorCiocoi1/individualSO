import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DriverCheckerLogic {

    // Metodă care returnează toate driverele
    public List<DriverInfo> getDriverInfo() {
        List<DriverInfo> drivers = new ArrayList<>();
        try {
            String command = "powershell.exe Get-WmiObject Win32_PnPSignedDriver | Select-Object DeviceName, DriverVersion, Manufacturer";
            Process process = Runtime.getRuntime().exec(command);

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            // Citirea datelor de la PowerShell
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s{2,}");
                if (parts.length == 3) {
                    drivers.add(new DriverInfo(parts[0], parts[1], parts[2]));
                }
            }

            reader.close();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return drivers;
    }

    // Metodă care returnează doar driverele care necesită actualizare
    public List<DriverInfo> getOutdatedDrivers() {
        List<DriverInfo> outdatedDrivers = new ArrayList<>();
        List<DriverInfo> allDrivers = getDriverInfo();

        for (DriverInfo driver : allDrivers) {
            // Logica pentru a considera un driver ca necesită actualizare (în acest caz, versiunea < 2.0)
            if (driver.getDriverVersion().startsWith("1.")) {
                outdatedDrivers.add(driver);
            }
        }
        return outdatedDrivers;
    }
}
