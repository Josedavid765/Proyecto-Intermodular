package com.ecodrop.backend.Security;

import com.ecodrop.backend.Model.Entities.ComercioLocal;
import com.ecodrop.backend.Model.Entities.Repartidor;
import com.ecodrop.backend.Repository.ComercioLocalRepository;
import com.ecodrop.backend.Repository.RepartidorRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final ComercioLocalRepository comercioRepository;
    private final RepartidorRepository repartidorRepository;

    public UserDetailsServiceImpl(ComercioLocalRepository comercioRepository, RepartidorRepository repartidorRepository) {
        this.comercioRepository = comercioRepository;
        this.repartidorRepository = repartidorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<ComercioLocal> comercio = comercioRepository.findByEmail(email);
        if (comercio.isPresent()) {
            ComercioLocal c = comercio.get();
            return User.builder()
                    .username(c.getEmail())
                    .password(c.getPassword())
                    .authorities(List.of(new SimpleGrantedAuthority(c.getRol().name())))
                    .build();
        }

        Optional<Repartidor> repartidor = repartidorRepository.findByEmail(email);
        if (repartidor.isPresent()) {
            Repartidor r = repartidor.get();
            return User.builder()
                    .username(r.getEmail())
                    .password(r.getPassword())
                    .authorities(List.of(new SimpleGrantedAuthority(r.getRol().name())))
                    .build();
        }

        throw new UsernameNotFoundException("No existe cuenta con email: " + email);
    }
}
