package com.seguranca.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    private Long id;
    private String nome;
    private String descricao;
    private Boolean ativo;
    private List<PermissaoDTO> permissoes;
}
