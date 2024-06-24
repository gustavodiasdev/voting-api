package com.example.votingapi.controller;

import com.example.votingapi.model.Pauta;
import com.example.votingapi.model.Sessao;
import com.example.votingapi.model.Voto;
import com.example.votingapi.service.VotingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/votacao")
public class VotingController {

    @Autowired
    private VotingService votingService;

    @PostMapping("/pautas")
    public Pauta criarPauta(@RequestBody String descricao) {
        return votingService.criarPauta(descricao);
    }

    @PostMapping("/pautas/{pautaId}/sessao")
    public Sessao abrirSessao(@PathVariable Long pautaId, @RequestParam(required = false) LocalDateTime dataFim) {
        return votingService.abrirSessao(pautaId, dataFim);
    }

    @PostMapping("/pautas/{pautaId}/votos")
    public Voto votar(@PathVariable Long pautaId, @RequestParam Long associadoId, @RequestParam boolean voto, @RequestParam String cpf) {
        return votingService.votar(pautaId, associadoId, voto, cpf);
    }

    @GetMapping("/pautas/{pautaId}/resultado")
    public String resultadoVotacao(@PathVariable Long pautaId) {
        return votingService.resultadoVotacao(pautaId);
    }
}
