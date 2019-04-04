package br.gov.dataprev.caged.captacao.negocio.facade;

import br.gov.dataprev.caged.captacao.integracao.exception.IntegrationException;
import br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException;
import br.gov.dataprev.caged.captacao.negocio.to.AutorizadoCAGEDTO;
import br.gov.dataprev.caged.captacao.negocio.to.DeclaracaoCAGEDTO;
import br.gov.dataprev.caged.captacao.negocio.to.DeclaracaoEstabelecimentoCAGEDTO;
import br.gov.dataprev.caged.captacao.negocio.to.MovimentacaoCAGEDTO;
import br.gov.dataprev.caged.componente.analisador.integracao.service.ISDCAnalisadorCAGEDService;
import br.gov.dataprev.caged.componente.analisador.novalidacao.NoValidacao;
import br.gov.dataprev.caged.modelo.competencia.CompetenciaCAGED;
import br.gov.dataprev.caged.modelo.declaracao.estabelecimento.EnderecoCAGED;
import br.gov.dataprev.caged.modelo.declaracao.estabelecimento.Porte;
import br.gov.dataprev.caged.modelo.declaracao.movimentacao.TipoMovimento;
import br.gov.dataprev.caged.modelo.declaracao.movimentacao.trabalhador.Deficiencia;
import br.gov.dataprev.caged.modelo.declaracao.movimentacao.trabalhador.GrauInstrucao;
import br.gov.dataprev.caged.modelo.declaracao.movimentacao.trabalhador.Raca;
import br.gov.dataprev.caged.modelo.endereco.CEP;
import br.gov.dataprev.caged.modelo.endereco.UF;
import br.gov.dataprev.caged.modelo.estabelecimento.AtividadeEconomica;
import br.gov.dataprev.caged.modelo.vinculo.CBO;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

public abstract interface ICaptacaoCAGEDFacade
{
  public abstract EntityManager getEntityManager();
  
  public abstract void shutdown()
    throws IntegrationException;
  
  public abstract ISDCAnalisadorCAGEDService getSdc();
  
  public abstract DeclaracaoEstabelecimentoCAGEDTO converte(AutorizadoCAGEDTO paramAutorizadoCAGEDTO);
  
  public abstract AutorizadoCAGEDTO converte(DeclaracaoEstabelecimentoCAGEDTO paramDeclaracaoEstabelecimentoCAGEDTO);
  
  public abstract AtividadeEconomica obterAtividadeEconomica(AtividadeEconomica paramAtividadeEconomica)
    throws BusinessException;
  
  public abstract List obterListaAtividadeEconomica()
    throws BusinessException;
  
  public abstract List obterListaAtividadeEconomica(String paramString1, String paramString2)
    throws BusinessException;
  
  public abstract CBO obterCBO(CBO paramCBO)
    throws BusinessException;
  
  public abstract List obterListaCBO()
    throws BusinessException;
  
  public abstract List obterListaCBO(String paramString1, String paramString2)
    throws BusinessException;
  
  public abstract CEP cepEhValido(CEP paramCEP)
    throws BusinessException;
  
  public abstract boolean cepEhCompativel(CEP paramCEP, UF paramUF)
    throws BusinessException;
  
  public abstract List obterListaUF()
    throws BusinessException;
  
  public abstract EnderecoCAGED obterEndereco(CEP paramCEP)
    throws BusinessException;
  
  public abstract List obterListaDDD(UF paramUF)
    throws BusinessException;
  
  public abstract UF obterUF(UF paramUF)
    throws BusinessException;
  
  public abstract List obterListaGrauInstrucao()
    throws BusinessException;
  
  public abstract GrauInstrucao obterGrauInstrucao(GrauInstrucao paramGrauInstrucao)
    throws BusinessException;
  
  public abstract List obterListaRaca()
    throws BusinessException;
  
  public abstract Raca obterRaca(Raca paramRaca)
    throws BusinessException;
  
  public abstract List obterListaDeficiencia()
    throws BusinessException;
  
