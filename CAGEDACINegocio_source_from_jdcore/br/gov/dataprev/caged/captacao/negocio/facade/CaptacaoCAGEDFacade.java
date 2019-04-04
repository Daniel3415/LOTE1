package br.gov.dataprev.caged.captacao.negocio.facade;

import br.gov.dataprev.caged.captacao.integracao.dao.IArquivoCAGEDXmlDAO;
import br.gov.dataprev.caged.captacao.integracao.desktop.dao.ArquivoCAGEDXmlDAO;
import br.gov.dataprev.caged.captacao.integracao.desktop.service.SDCCaptacaoCAGEDServiceDesktop;
import br.gov.dataprev.caged.captacao.integracao.util.JPAPersistenceUtils;
import br.gov.dataprev.caged.captacao.modelo.autorizado.AutorizadoCAGED;
import br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoCAGED;
import br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoEstabelecimentoCAGED;
import br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException;
import br.gov.dataprev.caged.captacao.negocio.service.CaptacaoAutorizadoCAGEDService;
import br.gov.dataprev.caged.captacao.negocio.service.CaptacaoDeclaracaoCAGEDService;
import br.gov.dataprev.caged.captacao.negocio.service.CaptacaoDeclaracaoEstabelecimentoCAGEDService;
import br.gov.dataprev.caged.captacao.negocio.service.CaptacaoMovimentacaoCAGEDService;
import br.gov.dataprev.caged.captacao.negocio.service.DeclaracaoAtivaSingleton;
import br.gov.dataprev.caged.captacao.negocio.service.ICaptacaoAutorizadoCAGEDService;
import br.gov.dataprev.caged.captacao.negocio.service.ICaptacaoDeclaracaoCAGEDService;
import br.gov.dataprev.caged.captacao.negocio.service.ICaptacaoEstabelecimentoCAGEDService;
import br.gov.dataprev.caged.captacao.negocio.service.ICaptacaoMovimentacaoCAGEDService;
import br.gov.dataprev.caged.captacao.negocio.to.AutorizadoCAGEDTO;
import br.gov.dataprev.caged.captacao.negocio.to.DeclaracaoCAGEDTO;
import br.gov.dataprev.caged.captacao.negocio.to.DeclaracaoEstabelecimentoCAGEDTO;
import br.gov.dataprev.caged.captacao.negocio.to.MovimentacaoCAGEDTO;
import br.gov.dataprev.caged.captacao.negocio.util.ConversorAutorizadoCAGED;
import br.gov.dataprev.caged.captacao.negocio.util.ConversorDeclaracaoCAGED;
import br.gov.dataprev.caged.captacao.negocio.util.ConversorDeclaracaoEstabelecimentoCAGED;
import br.gov.dataprev.caged.captacao.negocio.util.ConversorMovimentacao;
import br.gov.dataprev.caged.componente.analisador.integracao.service.ISDCAnalisadorCAGEDService;
import br.gov.dataprev.caged.componente.analisador.modelo.SalarioMinimo;
import br.gov.dataprev.caged.componente.analisador.modelo.SituacaoDeclaracao;
import br.gov.dataprev.caged.componente.analisador.novalidacao.NoValidacao;
import br.gov.dataprev.caged.modelo.captacao.declaracao.movimentacao.AcertoCAGED;
import br.gov.dataprev.caged.modelo.captacao.declaracao.movimentacao.Movimentacao;
import br.gov.dataprev.caged.modelo.captacao.declaracao.movimentacao.trabalhador.Trabalhador;
import br.gov.dataprev.caged.modelo.competencia.CompetenciaCAGED;
import br.gov.dataprev.caged.modelo.declaracao.estabelecimento.EnderecoCAGED;
import br.gov.dataprev.caged.modelo.declaracao.estabelecimento.Porte;
import br.gov.dataprev.caged.modelo.declaracao.movimentacao.TipoDesligamento;
import br.gov.dataprev.caged.modelo.declaracao.movimentacao.TipoMovimento;
import br.gov.dataprev.caged.modelo.declaracao.movimentacao.trabalhador.Deficiencia;
import br.gov.dataprev.caged.modelo.declaracao.movimentacao.trabalhador.GrauInstrucao;
import br.gov.dataprev.caged.modelo.declaracao.movimentacao.trabalhador.Raca;
import br.gov.dataprev.caged.modelo.endereco.CEP;
import br.gov.dataprev.caged.modelo.endereco.UF;
import br.gov.dataprev.caged.modelo.estabelecimento.AtividadeEconomica;
import br.gov.dataprev.caged.modelo.vinculo.CBO;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import org.apache.commons.beanutils.BeanUtils;










