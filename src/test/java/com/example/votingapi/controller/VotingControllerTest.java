package com.example.votingapi.controller;

import com.example.votingapi.controller.VotingController;
import com.example.votingapi.model.Pauta;
import com.example.votingapi.model.Sessao;
import com.example.votingapi.model.Voto;
import com.example.votingapi.service.CpfValidationService;
import com.example.votingapi.service.VotingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
public class VotingControllerTest {

    @Mock
    private VotingService votingService;

    @Mock
    private CpfValidationService cpfValidationService;

    @InjectMocks
    private VotingController votingController;

    private MockMvc mockMvc;

    private Pauta pauta;
    private Sessao sessao;
    private Voto voto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(votingController).build();

        pauta = new Pauta();
        pauta.setId(1L);
        pauta.setDescricao("Descrição da Pauta");

        sessao = new Sessao();
        sessao.setId(1L);
        sessao.setPauta(pauta);
        sessao.setAberta(true);
        sessao.setDataInicio(LocalDateTime.now());
        sessao.setDataFim(LocalDateTime.now().plusMinutes(1));

        voto = new Voto();
        voto.setId(1L);
        voto.setPauta(pauta);
        voto.setAssociadoId(1L);
        voto.setVoto(true);
    }

    @Test
    public void testCriarPauta() throws Exception {
        when(votingService.criarPauta(anyString())).thenReturn(pauta);

        mockMvc.perform(post("/api/v1/votacao/pautas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"descricao\": \"Descrição da Pauta\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Descrição da Pauta"));

        verify(votingService, times(1)).criarPauta(anyString());
    }

    @Test
    public void testAbrirSessao() throws Exception {
        when(votingService.abrirSessao(anyLong(), any())).thenReturn(sessao);

        mockMvc.perform(post("/api/v1/votacao/pautas/1/sessao")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.aberta").value(true));

        verify(votingService, times(1)).abrirSessao(anyLong(), any());
    }

//    @Test
    public void testVotar() throws Exception {
        when(votingService.votar(anyLong(), anyLong(), anyBoolean(), anyString())).thenReturn(voto);
        when(cpfValidationService.podeVotar(anyString())).thenReturn(true);

        mockMvc.perform(post("/api/v1/votacao/pautas/1/votos")
                        .param("associadoId", "1")
                        .param("voto", "true")
                        .param("cpf", "12345678901"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.voto").value(true));

        verify(votingService, times(1)).votar(anyLong(), anyLong(), anyBoolean(), anyString());
        verify(cpfValidationService, times(1)).podeVotar(anyString());
    }

    @Test
    public void testResultadoVotacao() throws Exception {
        when(votingService.resultadoVotacao(anyLong())).thenReturn("Resultado: Sim = 10, Não = 5");

        mockMvc.perform(get("/api/v1/votacao/pautas/1/resultado")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Resultado: Sim = 10, Não = 5"));

        verify(votingService, times(1)).resultadoVotacao(anyLong());
    }
}
