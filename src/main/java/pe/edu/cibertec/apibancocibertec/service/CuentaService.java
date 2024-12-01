package pe.edu.cibertec.apibancocibertec.service;

import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.apibancocibertec.dto.CuentaDto;
import pe.edu.cibertec.apibancocibertec.dto.TransferenciaDto;
import pe.edu.cibertec.apibancocibertec.model.Cuenta;
import pe.edu.cibertec.apibancocibertec.model.Transaccion;
import pe.edu.cibertec.apibancocibertec.repository.CuentaRepository;
import pe.edu.cibertec.apibancocibertec.repository.TransaccionRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class CuentaService implements ICuentaService{

    private final CuentaRepository cuentaRepository;
    private final TransaccionRepository transaccionRepository;


    @Override
    public List<CuentaDto> listarCuentas() {
        List<Cuenta> cuentas = cuentaRepository.findAll();
        List<CuentaDto> cuentasDto = new ArrayList<>();
        for (Cuenta cuenta : cuentas) {
            cuentasDto.add(CuentaDto.builder()
                    .id(cuenta.getId())
                    .cuenta(cuenta.getNombre())
                    .build());
        }

        return cuentasDto;
    }

    @Override
    @Transactional
    public void transferir(TransferenciaDto transferenciaDto) {

        Cuenta cuentaOrigen = cuentaRepository
                .findById(transferenciaDto
                        .getCuentaorigenid())
                .orElseThrow(() -> new RuntimeException("Cuenta de origen no existe"));

        Cuenta cuentaDestino = cuentaRepository
                .findById(transferenciaDto
                        .getCuentadestinoid())
                .orElseThrow(() -> new RuntimeException("Cuenta de destino no existe"));

        if(cuentaOrigen.getSaldo()<transferenciaDto.getMonto()){
            throw new RuntimeException("Saldo insuficiente");
        }

        cuentaOrigen.setSaldo(cuentaOrigen.getSaldo()
                - transferenciaDto.getMonto());
        cuentaDestino.setSaldo(cuentaDestino.getSaldo()
                + transferenciaDto.getMonto());

        Transaccion nuevatransaccion = new Transaccion();
        nuevatransaccion.setCuentaorigenid(cuentaOrigen.getId());
        nuevatransaccion.setCuentadestinoid(cuentaDestino.getId());
        nuevatransaccion.setMonto(transferenciaDto.getMonto());
        nuevatransaccion.setFecha(LocalDateTime.now());
        transaccionRepository.save(nuevatransaccion);
        cuentaRepository.save(cuentaOrigen);
        cuentaRepository.save(cuentaDestino);

    }
}