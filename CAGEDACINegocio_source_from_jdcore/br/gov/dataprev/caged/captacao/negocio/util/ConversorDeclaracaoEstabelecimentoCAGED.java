package br.gov.dataprev.caged.captacao.negocio.util;

import br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoCAGED;
import br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoEstabelecimentoCAGED;
import br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException;
import br.gov.dataprev.caged.captacao.negocio.to.DeclaracaoEstabelecimentoCAGEDTO;
import br.gov.dataprev.caged.captacao.negocio.to.MovimentacaoCAGEDTO;
import br.gov.dataprev.caged.componente.analisador.integracao.service.ISDCAnalisadorCAGEDService;
import br.gov.dataprev.caged.componente.analisador.integracao.service.IntegrationException;
import br.gov.dataprev.caged.modelo.captacao.declaracao.estabelecimento.EstabelecimentoCAGED;
import br.gov.dataprev.caged.modelo.captacao.declaracao.movimentacao.AcertoCAGED;
import br.gov.dataprev.caged.modelo.captacao.declaracao.movimentacao.Movimentacao;
import br.gov.dataprev.caged.modelo.captacao.declaracao.movimentacao.MovimentacaoCAGED;
import br.gov.dataprev.caged.modelo.declaracao.estabelecimento.EnderecoCAGED;
import br.gov.dataprev.caged.modelo.declaracao.estabelecimento.Porte;
import br.gov.dataprev.caged.modelo.estabelecimento.AtividadeEconomica;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;

public class ConversorDeclaracaoEstabelecimentoCAGED
{
  private ISDCAnalisadorCAGEDService sdc;
  
  public ConversorDeclaracaoEstabelecimentoCAGED() {}
  
  public static DeclaracaoEstabelecimentoCAGED miniLoadBusinessObject(DeclaracaoEstabelecimentoCAGEDTO to) throws BusinessException
  {
    DeclaracaoEstabelecimentoCAGED declaracaoEstabelecimento = new DeclaracaoEstabelecimentoCAGED();
    
    if (to.getDeclaracao() != null)
    {
      DeclaracaoCAGED dec = ConversorDeclaracaoCAGED.miniLoadBusinessObject(to.getDeclaracao());
      
      declaracaoEstabelecimento.setDeclaracaoCaged(dec);
    }
    
    declaracaoEstabelecimento.setCodigoAlteracao(to.getEncerrouAtividade());
    
    declaracaoEstabelecimento.setCodigoPrimeiraDeclaracao(to.getEhPrimeiraDeclaracao());
    declaracaoEstabelecimento.setEhAutorizado(to.getEhAutorizado());
    declaracaoEstabelecimento.setId(Long.valueOf(to.getId()));
    declaracaoEstabelecimento.setTotalEmpregadosPrimeiroDia(to.getTotalEmpregadosPrimeiroDia());
    declaracaoEstabelecimento.setTotalAdmissoes(to.getQtdAdmissoes());
    declaracaoEstabelecimento.setTotalDesligamentos(to.getQtdDemissoes());
    
    EstabelecimentoCAGED estabelecimento = new EstabelecimentoCAGED();
    
    estabelecimento.setId(Long.valueOf(to.getId()));
    estabelecimento.setNumeroDocumentoIdentificacao(to.getNumeroDocumentoIdentificacao());
    estabelecimento.setPorte(new Porte(to.getPorte()));
    estabelecimento.setRazaoSocial(to.getRazaoSocial());
    estabelecimento.setTipoDocumentoIdentificacao(to.getTipoDocumentoIdentificacao());
    estabelecimento.setAtividadeEconomica(new AtividadeEconomica(to.getNumeroCNAE()));
    
    EnderecoCAGED enderecoEstab = new EnderecoCAGED();
    
    enderecoEstab.setCep(to.getCep());
    enderecoEstab.setComplemento(to.getComplementoEndereco());
    enderecoEstab.setNomeBairro(to.getBairro());
    enderecoEstab.setNomeLogradouro(to.getNomeLogradouro());
    enderecoEstab.setNumeroLogradouro(to.getNumeroEndereco());
    enderecoEstab.setUf(to.getSiglaUF());
    enderecoEstab.setEnderecoCompleto(to.getNomeLogradouro(), to.getNumeroEndereco(), to.getComplementoEndereco());
    
    estabelecimento.setEndereco(enderecoEstab);
    
    declaracaoEstabelecimento.setEstabelecimento(estabelecimento);
    return declaracaoEstabelecimento;
  }
  
