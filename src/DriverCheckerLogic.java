import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DriverCheckerLogic {

    // Versiunea țintă pentru driverele actualizate
    private static final String TARGET_DRIVER_VERSION = "2.0";

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
            // Comparăm versiunea driverului cu versiunea țintă
            if (isOutdated(driver.getDriverVersion())) {
                outdatedDrivers.add(driver);
            }
        }
        return outdatedDrivers;
    }

    // Metodă care verifică dacă versiunea driverului este mai mică decât versiunea țintă
    private boolean isOutdated(String driverVersion) {
        try {
            // Se compară doar versiunea principală (de ex. 1.8, 2.0) pentru simplitate
            String[] driverVersionParts = driverVersion.split("\\.");
            String[] targetVersionParts = TARGET_DRIVER_VERSION.split("\\.");

            // Comparăm fiecare parte a versiunii (de exemplu, 1.8 vs 2.0)
            for (int i = 0; i < Math.min(driverVersionParts.length, targetVersionParts.length); i++) {
                int driverPart = Integer.parseInt(driverVersionParts[i]);
                int targetPart = Integer.parseInt(targetVersionParts[i]);

                // Dacă versiunea driverului este mai mică decât versiunea țintă, este considerată învechită
                if (driverPart < targetPart) {
                    return true;
                } else if (driverPart > targetPart) {
                    return false;
                }
            }

            // Dacă toate părțile sunt egale sau driverul nu are suficiente părți, se consideră învechit dacă e mai vechi
            return driverVersionParts.length < targetVersionParts.length;
        } catch (NumberFormatException e) {
            // Dacă există o problemă cu conversia versiunii la număr, considerăm că driverul este învechit
            return true;
        }
    }
}
