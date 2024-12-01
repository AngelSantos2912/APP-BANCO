package pe.edu.cibertec.apibancocibertec.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CuentaDto {

    private int id;
    private String cuenta;
}