  public static DeclaracaoEstabelecimentoCAGED loadBusinessObject(DeclaracaoEstabelecimentoCAGEDTO to, EntityManager em)
    throws BusinessException
  {
    DeclaracaoEstabelecimentoCAGED declaracaoEstabelecimento = miniLoadBusinessObject(to);
    


    List listaDeMovimentacaoes = new ArrayList();
    
    Iterator itm = new ArrayList().iterator();
    
    if ((to != null) && (to.getListaMovimentacoes() != null)) {
      itm = to.getListaMovimentacoes().iterator();
      

      while (itm.hasNext())
      {
        MovimentacaoCAGEDTO movTO = (MovimentacaoCAGEDTO)itm.next();
        

        Movimentacao movimentacao = ConversorMovimentacao.loadBusinessObject(movTO, em);
        movimentacao.setDecEstabelecimento(declaracaoEstabelecimento);
        listaDeMovimentacaoes.add(movimentacao);
      }
      
      if (listaDeMovimentacaoes.size() > 0) {
        declaracaoEstabelecimento.setListaDeMovimentacoes(listaDeMovimentacaoes);
      }
    }
    
    List listaDeAcertos = new ArrayList();
    

    Iterator ita = new ArrayList().iterator();
    
    if ((to != null) && (to.getListaAcertos() != null))
    {
      ita = to.getListaAcertos().iterator();
      

      while (ita.hasNext())
      {
        MovimentacaoCAGEDTO acertoTO = (MovimentacaoCAGEDTO)ita.next();
        
        Movimentacao acerto = ConversorMovimentacao.loadBusinessObject(acertoTO, em);
        acerto.setDecEstabelecimento(declaracaoEstabelecimento);
        listaDeAcertos.add(acerto);
      }
      
      if (listaDeAcertos.size() > 0) {
        declaracaoEstabelecimento.setListaDeAcertos(listaDeAcertos);
      }
    }
    
    return declaracaoEstabelecimento;
  }
  

