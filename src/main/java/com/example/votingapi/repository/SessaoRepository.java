package com.example.votingapi.repository;

import com.example.votingapi.model.Sessao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessaoRepository extends JpaRepository<Sessao, Long> {}