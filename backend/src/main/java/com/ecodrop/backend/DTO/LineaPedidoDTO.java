package com.ecodrop.backend.DTO;

public class LineaPedidoDTO {
    private Long idLineaPedido;
    private Integer cantidad;
    private Double precioVenta;
    private Long idPedido;
    private Long idProducto;

    public LineaPedidoDTO() {}

    public LineaPedidoDTO(Long idLineaPedido, Integer cantidad, Double precioVenta, Long idPedido, Long idProducto) {
        this.idLineaPedido = idLineaPedido;
        this.cantidad = cantidad;
        this.precioVenta = precioVenta;
        this.idPedido = idPedido;
        this.idProducto = idProducto;
    }

    public Long getIdLineaPedido() { return idLineaPedido; }
    public void setIdLineaPedido(Long idLineaPedido) { this.idLineaPedido = idLineaPedido; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public Double getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(Double precioVenta) { this.precioVenta = precioVenta; }
    public Long getIdPedido() { return idPedido; }
    public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }
    public Long getIdProducto() { return idProducto; }
    public void setIdProducto(Long idProducto) { this.idProducto = idProducto; }
}
