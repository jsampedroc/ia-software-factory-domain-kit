package com.application.application.dto;

import java.io.Serializable;

public record SpecialtyDTO(
    String id,
    String codigo,
    String nombre,
    String descripcion
) implements Serializable {
}