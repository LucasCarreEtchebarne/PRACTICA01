
package com.PRACTICA01.demo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "arbol")
public class Arbol implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_arbol")
    private Integer idArbol;

    @NotBlank
    @Size(max = 60)
    @Column(name = "nombre_comun", nullable = false, length = 60)
    private String nombreComun;

    @NotBlank
    @Size(max = 60)
    @Column(name = "tipo_flor", nullable = false, length = 60)
    private String tipoFlor;

    @NotNull
    @Min(0)
    @Column(name = "dureza_madera", nullable = false)
    private Integer durezaMadera;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = true)
    @Column(name = "altura_metros", nullable = false, precision = 6, scale = 2)
    private BigDecimal alturaMetros;

    @Size(max = 1024)
    @Column(name = "ruta_imagen", length = 1024)
    private String rutaImagen;

    @Column(name = "activo")
    private boolean activo = true;

    @Column(name = "fecha_creacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion", insertable = false, updatable = false)
    private LocalDateTime fechaModificacion;
}
