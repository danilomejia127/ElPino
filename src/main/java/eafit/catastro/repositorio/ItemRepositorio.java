package eafit.catastro.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import eafit.catastro.modelo.Item;

public interface ItemRepositorio extends JpaRepository<Item, Long> {

}
