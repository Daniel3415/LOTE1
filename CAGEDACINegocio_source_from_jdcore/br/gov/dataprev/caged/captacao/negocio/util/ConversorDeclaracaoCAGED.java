package br.gov.dataprev.caged.captacao.negocio.util;

import br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoCAGED;
import br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoEstabelecimentoCAGED;
import br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException;
import br.gov.dataprev.caged.captacao.negocio.to.DeclaracaoCAGEDTO;
import br.gov.dataprev.caged.captacao.negocio.to.DeclaracaoEstabelecimentoCAGEDTO;
import br.gov.dataprev.caged.componente.analisador.modelo.SituacaoDeclaracao;
import br.gov.dataprev.caged.modelo.competencia.CompetenciaCAGED;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;



public class ConversorDeclaracaoCAGED
{
  public ConversorDeclaracaoCAGED() {}
  
  public static DeclaracaoCAGED miniLoadBusinessObject(DeclaracaoCAGEDTO to)
    throws BusinessException
  {
    DeclaracaoCAGED declaracoCAGED = new DeclaracaoCAGED();
    if (to != null) {
      declaracoCAGED.setCompetencia(new CompetenciaCAGED(to.getMesCompetencia(), to.getAnoCompetencia()));
      declaracoCAGED.setId(to.getId());
      declaracoCAGED.setSalarioMinimo(Double.valueOf(to.getSalarioMinimo()));
      declaracoCAGED.setSituacao(new SituacaoDeclaracao(to.getCodigoSituacao()));
      if (to.getAutorizado() != null) {
        declaracoCAGED.setAutorizado(ConversorAutorizadoCAGED.load(to.getAutorizado()));
      }
    }
    return declaracoCAGED;
  }
  
  public static DeclaracaoCAGED loadBusinessObject(DeclaracaoCAGEDTO to, EntityManager em) throws BusinessException
  {
    DeclaracaoCAGED declaracoCAGED = miniLoadBusinessObject(to);
    List listaDeDeclaracoesEstabelecimento = new ArrayList();
    
    if (to.getListaDeclaracaoEstabelecimentoTO() != null) {
      for (Object objDeclaracao : to.getListaDeclaracaoEstabelecimentoTO()) {
        DeclaracaoEstabelecimentoCAGEDTO decTO = (DeclaracaoEstabelecimentoCAGEDTO)objDeclaracao;
        
        DeclaracaoEstabelecimentoCAGED declaracaoEstabelecimento = ConversorDeclaracaoEstabelecimentoCAGED.loadBusinessObject(decTO, em);
        
        declaracaoEstabelecimento.setDeclaracaoCaged(declaracoCAGED);
        listaDeDeclaracoesEstabelecimento.add(declaracaoEstabelecimento);
      }
    }
    
    if (listaDeDeclaracoesEstabelecimento.size() > 0) {
      declaracoCAGED.setDeclaracaoesEstabelecimento(listaDeDeclaracoesEstabelecimento);
    }
    
    return declaracoCAGED;
  }
  
  public static DeclaracaoCAGEDTO loadTransferObject(DeclaracaoCAGED declaracao, EntityManager em) throws BusinessException
  {
    DeclaracaoCAGEDTO decTO = new DeclaracaoCAGEDTO();
    
    decTO.setAnoCompetencia(declaracao.getCompetencia().getAno());
    decTO.setMesCompetencia(declaracao.getCompetencia().getMes());
    
    decTO.setId(declaracao.getId());
    
    if (declaracao.getSalarioMinimo() != null) {
      decTO.setSalarioMinimo(declaracao.getSalarioMinimo().doubleValue());
    } else {
      decTO.setSalarioMinimo(0.0D);
    }
    try {
      decTO.setAutorizado(ConversorAutorizadoCAGED.load(declaracao.getAutorizado()));
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
    
    if (declaracao.getSituacao() != null) {
      decTO.setCodigoSituacao(declaracao.getSituacao().getCodigo());
      if (declaracao.getSituacao().getDescricao() != null) {
        decTO.setDescricaoSituacao(declaracao.getSituacao().getDescricao());
      }
    }
    

    Iterator it = null;
    try
    {
      it = declaracao.getDeclaracaoesEstabelecimento().iterator();
    } catch (NullPointerException e) {
      declaracao.setDeclaracaoesEstabelecimento(new ArrayList());
      it = declaracao.getDeclaracaoesEstabelecimento().iterator();
    }
    
    List listaDeDeclaracoesEstabelecimento = new ArrayList();
    
    while (it.hasNext())
    {
      DeclaracaoEstabelecimentoCAGED declaracaoEstabelecimento = (DeclaracaoEstabelecimentoCAGED)it.next();
      
      DeclaracaoEstabelecimentoCAGEDTO declaracaoEstabelecimentoTO = ConversorDeclaracaoEstabelecimentoCAGED.loadTransferObject(declaracaoEstabelecimento, em);
      

      listaDeDeclaracoesEstabelecimento.add(declaracaoEstabelecimentoTO);
    }
    
    if (listaDeDeclaracoesEstabelecimento.size() > 0) {
      decTO.setListaDeclaracaoEstabelecimentoTO(listaDeDeclaracoesEstabelecimento);
    }
    
    return decTO;
  }
}
