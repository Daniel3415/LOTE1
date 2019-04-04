package br.gov.dataprev.caged.captacao.negocio.service;

import br.gov.dataprev.caged.captacao.integracao.dao.IAcertoCAGEDDAO;
import br.gov.dataprev.caged.captacao.integracao.dao.IDeclaracaoCAGEDDAO;
import br.gov.dataprev.caged.captacao.integracao.dao.IDeclaracaoEstabelecimentoCAGEDDAO;
import br.gov.dataprev.caged.captacao.integracao.dao.IMovimentacaoCAGEDDAO;
import br.gov.dataprev.caged.captacao.integracao.desktop.dao.AcertoCAGEDJPADAO;
import br.gov.dataprev.caged.captacao.integracao.desktop.dao.DeclaracaoCAGEDJPADAO;
import br.gov.dataprev.caged.captacao.integracao.desktop.dao.DeclaracaoEstabelecimentoCAGEDJPADAO;
import br.gov.dataprev.caged.captacao.integracao.desktop.dao.MovimentacaoCAGEDJPADAO;
import br.gov.dataprev.caged.captacao.integracao.desktop.service.SDCCaptacaoCAGEDServiceDesktop;
import br.gov.dataprev.caged.captacao.integracao.util.Conversor;
import br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoCAGED;
import br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoEstabelecimentoCAGED;
import br.gov.dataprev.caged.componente.analisador.integracao.service.ISDCAnalisadorCAGEDService;
import br.gov.dataprev.caged.componente.analisador.regras.RegrasAcerto;
import br.gov.dataprev.caged.componente.analisador.regras.RegrasMovimentacao;
import br.gov.dataprev.caged.componente.analisador.regras.mensagem.ErroValidacao;
import br.gov.dataprev.caged.componente.analisador.regras.mensagem.MensagemValidacao;
import br.gov.dataprev.caged.modelo.captacao.declaracao.movimentacao.Movimentacao;
import br.gov.dataprev.caged.modelo.captacao.declaracao.movimentacao.trabalhador.Trabalhador;
import br.gov.dataprev.caged.modelo.competencia.CompetenciaCAGED;
import br.gov.dataprev.caged.modelo.declaracao.movimentacao.TipoMovimento;
import br.gov.dataprev.caged.modelo.vinculo.CBO;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;








