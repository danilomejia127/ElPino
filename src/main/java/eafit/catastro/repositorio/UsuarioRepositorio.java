package eafit.catastro.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import eafit.catastro.modelo.Usuario;

public interface UsuarioRepositorio extends JpaRepository<Usuario, String> {

	@Query("SELECT u FROM Usuario u WHERE u.tipoDocumento = :tipoDocumento AND u.numeroDocumento = :numeroDocumento")
	Usuario buscarPorTipoDocumento(@Param("tipoDocumento") String tipoDocumento, @Param("numeroDocumento") String numeroDocumento);

}
