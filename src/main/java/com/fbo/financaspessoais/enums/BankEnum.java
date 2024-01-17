package com.fbo.financaspessoais.enums;

public enum BankEnum {

    C6("C6"),
    NUBANK("NUBANK");

    private final String value;

    BankEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static BankEnum fromValue(String value){
        for(BankEnum enumValue : values()){
            if(enumValue.value.equalsIgnoreCase(value)){
                return enumValue;
            }
        }
        throw new IllegalArgumentException("Invalid BankEnum value: " + value);
    }
}
