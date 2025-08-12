package com.java.meeting_room.helper;

import java.util.*;

public class bookingSummaryHelper {

    public static void ensureNestedMap(
            Map<String, Map<String, Map<String, List<Map<String, Object>>>>> result,
            String yearMonth,
            String office,
            String room
    ) {

        result.putIfAbsent(yearMonth, new LinkedHashMap<>());

        Map<String, Map<String, List<Map<String, Object>>>> officeMap = result.get(yearMonth);
        officeMap.putIfAbsent(office, new LinkedHashMap<>());

        Map<String, List<Map<String, Object>>> roomMap = officeMap.get(office);
        if (!roomMap.containsKey(room)) {
            roomMap.put(room, createDefaultList());
        }
    }

    private static List<Map<String, Object>> createDefaultList() {
        Map<String, Object> defaultData = new LinkedHashMap<>();
        defaultData.put("presentasi pemakaian", "0%");
        defaultData.put("nominal konsumsi", 0);

        Map<String, Integer> detailKonsumsi = new LinkedHashMap<>();
        detailKonsumsi.put("snack siang", 0);
        detailKonsumsi.put("makan siang", 0);
        detailKonsumsi.put("snack sore", 0);
        defaultData.put("detail konsumsi", detailKonsumsi);

        return new ArrayList<>(List.of(defaultData));
    }
}