public class CaptacaoCAGEDFacade
  implements ICaptacaoCAGEDFacade
{
  ISDCAnalisadorCAGEDService sdcService;
  ICaptacaoDeclaracaoCAGEDService declaracaoService;
  ICaptacaoMovimentacaoCAGEDService movimentacaoService;
  ICaptacaoAutorizadoCAGEDService autorizadoService;
  ICaptacaoEstabelecimentoCAGEDService estabelecimentoService;
  IArquivoCAGEDXmlDAO arquivocaged;
  private static final String PERSISTENCE_UNIT_NAME = "caged-desktop";
  private static EntityManager entityManager;
  
  public CaptacaoCAGEDFacade(boolean ehArquivo)
  {
    this(ehArquivo, null);
  }
  
  public CaptacaoCAGEDFacade(boolean ehArquivo, String diretorio) {
    if (entityManager == null) {
      if (diretorio != null) {
        Map<String, String> overrrides = new HashMap();
        overrrides.put("eclipselink.jdbc.url", "jdbc:hsqldb:file:" + diretorio + "cagedDB");
        overrrides.put("eclipselink.connection.url", "jdbc:hsqldb:file:" + diretorio + "cagedDB");
        entityManager = JPAPersistenceUtils.getEntityManager("caged-desktop", overrrides);
      } else {
        entityManager = JPAPersistenceUtils.getEntityManager("caged-desktop");
      }
    }
    
    checkSequencies();
    
    sdcService = new SDCCaptacaoCAGEDServiceDesktop(entityManager);
    arquivocaged = new ArquivoCAGEDXmlDAO();
    if (!ehArquivo) {
      declaracaoService = new CaptacaoDeclaracaoCAGEDService(entityManager);
      movimentacaoService = new CaptacaoMovimentacaoCAGEDService(entityManager);
      autorizadoService = new CaptacaoAutorizadoCAGEDService(entityManager);
      estabelecimentoService = new CaptacaoDeclaracaoEstabelecimentoCAGEDService(entityManager);
    }
  }
  
  public ISDCAnalisadorCAGEDService getSdc() {
    return sdcService;
  }
  
  public EntityManager getEntityManager() {
    return entityManager;
  }
  
  public EntityTransaction getTransaction() {
    return entityManager.getTransaction();
  }
  
  public void releaseResources() throws br.gov.dataprev.caged.captacao.integracao.exception.IntegrationException {
    try {
      entityManager.close();
    } catch (RuntimeException e) {
      throw new br.gov.dataprev.caged.captacao.integracao.exception.IntegrationException("Could not close EntityManager", e);
    }
  }
  
  public void shutdown() throws br.gov.dataprev.caged.captacao.integracao.exception.IntegrationException {
    getTransaction().begin();
    entityManager.createNativeQuery("SHUTDOWN").executeUpdate();
    releaseResources();
  }
  
  public DeclaracaoEstabelecimentoCAGEDTO converte(AutorizadoCAGEDTO toEntrada)
  {
    autorizadoService = new CaptacaoAutorizadoCAGEDService(entityManager);
    estabelecimentoService = new CaptacaoDeclaracaoEstabelecimentoCAGEDService(entityManager);
    
    DeclaracaoEstabelecimentoCAGEDTO toSaida = new DeclaracaoEstabelecimentoCAGEDTO();
    try {
      toEntrada = ConversorAutorizadoCAGED.load(autorizadoService.obterAutorizado(1L));
      BeanUtils.copyProperties(toSaida, toEntrada);
      List listaEstabelecimentoComRazaoSocial = estabelecimentoService.obterListaEstabelecimentos(toSaida.getNumeroDocumentoIdentificacao(), toSaida.getTipoDocumentoIdentificacao(), toSaida.getRazaoSocial());
      if ((listaEstabelecimentoComRazaoSocial != null) && (listaEstabelecimentoComRazaoSocial.size() == 1) && (listaEstabelecimentoComRazaoSocial.get(0) != null))
      {

        DeclaracaoEstabelecimentoCAGED temp = (DeclaracaoEstabelecimentoCAGED)listaEstabelecimentoComRazaoSocial.get(0);
        long id = temp.getId().longValue();
        toSaida.setId(id);
      }
      

      toSaida.setEhAutorizado(1);
    }
    catch (IllegalAccessException e)
    {
      e.printStackTrace();
    }
    catch (InvocationTargetException e) {
      e.printStackTrace();
    } catch (BusinessException e) {
      e.printStackTrace();
    }
    return toSaida;
  }
  
  public AutorizadoCAGEDTO converte(DeclaracaoEstabelecimentoCAGEDTO toEntrada)
  {
    autorizadoService = new CaptacaoAutorizadoCAGEDService(entityManager);
    
    AutorizadoCAGEDTO toSaida = new AutorizadoCAGEDTO();
    
    try
    {
      AutorizadoCAGEDTO autorizado = ConversorAutorizadoCAGED.load(autorizadoService.obterAutorizado(1L));
      BeanUtils.copyProperties(toSaida, toEntrada);
      toSaida.setNumeroDD(autorizado.getNumeroDD());
      toSaida.setNuCPF(autorizado.getNuCPF());
      toSaida.setEmailContato(autorizado.getEmailContato());
      toSaida.setNumeroTelefone(autorizado.getNumeroTelefone());
      toSaida.setNumeroRamal(autorizado.getNumeroRamal());
      toSaida.setNomeContato(autorizado.getNomeContato());

    }
    catch (IllegalAccessException e)
    {
      e.printStackTrace();
    }
    catch (InvocationTargetException e) {
      e.printStackTrace();
    } catch (BusinessException e) {
      e.printStackTrace();
    }
    return toSaida;
  }
  



  public CEP cepEhValido(CEP cep)
    throws BusinessException
  {
    try
    {
      return sdcService.obterCEP(cep);
    } catch (br.gov.dataprev.caged.componente.analisador.integracao.service.IntegrationException ie) {
      throw new BusinessException(ie.getMessage(), ie);
    }
  }
  




  public boolean cepEhCompativel(CEP cep, UF uf)
    throws BusinessException
  {
    try
    {
      return sdcService.cepEhCompativel(cep, uf);
    } catch (br.gov.dataprev.caged.componente.analisador.integracao.service.IntegrationException ie) {
      throw new BusinessException(ie.getMessage(), ie);
    }
  }
  
  public List obterListaDDD(UF uf)
  {
    List listaDDD = new ArrayList();
    try
    {
      listaDDD = sdcService.obterListaDDD(uf);
    }
    catch (br.gov.dataprev.caged.componente.analisador.integracao.service.IntegrationException ie) {
      ie.printStackTrace();
    }
    
    return listaDDD;
  }
  





  public AtividadeEconomica obterAtividadeEconomica(AtividadeEconomica atividade)
    throws BusinessException
  {
    try
    {
      return sdcService.obterAtividadeEconomica(atividade);
    } catch (br.gov.dataprev.caged.componente.analisador.integracao.service.IntegrationException ie) {
      throw new BusinessException(ie.getMessage(), ie);
    }
  }
  




  public List obterListaAtividadeEconomica()
    throws BusinessException
  {
    try
    {
      return sdcService.obterListaAtividadeEconomica();
    } catch (br.gov.dataprev.caged.componente.analisador.integracao.service.IntegrationException ie) {
      throw new BusinessException(ie.getMessage(), ie);
    }
  }
  







  public List obterListaAtividadeEconomica(String codigo, String descricao)
    throws BusinessException
  {
    try
    {
      return sdcService.obterListaAtividadeEconomica(codigo, descricao);
    } catch (br.gov.dataprev.caged.componente.analisador.integracao.service.IntegrationException ie) {
      throw new BusinessException(ie.getMessage(), ie);
    }
  }
  





  public CBO obterCBO(CBO cbo)
    throws BusinessException
  {
    try
    {
      return sdcService.obter(cbo);
    } catch (br.gov.dataprev.caged.componente.analisador.integracao.service.IntegrationException ie) {
      throw new BusinessException(ie.getMessage(), ie);
    }
  }
  




  public List obterListaCBO()
    throws BusinessException
  {
    try
    {
      return sdcService.obterListaCBO();
    } catch (br.gov.dataprev.caged.componente.analisador.integracao.service.IntegrationException ie) {
      throw new BusinessException(ie.getMessage());
    }
  }
  







  public List obterListaCBO(String codigo, String descricao)
    throws BusinessException
  {
    try
    {
      return sdcService.obterListaCBO(codigo, descricao);
    } catch (br.gov.dataprev.caged.componente.analisador.integracao.service.IntegrationException ie) {
      throw new BusinessException(ie.getMessage());
    }
  }
  
  public UF obterUF(UF uf) throws BusinessException
  {
    try {
      return sdcService.obterUF(uf);
    } catch (br.gov.dataprev.caged.componente.analisador.integracao.service.IntegrationException ie) {
      throw new BusinessException(ie.getMessage());
    }
  }
  







  public List obterListaUF()
    throws BusinessException
  {
    try
    {
      return sdcService.obterListaUF();
    } catch (br.gov.dataprev.caged.componente.analisador.integracao.service.IntegrationException ie) {
      throw new BusinessException(ie.getMessage());
    }
  }
  
  public EnderecoCAGED obterEndereco(CEP cep) throws BusinessException {
    return null;
  }
  



  public List obterListaGrauInstrucao()
    throws BusinessException
  {
    try
    {
      return sdcService.obterListaGrauInstrucao();
    } catch (br.gov.dataprev.caged.componente.analisador.integracao.service.IntegrationException ie) {
      throw new BusinessException(ie.getMessage());
    }
  }
  
  public GrauInstrucao obterGrauInstrucao(GrauInstrucao instrucao) throws BusinessException
  {
    try
    {
      return sdcService.obterGrauInstrucao(instrucao);
    } catch (br.gov.dataprev.caged.componente.analisador.integracao.service.IntegrationException ie) {
      throw new BusinessException(ie.getMessage());
    }
  }
  



  public List obterListaRaca()
    throws BusinessException
  {
    try
    {
      return sdcService.obterListaRaca();
    } catch (br.gov.dataprev.caged.componente.analisador.integracao.service.IntegrationException ie) {
      throw new BusinessException(ie.getMessage());
    }
  }
  
  public Raca obterRaca(Raca raca) throws BusinessException {
    return null;
  }
  
  public List obterListaDeficiencia() throws BusinessException
  {
    try {
      return sdcService.obterListaDeficiencia();
    } catch (br.gov.dataprev.caged.componente.analisador.integracao.service.IntegrationException ie) {
      throw new BusinessException(ie.getMessage());
    }
  }
  
  public Deficiencia obterDeficiencia(Deficiencia deficiencia) throws BusinessException
  {
    return null;
  }
  
  public List obterListaTipoMovimentacao() throws BusinessException
  {
    try {
      return sdcService.obterListaTipoMovimento();
    } catch (br.gov.dataprev.caged.componente.analisador.integracao.service.IntegrationException ie) {
      throw new BusinessException(ie.getMessage());
    }
  }
  
  public List obterListaCodigoTipoDesligamento() throws BusinessException
  {
    List codigosDesligamentos = new ArrayList();
    try
    {
      List tiposDesligamentos = sdcService.obterListaTipoDesligamento();
      Iterator it = tiposDesligamentos.iterator();
      
      while (it.hasNext()) {
        TipoDesligamento desligamento = (TipoDesligamento)it.next();
        codigosDesligamentos.add(String.valueOf(desligamento.getId()));
      }
    } catch (br.gov.dataprev.caged.componente.analisador.integracao.service.IntegrationException ie) {
      throw new BusinessException(ie.getMessage());
    }
    
    return codigosDesligamentos;
  }
  
  public List obterListaTipoDesligamento() throws BusinessException
  {
    List listaTiposDesligamento = new ArrayList();
    try
    {
      listaTiposDesligamento = sdcService.obterListaTipoDesligamento();
    } catch (br.gov.dataprev.caged.componente.analisador.integracao.service.IntegrationException ie) {
      throw new BusinessException(ie.getMessage());
    }
    
    return listaTiposDesligamento;
  }
  
  public List obterListaTipoAdmissao() throws BusinessException
  {
    List listaTiposAdmissao = new ArrayList();
    try
    {
      listaTiposAdmissao = sdcService.obterListaTipoAdmissao();
    } catch (br.gov.dataprev.caged.componente.analisador.integracao.service.IntegrationException ie) {
      throw new BusinessException(ie.getMessage());
    }
    
    return listaTiposAdmissao;
  }
  
  public TipoMovimento obterTipoMovimentacao(TipoMovimento tipoMovimentacao) throws BusinessException
  {
    return null;
  }
  







  public MovimentacaoCAGEDTO cadastrarMovimentacao(MovimentacaoCAGEDTO movimentacaoTo, boolean ehPersistivel)
    throws BusinessException
  {
    Movimentacao movimentacao = montarMovimentacao(movimentacaoTo);
    Movimentacao movimentacaoAlterada = null;
    try {
      getTransaction().begin();
      
      movimentacaoAlterada = movimentacaoService.cadastrarMovimentacao(movimentacao, ehPersistivel);
      

      getTransaction().commit();
    }
    catch (BusinessException be) {
      if (getTransaction().isActive())
        getTransaction().rollback();
      be.printStackTrace();
      throw new BusinessException("Cadastro de movimentação não pode ser efetivado. Favor, tente novamente ou entre em contato com o suporte.", be);
    }
    catch (Throwable e) {
      if (getTransaction().isActive())
        getTransaction().rollback();
      e.printStackTrace();
      throw new BusinessException("Cadastro de movimentação não pode ser efetivado. Favor, tente novamente ou entre em contato com o suporte.", e);
    }
    
    return ConversorMovimentacao.loadTransferObject(movimentacaoAlterada, entityManager);
  }
  











  public Boolean existeMovimentacao(Integer tipoDocIdentificacao, String numDocIdentificacao, String nit, Integer codMovimento, Date dataAdmissao, String cbo)
    throws BusinessException
  {
    try
    {
      return movimentacaoService.existeMovimentacao(tipoDocIdentificacao, numDocIdentificacao, nit, codMovimento, dataAdmissao, cbo);
    }
    catch (BusinessException e) {
      e.printStackTrace();
      throw new BusinessException("Não foi possível verificar a movimentação.", e);
    }
  }
  




  public void removerMovimentacao(long id)
    throws BusinessException
  {
    try
    {
      getTransaction().begin();
      
      movimentacaoService.removerMovimentacao(new Long(id));
      
      getTransaction().commit();
    }
    catch (BusinessException be) {
      if (getTransaction().isActive())
        getTransaction().rollback();
      be.printStackTrace();
      throw new BusinessException("Exclusão de movimentação não pode ser efetivada. Favor, tente novamente ou entre em contato com o suporte.", be);
    }
    catch (Throwable e) {
      if (getTransaction().isActive())
        getTransaction().rollback();
      e.printStackTrace();
      throw new BusinessException("Exclusão de movimentação não pode ser efetivada. Favor, tente novamente ou entre em contato com o suporte.", e);
    }
  }
  







  public MovimentacaoCAGEDTO alterarMovimentacao(MovimentacaoCAGEDTO movimentacaoTo, boolean ehPersistivel)
    throws BusinessException
  {
    Movimentacao movimentacao = montarMovimentacao(movimentacaoTo);
    Movimentacao movimentacaoAlterada = null;
    try {
      getTransaction().begin();
      
      movimentacaoAlterada = movimentacaoService.alterarMovimentacao(movimentacao, ehPersistivel);
      
      getTransaction().commit();
    }
    catch (BusinessException be) {
      if (getTransaction().isActive())
        getTransaction().rollback();
      be.printStackTrace();
      throw new BusinessException("Alteração de movimentação não pode ser efetivada. Favor, tente novamente ou entre em contato com o suporte.", be);
    }
    catch (Throwable e) {
      if (getTransaction().isActive())
        getTransaction().rollback();
      e.printStackTrace();
      throw new BusinessException("Alteração de movimentação não pode ser efetivada. Favor, tente novamente ou entre em contato com o suporte.", e);
    }
    
    return ConversorMovimentacao.loadTransferObject(movimentacaoAlterada, entityManager);
  }
  







  public MovimentacaoCAGEDTO migrarMovimentacao(MovimentacaoCAGEDTO movimentacaoTo, boolean ehPersistivel, int tipoAtualizacao)
    throws BusinessException
  {
    Movimentacao movimentacao = montarMovimentacao(movimentacaoTo);
    
    Movimentacao movimentacaoAlterada = null;
    try {
      getTransaction().begin();
      
      movimentacaoAlterada = movimentacaoService.migrarMovimentacao(movimentacao, ehPersistivel, tipoAtualizacao);
      
      getTransaction().commit();
    }
    catch (BusinessException be)
    {
      if (getTransaction().isActive())
        getTransaction().rollback();
      be.printStackTrace();
      throw new BusinessException("Migração de movimentação não pode ser efetivada. Favor, tente novamente ou entre em contato com o suporte.", be);
    }
    catch (Throwable e) {
      if (getTransaction().isActive())
        getTransaction().rollback();
      e.printStackTrace();
      throw new BusinessException("Migração de movimentação não pode ser efetivada. Favor, tente novamente ou entre em contato com o suporte.", e);
    }
    return ConversorMovimentacao.loadTransferObject(movimentacaoAlterada, entityManager);
  }
  
  public DeclaracaoCAGEDTO migrarMovimentacoesDeclaracaoEmACI(DeclaracaoCAGEDTO declaracaoTo, int mesCompetencia, int anoCompetencia) throws BusinessException
  {
    DeclaracaoCAGED declaracao = montarDeclaracao(declaracaoTo);
    try
    {
      getTransaction().begin();
      
      declaracao = movimentacaoService.migrarMovimentacoesDeclaracaoEmACI(declaracao, mesCompetencia, anoCompetencia);
      
      getTransaction().commit();
    }
    catch (BusinessException be) {
      if (getTransaction().isActive())
        getTransaction().rollback();
      be.printStackTrace();
      throw new BusinessException("Migração de movimentações não pode ser efetivada. Favor, tente novamente ou entre em contato com o suporte.", be);
    }
    catch (Throwable e) {
      if (getTransaction().isActive())
        getTransaction().rollback();
      e.printStackTrace();
      throw new BusinessException("Migração de movimentações não pode ser efetivada. Favor, tente novamente ou entre em contato com o suporte.", e);
    }
    
    return ConversorDeclaracaoCAGED.loadTransferObject(declaracao, entityManager);
  }
  
  public DeclaracaoCAGEDTO unificarDeclaracaoEmACI(DeclaracaoCAGEDTO declaracaoNovaTO, DeclaracaoCAGEDTO declaracaoExistenteTO, int mesCompetencia, int anoCompetencia) throws BusinessException {
    DeclaracaoCAGED declaracaoNova = montarDeclaracao(declaracaoNovaTO);
    DeclaracaoCAGED declaracaoExistente = montarDeclaracao(declaracaoExistenteTO);
    DeclaracaoCAGED declaracao = null;
    try {
      getTransaction().begin();
      
      declaracao = movimentacaoService.unificarDeclaracaoEmACI(declaracaoNova, declaracaoExistente, mesCompetencia, anoCompetencia);
      
      getTransaction().commit();
    }
    catch (BusinessException be) {
      if (getTransaction().isActive())
        getTransaction().rollback();
      be.printStackTrace();
      throw new BusinessException("Unificação de declarações não pode ser efetivada. Favor, tente novamente ou entre em contato com o suporte.", be);
    }
    catch (Throwable e) {
      if (getTransaction().isActive())
        getTransaction().rollback();
      e.printStackTrace();
      throw new BusinessException("Unificação de declarações não pode ser efetivada. Favor, tente novamente ou entre em contato com o suporte.", e);
    }
    
    return ConversorDeclaracaoCAGED.loadTransferObject(declaracao, entityManager);
  }
  
  public DeclaracaoCAGEDTO migrarMovimentacoesDeclaracao(DeclaracaoCAGEDTO declaracaoTo, int mesCompetencia, int anoCompetencia) throws BusinessException
  {
    DeclaracaoCAGED declaracao = montarDeclaracao(declaracaoTo);
    try {
      getTransaction().begin();
      
      declaracao = movimentacaoService.migrarMovimentacoesDeclaracao(declaracao, mesCompetencia, anoCompetencia, new ArrayList());
      
      getTransaction().commit();
    }
    catch (BusinessException be) {
      if (getTransaction().isActive())
        getTransaction().rollback();
      be.printStackTrace();
      throw new BusinessException("Migração de movimentações não pode ser efetivada. Favor, tente novamente ou entre em contato com o suporte.", be);
    }
    catch (Throwable e) {
      if (getTransaction().isActive())
        getTransaction().rollback();
      e.printStackTrace();
      throw new BusinessException("Migração de movimentações não pode ser efetivada. Favor, tente novamente ou entre em contato com o suporte.", e);
    }
    return ConversorDeclaracaoCAGED.loadTransferObject(declaracao, entityManager);
  }
  




  public void cadastrarAutorizado(AutorizadoCAGEDTO autorizadoTO, boolean ehPersistivel)
    throws BusinessException
  {
    AutorizadoCAGED autorizado = ConversorAutorizadoCAGED.load(autorizadoTO);
    try {
      getTransaction().begin();
      
      autorizadoService.cadastrarAutorizado(autorizado, ehPersistivel);
      
      getTransaction().commit();
    }
    catch (BusinessException be) {
      if (getTransaction().isActive())
        getTransaction().rollback();
      be.printStackTrace();
      throw new BusinessException("Cadastro de autorizada não pode ser efetivado. Favor, tente novamente ou entre em contato com o suporte.", be);
    }
    catch (Throwable e) {
      if (getTransaction().isActive())
        getTransaction().rollback();
      e.printStackTrace();
      throw new BusinessException("Cadastro de autorizada não pode ser efetivado. Favor, tente novamente ou entre em contato com o suporte.", e);
    }
  }
  

  public void alterarAutorizado(AutorizadoCAGEDTO registro, boolean ehPersistivel)
    throws BusinessException
  {
    AutorizadoCAGED autorizado = ConversorAutorizadoCAGED.load(registro);
    try {
      getTransaction().begin();
      
      autorizadoService.alterarAutorizado(autorizado, ehPersistivel);
      
      getTransaction().commit();
    }
    catch (BusinessException be) {
      if (getTransaction().isActive())
        getTransaction().rollback();
      throw new BusinessException("Atualização de autorizado não pode ser efetivado. Favor, tente novamente ou entre em contato com o suporte.", be);
    }
    catch (Throwable e) {
      if (getTransaction().isActive()) {
        getTransaction().rollback();
      }
      throw new BusinessException("Atualização de autorizado não pode ser efetivado. Favor, tente novamente ou entre em contato com o suporte.", e);
    }
  }
  
  public List validarAutorizado(AutorizadoCAGEDTO registro)
    throws BusinessException
  {
    AutorizadoCAGED autorizado = ConversorAutorizadoCAGED.load(registro);
    return autorizadoService.analisarAutorizado(autorizado);
  }
  
  public AutorizadoCAGEDTO obterAutorizadoCAGEDTO() throws BusinessException
  {
    AutorizadoCAGED autorizado = new AutorizadoCAGED();
    AutorizadoCAGEDTO autorizadoRetorno = new AutorizadoCAGEDTO();
    autorizado = autorizadoService.obterAutorizado(1L);
    

    if (autorizado != null) {
      autorizadoRetorno = ConversorAutorizadoCAGED.load(autorizado);
    } else {
      autorizadoRetorno = null;
    }
    
    return autorizadoRetorno;
  }
  
  public List listarDeclaracoesCAGED()
    throws BusinessException
  {
    List listaDeclaracao = declaracaoService.listarDeclaracoesCAGED();
    List listaDeclaracaoTO = new ArrayList();
    Iterator it = listaDeclaracao.iterator();
    
    while (it.hasNext())
    {
      DeclaracaoCAGED dec = (DeclaracaoCAGED)it.next();
      DeclaracaoCAGEDTO decTO = new DeclaracaoCAGEDTO();
      
      decTO.setId(dec.getId());
      decTO.setAnoCompetencia(dec.getCompetencia().getAno());
      decTO.setMesCompetencia(dec.getCompetencia().getMes());
      decTO.setNuRecibo(dec.getNumeroRecibo());
      decTO.setSalarioMinimo(dec.getSalarioMinimo().doubleValue());
      try {
        decTO.setDescricaoSituacao(sdcService.obterSituacaoDeclaracao(dec.getSituacao()).getDescricao());
      }
      catch (br.gov.dataprev.caged.componente.analisador.integracao.service.IntegrationException e) {
        e.printStackTrace();
      } catch (NullPointerException e) {
        e.printStackTrace();
      }
      
      decTO.setCodigoSituacao(dec.getSituacao().getCodigo());
      
      if (dec.ehDeclaracaoAcerto()) {
        decTO.setTipo("Acerto");
      } else {
        decTO.setTipo("Normal");
      }
      
      listaDeclaracaoTO.add(decTO);
    }
    
    return listaDeclaracaoTO;
  }
  
  public List obterListaCompetencias() throws BusinessException
  {
    return declaracaoService.obterListaCompetencias();
  }
  
  public DeclaracaoCAGEDTO abrirDeclaracaoCAGED(CompetenciaCAGED competencia)
    throws BusinessException
  {
    DeclaracaoCAGED dec = declaracaoService.abrirDeclaracaoCAGED(competencia);
    
    DeclaracaoCAGEDTO decTO = ConversorDeclaracaoCAGED.loadTransferObject(dec, entityManager);
    



    DeclaracaoAtivaSingleton.getInstance().setDeclaracaoAtiva(decTO);
    DeclaracaoAtivaSingleton.getInstance().setNomeArquivoAtivo("");
    return decTO;
  }
  
  public DeclaracaoCAGEDTO buscarDeclaracaoNaCompetencia(CompetenciaCAGED competencia)
    throws BusinessException
  {
    DeclaracaoCAGED dec = declaracaoService.abrirDeclaracaoCAGED(competencia);
    
    DeclaracaoCAGEDTO decTO = null;
    
    if (dec != null) {
      decTO = ConversorDeclaracaoCAGED.loadTransferObject(dec, entityManager);
    }
    
    return decTO;
  }
  
  public DeclaracaoCAGEDTO cadastrarNovaDeclaracaoCAGED(DeclaracaoCAGEDTO declaracao)
    throws BusinessException
  {
    DeclaracaoCAGED declaracaoCAGED = new DeclaracaoCAGED();
    
    declaracaoCAGED.setSalarioMinimo(Double.valueOf(declaracao.getSalarioMinimo()));
    
    declaracaoCAGED.setCompetencia(new CompetenciaCAGED(declaracao.getMesCompetencia(), declaracao.getAnoCompetencia()));
    

    DeclaracaoCAGED dec = null;
    try {
      getTransaction().begin();
      
      dec = declaracaoService.cadastrarNovaDeclaracaoCAGED(declaracaoCAGED);
      
      getTransaction().commit();
    }
    catch (BusinessException be) {
      if (getTransaction().isActive())
        getTransaction().rollback();
      be.printStackTrace();
      throw be;
    }
    catch (Throwable e) {
      if (getTransaction().isActive())
        getTransaction().rollback();
      e.printStackTrace();
      throw new BusinessException("Cadastro de nova declaração não pode ser efetivado. Favor, tente novamente ou entre em contato com o suporte.", e);
    }
    
    DeclaracaoCAGEDTO decTO = ConversorDeclaracaoCAGED.loadTransferObject(dec, entityManager);
    



    DeclaracaoAtivaSingleton.getInstance().setDeclaracaoAtiva(decTO);
    DeclaracaoAtivaSingleton.getInstance().setNomeArquivoAtivo("");
    return decTO;
  }
  
  public DeclaracaoCAGEDTO alteraDeclaracaoCAGED(DeclaracaoCAGEDTO declaracao, boolean ehPersistivel)
    throws BusinessException
  {
    DeclaracaoCAGED dec = null;
    try {
      getTransaction().begin();
      
      dec = declaracaoService.alterarDeclaracaoCAGED(ConversorDeclaracaoCAGED.loadBusinessObject(declaracao, entityManager), ehPersistivel);
      
      getTransaction().commit();
    }
    catch (BusinessException be) {
      if (getTransaction().isActive())
        getTransaction().rollback();
      be.printStackTrace();
      throw new BusinessException("Cadastro não pode ser efetivado. Favor, tente novamente ou entre em contato com o suporte.", be);
    }
    catch (Throwable e) {
      if (getTransaction().isActive())
        getTransaction().rollback();
      e.printStackTrace();
      throw new BusinessException("Cadastro não pode ser efetivado. Favor, tente novamente ou entre em contato com o suporte.", e);
    }
    

    DeclaracaoCAGEDTO decTO = ConversorDeclaracaoCAGED.loadTransferObject(dec, entityManager);
    


    DeclaracaoAtivaSingleton.getInstance().setDeclaracaoAtiva(decTO);
    DeclaracaoAtivaSingleton.getInstance().setNomeArquivoAtivo("");
    return decTO;
  }
  


  public void excluirDeclaracaoCAGED(DeclaracaoCAGEDTO declaracao)
    throws BusinessException
  {
    DeclaracaoCAGED dec = new DeclaracaoCAGED();
    
    dec.setId(declaracao.getId());
    try
    {
      getTransaction().begin();
      
      declaracaoService.excluirDeclaracaoCAGED(dec);
      
      getTransaction().commit();
    }
    catch (BusinessException be) {
      if (getTransaction().isActive())
        getTransaction().rollback();
      be.printStackTrace();
      throw be;
    }
    catch (Throwable e) {
      if (getTransaction().isActive())
        getTransaction().rollback();
      e.printStackTrace();
      throw new BusinessException("Exclusão de declaração não pode ser efetivada. Favor, tente novamente ou entre em contato com o suporte.", e);
    }
  }
  

  public NoValidacao validarDeclaracaoCAGED(DeclaracaoCAGEDTO declaracao)
    throws BusinessException
  {
    DeclaracaoCAGED declaracaoCAGED = ConversorDeclaracaoCAGED.loadBusinessObject(declaracao, entityManager);
    
    return declaracaoService.analisarDeclaracaoCAGED(declaracaoCAGED);
  }
  

  public List validarMovimentacaoCAGED(MovimentacaoCAGEDTO movimentacao)
    throws BusinessException
  {
    Movimentacao movimentacaoCAGED = montarMovimentacao(movimentacao);
    
    return movimentacaoService.analisarMovimentacao(movimentacaoCAGED);
  }
  







  public void gerarArquivoDeclaracao(Long IdDeclaracao, String nomeArquivo)
    throws BusinessException
  {
    declaracaoService.gerarArquivoDeclaracao(IdDeclaracao, nomeArquivo);
  }
  

  public void gerarArquivoDeclaracao(DeclaracaoCAGEDTO declaracao, String nomeArquivo)
    throws BusinessException
  {
    DeclaracaoCAGED declaracaoCAGED = ConversorDeclaracaoCAGED.loadBusinessObject(declaracao, entityManager);
    
    declaracaoService.gerarArquivoDeclaracao(declaracaoCAGED, nomeArquivo);
  }
  

  public void gerarArquivoPosicional(Long IdDeclaracao, int versao, String filename)
    throws BusinessException
  {
    declaracaoService.gerarArquivoPosicional(IdDeclaracao, versao, filename);
  }
  







  public void gerarArquivoPosicional(DeclaracaoCAGEDTO declaracao, int versao, String filename)
    throws BusinessException
  {
    DeclaracaoCAGED declaracaoCAGED = ConversorDeclaracaoCAGED.loadBusinessObject(declaracao, entityManager);
    
    declaracaoService.gerarArquivoPosicional(declaracaoCAGED, versao, filename);
  }
  

  public void gerarArquivoPosicionalSemAnalise(DeclaracaoCAGEDTO declaracao, int versao, String filename)
    throws BusinessException
  {
    DeclaracaoCAGED declaracaoCAGED = ConversorDeclaracaoCAGED.loadBusinessObject(declaracao, entityManager);
    
    declaracaoService.gerarArquivoPosicionalSemAnalise(declaracaoCAGED, versao, filename);
  }
  

  public DeclaracaoCAGEDTO importarDeclaracao(DeclaracaoCAGEDTO declaracao, int operacao)
    throws BusinessException
  {
    DeclaracaoCAGEDTO dec = null;
    try {
      getTransaction().begin();
      
      dec = ConversorDeclaracaoCAGED.loadTransferObject(declaracaoService.importarDeclaracao(ConversorDeclaracaoCAGED.loadBusinessObject(declaracao, entityManager), operacao), entityManager);
      

      getTransaction().commit();
    }
    catch (BusinessException be) {
      if (getTransaction().isActive())
        getTransaction().rollback();
      be.printStackTrace();
      throw be;
    }
    catch (Throwable e) {
      if (getTransaction().isActive())
        getTransaction().rollback();
      e.printStackTrace();
      throw new BusinessException("Importação de declaração não pode ser efetivada. Favor, tente novamente ou entre em contato com o suporte.", e);
    }
    
    return dec;
  }
  
  public List obterListaPorte() throws BusinessException
  {
    try {
      return sdcService.obterListaPorte();
    } catch (br.gov.dataprev.caged.componente.analisador.integracao.service.IntegrationException ie) {
      throw new BusinessException(ie.getMessage());
    }
  }
  
  public Porte obterPorte(Porte porte) throws BusinessException
  {
    try {
      return sdcService.obterPorte(porte);
    } catch (br.gov.dataprev.caged.componente.analisador.integracao.service.IntegrationException ie) {
      throw new BusinessException(ie.getMessage());
    }
  }
  


  public List obterListaEstabelecimentos(String numeroDocumentoIdentificacao, int tipoDocumentoIdentificacao, String razaoSocial)
    throws BusinessException
  {
    List listaDeEstabelecimentos = estabelecimentoService.obterListaEstabelecimentos(numeroDocumentoIdentificacao, tipoDocumentoIdentificacao, razaoSocial);
    

    List listaDeEstabelecimentosTO = new ArrayList();
    
    Iterator it = listaDeEstabelecimentos.iterator();
    
    while (it.hasNext())
    {
      DeclaracaoEstabelecimentoCAGED dec = (DeclaracaoEstabelecimentoCAGED)it.next();
      

      DeclaracaoEstabelecimentoCAGEDTO estabTO = ConversorDeclaracaoEstabelecimentoCAGED.loadTransferObject(dec, entityManager);
      

      listaDeEstabelecimentosTO.add(estabTO);
    }
    

    return listaDeEstabelecimentosTO;
  }
  




  public void transmitirArquivoDeclaracao(String nomeArquivo, String caminhoCertificado, String senha, CompetenciaCAGED competencia)
    throws BusinessException
  {}
  




  private Movimentacao montarMovimentacao(MovimentacaoCAGEDTO movimentacaoTo)
    throws BusinessException
  {
    Movimentacao movimentacao = ConversorMovimentacao.loadBusinessObject(movimentacaoTo, entityManager);
    

    DeclaracaoEstabelecimentoCAGED estabelecimento = ConversorDeclaracaoEstabelecimentoCAGED.miniLoadBusinessObject(movimentacaoTo.getEstabelecimentoTO());
    

    DeclaracaoCAGED declaracao = ConversorDeclaracaoCAGED.miniLoadBusinessObject(movimentacaoTo.getDeclaracao());
    

    estabelecimento.setDeclaracaoCaged(declaracao);
    
    movimentacao.setDecEstabelecimento(estabelecimento);
    
    return movimentacao;
  }
  
  private DeclaracaoCAGED montarDeclaracao(DeclaracaoCAGEDTO to) throws BusinessException {
    DeclaracaoCAGED declaracao = ConversorDeclaracaoCAGED.loadBusinessObject(to, entityManager);
    
    return declaracao;
  }
  


  public List obterListaEstabelecimentos(DeclaracaoCAGEDTO declaracaoTO, String numeroDocumentoIdentificacao, int tipoDocumentoIdentificacao, String razaoSocial)
    throws BusinessException
  {
    DeclaracaoCAGED declaracao = new DeclaracaoCAGED();
    declaracao.setId(declaracaoTO.getId());
    
    List listaDeEstabelecimentos = estabelecimentoService.obterListaEstabelecimentos(declaracao, numeroDocumentoIdentificacao, tipoDocumentoIdentificacao, razaoSocial);
    


    List listaDeEstabelecimentosTO = new ArrayList();
    
    Iterator it = listaDeEstabelecimentos.iterator();
    
    while (it.hasNext())
    {
      DeclaracaoEstabelecimentoCAGED dec = (DeclaracaoEstabelecimentoCAGED)it.next();
      

      DeclaracaoEstabelecimentoCAGEDTO estabTO = ConversorDeclaracaoEstabelecimentoCAGED.loadTransferObject(dec, entityManager);
      

      listaDeEstabelecimentosTO.add(estabTO);
    }
    

    return listaDeEstabelecimentosTO;
  }
  
  public List validarEstabelecimento(DeclaracaoEstabelecimentoCAGEDTO registro) throws BusinessException
  {
    DeclaracaoEstabelecimentoCAGED estabelecimento = new DeclaracaoEstabelecimentoCAGED();
    
    estabelecimento = ConversorDeclaracaoEstabelecimentoCAGED.loadBusinessObject(registro, entityManager);
    

    return estabelecimentoService.analisarEstabelecimentos(estabelecimento);
  }
  
  public DeclaracaoEstabelecimentoCAGEDTO alterarEstabelecimento(DeclaracaoEstabelecimentoCAGEDTO registro)
    throws BusinessException
  {
    DeclaracaoEstabelecimentoCAGED estabAlterado = null;
    try {
      getTransaction().begin();
      
      estabAlterado = estabelecimentoService.alterarEstabelecimento(ConversorDeclaracaoEstabelecimentoCAGED.loadBusinessObject(registro, entityManager));
      


      getTransaction().commit();
    }
    catch (BusinessException be) {
      if (getTransaction().isActive())
        getTransaction().rollback();
      be.printStackTrace();
      throw new BusinessException("Alteração de estabelecimento não pode ser efetivada. Favor, tente novamente ou entre em contato com o suporte.", be);
    }
    catch (Throwable e) {
      if (getTransaction().isActive())
        getTransaction().rollback();
      e.printStackTrace();
      throw new BusinessException("Alteração de estabelecimento não pode ser efetivada. Favor, tente novamente ou entre em contato com o suporte.", e);
    }
    
    DeclaracaoEstabelecimentoCAGEDTO to = ConversorDeclaracaoEstabelecimentoCAGED.loadTransferObject(estabAlterado, entityManager);
    



























    return to;
  }
  

  public DeclaracaoEstabelecimentoCAGEDTO cadastrarEstabelecimento(DeclaracaoEstabelecimentoCAGEDTO registro)
    throws BusinessException
  {
    DeclaracaoEstabelecimentoCAGED movimentacaoAlterada = null;
    try
    {
      getTransaction().begin();
      
      movimentacaoAlterada = estabelecimentoService.cadastrarDeclaracaoEstabelecimento(ConversorDeclaracaoEstabelecimentoCAGED.loadBusinessObject(registro, entityManager));
      


      getTransaction().commit();
    }
    catch (BusinessException be) {
      if (getTransaction().isActive())
        getTransaction().rollback();
      be.printStackTrace();
      throw new BusinessException("Cadastro de estabelecimento não pode ser efetivado. Favor, tente novamente ou entre em contato com o suporte.", be);
    }
    catch (Throwable e) {
      if (getTransaction().isActive())
        getTransaction().rollback();
      e.printStackTrace();
      throw new BusinessException("Cadastro de estabelecimento não pode ser efetivado. Favor, tente novamente ou entre em contato com o suporte.", e);
    }
    return ConversorDeclaracaoEstabelecimentoCAGED.loadTransferObject(movimentacaoAlterada, entityManager);
  }
  
  public void excluirEstabelecimento(Long id) throws BusinessException {
    try {
      getTransaction().begin();
      
      estabelecimentoService.removerDeclaracaoEstabelecimento(id);
      
      getTransaction().commit();
    }
    catch (BusinessException be) {
      if (getTransaction().isActive())
        getTransaction().rollback();
      be.printStackTrace();
      throw new BusinessException("Exclusão de estabelecimento não pode ser efetivada. Favor, tente novamente ou entre em contato com o suporte.", be);
    }
    catch (Throwable e) {
      if (getTransaction().isActive())
        getTransaction().rollback();
      e.printStackTrace();
      throw new BusinessException("Exclusão de estabelecimento não pode ser efetivada. Favor, tente novamente ou entre em contato com o suporte.", e);
    }
  }
  






  public List listarMovimentacoes(CompetenciaCAGED competencia, String cnpj, TipoMovimento tipo)
    throws BusinessException
  {
    DeclaracaoCAGED declaracao = new DeclaracaoCAGED();
    



    List<DeclaracaoCAGED> listDecTemp = declaracaoService.listarDeclaracoesCAGED();
    

    for (DeclaracaoCAGED declaracaoTemp : listDecTemp) {
      if (declaracaoTemp.getCompetencia().equals(competencia)) {
        declaracao = declaracaoTemp;
      }
    }
    

    System.out.println(declaracao);
    List listaDeclaracoesEstabelecimento;
    try {
      listaDeclaracoesEstabelecimento = movimentacaoService.listarMovimentacoes(competencia, cnpj, tipo);
    } catch (br.gov.dataprev.caged.captacao.integracao.exception.IntegrationException e) {
      throw new BusinessException(e.getMessage(), e);
    }
    
    declaracao.setDeclaracaoesEstabelecimento(listaDeclaracoesEstabelecimento);
    
    List listaTO = ConversorDeclaracaoCAGED.loadTransferObject(declaracao, entityManager).getListaDeclaracaoEstabelecimentoTO();
    
    return listaTO;
  }
  











  public Boolean existeAcerto(Integer tipoDocIdentificacao, String numDocIdentificacao, String nit, Integer codMovimento, String competenciaEstabelecimentoAno, String competenciaEstabelecimentoMes, Date dataAdmissao, String cbo)
    throws BusinessException
  {
    try
    {
      return movimentacaoService.existeAcerto(tipoDocIdentificacao, numDocIdentificacao, nit, codMovimento, competenciaEstabelecimentoAno, competenciaEstabelecimentoMes, dataAdmissao, cbo);
    } catch (BusinessException e) {
      e.printStackTrace();
      throw new BusinessException("Não foi possível verificar .", e);
    }
  }
  
  public List listarAcertos(CompetenciaCAGED competencia, String cnpj)
    throws BusinessException
  {
    DeclaracaoCAGED declaracao = new DeclaracaoCAGED();
    



    List<DeclaracaoCAGED> listDecTemp = declaracaoService.listarDeclaracoesCAGED();
    

    for (DeclaracaoCAGED declaracaoTemp : listDecTemp) {
      if (declaracaoTemp.getCompetencia().equals(competencia)) {
        declaracao = declaracaoTemp;
      }
    }
    

    System.out.println(declaracao);
    List listaDeclaracoesEstabelecimento;
    try {
      listaDeclaracoesEstabelecimento = movimentacaoService.listarMovimentacoesAcertos(competencia, cnpj);
    } catch (br.gov.dataprev.caged.captacao.integracao.exception.IntegrationException e) {
      throw new BusinessException(e.getMessage(), e);
    }
    
    declaracao.setDeclaracaoesEstabelecimento(listaDeclaracoesEstabelecimento);
    
    List listaTO = ConversorDeclaracaoCAGED.loadTransferObject(declaracao, entityManager).getListaDeclaracaoEstabelecimentoTO();
    
    return listaTO;
  }
  
  public List listarEstabelecimentos(CompetenciaCAGED competencia, int tipoConsulta)
    throws BusinessException
  {
    DeclaracaoCAGED declaracao = new DeclaracaoCAGED();
    



    List<DeclaracaoCAGED> listDecTemp = declaracaoService.listarDeclaracoesCAGED();
    

    for (DeclaracaoCAGED declaracaoTemp : listDecTemp) {
      if (declaracaoTemp.getCompetencia().equals(competencia)) {
        declaracao = declaracaoTemp;
      }
    }
    

    System.out.println(declaracao);
    List listaDeclaracoes;
    try {
      listaDeclaracoes = estabelecimentoService.listarEstabelecimentos(competencia, tipoConsulta);
    }
    catch (br.gov.dataprev.caged.captacao.integracao.exception.IntegrationException e) {
      throw new BusinessException(e.getMessage(), e);
    }
    
    declaracao.setDeclaracaoesEstabelecimento(listaDeclaracoes);
    
    return ConversorDeclaracaoCAGED.loadTransferObject(declaracao, entityManager).getListaDeclaracaoEstabelecimentoTO();
  }
  
  public List listarMovimentacoesPisZerados(CompetenciaCAGED competencia, String cnpj)
    throws BusinessException
  {
    DeclaracaoCAGED declaracao = new DeclaracaoCAGED();
    



    List<DeclaracaoCAGED> listDecTemp = declaracaoService.listarDeclaracoesCAGED();
    

    for (DeclaracaoCAGED declaracaoTemp : listDecTemp) {
      if (declaracaoTemp.getCompetencia().equals(competencia)) {
        declaracao = declaracaoTemp;
      }
    }
    

    System.out.println(declaracao);
    List<DeclaracaoEstabelecimentoCAGED> listaDeclaracoesEstabZerados;
    try
    {
      listaDeclaracoesEstabZerados = movimentacaoService.listarMovimentacoesPisZerados(competencia, cnpj);
    } catch (br.gov.dataprev.caged.captacao.integracao.exception.IntegrationException e) {
      throw new BusinessException(e.getMessage(), e);
    }
    
    DeclaracaoEstabelecimentoCAGED decEstab;
    
    for (int i = 0; i < listaDeclaracoesEstabZerados.size(); i++)
    {
      decEstab = (DeclaracaoEstabelecimentoCAGED)listaDeclaracoesEstabZerados.get(i);
      
      List<AcertoCAGED> listaAcertos = decEstab.getListaDeAcertos();
      
      for (AcertoCAGED acertoTemp : listaAcertos)
      {
        if (acertoTemp.getTrabalhador().getNit().equals("00000000000"))
        {
          listaDeclaracoesEstabZerados.remove(decEstab);
        }
      }
    }
    
    declaracao.setDeclaracaoesEstabelecimento(listaDeclaracoesEstabZerados);
    
    List listaTO = ConversorDeclaracaoCAGED.loadTransferObject(declaracao, entityManager).getListaDeclaracaoEstabelecimentoTO();
    
    return listaTO;
  }
  
  public List listarMovimentacoesPisZerados(DeclaracaoCAGEDTO declaracaoCAGED)
    throws BusinessException
  {
    DeclaracaoCAGED declaracao = ConversorDeclaracaoCAGED.loadBusinessObject(declaracaoCAGED, entityManager);
    
    List listaDeclaracoes;
    try
    {
      listaDeclaracoes = movimentacaoService.listarMovimentacoesPisZerados(declaracao);
    } catch (br.gov.dataprev.caged.captacao.integracao.exception.IntegrationException e) {
      throw new BusinessException(e.getMessage(), e);
    }
    
    declaracao.setDeclaracaoesEstabelecimento(listaDeclaracoes);
    
    List listaTO = ConversorDeclaracaoCAGED.loadTransferObject(declaracao, entityManager).getListaDeclaracaoEstabelecimentoTO();
    
    return listaTO;
  }
  
  public DeclaracaoCAGEDTO abrirDeclaracaoCAGEDXML(String nomeArquivo) throws BusinessException
  {
    DeclaracaoCAGED dec = declaracaoService.abrirDeclaracaoCAGEDXML(nomeArquivo);
    
    DeclaracaoCAGEDTO decTO = ConversorDeclaracaoCAGED.loadTransferObject(dec, entityManager);
    

    DeclaracaoAtivaSingleton.getInstance().setDeclaracaoAtiva(decTO);
    DeclaracaoAtivaSingleton.getInstance().setNomeArquivoAtivo(nomeArquivo);
    return decTO;
  }
  
  public double obterUltimoSalarioMinimo() {
    try {
      return sdcService.obterSalarioVigente().getValor().doubleValue();
    }
    catch (br.gov.dataprev.caged.componente.analisador.integracao.service.IntegrationException ie) {}
    return 0.0D;
  }
  
  public ISDCAnalisadorCAGEDService getSdcService()
  {
    return sdcService;
  }
  
  public void setSdcService(ISDCAnalisadorCAGEDService sdcService) {
    this.sdcService = sdcService;
  }
  
  public synchronized void checkSequencies() {
    if (chSeq) {
      return;
    }
    String[] sequences = { "SEQ_ESTABELECIMENTO", "SEQ_DECLARACAO_ESTABELECIMENTO", "SEQ_DECLARACAO", "SEQ_TRABALHADOR", "SEQ_MOVIMENTACAO" };
    




    String[] tabelas = { "PUBLIC.ACI_ESTABELECIMENTO_CAGED", "PUBLIC.ACI_DEC_ESTABELECIMENTO_CAGED", "PUBLIC.ACI_DECLARACOES_CAGED", "PUBLIC.ACI_TRABALHADOR_CAGED", "PUBLIC.ACI_MOVIMENTACAO" };
    



    String[] ids = { "ID_ESTABELECIMENTO", "ID_DEC_ESTABELECIMENTO", "ID_DECLARACAO", "ID_TRABALHADOR", "ID_MOVIMENTACAO" };
    




    String sql1 = "SELECT MAX({0}) FROM {1}";
    String sql2 = "SELECT SEQ_COUNT FROM PUBLIC.SEQUENCE WHERE SEQ_NAME=''{0}''";
    String sql3 = "UPDATE PUBLIC.SEQUENCE SET SEQ_COUNT={0,number,############} WHERE SEQ_NAME=''{1}''";
    
    getTransaction().begin();
    for (int i = 0; i < sequences.length; i++) {
      System.out.println("Verificando sequence da tabela " + tabelas[i]);
      String sSql = MessageFormat.format("SELECT MAX({0}) FROM {1}", new Object[] { ids[i], tabelas[i] });
      System.out.println(sSql);
      Query query = entityManager.createNativeQuery(sSql);
      Object o = query.getSingleResult();
      if ((o instanceof Long)) {
        Long tbKey = (Long)o;
        Query query2 = entityManager.createNativeQuery(MessageFormat.format("SELECT SEQ_COUNT FROM PUBLIC.SEQUENCE WHERE SEQ_NAME=''{0}''", new Object[] { sequences[i] }));
        o = query2.getSingleResult();
        if ((o instanceof Long)) {
          Long seqKey = (Long)o;
          if (seqKey.longValue() < tbKey.longValue()) {
            System.out.println("Sequence da Tabela " + tabelas[i] + " precisa ser atualizada. Sequence atual: " + seqKey + " ultimo id: " + tbKey);
            sSql = MessageFormat.format("UPDATE PUBLIC.SEQUENCE SET SEQ_COUNT={0,number,############} WHERE SEQ_NAME=''{1}''", new Object[] { tbKey, sequences[i] });
            System.out.println(sSql);
            Query query3 = entityManager.createNativeQuery(sSql);
            
            int x = query3.executeUpdate();
            
            System.out.println(x + " registros atualizados");
          } else {
            System.out.println("Ok com a sequencia " + sequences[i]);
          }
        }
      } else {
        System.out.println("null");
      }
    }
    getTransaction().commit();
    chSeq = true;
  }
  
  private static boolean chSeq = false;
}
