package br.gov.dataprev.caged.captacao.negocio.service;

import br.gov.dataprev.caged.captacao.integracao.dao.IArquivoCAGEDPosicionalDAO;
import br.gov.dataprev.caged.captacao.integracao.dao.IArquivoCAGEDXmlDAO;
import br.gov.dataprev.caged.captacao.integracao.dao.IAutorizadoCAGEDDAO;
import br.gov.dataprev.caged.captacao.integracao.dao.IDeclaracaoCAGEDDAO;
import br.gov.dataprev.caged.captacao.integracao.dao.IDeclaracaoEstabelecimentoCAGEDDAO;
import br.gov.dataprev.caged.captacao.integracao.dao.IMovimentacaoCAGEDDAO;
import br.gov.dataprev.caged.captacao.integracao.desktop.dao.ArquivoCAGEDPosicionalDAO;
import br.gov.dataprev.caged.captacao.integracao.desktop.dao.ArquivoCAGEDXmlDAO;
import br.gov.dataprev.caged.captacao.integracao.desktop.dao.AutorizadoCAGEDJPADAO;
import br.gov.dataprev.caged.captacao.integracao.desktop.dao.DeclaracaoCAGEDJPADAO;
import br.gov.dataprev.caged.captacao.integracao.desktop.dao.DeclaracaoEstabelecimentoCAGEDJPADAO;
import br.gov.dataprev.caged.captacao.integracao.desktop.dao.ISalarioMinimoDAO;
import br.gov.dataprev.caged.captacao.integracao.desktop.dao.MovimentacaoCAGEDJPADAO;
import br.gov.dataprev.caged.captacao.integracao.desktop.dao.SalarioMinimoJPADAO;
import br.gov.dataprev.caged.captacao.integracao.desktop.service.SDCCaptacaoCAGEDServiceDesktop;
import br.gov.dataprev.caged.captacao.integracao.exception.ArquivoNaoAssinadoException;
import br.gov.dataprev.caged.captacao.integracao.exception.IntegrationException;
import br.gov.dataprev.caged.captacao.integracao.util.Conversor;
import br.gov.dataprev.caged.captacao.modelo.autorizado.AutorizadoCAGED;
import br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoEstabelecimentoCAGED;
import br.gov.dataprev.caged.captacao.negocio.exceptions.CompetenciaAtualException;
import br.gov.dataprev.caged.captacao.negocio.exceptions.DeclaracaoDoFuturoBussinessException;
import br.gov.dataprev.caged.captacao.negocio.exceptions.DeclaracaoExistenteBussinessException;
import br.gov.dataprev.caged.captacao.negocio.util.CompetenciaVigenteValidator;
import br.gov.dataprev.caged.componente.analisador.integracao.service.ISDCAnalisadorCAGEDService;
import br.gov.dataprev.caged.componente.analisador.modelo.SalarioMinimo;
import br.gov.dataprev.caged.componente.analisador.modelo.SituacaoDeclaracao;
import br.gov.dataprev.caged.componente.analisador.novalidacao.NoValidacao;
import br.gov.dataprev.caged.componente.analisador.regras.declaracao.RegraValidacaoSalarioMinimo;
import br.gov.dataprev.caged.componente.analisador.regras.declaracao.nova.RegraValidacaoNovaCompetencia;
import br.gov.dataprev.caged.componente.analisador.regras.mensagem.AlertaValidacao;
import br.gov.dataprev.caged.componente.analisador.regras.mensagem.ErroValidacao;
import br.gov.dataprev.caged.componente.analisador.regras.mensagem.MensagemValidacao;
import br.gov.dataprev.caged.componente.analisador.service.AnalisadorDeclaracaoCAGEDService;
import br.gov.dataprev.caged.componente.analisador.service.IAnalisadorDeclaracaoCAGEDService;
import br.gov.dataprev.caged.componentes.extrator.exception.ExtracaoException;
import br.gov.dataprev.caged.modelo.captacao.declaracao.estabelecimento.EstabelecimentoCAGED;
import br.gov.dataprev.caged.modelo.captacao.declaracao.movimentacao.AcertoCAGED;
import br.gov.dataprev.caged.modelo.captacao.declaracao.movimentacao.MovimentacaoCAGED;
import br.gov.dataprev.caged.modelo.captacao.declaracao.movimentacao.trabalhador.Trabalhador;
import br.gov.dataprev.caged.modelo.competencia.CompetenciaCAGED;
import br.gov.dataprev.caged.modelo.declaracao.movimentacao.TipoMovimento;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import org.w3c.dom.Document;



