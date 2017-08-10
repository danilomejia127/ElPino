package eafit.catastro.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import eafit.catastro.modelo.Orden;

public interface OrdenRepositorio extends JpaRepository<Orden, Long> {

	@Modifying
	@Query("UPDATE Orden o SET o.valorTotal = (SELECT SUM(i.valorTotal) FROM Item i WHERE i.orden.id = o.id) WHERE o.id = :ordenId")
	void recalcularValorTotal(@Param("ordenId") long ordenId);

}
