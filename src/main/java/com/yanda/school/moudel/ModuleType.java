package com.yanda.school.moudel;

import org.springframework.util.ObjectUtils;

public enum ModuleType {
    idle(0, "闲置"),
    Courier(1, "快递"),
    takeout(2, "外卖"),
    lostAndFound(3, "失物招领"),
    reservation(4, "预约"),
    mindPost(5, "心灵驿站");

    private Integer code;
    private String name;

    ModuleType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static ModuleType getModuleType(Integer code) {
        if (ObjectUtils.isEmpty(code)) {
            return null;
        }
        for (ModuleType moduleType : ModuleType.values()) {
            if (moduleType.code.equals(code)) {
                return moduleType;
            }
        }
        return null;
    }
}
