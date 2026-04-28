package com.ecodrop.backend.Model.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.CascadeType;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comercio_local")
public class ComercioLocal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idcomercio;

    @NotBlank(message = "El nombre del comercio es obligatorios")
    @Size(max = 100)
    private String nombreComercio;

    @NotBlank(message = "La categoria es obligatoria")
    private String categoria;

    @NotBlank(message = "La direccion es obligatoria")
    private String direccionComercio;

    @NotBlank(message = "El telefono es obligatorio")
    @Pattern(regexp = "^[0-9]{9}$", message = "El telefono debe tener 9 digitos")
    private String telefono;

    @NotBlank(message = "El horario de apertura es obligatorio")
    private String horarioApertura;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "comercio", cascade = CascadeType.ALL)
    private List<Pedido> pedidos;

    @OneToMany(mappedBy = "comercio", cascade = CascadeType.ALL)
    private List<Producto> productos;
}
