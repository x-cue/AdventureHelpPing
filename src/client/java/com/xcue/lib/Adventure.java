package com.xcue.lib;

public enum Adventure {
    CHAIN("Abandoned Ruins"),
    IRON("Lost Wasteland"),
    DIAMOND("Demonic Realm");
    private final String name;

    Adventure(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static Adventure forName(String name) {
        for (Adventure adv : Adventure.values()) {
            if (adv.getName().equalsIgnoreCase(name)) {
                return adv;
            }
        }

        throw new EnumConstantNotPresentException(Adventure.class, "Enum does not exist with name " + name);
    }
}