  public abstract Deficiencia obterDeficiencia(Deficiencia paramDeficiencia)
    throws BusinessException;
  
  public abstract List obterListaTipoMovimentacao()
    throws BusinessException;
  
  public abstract TipoMovimento obterTipoMovimentacao(TipoMovimento paramTipoMovimento)
    throws BusinessException;
  
  public abstract List obterListaCodigoTipoDesligamento()
    throws BusinessException;
  
  public abstract List obterListaTipoAdmissao()
    throws BusinessException;
  
  public abstract List obterListaTipoDesligamento()
    throws BusinessException;
  
  public abstract List obterListaPorte()
    throws BusinessException;
  
  public abstract Porte obterPorte(Porte paramPorte)
    throws BusinessException;
  
  public abstract List validarMovimentacaoCAGED(MovimentacaoCAGEDTO paramMovimentacaoCAGEDTO)
    throws BusinessException;
  
  public abstract MovimentacaoCAGEDTO cadastrarMovimentacao(MovimentacaoCAGEDTO paramMovimentacaoCAGEDTO, boolean paramBoolean)
    throws BusinessException;
  
  public abstract Boolean existeMovimentacao(Integer paramInteger1, String paramString1, String paramString2, Integer paramInteger2, Date paramDate, String paramString3)
    throws BusinessException;
  
  public abstract void removerMovimentacao(long paramLong)
    throws BusinessException;
  
  public abstract MovimentacaoCAGEDTO alterarMovimentacao(MovimentacaoCAGEDTO paramMovimentacaoCAGEDTO, boolean paramBoolean)
    throws BusinessException;
  
  public abstract MovimentacaoCAGEDTO migrarMovimentacao(MovimentacaoCAGEDTO paramMovimentacaoCAGEDTO, boolean paramBoolean, int paramInt)
    throws BusinessException;
  
  public abstract DeclaracaoCAGEDTO migrarMovimentacoesDeclaracaoEmACI(DeclaracaoCAGEDTO paramDeclaracaoCAGEDTO, int paramInt1, int paramInt2)
    throws BusinessException;
  
  public abstract DeclaracaoCAGEDTO unificarDeclaracaoEmACI(DeclaracaoCAGEDTO paramDeclaracaoCAGEDTO1, DeclaracaoCAGEDTO paramDeclaracaoCAGEDTO2, int paramInt1, int paramInt2)
    throws BusinessException;
  
  public abstract DeclaracaoCAGEDTO migrarMovimentacoesDeclaracao(DeclaracaoCAGEDTO paramDeclaracaoCAGEDTO, int paramInt1, int paramInt2)
    throws BusinessException;
  
  public abstract List validarAutorizado(AutorizadoCAGEDTO paramAutorizadoCAGEDTO)
    throws BusinessException;
  
  public abstract List validarEstabelecimento(DeclaracaoEstabelecimentoCAGEDTO paramDeclaracaoEstabelecimentoCAGEDTO)
    throws BusinessException;
  
  public abstract DeclaracaoEstabelecimentoCAGEDTO cadastrarEstabelecimento(DeclaracaoEstabelecimentoCAGEDTO paramDeclaracaoEstabelecimentoCAGEDTO)
    throws BusinessException;
  
  public abstract DeclaracaoEstabelecimentoCAGEDTO alterarEstabelecimento(DeclaracaoEstabelecimentoCAGEDTO paramDeclaracaoEstabelecimentoCAGEDTO)
    throws BusinessException;
  
  public abstract void excluirEstabelecimento(Long paramLong)
    throws BusinessException;
  
  public abstract void cadastrarAutorizado(AutorizadoCAGEDTO paramAutorizadoCAGEDTO, boolean paramBoolean)
    throws BusinessException;
  
  public abstract void alterarAutorizado(AutorizadoCAGEDTO paramAutorizadoCAGEDTO, boolean paramBoolean)
    throws BusinessException;
  
  public abstract AutorizadoCAGEDTO obterAutorizadoCAGEDTO()
    throws BusinessException;
  
