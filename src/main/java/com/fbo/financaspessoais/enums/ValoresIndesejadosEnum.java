package com.fbo.financaspessoais.enums;

public enum ValoresIndesejadosEnum implements Valuable{
    EMISSAO_E_ENVIO("EMISSÃO E ENVIO"),
    INCLUSAO_PAGAMENTO("Inclusao de Pagamento");

    private final String value;

    ValoresIndesejadosEnum(String value) {
        this.value = value;
    }



    public static ValoresIndesejadosEnum fromValue(String value){
        for(ValoresIndesejadosEnum enumValue : values()){
            if(enumValue.value.equalsIgnoreCase(value)){
                return enumValue;
            }
        }
        throw new IllegalArgumentException("Invalid ValoresIndesejadosEnum value: " + value);
    }

    @Override
    public String getValue() {
        return value;
    }
}
