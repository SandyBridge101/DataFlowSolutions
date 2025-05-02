

package Comparators;

import java.util.Comparator;

import model.CustomDataObject;

public class StringValueComparator<T> implements Comparator<CustomDataObject> {
    public int compare(CustomDataObject e1, CustomDataObject e2) {
        return CharSequence.compare(e2.getValue(), e1.getValue());
    }
}