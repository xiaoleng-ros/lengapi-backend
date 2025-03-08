package com.leng.project.model.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 接口信息状态枚举
 */
@Getter
public enum InterfaceInfoStatusEnum {

    OFFLINE("关闭", 0),
    ONLINE("开启", 1),
    USER_AVATAR("用户头像", 2); // 假设 USER_AVATAR 对应的整数值是 2

    private final String text;
    private final Integer value; // 这里将 value 声明为 Integer 类型

    InterfaceInfoStatusEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return 返回所有枚举项的 value 值的列表
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(InterfaceInfoStatusEnum::getValue).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value 要查找的 value
     * @return 匹配的枚举项，如果没有找到则返回 null
     */
    public static InterfaceInfoStatusEnum getEnumByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (InterfaceInfoStatusEnum anEnum : values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

}