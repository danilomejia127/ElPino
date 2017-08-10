package eafit.catastro.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import eafit.catastro.modelo.Predio;

public interface PredioRepositorio extends JpaRepository<Predio, String> {

	@Query("SELECT p FROM Predio p WHERE p.tipoDocUsuario = :tipoDocUsuario AND p.idPropietario = :idPropietario")
	List <Predio> buscarPredioPorPropietario(@Param("tipoDocUsuario") String tipoDocUsuario, @Param("idPropietario") String idPropietario);

}
