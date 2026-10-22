CREATE INDEX idx_telemetry_machine_timestamp_id
    ON telemetry (machine_code, timestamp, id);
