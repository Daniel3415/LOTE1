package br.gov.dataprev.caged.captacao.negocio.service;

import br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoCAGED;
import br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException;
import br.gov.dataprev.caged.componente.analisador.novalidacao.NoValidacao;
import br.gov.dataprev.caged.modelo.competencia.CompetenciaCAGED;
import java.util.List;

public abstract interface ICaptacaoDeclaracaoCAGEDService
{
  public abstract List listarDeclaracoesCAGED()
    throws BusinessException;
  
  public abstract DeclaracaoCAGED abrirDeclaracaoCAGED(CompetenciaCAGED paramCompetenciaCAGED)
    throws BusinessException;
  
  public abstract DeclaracaoCAGED cadastrarNovaDeclaracaoCAGED(DeclaracaoCAGED paramDeclaracaoCAGED)
    throws BusinessException;
  
  public abstract void excluirDeclaracaoCAGED(DeclaracaoCAGED paramDeclaracaoCAGED)
    throws BusinessException;
  
  public abstract DeclaracaoCAGED alterarDeclaracaoCAGED(DeclaracaoCAGED paramDeclaracaoCAGED, boolean paramBoolean)
    throws BusinessException;
  
  public abstract NoValidacao analisarDeclaracaoCAGED(DeclaracaoCAGED paramDeclaracaoCAGED)
    throws BusinessException;
  
  public abstract void gerarArquivoDeclaracao(Long paramLong, String paramString)
    throws BusinessException;
  
  public abstract void gerarArquivoDeclaracao(DeclaracaoCAGED paramDeclaracaoCAGED, String paramString)
    throws BusinessException;
  
  public abstract void gerarArquivoPosicional(Long paramLong, int paramInt, String paramString)
    throws BusinessException;
  
  public abstract void gerarArquivoPosicional(DeclaracaoCAGED paramDeclaracaoCAGED, int paramInt, String paramString)
    throws BusinessException;
  
  public abstract DeclaracaoCAGED importarDeclaracao(DeclaracaoCAGED paramDeclaracaoCAGED, int paramInt)
    throws BusinessException;
  
  public abstract List obterListaCompetencias()
    throws BusinessException;
  
  public abstract DeclaracaoCAGED abrirDeclaracaoCAGEDXML(String paramString)
    throws BusinessException;
  
  public abstract DeclaracaoCAGED unificarDeclaracoes(DeclaracaoCAGED paramDeclaracaoCAGED1, DeclaracaoCAGED paramDeclaracaoCAGED2)
    throws BusinessException;
  
  public abstract void gerarArquivoPosicionalSemAnalise(DeclaracaoCAGED paramDeclaracaoCAGED, int paramInt, String paramString)
    throws BusinessException;
}
