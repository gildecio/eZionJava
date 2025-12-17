package com.seguranca.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaDto {
    private Long id;
    private String nomeFantasia;
    private String razaoSocial;
    private String cnpj;
}