package com.facebook.react.uimanager.layoutanimation;

enum LayoutAnimationType {
    CREATE,
    UPDATE,
    DELETE;

    public static String toString(LayoutAnimationType layoutAnimationType) {
        switch (layoutAnimationType) {
            case CREATE:
                return "create";
            case UPDATE:
                return "update";
            case DELETE:
                return "delete";
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("Unsupported LayoutAnimationType: ");
                sb.append(layoutAnimationType);
                throw new IllegalArgumentException(sb.toString());
        }
    }
}
