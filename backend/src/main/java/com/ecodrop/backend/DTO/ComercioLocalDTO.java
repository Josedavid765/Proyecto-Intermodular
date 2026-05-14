package com.ecodrop.backend.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ComercioLocalDTO {
    private Long idComercio;

    @NotBlank(message = "El nombre del comercio es obligatorio")
    @Size(max = 100)
    private String nombreComercio;

    @NotBlank(message = "La categoria es obligatoria")
    private String categoria;

    @NotBlank(message = "La direccion es obligatoria")
    private String direccionComercio;

    private String logo;

    @NotBlank(message = "El telefono es obligatorio")
    @Pattern(regexp = "^[0-9]{9}$", message = "El telefono debe tener 9 digitos")
    private String telefono;

    @NotBlank(message = "El horario de apertura es obligatorio")
    private String horarioApertura;

    private String email;

    public ComercioLocalDTO() {}

    public Long getIdComercio() { return idComercio; }
    public void setIdComercio(Long idComercio) { this.idComercio = idComercio; }
    public String getNombreComercio() { return nombreComercio; }
    public void setNombreComercio(String nombreComercio) { this.nombreComercio = nombreComercio; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getDireccionComercio() { return direccionComercio; }
    public void setDireccionComercio(String direccionComercio) { this.direccionComercio = direccionComercio; }
    public String getLogo() { return logo; }
    public void setLogo(String logo) { this.logo = logo; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getHorarioApertura() { return horarioApertura; }
    public void setHorarioApertura(String horarioApertura) { this.horarioApertura = horarioApertura; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
