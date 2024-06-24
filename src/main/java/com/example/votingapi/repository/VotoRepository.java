package com.example.votingapi.repository;

import com.example.votingapi.model.Pauta;
import com.example.votingapi.model.Voto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotoRepository extends JpaRepository<Voto, Long> {
    boolean existsByPautaAndAssociadoId(Pauta pauta, Long associadoId);
    long countByPautaAndVoto(Pauta pauta, boolean voto);
}
