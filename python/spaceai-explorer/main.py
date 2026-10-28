from datetime import datetime


def check_telemetry(readings:dict)-> list[str]:
    """Check the telemetry readings and return a list of status messages."""

    warnings = []

    if readings ["battery_voltage"] < 10.0:
        warnings.append(f"Warning: Battery voltage is low ({readings['battery_voltage']}V)")
    if readings["temperature"] > 30.0:
        warnings.append(f"Warning: Temperature is high ({readings['temperature']}°C)")
    if readings["solar_panel_voltage"] < 10.0:
        warnings.append(f"warning: Solar panel voltage is low ({readings['solar_panel_voltage']}V)")

    return warnings



def analyze_satellite(readings:list[dict])-> None:
    """Analyze the telemetry readings and print the status of each satellite."""

    print("=== SPACEAI EXPLORER ===")
    print("Analysis time:", datetime.now().strftime("%Y-%m-%d %H:%M:%S"))

    for reading in readings:
        warnings = check_telemetry(reading)

        print(f"\nsatellite: {reading['name']}")
        print(f"battery voltage: {reading['battery_voltage']}V")
        print(f"temperature: {reading['temperature']}°C")
        print(f"solar panel voltage: {reading['solar_panel_voltage']}V")

        if warnings:
            print("Status: WARNING")
            for warning in warnings:
                print(warning)
        else:
            print("Status: Normal")



if __name__ == "__main__":
    telemetry_readings = [
        {
            "name": "space ai1",
            "battery_voltage": 9.8,
            "temperature": 24.5,
            "solar_panel_voltage": 12.5,
        },
        {
            "name": "space ai2",
            "battery_voltage": 9.5,
            "temperature": 25.0,
            "solar_panel_voltage": 12.0,
        },
        {
            "name": "space ai3",
            "battery_voltage": 9.2,
            "temperature": 26.0,
            "solar_panel_voltage": 11.5,
        },
    ]
    analyze_satellite(telemetry_readings)