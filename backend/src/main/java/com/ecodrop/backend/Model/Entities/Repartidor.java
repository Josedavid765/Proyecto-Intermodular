package com.ecodrop.backend.Model.Entities;

import com.ecodrop.backend.Model.Enum.EstadoRepartidor;
import com.ecodrop.backend.Model.Enum.Vehiculo;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.persistence.CascadeType;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "repartidor")
public class Repartidor {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRepartidor;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellidos;

    @NotBlank(message = "El telefono es obligatorio")
    @Pattern(regexp = "^[0-9]{9}$", message = "El telefono debe tener 9 digitos")
    private String telefono;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Vehiculo vehiculo;

    private Boolean disponibilidad;

    @Enumerated(EnumType.STRING)
    @NotNull
    private EstadoRepartidor estado;

    @OneToMany(mappedBy = "repartidor", cascade = CascadeType.ALL)
    private List<Pedido> pedidos;

    @OneToOne
    private Usuario usuario;
}
