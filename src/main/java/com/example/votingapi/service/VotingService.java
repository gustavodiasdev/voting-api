package com.example.votingapi.service;


import com.example.votingapi.model.Pauta;
import com.example.votingapi.model.Sessao;
import com.example.votingapi.model.Voto;
import com.example.votingapi.repository.PautaRepository;
import com.example.votingapi.repository.SessaoRepository;
import com.example.votingapi.repository.VotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VotingService {

    @Autowired
    private PautaRepository pautaRepository;

    @Autowired
    private SessaoRepository sessaoRepository;

    @Autowired
    private VotoRepository votoRepository;

    @Autowired
    private CpfValidationService cpfValidationService;

    public Pauta criarPauta(String descricao) {
        Pauta pauta = new Pauta();
        pauta.setDescricao(descricao);
        return pautaRepository.save(pauta);
    }

    public Sessao abrirSessao(Long pautaId, LocalDateTime dataFim) {
        Pauta pauta = pautaRepository.findById(pautaId).orElseThrow(() -> new RuntimeException("Pauta não encontrada"));
        Sessao sessao = new Sessao();
        sessao.setPauta(pauta);
        sessao.setAberta(true);
        sessao.setDataInicio(LocalDateTime.now());
        sessao.setDataFim(dataFim != null ? dataFim : LocalDateTime.now().plusMinutes(1));
        return sessaoRepository.save(sessao);
    }

    public Voto votar(Long pautaId, Long associadoId, boolean voto, String cpf) {
        Pauta pauta = pautaRepository.findById(pautaId)
                .orElseThrow(() -> new RuntimeException("Pauta não encontrada"));
        if (votoRepository.existsByPautaAndAssociadoId(pauta, associadoId)) {
            throw new RuntimeException("Associado já votou nesta pauta");
        }
        if (!cpfValidationService.podeVotar(cpf)) {
            throw new RuntimeException("Associado não está autorizado a votar");
        }
        Voto novoVoto = new Voto();
        novoVoto.setPauta(pauta);
        novoVoto.setAssociadoId(associadoId);
        novoVoto.setVoto(voto);
        return votoRepository.save(novoVoto);
    }

    public String resultadoVotacao(Long pautaId) {
        Pauta pauta = pautaRepository.findById(pautaId).orElseThrow(() -> new RuntimeException("Pauta não encontrada"));
        long votosSim = votoRepository.countByPautaAndVoto(pauta, true);
        long votosNao = votoRepository.countByPautaAndVoto(pauta, false);
        return "Resultado: Sim = " + votosSim + ", Não = " + votosNao;
    }
}
