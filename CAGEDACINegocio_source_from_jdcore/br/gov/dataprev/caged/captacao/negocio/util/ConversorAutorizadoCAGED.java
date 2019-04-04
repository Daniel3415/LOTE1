package br.gov.dataprev.caged.captacao.negocio.util;

import br.gov.dataprev.caged.captacao.modelo.autorizado.AutorizadoCAGED;
import br.gov.dataprev.caged.captacao.negocio.to.AutorizadoCAGEDTO;
import br.gov.dataprev.caged.modelo.contato.Contato;
import br.gov.dataprev.caged.modelo.declaracao.estabelecimento.EnderecoCAGED;

















































































public class ConversorAutorizadoCAGED
{
  public ConversorAutorizadoCAGED() {}
  
  public static AutorizadoCAGED load(AutorizadoCAGEDTO to)
  {
    AutorizadoCAGED autorizadoCAGED = new AutorizadoCAGED();
    Contato contato = new Contato();
    EnderecoCAGED endereco = new EnderecoCAGED();
    

    autorizadoCAGED.setId(to.getId());
    autorizadoCAGED.setNumeroDocumentoIdentificacao(to.getNumeroDocumentoIdentificacao());
    autorizadoCAGED.setRazaoSocial(to.getRazaoSocial());
    autorizadoCAGED.setTipoDocumentoIdentificacao(Integer.valueOf(to.getTipoDocumentoIdentificacao()));
    
    contato.setCodigoArea(to.getNumeroDD());
    contato.setCpf(to.getNuCPF());
    contato.setEmail(to.getEmailContato());
    contato.setNome(to.getNomeContato());
    contato.setNumeroTelefone(to.getNumeroTelefone());
    contato.setRamal(to.getNumeroRamal());
    
    autorizadoCAGED.setContato(contato);
    
    endereco.setCep(to.getCep());
    endereco.setComplemento(to.getComplementoEndereco());
    endereco.setNomeBairro(to.getBairro());
    endereco.setNomeLogradouro(to.getNomeLogradouro());
    endereco.setNumeroLogradouro(to.getNumeroEndereco());
    endereco.setUf(to.getSiglaUF());
    endereco.setEnderecoCompleto(to.getNomeLogradouro() == null ? "" : to.getNomeLogradouro(), to.getNumeroEndereco() == null ? "" : to.getNumeroEndereco(), to.getComplementoEndereco() == null ? "" : to.getComplementoEndereco());
    
    autorizadoCAGED.setEndereco(endereco);
    
    return autorizadoCAGED;
  }
  

  public static AutorizadoCAGEDTO load(AutorizadoCAGED bo)
  {
    AutorizadoCAGEDTO autTO = new AutorizadoCAGEDTO();
    

    autTO.setId(bo.getId());
    autTO.setNumeroDocumentoIdentificacao(bo.getNumeroDocumentoIdentificacao());
    autTO.setRazaoSocial(bo.getRazaoSocial());
    try
    {
      autTO.setTipoDocumentoIdentificacao(bo.getTipoDocumentoIdentificacao().intValue());
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
    

    if (bo.getContato() != null) {
      autTO.setNumeroDD(String.valueOf(bo.getContato().getCodigoArea()));
      autTO.setNuCPF(bo.getContato().getCpf());
      autTO.setEmailContato(bo.getContato().getEmail());
      autTO.setNomeContato(bo.getContato().getNome());
      autTO.setNumeroTelefone(bo.getContato().getNumeroTelefone());
      autTO.setNumeroRamal(bo.getContato().getRamal());
    }
    if (bo.getEndereco() != null) {
      autTO.setCep(bo.getEndereco().getCep());
      autTO.setComplementoEndereco(bo.getEndereco().getComplemento());
      autTO.setBairro(bo.getEndereco().getNomeBairro());
      autTO.setNomeLogradouro(bo.getEndereco().getNomeLogradouro());
      autTO.setNumeroEndereco(bo.getEndereco().getNumeroLogradouro());
      autTO.setSiglaUF(bo.getEndereco().getUf());
    }
    





    return autTO;
  }
}
