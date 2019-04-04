package br.gov.dataprev.caged.captacao.negocio.service;

import br.gov.dataprev.caged.captacao.integracao.exception.IntegrationException;
import br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoCAGED;
import br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException;
import br.gov.dataprev.caged.modelo.captacao.declaracao.movimentacao.Movimentacao;
import br.gov.dataprev.caged.modelo.competencia.CompetenciaCAGED;
import br.gov.dataprev.caged.modelo.declaracao.movimentacao.TipoMovimento;
import java.util.Date;
import java.util.List;

public abstract interface ICaptacaoMovimentacaoCAGEDService
{
  public abstract Movimentacao cadastrarMovimentacao(Movimentacao paramMovimentacao, boolean paramBoolean)
    throws BusinessException;
  
  public abstract void removerMovimentacao(Long paramLong)
    throws BusinessException;
  
  public abstract Movimentacao alterarMovimentacao(Movimentacao paramMovimentacao, boolean paramBoolean)
    throws BusinessException;
  
  public abstract List analisarMovimentacao(Movimentacao paramMovimentacao)
    throws BusinessException;
  
  public abstract Movimentacao migrarMovimentacao(Movimentacao paramMovimentacao, boolean paramBoolean, int paramInt)
    throws BusinessException;
  
  public abstract DeclaracaoCAGED migrarMovimentacoesDeclaracaoEmACI(DeclaracaoCAGED paramDeclaracaoCAGED, int paramInt1, int paramInt2)
    throws BusinessException;
  
  public abstract DeclaracaoCAGED unificarDeclaracaoEmACI(DeclaracaoCAGED paramDeclaracaoCAGED1, DeclaracaoCAGED paramDeclaracaoCAGED2, int paramInt1, int paramInt2)
    throws BusinessException;
  
  public abstract DeclaracaoCAGED migrarMovimentacoesDeclaracao(DeclaracaoCAGED paramDeclaracaoCAGED, int paramInt1, int paramInt2, List paramList)
    throws BusinessException;
  
  public abstract List listarMovimentacoes(CompetenciaCAGED paramCompetenciaCAGED, String paramString, TipoMovimento paramTipoMovimento)
    throws IntegrationException;
  
  public abstract List listarMovimentacoesAcertos(CompetenciaCAGED paramCompetenciaCAGED, String paramString)
    throws IntegrationException;
  
  public abstract List listarMovimentacoesPisZerados(CompetenciaCAGED paramCompetenciaCAGED, String paramString)
    throws IntegrationException;
  
  public abstract List listarMovimentacoesPisZerados(DeclaracaoCAGED paramDeclaracaoCAGED)
    throws IntegrationException;
  
  public abstract Boolean existeMovimentacao(Integer paramInteger1, String paramString1, String paramString2, Integer paramInteger2, Date paramDate, String paramString3)
    throws BusinessException;
  
  public abstract Boolean existeAcerto(Integer paramInteger1, String paramString1, String paramString2, Integer paramInteger2, String paramString3, String paramString4, Date paramDate, String paramString5)
    throws BusinessException;
}