public class CaptacaoMovimentacaoCAGEDService
  implements ICaptacaoMovimentacaoCAGEDService
{
  private EntityManager entityManager;
  private IMovimentacaoCAGEDDAO daoMovimentacao;
  private IAcertoCAGEDDAO daoAcerto;
  private ISDCAnalisadorCAGEDService sdcService;
  private IDeclaracaoEstabelecimentoCAGEDDAO daoDeclaracaoEstabelecimento = new DeclaracaoEstabelecimentoCAGEDJPADAO();
  private IDeclaracaoCAGEDDAO daoDeclaracao = new DeclaracaoCAGEDJPADAO();
  ICaptacaoDeclaracaoCAGEDService captacaoDeclaracao;
  
  public CaptacaoMovimentacaoCAGEDService(EntityManager em)
  {
    entityManager = em;
    
    daoMovimentacao = new MovimentacaoCAGEDJPADAO(entityManager);
    daoAcerto = new AcertoCAGEDJPADAO(entityManager);
    daoDeclaracaoEstabelecimento = new DeclaracaoEstabelecimentoCAGEDJPADAO(entityManager);
    daoDeclaracao = new DeclaracaoCAGEDJPADAO(entityManager);
    
    sdcService = new SDCCaptacaoCAGEDServiceDesktop(entityManager);
    captacaoDeclaracao = new CaptacaoDeclaracaoCAGEDService(entityManager);
  }
  
  public Movimentacao alterarMovimentacao(Movimentacao movimentacao, boolean ehPersistivel) throws br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException
  {
    List mensagens = analisarMovimentacao(movimentacao);
    
    if (movimentacaoSemErros(mensagens)) {
      try
      {
        Movimentacao movimentacaoOriginal = movimentacao;
        if (ehPersistivel) {
          movimentacaoOriginal = daoMovimentacao.obter(movimentacao.getId());
        }
        
        String tipoTrabalhadorOriginal = movimentacaoOriginal.getTrabalhador().getClass().getName();
        String tipoTrabalhadorAtual = movimentacao.getTrabalhador().getClass().getName();
        
        if (tipoTrabalhadorOriginal.equals(tipoTrabalhadorAtual)) {
          if (ehPersistivel) {
            daoMovimentacao.alterar(movimentacao);
            return daoMovimentacao.obter(movimentacao.getId());
          }
          
          return movimentacao;
        }
        




        if (ehPersistivel) {
          removerMovimentacao(movimentacao.getId());
        }
        
        movimentacao.setId(null);
        return cadastrarMovimentacao(movimentacao, ehPersistivel);
      }
      catch (br.gov.dataprev.caged.captacao.integracao.exception.IntegrationException e)
      {
        throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage());
      }
    }
    
    return null;
  }
  




  public List analisarMovimentacao(Movimentacao movimentacao)
    throws br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException
  {
    RegrasMovimentacao regrasMovimentacao;
    



    try
    {
      regrasMovimentacao = new RegrasMovimentacao(sdcService);
    } catch (br.gov.dataprev.caged.componente.analisador.regras.exception.BusinessException e) {
      throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage());
    }
    

    br.gov.dataprev.caged.modelo.declaracao.movimentacao.MovimentacaoCAGED movimentacaoAnalise = Conversor.converter(movimentacao);
    
    regrasMovimentacao.setMovimentacao(movimentacaoAnalise);
    




    if ((movimentacaoAnalise instanceof br.gov.dataprev.caged.modelo.declaracao.movimentacao.AcertoCAGED))
    {
      RegrasAcerto regrasAcerto;
      try
      {
        regrasAcerto = new RegrasAcerto(sdcService);
      }
      catch (br.gov.dataprev.caged.componente.analisador.regras.exception.BusinessException e) {
        throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage());
      }
      
      regrasMovimentacao.getRegrasValidacao().addAll(regrasAcerto.getRegrasValidacao());
    }
    
    regrasMovimentacao.validar();
    
    return regrasMovimentacao.getMensagens();
  }
  






  public Movimentacao cadastrarMovimentacao(Movimentacao movimentacao, boolean ehPersistivel)
    throws br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException
  {
    List mensagens = analisarMovimentacao(movimentacao);
    
    if (movimentacaoSemErros(mensagens)) {
      try
      {
        if (ehPersistivel) {
          daoMovimentacao.cadastrar(movimentacao);
        }
        
        return completarMovimentacao(movimentacao);
      }
      catch (br.gov.dataprev.caged.captacao.integracao.exception.IntegrationException e) {
        throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage());
      }
    }
    
    return null;
  }
  
  public void removerMovimentacao(Long id) throws br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException
  {
    try {
      daoMovimentacao.remover(id);
    }
    catch (br.gov.dataprev.caged.captacao.integracao.exception.IntegrationException e) {
      e.printStackTrace();
      throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage());
    }
  }
  
  public Movimentacao migrarMovimentacao(Movimentacao movimentacao, boolean ehPersistivel, int tipoAtualizacao) throws br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException
  {
    try {
      Movimentacao movRecuperada = movimentacao;
      Movimentacao retorno = movimentacao;
      br.gov.dataprev.caged.modelo.captacao.declaracao.movimentacao.AcertoCAGED acerto = new br.gov.dataprev.caged.modelo.captacao.declaracao.movimentacao.AcertoCAGED(movimentacao);
      
      if (ehPersistivel)
      {
        movRecuperada = daoMovimentacao.obter(movimentacao.getId());
        daoMovimentacao.remover(movimentacao.getId());
        retorno = completarMovimentacao(movRecuperada);
        
        AcertoCAGEDJPADAO acertoDAO = new AcertoCAGEDJPADAO(entityManager);
        acerto.setDecEstabelecimento(retorno.getDecEstabelecimento());
        acerto.setId(null);
        
        CompetenciaCAGED competencia = new CompetenciaCAGED(movimentacao.getDataAdmissao());
        acerto.setCompetencia(competencia);
        acerto.setAtualizacao(tipoAtualizacao);
        acertoDAO.cadastrar(acerto);
      }
      
      return acerto;
    }
    catch (br.gov.dataprev.caged.captacao.integracao.exception.IntegrationException e) {
      e.printStackTrace();
      throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage());
    }
  }
  









  public Boolean existeMovimentacao(Integer tipoDocIdentificacao, String numDocIdentificacao, String nit, Integer codMovimento, Date dataAdmissao, String cbo)
    throws br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException
  {
    try
    {
      return daoMovimentacao.existeMovimentacao(tipoDocIdentificacao, numDocIdentificacao, nit, codMovimento, dataAdmissao, cbo);
    } catch (br.gov.dataprev.caged.captacao.integracao.exception.IntegrationException e) {
      e.printStackTrace();
      throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage());
    }
  }
  












  public DeclaracaoCAGED migrarMovimentacoesDeclaracaoEmACI(DeclaracaoCAGED declaracao, int mesCompetencia, int anoCompetencia)
    throws br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException
  {
    List movimentacoesParaExcluir = new ArrayList();
    
    declaracao = migrarMovimentacoesDeclaracao(declaracao, mesCompetencia, anoCompetencia, movimentacoesParaExcluir);
    
    gravarDeclaracaoComGrafo(declaracao, movimentacoesParaExcluir);
    return declaracao;
  }
  
  private void gravarDeclaracaoComGrafo(DeclaracaoCAGED declaracao, List movimentacoesParaExcluir) throws br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException
  {
    try {
      Iterator it = movimentacoesParaExcluir.iterator();
      while (it.hasNext()) {
        Movimentacao mov = (Movimentacao)it.next();
        daoMovimentacao.remover(mov.getId());
      }
      if (declaracao.getDeclaracaoesEstabelecimento() != null) {
        for (DeclaracaoEstabelecimentoCAGED declaracaoEstabelecimentoCAGED : declaracao.getDeclaracaoesEstabelecimento()) {
          if (declaracaoEstabelecimentoCAGED.getListaDeAcertos() != null) {
            for (Object acertos : declaracaoEstabelecimentoCAGED.getListaDeAcertos()) {
              daoAcerto.alterar((br.gov.dataprev.caged.modelo.captacao.declaracao.movimentacao.AcertoCAGED)acertos);
            }
          } else {
            throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException("Não há acertos cadastrados.");
          }
        }
      } else {
        throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException("Não há estabelecimentos cadastrados.");
      }
      

      daoDeclaracao.alterar(declaracao);
    }
    catch (br.gov.dataprev.caged.captacao.integracao.exception.IntegrationException e) {
      e.printStackTrace();
      throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage());
    }
  }
  
  public DeclaracaoCAGED unificarDeclaracaoEmACI(DeclaracaoCAGED declaracaoNova, DeclaracaoCAGED declaracaoExistente, int mesCompetencia, int anoCompetencia) throws br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException
  {
    List movimentacoesParaExcluir = new ArrayList();
    
    declaracaoNova = migrarMovimentacoesDeclaracao(declaracaoNova, mesCompetencia, anoCompetencia, movimentacoesParaExcluir);
    
    DeclaracaoCAGED declaracaoUnificada = captacaoDeclaracao.unificarDeclaracoes(declaracaoNova, declaracaoExistente);
    try
    {
      declaracaoNova = daoDeclaracao.obter(declaracaoNova.getId());
      
      gravarDeclaracaoComGrafo(declaracaoUnificada, movimentacoesParaExcluir);
      
      daoDeclaracao.excluirSemTratarDetached(declaracaoNova);
    }
    catch (br.gov.dataprev.caged.captacao.integracao.exception.IntegrationException e) {
      e.printStackTrace();
      throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage());
    }
    
    return declaracaoUnificada;
  }
  












  public DeclaracaoCAGED migrarMovimentacoesDeclaracao(DeclaracaoCAGED declaracao, int mesCompetencia, int anoCompetencia, List movimentacoesParaExcluir)
    throws br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException
  {
    if (declaracao.getDeclaracaoesEstabelecimento() != null) {
      for (DeclaracaoEstabelecimentoCAGED declaracaoEstabelecimentoCAGED : declaracao.getDeclaracaoesEstabelecimento()) {
        if (declaracaoEstabelecimentoCAGED.getListaDeMovimentacoes() != null)
        {
          for (Object movimentacaoNaoCasteada : declaracaoEstabelecimentoCAGED.getListaDeMovimentacoes()) {
            br.gov.dataprev.caged.modelo.captacao.declaracao.movimentacao.MovimentacaoCAGED movimentacaoCasteada = (br.gov.dataprev.caged.modelo.captacao.declaracao.movimentacao.MovimentacaoCAGED)movimentacaoNaoCasteada;
            

            br.gov.dataprev.caged.modelo.captacao.declaracao.movimentacao.AcertoCAGED acerto = new br.gov.dataprev.caged.modelo.captacao.declaracao.movimentacao.AcertoCAGED(movimentacaoCasteada);
            
            acerto.setAtualizacao(2);
            acerto.setCompetencia(declaracao.getCompetencia());
            acerto.setDecEstabelecimento(declaracaoEstabelecimentoCAGED);
            
            declaracaoEstabelecimentoCAGED.getListaDeAcertos().add(acerto);
            movimentacoesParaExcluir.add(movimentacaoCasteada);
            movimentacaoCasteada.setDecEstabelecimento(null);
          }
          for (Object object : movimentacoesParaExcluir) {
            declaracaoEstabelecimentoCAGED.getListaDeMovimentacoes().remove(object);
          }
        } else {
          throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException("Não há movimentações cadastradas.");
        }
      }
    } else {
      throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException("Não há estabelecimentos cadastrados.");
    }
    

    CompetenciaCAGED competencia = new CompetenciaCAGED(mesCompetencia, anoCompetencia);
    declaracao.setCompetencia(competencia);
    
    return declaracao;
  }
  
  private boolean movimentacaoSemErros(List mensagens)
  {
    if (mensagens.size() == 0) { return true;
    }
    Iterator it = mensagens.iterator();
    
    while (it.hasNext()) {
      if (((MensagemValidacao)it.next() instanceof ErroValidacao)) return false;
    }
    return true;
  }
  
  private Movimentacao completarMovimentacao(Movimentacao movimentacao)
    throws br.gov.dataprev.caged.captacao.integracao.exception.IntegrationException
  {
    try
    {
      CBO cbo = sdcService.obter(new CBO(movimentacao.getCbo().getCodigo()));
      movimentacao.setCbo(cbo);
    } catch (br.gov.dataprev.caged.componente.analisador.integracao.service.IntegrationException e) {
      e.printStackTrace();
    }
    





    movimentacao.setTipoMovimento(movimentacao.getTipoMovimento());
    



    return movimentacao;
  }
  






  public List listarMovimentacoes(CompetenciaCAGED competencia, String cnpj, TipoMovimento tipo)
    throws br.gov.dataprev.caged.captacao.integracao.exception.IntegrationException
  {
    List listaDeclaracaoEstabelecimento = daoDeclaracaoEstabelecimento.listarDeclaracoesEstabelecimento(competencia, cnpj, tipo);
    
    List listaNovaDeclaracao = new ArrayList();
    
    for (int i = 0; i < listaDeclaracaoEstabelecimento.size(); i++)
    {
      DeclaracaoEstabelecimentoCAGED declaracao = (DeclaracaoEstabelecimentoCAGED)listaDeclaracaoEstabelecimento.get(i);
      

      declaracao.setTotalAdmissoes();
      declaracao.setTotalDesligamentos();
    }
    
    if (tipo == null) { return listaDeclaracaoEstabelecimento;
    }
    for (int i = 0; i < listaDeclaracaoEstabelecimento.size(); i++)
    {
      DeclaracaoEstabelecimentoCAGED declaracao = (DeclaracaoEstabelecimentoCAGED)listaDeclaracaoEstabelecimento.get(i);
      

      List movimentacoes = declaracao.getListaDeMovimentacoes();
      
      if (movimentacoes != null)
      {
        List movimentacoesNovas = new ArrayList();
        Iterator itMovimentacoes = movimentacoes.iterator();
        
        while (itMovimentacoes.hasNext())
        {
          Movimentacao movimentacao = (Movimentacao)itMovimentacoes.next();
          
          if (movimentacao.getTipoMovimento().getClass() == tipo.getClass())
          {
            if ((tipo.getId() == null) || (movimentacao.getTipoMovimento().equals(tipo))) {
              movimentacoesNovas.add(movimentacao);
            }
          }
        }
        declaracao.setListaDeMovimentacoes(movimentacoesNovas);
        listaNovaDeclaracao.add(declaracao);
      }
    }
    
    return listaNovaDeclaracao;
  }
  











  public Boolean existeAcerto(Integer tipoDocIdentificacao, String numDocIdentificacao, String nit, Integer codMovimento, String competenciaEstabelecimentoAno, String competenciaEstabelecimentoMes, Date dataAdmissao, String cbo)
    throws br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException
  {
    try
    {
      return daoAcerto.existeAcerto(tipoDocIdentificacao, numDocIdentificacao, nit, codMovimento, competenciaEstabelecimentoAno, competenciaEstabelecimentoMes, dataAdmissao, cbo);
    } catch (br.gov.dataprev.caged.captacao.integracao.exception.IntegrationException e) {
      e.printStackTrace();
      throw new br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException(e.getMessage());
    }
  }
  





  public List listarMovimentacoesAcertos(CompetenciaCAGED competencia, String cnpj)
    throws br.gov.dataprev.caged.captacao.integracao.exception.IntegrationException
  {
    List<DeclaracaoEstabelecimentoCAGED> listaDeclaracaoEstabelecimento = daoDeclaracaoEstabelecimento.listarDeclaracoesEstabelecimentoAcertos(competencia, cnpj);
    List<DeclaracaoEstabelecimentoCAGED> listaRetorno = new ArrayList();
    for (DeclaracaoEstabelecimentoCAGED t : listaDeclaracaoEstabelecimento) {
      if (t.getListaDeAcertos().size() == 0) {
        listaRetorno.add(t);
      }
    }
    listaDeclaracaoEstabelecimento.removeAll(listaRetorno);
    return listaDeclaracaoEstabelecimento;
  }
  
  public List listarMovimentacoesPisZerados(CompetenciaCAGED competencia, String cnpj)
    throws br.gov.dataprev.caged.captacao.integracao.exception.IntegrationException
  {
    List listaDeclaracaoEstabelecimento = daoDeclaracaoEstabelecimento.listarDeclaracoesEstabelecimentoPisZerados(competencia, cnpj);
    List novaListaDeclaracao = new ArrayList();
    

    for (int i = 0; i < listaDeclaracaoEstabelecimento.size(); i++) {
      DeclaracaoEstabelecimentoCAGED declaracao = (DeclaracaoEstabelecimentoCAGED)listaDeclaracaoEstabelecimento.get(i);
      

      declaracao.setTotalAdmissoes();
      declaracao.setTotalDesligamentos();
      
      List movimentacoes = declaracao.getListaDeMovimentacoes();
      if (movimentacoes != null) {
        List movimentacoesNovas = new ArrayList();
        Iterator itMovimentacoes = movimentacoes.iterator();
        while (itMovimentacoes.hasNext()) {
          Movimentacao movimentacao = (Movimentacao)itMovimentacoes.next();
          if (movimentacao.getTrabalhador().getNit().equals("00000000000")) {
            movimentacoesNovas.add(movimentacao);
          }
        }
        
        declaracao.setListaDeMovimentacoes(movimentacoesNovas);
      }
      
      novaListaDeclaracao.add(declaracao);
    }
    
    return novaListaDeclaracao;
  }
  

  public List listarMovimentacoesPisZerados(DeclaracaoCAGED declaracaoCAGED)
    throws br.gov.dataprev.caged.captacao.integracao.exception.IntegrationException
  {
    List listaDeclaracaoEstabelecimento = declaracaoCAGED.getDeclaracaoesEstabelecimento();
    List novaListaDeclaracao = new ArrayList();
    

    if (listaDeclaracaoEstabelecimento != null) {
      for (int i = 0; i < listaDeclaracaoEstabelecimento.size(); i++) {
        DeclaracaoEstabelecimentoCAGED declaracao = (DeclaracaoEstabelecimentoCAGED)listaDeclaracaoEstabelecimento.get(i);
        

        declaracao.setTotalAdmissoes();
        declaracao.setTotalDesligamentos();
        
        List movimentacoes = declaracao.getListaDeMovimentacoes();
        
        if (movimentacoes != null) {
          List movimentacoesNovas = new ArrayList();
          Iterator itMovimentacoes = movimentacoes.iterator();
          while (itMovimentacoes.hasNext()) {
            Movimentacao movimentacao = (Movimentacao)itMovimentacoes.next();
            if (movimentacao.getTrabalhador().getNit().equals("00000000000")) {
              movimentacoesNovas.add(movimentacao);
            }
          }
          
          declaracao.setListaDeMovimentacoes(movimentacoesNovas);
        }
        
        if ((declaracao.getListaDeMovimentacoes() != null) && 
          (declaracao.getListaDeMovimentacoes().size() > 0))
          novaListaDeclaracao.add(declaracao);
      }
    }
    return novaListaDeclaracao;
  }
}
