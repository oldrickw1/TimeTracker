package com.example.timetracker;

import java.util.ArrayList;
import java.util.Arrays;

public class TimeSpendDOA {
    public TimeSpendDOA() {
    }
    final ArrayList<ActivityEntry> dummyEntries = new ArrayList<>(
            Arrays.asList(
                    new ActivityEntry(4, 0,"Programming"),
                    new ActivityEntry(5, 1, "Programming"),
                    new ActivityEntry(3, 2, "Programming"),
                    new ActivityEntry(5, 3, "Programming"),
                    new ActivityEntry(7, 4, "Programming")
            )
    );

    public ArrayList<ActivityEntry> getDummyEntries() {
        return dummyEntries;
    }
}
