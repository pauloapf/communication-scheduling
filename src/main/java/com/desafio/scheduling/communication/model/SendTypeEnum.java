package com.desafio.scheduling.communication.model;

/**
 * Forma de envio: 1- Email, 2-SMS, 3-Push e 4-WhatsApp
 */
public enum SendTypeEnum {

    EMAIL("1"),
    
    SMS("2"),
    
    PUSH("3"),
    
    WHATSAPP("4");

    private String value;

    SendTypeEnum(String value) {
      this.value = value;
    }

    public String toString() {
      return String.valueOf(value);
    }

    public static SendTypeEnum fromValue(String text) {
      for (SendTypeEnum b : SendTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
	
}