  public abstract List listarDeclaracoesCAGED()
    throws BusinessException;
  
  public abstract DeclaracaoCAGEDTO abrirDeclaracaoCAGED(CompetenciaCAGED paramCompetenciaCAGED)
    throws BusinessException;
  
  public abstract DeclaracaoCAGEDTO buscarDeclaracaoNaCompetencia(CompetenciaCAGED paramCompetenciaCAGED)
    throws BusinessException;
  
  public abstract DeclaracaoCAGEDTO cadastrarNovaDeclaracaoCAGED(DeclaracaoCAGEDTO paramDeclaracaoCAGEDTO)
    throws BusinessException;
  
  public abstract void excluirDeclaracaoCAGED(DeclaracaoCAGEDTO paramDeclaracaoCAGEDTO)
    throws BusinessException;
  
  public abstract NoValidacao validarDeclaracaoCAGED(DeclaracaoCAGEDTO paramDeclaracaoCAGEDTO)
    throws BusinessException;
  
  public abstract void gerarArquivoDeclaracao(Long paramLong, String paramString)
    throws BusinessException;
  
  public abstract void gerarArquivoDeclaracao(DeclaracaoCAGEDTO paramDeclaracaoCAGEDTO, String paramString)
    throws BusinessException;
  
  public abstract void gerarArquivoPosicional(Long paramLong, int paramInt, String paramString)
    throws BusinessException;
  
  public abstract void gerarArquivoPosicional(DeclaracaoCAGEDTO paramDeclaracaoCAGEDTO, int paramInt, String paramString)
    throws BusinessException;
  
  public abstract DeclaracaoCAGEDTO importarDeclaracao(DeclaracaoCAGEDTO paramDeclaracaoCAGEDTO, int paramInt)
    throws BusinessException;
  
  public abstract List obterListaEstabelecimentos(String paramString1, int paramInt, String paramString2)
    throws BusinessException;
  
  public abstract List obterListaEstabelecimentos(DeclaracaoCAGEDTO paramDeclaracaoCAGEDTO, String paramString1, int paramInt, String paramString2)
    throws BusinessException;
  
  public abstract void transmitirArquivoDeclaracao(String paramString1, String paramString2, String paramString3, CompetenciaCAGED paramCompetenciaCAGED)
    throws BusinessException;
  
  public abstract DeclaracaoCAGEDTO alteraDeclaracaoCAGED(DeclaracaoCAGEDTO paramDeclaracaoCAGEDTO, boolean paramBoolean)
    throws BusinessException;
  
  public abstract List obterListaCompetencias()
    throws BusinessException;
  
  public abstract List listarMovimentacoes(CompetenciaCAGED paramCompetenciaCAGED, String paramString, TipoMovimento paramTipoMovimento)
    throws BusinessException;
  
  public abstract Boolean existeAcerto(Integer paramInteger1, String paramString1, String paramString2, Integer paramInteger2, String paramString3, String paramString4, Date paramDate, String paramString5)
    throws BusinessException;
  
  public abstract List listarAcertos(CompetenciaCAGED paramCompetenciaCAGED, String paramString)
    throws BusinessException;
  
  public abstract List listarEstabelecimentos(CompetenciaCAGED paramCompetenciaCAGED, int paramInt)
    throws BusinessException;
  
  public abstract List listarMovimentacoesPisZerados(CompetenciaCAGED paramCompetenciaCAGED, String paramString)
    throws BusinessException;
  
  public abstract List listarMovimentacoesPisZerados(DeclaracaoCAGEDTO paramDeclaracaoCAGEDTO)
    throws BusinessException;
  
  public abstract DeclaracaoCAGEDTO abrirDeclaracaoCAGEDXML(String paramString)
    throws BusinessException;
  
  public abstract double obterUltimoSalarioMinimo();
  
  public abstract void gerarArquivoPosicionalSemAnalise(DeclaracaoCAGEDTO paramDeclaracaoCAGEDTO, int paramInt, String paramString)
    throws BusinessException;
}
