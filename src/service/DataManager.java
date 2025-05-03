package service;

import Comparators.StringValueComparator;
import model.CustomDataObject;

import java.util.*;
import java.util.stream.Collectors;

public class DataManager {
    private static Map<UUID,CustomDataObject> dataObjects = new HashMap<>();

    public void addEntry(UUID id, String value,int start, int end) {
        dataObjects.put(id,new CustomDataObject(id,value,start,end));
    }

    public CustomDataObject getEntry(UUID id) {
        return dataObjects.get(id);
    }

    public void clearAllEntries() {
        dataObjects = new HashMap<>();
    }


    public List<CustomDataObject> getDataObjects() {
        return new ArrayList<>(dataObjects.values());
    }



    public void deleteEntry(UUID id) {
        dataObjects.remove(id);
    }

    public void updateEntry(UUID id, String value ) {
        CustomDataObject object = dataObjects.get(id);
        object.setValue(value);
    }

    public Map<UUID, CustomDataObject> listEntries() {
        return dataObjects;
    }

    public List<CustomDataObject> searchByValue(String value) {
        return dataObjects.values().stream()
                .filter(e -> e.getValue().toLowerCase().contains(value.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<CustomDataObject> sortByValue(String value) {
        return dataObjects.values().stream()
                .sorted(new StringValueComparator<>())
                .collect(Collectors.toList());
    }

    public void transformAll(String inputValue) {

        for (Map.Entry<UUID, CustomDataObject> object : dataObjects.entrySet()) {
            object.getValue().setValue(inputValue);
        }
    }
}