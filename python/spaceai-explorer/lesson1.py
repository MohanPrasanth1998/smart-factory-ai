satellite_name="space ai"
battery_voltage=9.8
temperature=24.5
satelitte_id=101
is_operational=True

print(satellite_name)
print(satelitte_id)

print(type(battery_voltage))
print(type(temperature))
print(type(is_operational))


intial_voltage=9.8
current_voltage=8.2

drop_voltage=intial_voltage-current_voltage
percentage_drop=(drop_voltage/intial_voltage)*100

print("voltage drop",drop_voltage,"v")
print("voltage drop percentage",round(percentage_drop,2),"%")



if battery_voltage>=9.8:
    print("battery is full")
elif battery_voltage>7.5:
    print("battery is medium")
else:
    print("battery is low")


battery_readings=[10.8,10.5,9.2,8.9,8.6,8.3,8.0,7.7,7.4]

for battery_voltage in battery_readings:
    if battery_voltage>9.8:
        print("battery is full")
    elif battery_voltage>7.5:
        print("battery is medium")
    else:
        print("battery is low")




satellite={
    "name": "space ai",
    "id": 101,
    "battery_voltage": 9.8,
    "temperature": 24.5,
    "is_operational": True
}

print(satellite["name"])
print(satellite["id"])

satellite["battery_voltage"]=9.9
print(satellite["battery_voltage"])