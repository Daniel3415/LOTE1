package br.gov.dataprev.caged.captacao.negocio.to;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.builder.EqualsBuilder;







































public class DeclaracaoEstabelecimentoCAGEDTO
  implements Serializable
{
  private static final long serialVersionUID = -6669531120677992519L;
  private long id;
  private String numeroDocumentoIdentificacao;
  private int tipoDocumentoIdentificacao;
  private String razaoSocial;
  private String nomeLogradouro;
  private String numeroEndereco;
  private String complementoEndereco;
  private String bairro;
  private String cep;
  private String siglaUF;
  private String numeroCNAE;
  private String descricaoCNAE;
  private int porte;
  private String descricaoPorte;
  private int ehAutorizado;
  private int encerrouAtividade;
  private int ehPrimeiraDeclaracao;
  private int totalEmpregadosPrimeiroDia;
  private int qtdAdmissoes;
  private int qtdDemissoes;
  private int totalAdmissoes;
  private int totalDemissoes;
  private int totalMovimentacoes;
  private int totalAcertos;
  private int ultimoDia;
  private String competencia;
  private List listaMovimentacoes;
  private List<MovimentacaoCAGEDTO> listaAcertos;
  private DeclaracaoCAGEDTO declaracao;
  
  public DeclaracaoEstabelecimentoCAGEDTO() {}
  
  public boolean equals(Object obj)
  {
    if ((obj != null) && ((obj instanceof DeclaracaoEstabelecimentoCAGEDTO)))
    {
      DeclaracaoEstabelecimentoCAGEDTO mov = (DeclaracaoEstabelecimentoCAGEDTO)obj;
      
      return new EqualsBuilder().append(numeroDocumentoIdentificacao, numeroDocumentoIdentificacao).append(tipoDocumentoIdentificacao, tipoDocumentoIdentificacao).isEquals();
    }
    



    return false;
  }
  
  public DeclaracaoCAGEDTO getDeclaracao() {
    return declaracao;
  }
  
  public void setDeclaracao(DeclaracaoCAGEDTO declaracao) {
    this.declaracao = declaracao;
  }
  
  public void setUltimoDia(int ultimoDia) {
    this.ultimoDia = ultimoDia;
  }
  
  public long getId() {
    return id;
  }
  
  public void setId(long id) {
    this.id = id;
  }
  
  public String getNumeroDocumentoIdentificacao() {
    return numeroDocumentoIdentificacao;
  }
  
  public void setNumeroDocumentoIdentificacao(String numeroDocumentoIdentificacao) {
    this.numeroDocumentoIdentificacao = numeroDocumentoIdentificacao;
  }
  
  public int getTipoDocumentoIdentificacao() {
    return tipoDocumentoIdentificacao;
  }
  
  public void setTipoDocumentoIdentificacao(int tipoDocumentoIdentificacao) {
    this.tipoDocumentoIdentificacao = tipoDocumentoIdentificacao;
  }
  
  public String getRazaoSocial() {
    return razaoSocial;
  }
  
  public void setRazaoSocial(String razaoSocial) {
    this.razaoSocial = razaoSocial;
  }
  
  public String getNomeLogradouro() {
    return nomeLogradouro;
  }
  
  public void setNomeLogradouro(String nomeLogradouro) {
    this.nomeLogradouro = nomeLogradouro;
  }
  
  public String getNumeroEndereco() {
    return numeroEndereco;
  }
  
  public void setNumeroEndereco(String numeroEndereco) {
    this.numeroEndereco = numeroEndereco;
  }
  
  public String getComplementoEndereco() {
    return complementoEndereco;
  }
  
  public void setComplementoEndereco(String complementoEndereco) {
    this.complementoEndereco = complementoEndereco;
  }
  
  public String getBairro() {
    return bairro;
  }
  
  public void setBairro(String bairro) {
    this.bairro = bairro;
  }
  
  public String getCep() {
    return cep;
  }
  
  public void setCep(String cep) {
    this.cep = cep;
  }
  
  public String getSiglaUF() {
    return siglaUF;
  }
  
  public void setSiglaUF(String siglaUF) {
    this.siglaUF = siglaUF;
  }
  
  public String getNumeroCNAE() {
    return numeroCNAE;
  }
  
  public void setNumeroCNAE(String numeroCNAE) {
    this.numeroCNAE = numeroCNAE;
  }
  
  public int getPorte() {
    return porte;
  }
  
  public void setPorte(int porte) {
    this.porte = porte;
  }
  
  public int getEhAutorizado() {
    return ehAutorizado;
  }
  
  public void setEhAutorizado(int ehAutorizado) {
    this.ehAutorizado = ehAutorizado;
  }
  
  public int getEhPrimeiraDeclaracao() {
    return ehPrimeiraDeclaracao;
  }
  
  public void setEhPrimeiraDeclaracao(int ehPrimeiraDeclaracao) {
    this.ehPrimeiraDeclaracao = ehPrimeiraDeclaracao;
  }
  
  public int getTotalEmpregadosPrimeiroDia() {
    return totalEmpregadosPrimeiroDia;
  }
  
  public void setTotalEmpregadosPrimeiroDia(int totalEmpregadosPrimeiroDia) {
    this.totalEmpregadosPrimeiroDia = totalEmpregadosPrimeiroDia;
  }
  
  public List getListaMovimentacoes() {
    if (listaMovimentacoes == null) {
      listaMovimentacoes = new ArrayList();
    }
    return listaMovimentacoes;
  }
  
  public void setListaMovimentacoes(List listaMovimentacoes) {
    this.listaMovimentacoes = listaMovimentacoes;
  }
  
  public List<MovimentacaoCAGEDTO> getListaAcertos() {
    return listaAcertos;
  }
  
  public void setListaAcertos(List listaAcertos) {
    this.listaAcertos = listaAcertos;
  }
  
  public String getDescricaoCNAE() {
    return descricaoCNAE;
  }
  
  public void setDescricaoCNAE(String descricaoCNAE) {
    this.descricaoCNAE = descricaoCNAE;
  }
  
  public int getTotalAdmissoes() {
    return totalAdmissoes;
  }
  
  public void setTotalAdmissoes(int totalAdmissoes) {
    this.totalAdmissoes = totalAdmissoes;
  }
  
  public int getTotalDemissoes() {
    return totalDemissoes;
  }
  
  public void setTotalDemissoes(int totalDemissoes) {
    this.totalDemissoes = totalDemissoes;
  }
  
  public int getQtdAdmissoes() {
    return qtdAdmissoes;
  }
  
  public void setQtdAdmissoes(int qtdAdmissoes) {
    this.qtdAdmissoes = qtdAdmissoes;
  }
  
  public int getQtdDemissoes() {
    return qtdDemissoes;
  }
  
  public void setQtdDemissoes(int qtdDemissoes) {
    this.qtdDemissoes = qtdDemissoes;
  }
  
  public int getUltimoDia() {
    ultimoDia = (totalEmpregadosPrimeiroDia + qtdAdmissoes - qtdDemissoes);
    return ultimoDia;
  }
  
  public String getCompetencia() {
    return competencia;
  }
  
  public void setCompetencia(String competencia) {
    this.competencia = competencia;
  }
  
  public int getEncerrouAtividade() {
    return encerrouAtividade;
  }
  
  public void setEncerrouAtividade(int encerrouAtividade) {
    this.encerrouAtividade = encerrouAtividade;
  }
  
  public String toString()
  {
    return razaoSocial;
  }
  
  public int getTotalMovimentacoes()
  {
    return totalMovimentacoes;
  }
  
  public void setTotalMovimentacoes(int totalMovimentacoes) {
    this.totalMovimentacoes = totalMovimentacoes;
  }
  
  public int getTotalAcertos() {
    return totalAcertos;
  }
  
  public void setTotalAcertos(int totalAcertos) {
    this.totalAcertos = totalAcertos;
  }
  
  public String getDescricaoPorte() {
    return descricaoPorte;
  }
  
  public void setDescricaoPorte(String descricaoPorte) {
    this.descricaoPorte = descricaoPorte;
  }
}
