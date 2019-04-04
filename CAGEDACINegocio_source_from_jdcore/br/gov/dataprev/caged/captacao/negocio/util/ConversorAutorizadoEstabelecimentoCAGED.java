package br.gov.dataprev.caged.captacao.negocio.util;

import br.gov.dataprev.caged.captacao.modelo.declaracao.DeclaracaoEstabelecimentoCAGED;
import br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException;
import br.gov.dataprev.caged.captacao.negocio.service.CaptacaoAutorizadoCAGEDService;
import br.gov.dataprev.caged.captacao.negocio.service.CaptacaoDeclaracaoEstabelecimentoCAGEDService;
import br.gov.dataprev.caged.captacao.negocio.service.ICaptacaoAutorizadoCAGEDService;
import br.gov.dataprev.caged.captacao.negocio.service.ICaptacaoEstabelecimentoCAGEDService;
import br.gov.dataprev.caged.captacao.negocio.to.AutorizadoCAGEDTO;
import br.gov.dataprev.caged.captacao.negocio.to.DeclaracaoEstabelecimentoCAGEDTO;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.persistence.EntityManager;
import org.apache.commons.beanutils.BeanUtils;




public class ConversorAutorizadoEstabelecimentoCAGED
{
  ICaptacaoAutorizadoCAGEDService autorizadoService;
  ICaptacaoEstabelecimentoCAGEDService estabelecimentoService;
  
  public ConversorAutorizadoEstabelecimentoCAGED() {}
  
  public DeclaracaoEstabelecimentoCAGEDTO converte(AutorizadoCAGEDTO toEntrada, EntityManager em)
  {
    autorizadoService = new CaptacaoAutorizadoCAGEDService(em);
    estabelecimentoService = new CaptacaoDeclaracaoEstabelecimentoCAGEDService(em);
    
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
  
  public AutorizadoCAGEDTO converte(DeclaracaoEstabelecimentoCAGEDTO toEntrada, EntityManager em)
  {
    autorizadoService = new CaptacaoAutorizadoCAGEDService(em);
    
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
}
