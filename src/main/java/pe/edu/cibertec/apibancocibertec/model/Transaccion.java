package pe.edu.cibertec.apibancocibertec.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@Table(name = "transaccion")
public class Transaccion {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;
    private Integer cuentaorigenid;
    private Integer cuentadestinoid;
    private Double monto;
    private LocalDateTime fecha;
}