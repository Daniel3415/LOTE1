package br.gov.dataprev.caged.captacao.negocio.service;

import br.gov.dataprev.caged.captacao.integracao.dao.IDeclaracaoEstabelecimentoCAGEDDAO;
import br.gov.dataprev.caged.captacao.integracao.desktop.dao.DeclaracaoEstabelecimentoCAGEDJPADAO;
import br.gov.dataprev.caged.captacao.integracao.desktop.service.SDCCaptacaoCAGEDServiceDesktop;
import br.gov.dataprev.caged.captacao.integracao.exception.IntegrationException;
import br.gov.dataprev.caged.captacao.integracao.util.Conversor;
import br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoCAGED;
import br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoEstabelecimentoCAGED;
import br.gov.dataprev.caged.componente.analisador.integracao.service.ISDCAnalisadorCAGEDService;
import br.gov.dataprev.caged.componente.analisador.regras.RegrasDeclaracaoEstabelecimentoCAGED;
import br.gov.dataprev.caged.componente.analisador.regras.mensagem.AlertaValidacao;
import br.gov.dataprev.caged.componente.analisador.regras.mensagem.ErroValidacao;
import br.gov.dataprev.caged.modelo.captacao.declaracao.estabelecimento.EstabelecimentoCAGED;
import br.gov.dataprev.caged.modelo.competencia.CompetenciaCAGED;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;





