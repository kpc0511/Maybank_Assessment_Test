package com.maybank.platform.services.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    public static HashMap<String, BigDecimal> extractKeyValuePairs(String data) {
        HashMap<String, BigDecimal> map = new HashMap<>();

        // Regular expression pattern to match key-value pairs
        Pattern pattern = Pattern.compile("(\\w+)\\s*>\\s*([^|]+)\\|");
        Matcher matcher = pattern.matcher(data);

        // Find and extract each key-value pair
        while (matcher.find()) {
            String key = matcher.group(1).trim();
            map.put(key, BigDecimal.ZERO);
        }
        return map;
    }

    public static void main(String[] args) {
        String data = "TIME|PM1125H > active_energy(Dlivered)3206|BYD2 > Energy import 0030|BYD3 > Energy import 0030|";

        // Extracted key-value pairs will be stored in this HashMap
        Map<String, BigDecimal> map = extractKeyValuePairs(data);

        // Print the HashMap to verify the results
        map.forEach((key, value) -> System.out.println(key + " -> " + value));
    }
}
