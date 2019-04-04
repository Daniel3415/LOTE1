package br.gov.dataprev.caged.captacao.negocio.to;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;






public class AutorizadoCAGEDTO
  implements Serializable
{
  private static final long serialVersionUID = -5679567201809576916L;
  private int id;
  private String numeroDocumentoIdentificacao;
  private int tipoDocumentoIdentificacao;
  private String nuCPF;
  private String razaoSocial;
  private String nomeLogradouro;
  private String numeroEndereco;
  private String complementoEndereco;
  private String bairro;
  private String cep;
  private String siglaUF;
  private String nomeContato;
  private String emailContato;
  private String numeroDD;
  private String numeroTelefone;
  private String numeroRamal;
  
  public AutorizadoCAGEDTO() {}
  
  public int getId()
  {
    return id;
  }
  
  public void setId(int id) { this.id = id; }
  
  public String getNumeroDocumentoIdentificacao() {
    return numeroDocumentoIdentificacao;
  }
  
  public void setNumeroDocumentoIdentificacao(String numeroDocumentoIdentificacao) { this.numeroDocumentoIdentificacao = numeroDocumentoIdentificacao; }
  
  public int getTipoDocumentoIdentificacao() {
    return tipoDocumentoIdentificacao;
  }
  
  public void setTipoDocumentoIdentificacao(int tipoDocumentoIdentificacao) { this.tipoDocumentoIdentificacao = tipoDocumentoIdentificacao; }
  
  public String getRazaoSocial() {
    return razaoSocial;
  }
  
  public void setRazaoSocial(String razaoSocial) { this.razaoSocial = razaoSocial; }
  
  public String getNomeLogradouro() {
    return nomeLogradouro;
  }
  
  public void setNomeLogradouro(String nomeLogradouro) { this.nomeLogradouro = nomeLogradouro; }
  
  public String getNumeroEndereco() {
    return numeroEndereco;
  }
  
  public void setNumeroEndereco(String numeroEndereco) { this.numeroEndereco = numeroEndereco; }
  
  public String getComplementoEndereco() {
    return complementoEndereco;
  }
  
  public void setComplementoEndereco(String complementoEndereco) { this.complementoEndereco = complementoEndereco; }
  
  public String getBairro() {
    return bairro;
  }
  
  public void setBairro(String bairro) { this.bairro = bairro; }
  
  public String getCep() {
    return cep;
  }
  
  public void setCep(String cep) { this.cep = cep; }
  
  public String getSiglaUF() {
    return siglaUF;
  }
  
  public void setSiglaUF(String siglaUF) { this.siglaUF = siglaUF; }
  
  public String getNomeContato() {
    return nomeContato;
  }
  
  public void setNomeContato(String nomeContato) { this.nomeContato = nomeContato; }
  
  public String getEmailContato() {
    return emailContato;
  }
  
  public void setEmailContato(String emailContato) { this.emailContato = emailContato; }
  

  public String getNumeroDD() { return numeroDD; }
  
  public void setNumeroDD(String numeroDD) {
    if ((numeroDD != null) && (numeroDD.length() == 4)) {
      numeroDD = numeroDD.substring(2);
    }
    this.numeroDD = numeroDD;
  }
  
  public String getNumeroTelefone() { return numeroTelefone; }
  
  public void setNumeroTelefone(String numeroTelefone) {
    this.numeroTelefone = numeroTelefone;
  }
  
  public String getNumeroRamal() { return numeroRamal; }
  
  public void setNumeroRamal(String numeroRamal) {
    this.numeroRamal = numeroRamal;
  }
  
  public String getNuCPF() { return nuCPF; }
  
  public void setNuCPF(String nuCPF) {
    this.nuCPF = nuCPF;
  }
  
  public String toString()
  {
    return new ToStringBuilder(this).append("id", id).append("numeroDocumentoIdentificacao", numeroDocumentoIdentificacao).append("tipoDocumentoIdentificacao", tipoDocumentoIdentificacao).append("nuCPF", nuCPF).append("razaoSocial", razaoSocial).append("nomeLogradouro", nomeLogradouro).append("numeroEndereco", numeroEndereco).append("complementoEndereco", complementoEndereco).append("bairro", bairro).append("cep", cep).append("siglaUF", siglaUF).append("nomeContato", nomeContato).append("emailContato", emailContato).append("numeroDD", numeroDD).append("numeroTelefone", numeroTelefone).append("numeroRamal", numeroRamal).toString();
  }
}
