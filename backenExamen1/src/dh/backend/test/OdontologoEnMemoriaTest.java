package dh.backend.test;
import dh.backend.dao.impl.OdontologoDaoH2;
import dh.backend.dao.impl.OdontologoEnMemoria;
import dh.backend.model.Odontologo;
import dh.backend.service.OdontologoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OdontologoEnMemoriaTest {
    private static OdontologoService odontologoService = new OdontologoService(new OdontologoEnMemoria());

    @Test
    @DisplayName("Testear busqueda todos los odontologos")
    void testBusquedaTodos() {

        Odontologo odontologo = new Odontologo(41723, "Pepito", "Perez");
        odontologoService.registrarOdontologo(odontologo);

        List<Odontologo> odontologos = odontologoService.buscarTodosOdontologos();

        assertEquals(1, odontologos.size());

    }

}
