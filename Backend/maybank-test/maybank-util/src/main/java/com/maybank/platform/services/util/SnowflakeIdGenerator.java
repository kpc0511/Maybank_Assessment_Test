package com.maybank.platform.services.util;

import java.util.HashSet;
import java.util.Set;

public class SnowflakeIdGenerator {
    private static final long EPOCH = 1672531200000L; // Custom epoch (e.g., 2021-01-01)
    private static final long MACHINE_ID_BITS = 2L;  // 2 bits (4 machines)
    private static final long SEQUENCE_BITS = 8L;   // 8 bits (256 IDs/ms)
    private static final long TIMESTAMP_BITS = 38L; // 38 bits (Good for ~8 years)

    private static final long MAX_MACHINE_ID = ~(-1L << MACHINE_ID_BITS);
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);

    private static final long MACHINE_ID_SHIFT = SEQUENCE_BITS;
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + MACHINE_ID_BITS;

    private static long machineId = 1;
    private static long sequence = 0L;
    private static long lastTimestamp = -1L;

    // Synchronized static method to generate unique IDs
    public static synchronized long generateId() {
        long timestamp = currentTimeMillis() - EPOCH;

        if (timestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards.");
        }

        if (timestamp >= (1L << TIMESTAMP_BITS)) {  // Enforce 38-bit limit
            throw new RuntimeException("Timestamp exceeded 38-bit limit!");
        }

        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0) {
                timestamp = waitUntilNextMillis(timestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        long id = (timestamp << TIMESTAMP_SHIFT) | (machineId << MACHINE_ID_SHIFT) | sequence;

        return enforce16Digits(id);
    }

    private static long waitUntilNextMillis(long timestamp) {
        while (timestamp == lastTimestamp) {
            timestamp = currentTimeMillis() - EPOCH;
        }
        return timestamp;
    }

    private static long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    public static void setMachineId(long id) {
        if (id > MAX_MACHINE_ID || id < 0) {
            throw new IllegalArgumentException(String.format("Machine ID can't be greater than %d or less than 0", MAX_MACHINE_ID));
        }
        machineId = id;
    }

    private static long enforce16Digits(long id) {
        return id % 10_000_000_000_000_000L; // Ensure max 16 digits
    }

    public static void main(String[] args) {
        Set<Long> ids = new HashSet<>();
        for (int i = 0; i < 1_000_000; i++) {
            long id = generateId();
            System.out.println("id: " + id);
            if (!ids.add(id)) {
                System.out.println("Duplicate found: " + id);
            }
        }
        System.out.println("Done. Unique IDs generated: " + ids.size());
    }
}