public class CaptacaoDeclaracaoEstabelecimentoCAGEDService
  implements ICaptacaoEstabelecimentoCAGEDService
{
  private EntityManager entityManager;
  IDeclaracaoEstabelecimentoCAGEDDAO declaracaoEstabelecimentoCAGEDDAO;
  ISDCAnalisadorCAGEDService sdcService;
  
  public CaptacaoDeclaracaoEstabelecimentoCAGEDService(EntityManager em)
  {
    entityManager = em;
    
    declaracaoEstabelecimentoCAGEDDAO = new DeclaracaoEstabelecimentoCAGEDJPADAO(entityManager);
    sdcService = new SDCCaptacaoCAGEDServiceDesktop(entityManager);
  }
  
  public DeclaracaoEstabelecimentoCAGED alterarEstabelecimento(DeclaracaoEstabelecimentoCAGED declaracaoEstabelecimentoCAGED) throws br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException
  {
    List listaMensagens = analisarEstabelecimentos(declaracaoEstabelecimentoCAGED);
    DeclaracaoEstabelecimentoCAGED retorno = null;
    try
    {
      if (listaMensagens.size() != 0) {
        Iterator it = listaMensagens.iterator();
        
        while (it.hasNext())
        {
          Object mensagem = it.next();
          if (mensagem.getClass() != AlertaValidacao.class) {
            throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(((ErroValidacao)mensagem).getDescricao());
          }
        }
      }
      

      declaracaoEstabelecimentoCAGEDDAO.alterar(declaracaoEstabelecimentoCAGED);
      




      retorno = obterEstabelecimento(declaracaoEstabelecimentoCAGED.getId());
    }
    catch (IntegrationException e)
    {
      throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage());
    }
    

    return retorno;
  }
  
  public List analisarEstabelecimentos(DeclaracaoEstabelecimentoCAGED declaracao) throws br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException
  {
    RegrasDeclaracaoEstabelecimentoCAGED regrasEstabelecimentoCAGED;
    try
    {
      regrasEstabelecimentoCAGED = new RegrasDeclaracaoEstabelecimentoCAGED(sdcService);
    } catch (br.gov.dataprev.caged.componente.analisador.regras.exception.BusinessException e) {
      throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage());
    }
    
    regrasEstabelecimentoCAGED.setDeclaracaoEstabelecimento(Conversor.converter(declaracao));
    regrasEstabelecimentoCAGED.validar();
    
    return regrasEstabelecimentoCAGED.getMensagens();
  }
  

  public DeclaracaoEstabelecimentoCAGED cadastrarDeclaracaoEstabelecimento(DeclaracaoEstabelecimentoCAGED declaracao)
    throws br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException
  {
    List listaMensagens = analisarEstabelecimentos(declaracao);
    try
    {
      if (listaMensagens.size() == 0) {
        declaracaoEstabelecimentoCAGEDDAO.cadastrar(declaracao);
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
        

        declaracaoEstabelecimentoCAGEDDAO.cadastrar(declaracao);
      }
    }
    catch (IntegrationException e)
    {
      throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage());
    }
    DeclaracaoEstabelecimentoCAGED retorno = obterEstabelecimento(declaracao.getId());
    return retorno;
  }
  
  public DeclaracaoEstabelecimentoCAGED obterEstabelecimento(Long id)
    throws br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException
  {
    DeclaracaoEstabelecimentoCAGED retorno = null;
    try {
      retorno = declaracaoEstabelecimentoCAGEDDAO.obter(id);
    } catch (IntegrationException e) {
      throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage());
    }
    
    return retorno;
  }
  
  public List obterListaEstabelecimentos(DeclaracaoCAGED declaracao, String numeroDocumentoIdentificacao, int tipoDocumentoIdentificacao, String razaoSocial) throws br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException
  {
    try
    {
      return declaracaoEstabelecimentoCAGEDDAO.obterLista(declaracao, numeroDocumentoIdentificacao, tipoDocumentoIdentificacao, razaoSocial);
    } catch (IntegrationException e) {
      throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage());
    }
  }
  
  public List obterListaEstabelecimentos(String numeroDocumentoIdentificacao, int tipoDocumentoIdentificacao, String razaoSocial) throws br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException
  {
    try {
      return declaracaoEstabelecimentoCAGEDDAO.obterUltimas(numeroDocumentoIdentificacao, tipoDocumentoIdentificacao, razaoSocial);
    } catch (IntegrationException e) {
      throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage());
    }
  }
  
  public void removerDeclaracaoEstabelecimento(Long id) throws br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException {
    try {
      declaracaoEstabelecimentoCAGEDDAO.excluir(id);
    } catch (IntegrationException e) {
      throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage());
    }
  }
  
















  public List listarEstabelecimentos(CompetenciaCAGED competencia, int tipoConsulta)
    throws IntegrationException
  {
    List listaDeclaracoes = declaracaoEstabelecimentoCAGEDDAO.listarDeclaracoesEstabelecimento(competencia, tipoConsulta);
    List novaListaDeclaracoes = new ArrayList();
    
    for (int i = 0; i < listaDeclaracoes.size(); i++)
    {
      DeclaracaoEstabelecimentoCAGED declaracao = (DeclaracaoEstabelecimentoCAGED)listaDeclaracoes.get(i);
      

      declaracao.setTotalAdmissoes();
      declaracao.setTotalDesligamentos();
    }
    

    if (listaDeclaracoes != null) {
      Iterator itListaDeclaracoes = listaDeclaracoes.iterator();
      try
      {
        DeclaracaoEstabelecimentoCAGED declaracaoAnterior = (DeclaracaoEstabelecimentoCAGED)listaDeclaracoes.get(0);
        
        while (itListaDeclaracoes.hasNext()) {
          DeclaracaoEstabelecimentoCAGED declaracao = (DeclaracaoEstabelecimentoCAGED)itListaDeclaracoes.next();
          
          if (!declaracao.getEstabelecimento().getId().equals(declaracaoAnterior.getEstabelecimento().getId())) {
            novaListaDeclaracoes.add(declaracaoAnterior);
          }
          
          declaracaoAnterior = declaracao;
          if (!itListaDeclaracoes.hasNext()) {
            novaListaDeclaracoes.add(declaracao);
          }
        }
      }
      catch (ArrayIndexOutOfBoundsException e) {}
    }
    


    return novaListaDeclaracoes;
  }
}
