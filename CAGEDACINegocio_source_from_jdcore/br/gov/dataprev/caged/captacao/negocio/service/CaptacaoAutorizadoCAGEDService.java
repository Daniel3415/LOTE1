package br.gov.dataprev.caged.captacao.negocio.service;

import br.gov.dataprev.caged.captacao.integracao.dao.IAutorizadoCAGEDDAO;
import br.gov.dataprev.caged.captacao.integracao.desktop.dao.AutorizadoCAGEDJPADAO;
import br.gov.dataprev.caged.captacao.integracao.desktop.service.SDCCaptacaoCAGEDServiceDesktop;
import br.gov.dataprev.caged.captacao.integracao.exception.IntegrationException;
import br.gov.dataprev.caged.captacao.integracao.util.Conversor;
import br.gov.dataprev.caged.captacao.modelo.autorizado.AutorizadoCAGED;
import br.gov.dataprev.caged.componente.analisador.integracao.service.ISDCAnalisadorCAGEDService;
import br.gov.dataprev.caged.componente.analisador.regras.RegrasAutorizadoCAGED;
import br.gov.dataprev.caged.componente.analisador.regras.mensagem.AlertaValidacao;
import br.gov.dataprev.caged.componente.analisador.regras.mensagem.ErroValidacao;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;





public class CaptacaoAutorizadoCAGEDService
  implements ICaptacaoAutorizadoCAGEDService
{
  private EntityManager entityManager;
  IAutorizadoCAGEDDAO autorizadoCAGEDDAO;
  ISDCAnalisadorCAGEDService sdcService;
  
  public CaptacaoAutorizadoCAGEDService(EntityManager em)
  {
    entityManager = em;
    
    autorizadoCAGEDDAO = new AutorizadoCAGEDJPADAO(entityManager);
    sdcService = new SDCCaptacaoCAGEDServiceDesktop(entityManager);
  }
  
  public void cadastrarAutorizado(AutorizadoCAGED autorizado, boolean ehPersistivel) throws br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException {
    List listaMensagens = analisarAutorizado(autorizado);
    try
    {
      if (listaMensagens.size() == 0) {
        if (ehPersistivel) {
          autorizadoCAGEDDAO.gravar(autorizado);
        }
      }
      else
      {
        Iterator it = listaMensagens.iterator();
        
        while (it.hasNext())
        {
          Object mensagem = it.next();
          if (mensagem.getClass() != AlertaValidacao.class) {
            throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(((ErroValidacao)mensagem).getDescricao());
          }
        }
        
        if (ehPersistivel) {
          autorizadoCAGEDDAO.gravar(autorizado);
        }
      }
    }
    catch (IntegrationException e)
    {
      throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage());
    }
  }
  



  public void alterarAutorizado(AutorizadoCAGED autorizado, boolean ehPersistivel)
    throws br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException
  {
    List listaMensagens = analisarAutorizado(autorizado);
    try
    {
      if (listaMensagens.size() == 0) {
        if (ehPersistivel) {
          autorizadoCAGEDDAO.alterar(autorizado);
        }
      }
      else {
        Iterator it = listaMensagens.iterator();
        
        while (it.hasNext())
        {
          Object mensagem = it.next();
          if (mensagem.getClass() != AlertaValidacao.class) {
            throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(((ErroValidacao)mensagem).getDescricao());
          }
        }
        
        if (ehPersistivel) {
          autorizadoCAGEDDAO.alterar(autorizado);
        }
      }
    }
    catch (IntegrationException e) {
      throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage());
    }
  }
  
  public List analisarAutorizado(AutorizadoCAGED autorizado) throws br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException
  {
    RegrasAutorizadoCAGED regrasAutorizadoCAGED;
    try {
      regrasAutorizadoCAGED = new RegrasAutorizadoCAGED(sdcService);
    } catch (br.gov.dataprev.caged.componente.analisador.regras.exception.BusinessException e) {
      throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage());
    }
    
    regrasAutorizadoCAGED.setAutorizado(Conversor.converter(autorizado));
    regrasAutorizadoCAGED.validar();
    
    return regrasAutorizadoCAGED.getMensagens();
  }
  
  public AutorizadoCAGED obterAutorizado(long id)
    throws br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException
  {
    AutorizadoCAGED retorno = null;
    try {
      retorno = autorizadoCAGEDDAO.obter(id);
    } catch (IntegrationException e) {
      throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage());
    }
    
    return retorno;
  }
}
