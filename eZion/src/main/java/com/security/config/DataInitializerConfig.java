package com.security.config;

import com.contabil.model.Empresa;
import com.contabil.repository.EmpresaRepository;
import com.security.model.Role;
import com.security.model.Usuario;
import com.security.repository.RoleRepository;
import com.security.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Configuration
public class DataInitializerConfig {

    @Bean
    public CommandLineRunner initializeData(
            EmpresaRepository empresaRepository,
            RoleRepository roleRepository,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            // Criar empresa padrão
            Empresa empresaPadrao = empresaRepository.findById(1L).orElseGet(() -> {
                Empresa empresa = new Empresa();
                empresa.setId(1L);
                empresa.setRazaoSocial("EZion Matriz");
                empresa.setNomeFantasia("EZion");
                empresa.setCnpj("00000000000000");
                empresa.setAtiva(true);
                return empresaRepository.save(empresa);
            });

            // Criar roles
            Role roleAdmin = roleRepository.findByNome(Role.RoleType.ADMIN)
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setNome(Role.RoleType.ADMIN);
                        role.setDescricao("Administrador do Sistema");
                        return roleRepository.save(role);
                    });

            roleRepository.findByNome(Role.RoleType.USER)
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setNome(Role.RoleType.USER);
                        role.setDescricao("Usuário Padrão");
                        return roleRepository.save(role);
                    });

            roleRepository.findByNome(Role.RoleType.VIEWER)
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setNome(Role.RoleType.VIEWER);
                        role.setDescricao("Visualizador");
                        return roleRepository.save(role);
                    });

            roleRepository.findByNome(Role.RoleType.GERENCIADOR_ESTOQUE)
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setNome(Role.RoleType.GERENCIADOR_ESTOQUE);
                        role.setDescricao("Gerenciador de Estoque");
                        return roleRepository.save(role);
                    });

            // Criar usuário admin se não existir
            Optional<Usuario> adminOptional = usuarioRepository.findByLogin("admin");
            if (adminOptional.isEmpty()) {
                Usuario admin = new Usuario();
                admin.setLogin("admin");
                admin.setSenha(passwordEncoder.encode("admin"));
                admin.setNome("Administrador");
                admin.setEmail("admin@ezion.com");
                admin.setEmpresa(empresaPadrao);
                admin.setAtivo(true);

                Set<Role> adminRoles = new HashSet<>();
                adminRoles.add(roleAdmin);
                admin.setRoles(adminRoles);

                usuarioRepository.save(admin);
                System.out.println("✓ Usuário admin/admin criado com sucesso!");
            }
        };
    }
}
