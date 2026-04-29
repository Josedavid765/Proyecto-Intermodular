package com.ecodrop.backend.DTO;

import com.ecodrop.backend.Model.Enum.EstadoRepartidor;
import com.ecodrop.backend.Model.Enum.Vehiculo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class RepartidorDTO {
    private Long idRepartidor;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellidos;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^[0-9]{9}$", message = "El teléfono debe tener 9 dígitos")
    private String telefono;

    @NotNull(message = "El tipo de vehículo es obligatorio")
    private Vehiculo vehiculo;

    @NotNull(message = "El estado de disponibilidad es obligatorio")
    private EstadoRepartidor estado;

    public RepartidorDTO() {}

    public RepartidorDTO(Long idRepartidor, String nombre, String apellidos, String telefono, Vehiculo vehiculo, EstadoRepartidor estado) {
        this.idRepartidor = idRepartidor;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.vehiculo = vehiculo;
        this.estado = estado;
    }

    public Long getIdRepartidor() { return idRepartidor; }
    public void setIdRepartidor(Long idRepartidor) { this.idRepartidor = idRepartidor; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public Vehiculo getVehiculo() { return vehiculo; }
    public void setVehiculo(Vehiculo vehiculo) { this.vehiculo = vehiculo; }
    public EstadoRepartidor getEstado() { return estado; }
    public void setEstado(EstadoRepartidor estado) { this.estado = estado; }
}
