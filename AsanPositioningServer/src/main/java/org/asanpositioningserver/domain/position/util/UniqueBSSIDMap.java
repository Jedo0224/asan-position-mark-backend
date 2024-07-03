package org.asanpositioningserver.domain.position.util;

import java.util.*;

public class UniqueBSSIDMap {
    private static UniqueBSSIDMap instance;
    private final Map<String, String> bssidMap;
    private final Set<String> updatedKeys;

    public UniqueBSSIDMap() {
        bssidMap = new LinkedHashMap<>();
        updatedKeys = new HashSet<>();
    }

    public static synchronized UniqueBSSIDMap getInstance() {
        if (instance == null) {
            instance = new UniqueBSSIDMap();
        }
        return instance;
    }

    public synchronized void initializeBSSIDMap(Set<String> uniqueBssids) {
        Set<String> sortedBssids = new TreeSet<>(uniqueBssids);
        for (String bssid : sortedBssids) {
            bssidMap.put(bssid, null);
        }
    }

    public synchronized void updateBSSIDMap(String bssid, String rssi) {
        if (bssidMap.containsKey(bssid)) {
            bssidMap.put(bssid, rssi);
            updatedKeys.add(bssid);
        }
    }

    public synchronized Map<String, String> getBSSIDMap() {
        return new TreeMap<>(bssidMap);
    }

    public synchronized void resetUpdatedBSSIDMapValues() {
        for (String bssid : updatedKeys) {
            System.out.println("Resetting updated bssid: " + bssid);
            bssidMap.put(bssid, null);
        }
        updatedKeys.clear();
    }

    public synchronized void resetBSSIDMapValues() {
        for (String bssid : bssidMap.keySet()) {
            System.out.println("Resetting bssid: " + bssid);
            bssidMap.put(bssid, null);
        }
        updatedKeys.clear();
    }

    public synchronized void copyFrom(UniqueBSSIDMap other) {
        this.bssidMap.clear();
        this.bssidMap.putAll(other.bssidMap);
        this.updatedKeys.clear();
        this.updatedKeys.addAll(other.updatedKeys);
    }
}