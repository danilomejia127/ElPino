package eafit.catastro.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import eafit.catastro.modelo.PlantillaNotificacion;

public interface PlantillaNotificacionRepositorio extends JpaRepository<PlantillaNotificacion, Long> {

	PlantillaNotificacion findByTipo(String tipo);

}