  public static DeclaracaoEstabelecimentoCAGEDTO loadTransferObject(DeclaracaoEstabelecimentoCAGED bo, EntityManager em)
    throws BusinessException
  {
    DeclaracaoEstabelecimentoCAGEDTO declaracaoEstabelecimentoTO = new DeclaracaoEstabelecimentoCAGEDTO();
    declaracaoEstabelecimentoTO.setEncerrouAtividade(bo.getCodigoAlteracao());
    
    declaracaoEstabelecimentoTO.setEhPrimeiraDeclaracao(bo.getCodigoPrimeiraDeclaracao());
    declaracaoEstabelecimentoTO.setEhAutorizado(bo.getEhAutorizado());
    if (bo.getId() != null) {
      declaracaoEstabelecimentoTO.setId(bo.getId().longValue());
    }
    declaracaoEstabelecimentoTO.setTotalEmpregadosPrimeiroDia(bo.getTotalEmpregadosPrimeiroDia());
    
    if (bo.getEstabelecimento() != null) {
      declaracaoEstabelecimentoTO.setNumeroDocumentoIdentificacao(bo.getEstabelecimento().getNumeroDocumentoIdentificacao());
      declaracaoEstabelecimentoTO.setPorte(bo.getEstabelecimento().getPorte().getCodigo());
      
      ISDCAnalisadorCAGEDService sdc = new br.gov.dataprev.caged.captacao.integracao.desktop.service.SDCCaptacaoCAGEDServiceDesktop(em);
      try
      {
        declaracaoEstabelecimentoTO.setDescricaoPorte(sdc.obterPorte(bo.getEstabelecimento().getPorte()).getDescricao());
      } catch (IntegrationException e) {
        e.printStackTrace();
      }
      
      declaracaoEstabelecimentoTO.setRazaoSocial(bo.getEstabelecimento().getRazaoSocial());
      declaracaoEstabelecimentoTO.setTipoDocumentoIdentificacao(bo.getEstabelecimento().getTipoDocumentoIdentificacao());
      if (bo.getEstabelecimento().getAtividadeEconomica() != null) {
        declaracaoEstabelecimentoTO.setNumeroCNAE(bo.getEstabelecimento().getAtividadeEconomica().getCodigo());
        if (bo.getEstabelecimento().getAtividadeEconomica().getDescricao() == null) {
          try {
            bo.getEstabelecimento().setAtividadeEconomica(sdc.obterAtividadeEconomica(new AtividadeEconomica(bo.getEstabelecimento().getAtividadeEconomica().getCodigo())));
          } catch (IntegrationException e) {
            e.printStackTrace();
          }
        }
        declaracaoEstabelecimentoTO.setDescricaoCNAE(bo.getEstabelecimento().getAtividadeEconomica().getDescricao());
      }
      

      declaracaoEstabelecimentoTO.setCep(bo.getEstabelecimento().getEndereco().getCep());
      declaracaoEstabelecimentoTO.setComplementoEndereco(bo.getEstabelecimento().getEndereco().getComplemento());
      declaracaoEstabelecimentoTO.setBairro(bo.getEstabelecimento().getEndereco().getNomeBairro());
      declaracaoEstabelecimentoTO.setNomeLogradouro(bo.getEstabelecimento().getEndereco().getNomeLogradouro());
      declaracaoEstabelecimentoTO.setNumeroEndereco(bo.getEstabelecimento().getEndereco().getNumeroLogradouro());
      declaracaoEstabelecimentoTO.setSiglaUF(bo.getEstabelecimento().getEndereco().getUf());
    }
    

    declaracaoEstabelecimentoTO.setQtdAdmissoes(bo.obterTotalDeAdmissoes());
    declaracaoEstabelecimentoTO.setQtdDemissoes(bo.obterTotalDeDemissoes());
    declaracaoEstabelecimentoTO.setTotalAdmissoes(bo.getTotalAdmissoes());
    declaracaoEstabelecimentoTO.setTotalDemissoes(bo.getTotalDesligamentos());
    declaracaoEstabelecimentoTO.setTotalEmpregadosPrimeiroDia(bo.getTotalEmpregadosPrimeiroDia());
    declaracaoEstabelecimentoTO.setTotalAcertos(bo.obterTotalDeAcertos());
    declaracaoEstabelecimentoTO.setTotalMovimentacoes(bo.obterTotalDeMovimentacoes());
    declaracaoEstabelecimentoTO.setCompetencia(bo.getDeclaracaoCaged().getCompetencia().toString());
    declaracaoEstabelecimentoTO.setUltimoDia(bo.obterUltimoDia());
    



    Iterator itm = null;
    try
    {
      itm = bo.getListaDeMovimentacoes().iterator();
    } catch (NullPointerException e) {
      bo.setListaDeMovimentacoes(new ArrayList());
      itm = bo.getListaDeMovimentacoes().iterator();
    }
    
    List listaDeMovimentacaoes = new ArrayList();
    
    while (itm.hasNext())
    {
      MovimentacaoCAGED movimentacao = (MovimentacaoCAGED)itm.next();
      
      MovimentacaoCAGEDTO movTO = ConversorMovimentacao.loadTransferObject(movimentacao, em);
      
      listaDeMovimentacaoes.add(movTO);
    }
    
    if (listaDeMovimentacaoes.size() > 0) {
      declaracaoEstabelecimentoTO.setListaMovimentacoes(listaDeMovimentacaoes);
    }
    
    Iterator ita = null;
    try
    {
      ita = bo.getListaDeAcertos().iterator();
    } catch (NullPointerException e) {
      bo.setListaDeAcertos(new ArrayList());
      ita = bo.getListaDeAcertos().iterator();
    }
    
    List listaDeAcertos = new ArrayList();
    

    while (ita.hasNext())
    {
      AcertoCAGED movimentacao = (AcertoCAGED)ita.next();
      
      MovimentacaoCAGEDTO movTO = ConversorMovimentacao.loadTransferObject(movimentacao, em);
      
      listaDeAcertos.add(movTO);
    }
    

    if (listaDeAcertos.size() > 0) {
      declaracaoEstabelecimentoTO.setListaAcertos(listaDeAcertos);
    }
    
    return declaracaoEstabelecimentoTO;
  }
}
