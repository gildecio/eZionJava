package com.seguranca.security;

import com.seguranca.model.Usuario;
import com.seguranca.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

        if (!usuario.getAtivo()) {
            throw new UsernameNotFoundException("Usuário inativo: " + username);
        }

        if (usuario.getBloqueado()) {
            throw new UsernameNotFoundException("Usuário bloqueado: " + username);
        }

        return User.builder()
            .username(usuario.getUsername())
            .password(usuario.getSenha())
            .authorities(mapToGrantedAuthorities(usuario))
            .accountLocked(usuario.getBloqueado())
            .disabled(!usuario.getAtivo())
            .build();
    }

    private Collection<? extends GrantedAuthority> mapToGrantedAuthorities(Usuario usuario) {
        return usuario.getRoles().stream()
            .flatMap(role -> 
                role.getPermissoes().stream()
                    .map(permissao -> new SimpleGrantedAuthority(permissao.getNome()))
            )
            .collect(Collectors.toSet());
    }
}
