package com.desafio.scheduling.communication.model;

/**
 * Status da comunicação. 1-Agendado, 2-Enviado com Sucesso, 3-Falha no envio
 */
public enum StatusCommunicationEnum {
	
	AGENDADO("1"),

	ENVIADO_SUCESSO("2"),

	FALHA_ENVIO("3");

	private String value;

	StatusCommunicationEnum(String value) {
		this.value = value;
	}

	public String toString() {
		return String.valueOf(value);
	}

	public static StatusCommunicationEnum fromValue(String text) {
		for (StatusCommunicationEnum b : StatusCommunicationEnum.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
