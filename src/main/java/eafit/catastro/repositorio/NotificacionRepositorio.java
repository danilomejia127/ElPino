package eafit.catastro.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import eafit.catastro.modelo.Notificacion;

public interface NotificacionRepositorio extends JpaRepository<Notificacion, Long> {

}