public class CaptacaoDeclaracaoCAGEDService
  implements ICaptacaoDeclaracaoCAGEDService
{
  private EntityManager entityManager;
  IDeclaracaoCAGEDDAO declaracaoCAGEDDAO;
  IDeclaracaoCAGEDDAO declaracaoCAGEDDAOArquivo;
  IArquivoCAGEDXmlDAO arquivoCAGEDDAO;
  IArquivoCAGEDPosicionalDAO arquivoPosicionalCAGEDDAO;
  ISDCAnalisadorCAGEDService sdcService;
  IAutorizadoCAGEDDAO autorizadoCAGEDDAO;
  IDeclaracaoEstabelecimentoCAGEDDAO estabelecimentoDAO;
  IMovimentacaoCAGEDDAO movimentacaoDAO;
  ISalarioMinimoDAO salarioDAO;
  
  public CaptacaoDeclaracaoCAGEDService(EntityManager em)
  {
    entityManager = em;
    
    declaracaoCAGEDDAO = new DeclaracaoCAGEDJPADAO(entityManager);
    declaracaoCAGEDDAOArquivo = new DeclaracaoCAGEDJPADAO(entityManager);
    autorizadoCAGEDDAO = new AutorizadoCAGEDJPADAO(entityManager);
    estabelecimentoDAO = new DeclaracaoEstabelecimentoCAGEDJPADAO(entityManager);
    movimentacaoDAO = new MovimentacaoCAGEDJPADAO(entityManager);
    salarioDAO = new SalarioMinimoJPADAO(entityManager);
    
    sdcService = new SDCCaptacaoCAGEDServiceDesktop(entityManager);
    
    arquivoCAGEDDAO = new ArquivoCAGEDXmlDAO();
    arquivoPosicionalCAGEDDAO = new ArquivoCAGEDPosicionalDAO(entityManager);
  }
  







  public br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoCAGED abrirDeclaracaoCAGED(CompetenciaCAGED competencia)
    throws br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException
  {
    try
    {
      return declaracaoCAGEDDAO.obter(competencia);
    }
    catch (IntegrationException e) {
      throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage());
    }
  }
  









  public br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoCAGED abrirDeclaracaoCAGEDXML(String nomeArquivo)
    throws br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException
  {
    br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoCAGED declaracao = null;
    
    if (nomeArquivo.endsWith("xml"))
    {
      Document doc;
      try {
        doc = arquivoCAGEDDAO.analisarArquivoAssinado(nomeArquivo);
      }
      catch (ExtracaoException e)
      {
        try {
          doc = arquivoCAGEDDAO.analisarArquivoNaoAssinado(nomeArquivo);
        }
        catch (IntegrationException e1) {
          throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException("Arquivo no formato inválido");
        }
      }
      catch (IntegrationException e) {
        throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage());
      }
      try
      {
        declaracao = arquivoCAGEDDAO.obter(doc);
      }
      catch (IntegrationException e) {
        throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage());
      }
    } else {
      IArquivoCAGEDPosicionalDAO arquivoPosicionalDAO = new ArquivoCAGEDPosicionalDAO(entityManager);
      try
      {
        declaracao = arquivoPosicionalDAO.obter(nomeArquivo);
      }
      catch (IntegrationException e) {
        throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage());
      }
    }
    
    if ((declaracao.getSalarioMinimo() == null) || (declaracao.getSalarioMinimo().equals(Double.valueOf(0.0D)))) {
      try {
        declaracao.setSalarioMinimo(salarioDAO.obterMaisRecente().getValor());
      } catch (IntegrationException e) {
        throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage());
      }
    }
    
    return declaracao;
  }
  









  public br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoCAGED alterarDeclaracaoCAGED(br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoCAGED dec, boolean ehPersistivel)
    throws br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException
  {
    br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoCAGED retorno = null;
    br.gov.dataprev.caged.modelo.declaracao.DeclaracaoCAGED declaracao = Conversor.converter(dec);
    
    RegraValidacaoSalarioMinimo rv2 = new RegraValidacaoSalarioMinimo();
    rv2.setSdc(sdcService);
    
    List listaMenssagens = new ArrayList();
    

    listaMenssagens.addAll(rv2.validar(declaracao));
    try
    {
      AutorizadoCAGED autorizado = autorizadoCAGEDDAO.obter(1L);
      dec.setAutorizado(autorizado);
    } catch (Exception e) {
      new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage());
    }
    



    try
    {
      if (listaMenssagens.size() == 0) {
        retorno = dec;
        if (ehPersistivel)
          alterar(dec);
        return declaracaoCAGEDDAO.obter(dec.getCompetencia());
      }
      



      Iterator it = listaMenssagens.iterator();
      
      while (it.hasNext())
      {
        Object mensagem = it.next();
        if (mensagem.getClass().getGenericSuperclass() != AlertaValidacao.class) {
          throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(((ErroValidacao)mensagem).getDescricao());
        }
      }
      

      declaracaoCAGEDDAO.alterar(dec);
      return declaracaoCAGEDDAO.obter(dec.getCompetencia());

    }
    catch (IntegrationException e)
    {
      throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage());
    }
  }
  
  private void alterar(br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoCAGED dec) throws IntegrationException
  {
    declaracaoCAGEDDAO.alterar(dec);
  }
  






  public NoValidacao analisarDeclaracaoCAGED(br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoCAGED declaracao)
    throws br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException
  {
    IAnalisadorDeclaracaoCAGEDService analisador = new AnalisadorDeclaracaoCAGEDService(sdcService, false);
    CompetenciaVigenteValidator competenciaValidator = new CompetenciaVigenteValidator();
    try
    {
      NoValidacao noValidacao = analisador.analisarDeclaracaoCAGEDOffLine(Conversor.converter(declaracao));
      
      List listaMensagens = noValidacao.getAllMesage();
      







      boolean competenciaValida = false;
      MensagemValidacao msgVal = null;
      String idMsgCompNaoVig = "MSG_112_COMPETENCIA_NAO_VIG";
      

      for (Object objVal : listaMensagens)
      {
        msgVal = (MensagemValidacao)objVal;
        
        if ((msgVal.getIdentificador() != null) && (msgVal.getIdentificador().equals(idMsgCompNaoVig)))
        {
          if (competenciaValidator.ehValida(declaracao.getCompetencia())) {
            competenciaValida = true;
            break;
          }
        }
      }
      
      if ((competenciaValida) && (msgVal != null)) { noValidacao.removeAllMesage(msgVal);
      }
      return noValidacao;
    }
    catch (br.gov.dataprev.caged.componente.analisador.regras.exception.BusinessException e) {
      throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e);
    }
  }
  









  public br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoCAGED cadastrarNovaDeclaracaoCAGED(br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoCAGED declaracao)
    throws br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException
  {
    br.gov.dataprev.caged.modelo.declaracao.DeclaracaoCAGED dec = Conversor.converter(declaracao);
    


    declaracao.setSituacao(new SituacaoDeclaracao(1));
    


    RegraValidacaoNovaCompetencia rv1 = new RegraValidacaoNovaCompetencia();
    RegraValidacaoSalarioMinimo rv2 = new RegraValidacaoSalarioMinimo();
    
    List listaMenssagens = new ArrayList();
    
    rv2.setSdc(sdcService);
    

    listaMenssagens.addAll(rv1.validar(dec));
    listaMenssagens.addAll(rv2.validar(dec));
    try
    {
      AutorizadoCAGED autorizado = autorizadoCAGEDDAO.obter(1L);
      declaracao.setAutorizado(autorizado);
    } catch (IntegrationException e) {
      new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage());
    }
    




    try
    {
      if (listaMenssagens.size() == 0) {
        declaracaoCAGEDDAO.gravar(declaracao);
        return declaracaoCAGEDDAO.obter(declaracao.getCompetencia());
      }
      
      Iterator it = listaMenssagens.iterator();
      while (it.hasNext())
      {
        Object mensagem = it.next();
        if (mensagem.getClass().getGenericSuperclass() != AlertaValidacao.class) {
          throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(((ErroValidacao)mensagem).getDescricao());
        }
      }
      
      declaracaoCAGEDDAO.gravar(declaracao);
      return declaracaoCAGEDDAO.obter(declaracao.getCompetencia());
    }
    catch (IntegrationException e) {
      throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage(), e);
    }
  }
  











  public void excluirDeclaracaoCAGED(br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoCAGED declaracao)
    throws br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException
  {
    try
    {
      br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoCAGED dec = declaracaoCAGEDDAO.obter(declaracao.getId());
      
      Iterator it = new ArrayList().iterator();
      try
      {
        it = dec.getDeclaracaoesEstabelecimento().iterator();
      } catch (NullPointerException e) {
        e.printStackTrace();
      }
      

      while (it.hasNext())
      {
        DeclaracaoEstabelecimentoCAGED declaracaoEstabelecimentoCAGED = (DeclaracaoEstabelecimentoCAGED)it.next();
        

        Iterator itm = new ArrayList().iterator();
        try
        {
          itm = declaracaoEstabelecimentoCAGED.getListaDeMovimentacoes().iterator();
        } catch (NullPointerException e) {
          e.printStackTrace();
        }
        

        if (itm.hasNext()) {
          throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException("Esta Declaração contém Movimentações. Antes de excluir a declaração, exclua as movimentações relacionadas a ela.");
        }
        
        Iterator ita = new ArrayList().iterator();
        try
        {
          ita = declaracaoEstabelecimentoCAGED.getListaDeMovimentacoes().iterator();
        } catch (NullPointerException e) {
          e.printStackTrace();
        }
        
        if (ita.hasNext()) {
          throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException("Esta Declaração contém Movimentações. Antes de excluir a declaração, exclua as movimentações relacionadas a ela.");
        }
      }
      



      declaracaoCAGEDDAO.excluir(declaracao);
    }
    catch (IntegrationException e) {
      throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage());
    }
  }
  

  public void gerarArquivoDeclaracao(Long idDeclaracao, String nomeArquivo)
    throws br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException
  {
    try
    {
      br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoCAGED dec = declaracaoCAGEDDAO.obter(idDeclaracao);
      
      gerarArquivoDeclaracao(dec, nomeArquivo);
      
      dec.setSituacao(new SituacaoDeclaracao(2));
      declaracaoCAGEDDAO.alterar(dec);
    }
    catch (IntegrationException e) {
      throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage(), e);
    }
  }
  













  public void gerarArquivoDeclaracao(br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoCAGED dec, String nomeArquivo)
    throws br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException
  {
    try
    {
      List listaMenssagens = analisarDeclaracaoCAGED(dec).getAllMesage();
      




      if (listaMenssagens.size() == 0) {
        arquivoCAGEDDAO.gerar(dec, nomeArquivo);
      } else {
        Iterator it = listaMenssagens.iterator();
        while (it.hasNext())
        {
          Object mensagem = it.next();
          
          if (((mensagem.getClass().getGenericSuperclass() != AlertaValidacao.class ? 1 : 0) & (mensagem.getClass() != AlertaValidacao.class ? 1 : 0)) != 0) {
            throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(((ErroValidacao)mensagem).getDescricao());
          }
        }
        
        arquivoCAGEDDAO.gerar(dec, nomeArquivo);
      }
      

      arquivoCAGEDDAO.assinarACI(nomeArquivo);

    }
    catch (IntegrationException e)
    {
      throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage(), e);
    }
  }
  


  public void gerarArquivoPosicional(Long idDeclaracao, int versao, String filename)
    throws br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException
  {
    try
    {
      br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoCAGED dec = declaracaoCAGEDDAO.obter(idDeclaracao);
      
      gerarArquivoPosicional(dec, versao, filename);
      
      dec.setSituacao(new SituacaoDeclaracao(2));
      declaracaoCAGEDDAO.alterar(dec);
    }
    catch (IntegrationException e) {
      throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage(), e);
    }
  }
  




  public void gerarArquivoPosicional(br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoCAGED dec, int versao, String filename)
    throws br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException
  {
    try
    {
      List listaMenssagens = analisarDeclaracaoCAGED(dec).getAllMesage();
      





      if (listaMenssagens.size() == 0) {
        arquivoPosicionalCAGEDDAO.gerarArquivoPosicional(dec, versao, filename);
      } else {
        Iterator it = listaMenssagens.iterator();
        while (it.hasNext())
        {
          Object mensagem = it.next();
          
          if (((mensagem.getClass().getGenericSuperclass() != AlertaValidacao.class ? 1 : 0) & (mensagem.getClass() != AlertaValidacao.class ? 1 : 0)) != 0) {
            throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(((ErroValidacao)mensagem).getDescricao());
          }
        }
        
        arquivoPosicionalCAGEDDAO.gerarArquivoPosicional(dec, versao, filename);
      }
    }
    catch (IntegrationException e) {
      throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage(), e);
    }
  }
  
  public void gerarArquivoPosicionalSemAnalise(br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoCAGED dec, int versao, String filename) throws br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException
  {
    try
    {
      arquivoPosicionalCAGEDDAO.gerarArquivoPosicional(dec, versao, filename);
    }
    catch (IntegrationException e) {
      throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage(), e);
    }
  }
  








  public br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoCAGED importarDeclaracao(br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoCAGED decImportada, int operacao)
    throws br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException
  {
    if ((decImportada.getSalarioMinimo() == null) || (decImportada.getSalarioMinimo().equals(Double.valueOf(0.0D)))) {
      try {
        decImportada.setSalarioMinimo(salarioDAO.obterMaisRecente().getValor());
      } catch (IntegrationException e) {
        throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage());
      }
    }
    
    br.gov.dataprev.caged.modelo.declaracao.DeclaracaoCAGED decImportadaModelo = Conversor.converter(decImportada);
    



    try
    {
      AutorizadoCAGED autorizadoACI = autorizadoCAGEDDAO.obter(1L);
      
      if ((autorizadoACI.getContato() == null) || (autorizadoACI.getEndereco() == null))
      {



        autorizadoACI = decImportada.getAutorizado();
        autorizadoACI.setId(1);
        autorizadoCAGEDDAO.alterar(autorizadoACI);


      }
      else
      {

        decImportada.setAutorizado(autorizadoACI);
      }
      


      RegraValidacaoNovaCompetencia regraNovaCompetencia = new RegraValidacaoNovaCompetencia();
      




      br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoCAGED dec = null;
      
      if (regraNovaCompetencia.validar(decImportadaModelo).isEmpty())
      {
        try {
          dec = declaracaoCAGEDDAO.obter(decImportada.getCompetencia());
        }
        catch (IntegrationException e) {
          throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage());
        }
        
        if (dec != null)
        {
          if (operacao == 3) {
            decImportada = unificarDeclaracoes(decImportada, dec);
          }
          else {
            throw new DeclaracaoExistenteBussinessException("Já existe declaração na mesma competência. Deseja unificar informações?");
          }
          
        }
        
      }
      else if (operacao == 2)
      {
        decImportada = converteParaNovaCompetencia(decImportada.getCompetencia().obterCompetenciaAtual(), decImportada);
        try
        {
          dec = declaracaoCAGEDDAO.obter(decImportada.getCompetencia());
        }
        catch (IntegrationException e) {
          throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage());
        }
        
        if (dec != null) {
          throw new DeclaracaoExistenteBussinessException("Já existe declaração na mesma competência. Deseja unificar informações?");
        }
      }
      else if (operacao == 4)
      {
        decImportada = converteParaNovaCompetencia(decImportada.getCompetencia().obterCompetenciaAtual(), decImportada);
        try
        {
          dec = declaracaoCAGEDDAO.obter(decImportada.getCompetencia());
        }
        catch (IntegrationException e) {
          throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage(), e);
        }
        
        if (dec != null) {
          decImportada = unificarDeclaracoes(decImportada, dec);
        }
      }
      else
      {
        throw new CompetenciaAtualException("Declaração é de competencia anterior. Deseja converter para competência atual?");
      }
      



      if ((operacao == 3) || (operacao == 4))
      {
        gravarDeclaracaoImportada(decImportada);
        
        br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoCAGED declaracaoRecuperada = declaracaoCAGEDDAO.obter(decImportada.getCompetencia());
        
        declaracaoCAGEDDAO.alterar(declaracaoRecuperada);
        
        return declaracaoRecuperada;
      }
      
      decImportada.setSituacao(new SituacaoDeclaracao(1));
      declaracaoCAGEDDAO.gravar(decImportada);
      return declaracaoCAGEDDAO.obter(decImportada.getCompetencia());

    }
    catch (ArquivoNaoAssinadoException e1)
    {

      e1.printStackTrace();
    } catch (IntegrationException e) {
      throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage(), e);
    }
    
    return null;
  }
  







  private void gravarDeclaracaoImportada(br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoCAGED declaracao)
    throws br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException
  {
    if ((declaracao.getDeclaracaoesEstabelecimento() == null) || (declaracao.getDeclaracaoesEstabelecimento().isEmpty())) {
      return;
    }
    br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoCAGED declaracaoRecuperada = new br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoCAGED();
    try
    {
      declaracaoRecuperada = declaracaoCAGEDDAO.obter(declaracao.getId());
    }
    catch (IntegrationException e) {
      throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage(), e);
    }
    

    for (Object obj : declaracao.getDeclaracaoesEstabelecimento())
    {
      DeclaracaoEstabelecimentoCAGED dec = (DeclaracaoEstabelecimentoCAGED)obj;
      
      if ((dec.getId() == null) || (dec.getId().longValue() == 0L))
      {




        dec.setDeclaracaoCaged(declaracaoRecuperada);
        
        try
        {
          estabelecimentoDAO.cadastrar(dec);
          
          if (dec.getListaDeMovimentacoes() != null)
          {
            Iterator itm = dec.getListaDeMovimentacoes().iterator();
            
            while (itm.hasNext())
            {
              MovimentacaoCAGED mov = (MovimentacaoCAGED)itm.next();
              
              if ((mov.getId() == null) || (mov.getId().longValue() == 0L)) {
                movimentacaoDAO.cadastrar(mov);
              }
            }
          }
          



          if (dec.getListaDeAcertos() != null)
          {
            Iterator ita = dec.getListaDeAcertos().iterator();
            
            while (ita.hasNext())
            {
              AcertoCAGED mov = (AcertoCAGED)ita.next();
              
              if ((mov.getId() == null) || (mov.getId().longValue() == 0L)) {
                mov.setDecEstabelecimento(dec);
                movimentacaoDAO.cadastrar(mov);
              }
              
            }
            
          }
        }
        catch (IntegrationException e)
        {
          throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage(), e);
        }
        

      }
      else
      {
        try
        {
          if (dec.getListaDeMovimentacoes() != null)
          {
            Iterator itm = dec.getListaDeMovimentacoes().iterator();
            
            while (itm.hasNext())
            {
              MovimentacaoCAGED mov = (MovimentacaoCAGED)itm.next();
              
              if ((mov.getId() == null) || (mov.getId().longValue() == 0L)) {
                mov.setDecEstabelecimento(dec);
                movimentacaoDAO.cadastrar(mov);
              }
            }
          }
          



          if (dec.getListaDeAcertos() != null)
          {
            Iterator ita = dec.getListaDeAcertos().iterator();
            
            while (ita.hasNext())
            {
              AcertoCAGED mov = (AcertoCAGED)ita.next();
              
              if ((mov.getId() == null) || (mov.getId().longValue() == 0L)) {
                mov.setDecEstabelecimento(dec);
                movimentacaoDAO.cadastrar(mov);
              }
              
            }
            
          }
          
        }
        catch (IntegrationException e)
        {
          throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage(), e);
        }
      }
    }
  }
  








  public List listarDeclaracoesCAGED()
    throws br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException
  {
    try
    {
      return declaracaoCAGEDDAO.obterLista(null);
    } catch (IntegrationException e) {
      throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage());
    }
  }
  





  public List obterListaCompetencias()
    throws br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException
  {
    try
    {
      return declaracaoCAGEDDAO.obterListaCompetencias();
    } catch (IntegrationException e) {
      throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage());
    }
  }
  









  private br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoCAGED converteParaNovaCompetencia(CompetenciaCAGED novaCompetencia, br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoCAGED declaracaoCAGED)
    throws br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException
  {
    if (novaCompetencia.toDate().before(declaracaoCAGED.getCompetencia().toDate())) {
      throw new DeclaracaoDoFuturoBussinessException("Impossível converter de uma competência futura, confira a data do sistema e também a competência do arquivo.");
    }
    
    Iterator ite = declaracaoCAGED.getDeclaracaoesEstabelecimento().iterator();
    
    while (ite.hasNext())
    {
      DeclaracaoEstabelecimentoCAGED dec = (DeclaracaoEstabelecimentoCAGED)ite.next();
      





      dec.setTotalEmpregadosPrimeiroDia(dec.obterUltimoDia());
      
      if (dec.getListaDeMovimentacoes() != null)
      {
        Iterator itm = dec.getListaDeMovimentacoes().iterator();
        
        while (itm.hasNext())
        {

          MovimentacaoCAGED mov = (MovimentacaoCAGED)itm.next();
          
          AcertoCAGED acerto = new AcertoCAGED();
          
          acerto.setCompetencia(declaracaoCAGED.getCompetencia());
          acerto.setAtualizacao(2);
          acerto.setCbo(mov.getCbo());
          acerto.setDataAdmissao(mov.getDataAdmissao());
          acerto.setDecEstabelecimento(mov.getDecEstabelecimento());
          acerto.setDiaDesligamento(mov.getDiaDesligamento());
          acerto.setHorasContratuais(mov.getHorasContratuais());
          acerto.setId(mov.getId());
          acerto.setRemuneracao(mov.getRemuneracao());
          acerto.setTipoMovimento(mov.getTipoMovimento());
          acerto.setTrabalhador(mov.getTrabalhador());
          
          if (dec.getListaDeAcertos() == null) {
            dec.setListaDeAcertos(new ArrayList());
          }
          
          dec.getListaDeAcertos().add(acerto);
        }
      }
      


      dec.setListaDeMovimentacoes(null);
    }
    


    declaracaoCAGED.setCompetencia(novaCompetencia);
    
    return declaracaoCAGED;
  }
  











  public br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoCAGED unificarDeclaracoes(br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoCAGED novaDeclaracao, br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoCAGED declaracaoExistente)
    throws br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException
  {
    if (!novaDeclaracao.getCompetencia().equals(declaracaoExistente.getCompetencia())) {
      throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException("Impossível unificar com competência diferente");
    }
    
    br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoCAGED declaracaoUnificada = declaracaoExistente;
    
    Iterator ite = novaDeclaracao.getDeclaracaoesEstabelecimento().iterator();
    while (ite.hasNext())
    {
      DeclaracaoEstabelecimentoCAGED decEstabNova = (DeclaracaoEstabelecimentoCAGED)ite.next();
      
      if (!declaracaoEstabelecimentoJaCadatrado(declaracaoExistente.getDeclaracaoesEstabelecimento(), decEstabNova)) {
        DeclaracaoEstabelecimentoCAGED decEstabSubistituta = new DeclaracaoEstabelecimentoCAGED(decEstabNova);
        declaracaoUnificada.getDeclaracaoesEstabelecimento().add(decEstabSubistituta);
        decEstabSubistituta.setDeclaracaoCaged(declaracaoUnificada);
        copiarMovimentacoesAcertos(decEstabNova, decEstabSubistituta);
      }
      else
      {
        DeclaracaoEstabelecimentoCAGED decEstabJaExistente = getDeclaracao(declaracaoExistente.getDeclaracaoesEstabelecimento(), decEstabNova);
        

        copiarMovimentacoesAcertos(decEstabNova, decEstabJaExistente);
      }
    }
    

    return declaracaoUnificada;
  }
  









  private void copiarMovimentacoesAcertos(DeclaracaoEstabelecimentoCAGED decEstabOrigem, DeclaracaoEstabelecimentoCAGED decEstabDestino)
  {
    if (decEstabOrigem.getListaDeMovimentacoes() != null) {
      Iterator itm = decEstabOrigem.getListaDeMovimentacoes().iterator();
      while (itm.hasNext())
      {
        MovimentacaoCAGED mov = (MovimentacaoCAGED)itm.next();
        
        if (!movimentacaoJaCadastrada(decEstabDestino.getListaDeMovimentacoes(), mov)) {
          MovimentacaoCAGED movSubstituta = new MovimentacaoCAGED(mov);
          decEstabDestino.getListaDeMovimentacoes().add(movSubstituta);
          movSubstituta.setDecEstabelecimento(decEstabDestino);
        }
      }
    }
    
    if (decEstabOrigem.getListaDeAcertos() != null) {
      Iterator ita = decEstabOrigem.getListaDeAcertos().iterator();
      while (ita.hasNext())
      {
        AcertoCAGED acerto = (AcertoCAGED)ita.next();
        
        if (!acertoJaCadastrado(decEstabDestino.getListaDeAcertos(), acerto)) {
          AcertoCAGED acertoSubstituta = new AcertoCAGED(acerto);
          decEstabDestino.getListaDeAcertos().add(acertoSubstituta);
          acertoSubstituta.setDecEstabelecimento(decEstabDestino);
        }
      }
    }
  }
  








  private boolean declaracaoEstabelecimentoJaCadatrado(List listadeEstabelecimento, DeclaracaoEstabelecimentoCAGED estabelecimento)
  {
    Iterator it = listadeEstabelecimento.iterator();
    






    while (it.hasNext())
    {
      DeclaracaoEstabelecimentoCAGED dec = (DeclaracaoEstabelecimentoCAGED)it.next();
      
      if (dec.getEstabelecimento() != null) { if ((dec.getEstabelecimento().getNumeroDocumentoIdentificacao().equals(estabelecimento.getEstabelecimento().getNumeroDocumentoIdentificacao()) & dec.getEstabelecimento().getTipoDocumentoIdentificacao() == estabelecimento.getEstabelecimento().getTipoDocumentoIdentificacao()))
        {


          return true;
        }
      }
    }
    

    return false;
  }
  










  private DeclaracaoEstabelecimentoCAGED getDeclaracao(List listadeEstabelecimento, DeclaracaoEstabelecimentoCAGED estabelecimento)
  {
    Iterator it = listadeEstabelecimento.iterator();
    
    while (it.hasNext())
    {
      DeclaracaoEstabelecimentoCAGED dec = (DeclaracaoEstabelecimentoCAGED)it.next();
      
      if ((dec.getEstabelecimento().getNumeroDocumentoIdentificacao().equals(estabelecimento.getEstabelecimento().getNumeroDocumentoIdentificacao())) && (dec.getEstabelecimento().getTipoDocumentoIdentificacao() == estabelecimento.getEstabelecimento().getTipoDocumentoIdentificacao()))
      {


        return dec;
      }
    }
    


    return null;
  }
  









  private boolean movimentacaoJaCadastrada(List listadeMovimentacoes, MovimentacaoCAGED movimentacao)
  {
    Iterator it = listadeMovimentacoes.iterator();
    
    while (it.hasNext())
    {
      MovimentacaoCAGED mov = (MovimentacaoCAGED)it.next();
      
      if ((mov.getTrabalhador().getNit().equals(movimentacao.getTrabalhador().getNit())) && (mov.getTrabalhador().getNome().trim().equals(movimentacao.getTrabalhador().getNome().trim())) && (mov.getDataAdmissao().equals(movimentacao.getDataAdmissao())) && (mov.getDiaDesligamento() == movimentacao.getDiaDesligamento()) && (mov.getTipoMovimento().getId() == mov.getTipoMovimento().getId()))
      {




        return true;
      }
    }
    


    return false;
  }
  









  private boolean acertoJaCadastrado(List listadeAcertos, AcertoCAGED acerto)
  {
    Iterator it = listadeAcertos.iterator();
    
    while (it.hasNext())
    {
      AcertoCAGED mov = (AcertoCAGED)it.next();
      
      if ((mov.getTrabalhador().getNit().equals(acerto.getTrabalhador().getNit())) && (mov.getTrabalhador().getNome().trim().equals(acerto.getTrabalhador().getNome().trim())) && (mov.getDataAdmissao().equals(acerto.getDataAdmissao())) && (mov.getDiaDesligamento() == acerto.getDiaDesligamento()) && (mov.getTipoMovimento().getId() == mov.getTipoMovimento().getId()))
      {




        return true;
      }
    }
    


    return false;
  }
}
