package com.renatom.minhasfinancas.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UsuarioDTO {
	
	private String email;
	private String name;
	private String senha;

}
