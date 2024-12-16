public class DriverInfo {
    private final String deviceName;
    private final String driverVersion;
    private final String manufacturer;

    public DriverInfo(String deviceName, String driverVersion, String manufacturer) {
        this.deviceName = deviceName;
        this.driverVersion = driverVersion;
        this.manufacturer = manufacturer;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDriverVersion() {
        return driverVersion;
    }

    public String getManufacturer() {
        return manufacturer;
    }
}