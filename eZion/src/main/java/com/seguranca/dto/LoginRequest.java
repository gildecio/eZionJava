package com.seguranca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @Schema(description = "ID da empresa", example = "1")
    @JsonProperty("empresa_id")
    private Long empresaId;
    
    @Schema(description = "Nome de usuário", example = "admin")
    @JsonProperty("usuario")
    @JsonAlias("username")
    private String username;
    
    @Schema(description = "Senha do usuário", example = "admin")
    @JsonProperty("senha")
    @JsonAlias("password")
    private String password;
}
