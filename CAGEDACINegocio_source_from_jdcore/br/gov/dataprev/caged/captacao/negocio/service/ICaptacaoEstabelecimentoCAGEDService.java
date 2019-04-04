package br.gov.dataprev.caged.captacao.negocio.service;

import br.gov.dataprev.caged.captacao.integracao.exception.IntegrationException;
import br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoCAGED;
import br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoEstabelecimentoCAGED;
import br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException;
import br.gov.dataprev.caged.modelo.competencia.CompetenciaCAGED;
import java.util.List;

public abstract interface ICaptacaoEstabelecimentoCAGEDService
{
  public abstract List obterListaEstabelecimentos(DeclaracaoCAGED paramDeclaracaoCAGED, String paramString1, int paramInt, String paramString2)
    throws BusinessException;
  
  public abstract List obterListaEstabelecimentos(String paramString1, int paramInt, String paramString2)
    throws BusinessException;
  
  public abstract DeclaracaoEstabelecimentoCAGED cadastrarDeclaracaoEstabelecimento(DeclaracaoEstabelecimentoCAGED paramDeclaracaoEstabelecimentoCAGED)
    throws BusinessException;
  
  public abstract void removerDeclaracaoEstabelecimento(Long paramLong)
    throws BusinessException;
  
  public abstract DeclaracaoEstabelecimentoCAGED alterarEstabelecimento(DeclaracaoEstabelecimentoCAGED paramDeclaracaoEstabelecimentoCAGED)
    throws BusinessException;
  
  public abstract List analisarEstabelecimentos(DeclaracaoEstabelecimentoCAGED paramDeclaracaoEstabelecimentoCAGED)
    throws BusinessException;
  
  public abstract DeclaracaoEstabelecimentoCAGED obterEstabelecimento(Long paramLong)
    throws BusinessException;
  
  public abstract List listarEstabelecimentos(CompetenciaCAGED paramCompetenciaCAGED, int paramInt)
    throws IntegrationException;
}
