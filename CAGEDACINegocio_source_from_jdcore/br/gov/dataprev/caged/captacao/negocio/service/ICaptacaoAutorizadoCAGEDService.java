package br.gov.dataprev.caged.captacao.negocio.service;

import br.gov.dataprev.caged.captacao.modelo.autorizado.AutorizadoCAGED;
import br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException;
import java.util.List;

public abstract interface ICaptacaoAutorizadoCAGEDService
{
  public abstract void cadastrarAutorizado(AutorizadoCAGED paramAutorizadoCAGED, boolean paramBoolean)
    throws BusinessException;
  
  public abstract void alterarAutorizado(AutorizadoCAGED paramAutorizadoCAGED, boolean paramBoolean)
    throws BusinessException;
  
  public abstract List analisarAutorizado(AutorizadoCAGED paramAutorizadoCAGED)
    throws BusinessException;
  
  public abstract AutorizadoCAGED obterAutorizado(long paramLong)
    throws BusinessException;
}
