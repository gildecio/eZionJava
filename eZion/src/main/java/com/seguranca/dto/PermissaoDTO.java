package com.seguranca.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissaoDTO {
    private Long id;
    private String nome;
    private String descricao;
}
