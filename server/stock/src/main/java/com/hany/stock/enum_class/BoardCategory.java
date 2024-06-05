package com.hany.stock.enum_class;

public enum BoardCategory {
    FREE, GREETING,REQUEST;

    public static BoardCategory of(String category) {
        if (category.equalsIgnoreCase("free")) return BoardCategory.FREE;
        else if (category.equalsIgnoreCase("greeting")) return BoardCategory.GREETING;
        else if (category.equalsIgnoreCase("request")) return BoardCategory.REQUEST;
        else return null;
    }
}
