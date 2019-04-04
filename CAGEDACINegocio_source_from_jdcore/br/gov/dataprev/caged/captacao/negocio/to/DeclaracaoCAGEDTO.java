package br.gov.dataprev.caged.captacao.negocio.to;

import java.util.ArrayList;
import java.util.List;


public class DeclaracaoCAGEDTO
{
  private Long id;
  private int anoCompetencia;
  private int mesCompetencia;
  private String nuRecibo;
  private String tipo;
  private String descricaoSituacao;
  private int codigoSituacao;
  private double salarioMinimo;
  private AutorizadoCAGEDTO autorizado;
  private List listaDeclaracaoEstabelecimentoTO;
  
  public DeclaracaoCAGEDTO()
  {
    listaDeclaracaoEstabelecimentoTO = new ArrayList();
  }
  
  public AutorizadoCAGEDTO getAutorizado() {
    return autorizado;
  }
  
  public void setAutorizado(AutorizadoCAGEDTO autorizado) { this.autorizado = autorizado; }
  
  public double getSalarioMinimo() {
    return salarioMinimo;
  }
  
  public void setSalarioMinimo(double salarioMinimo) { this.salarioMinimo = salarioMinimo; }
  
  public int getAnoCompetencia() {
    return anoCompetencia;
  }
  
  public void setAnoCompetencia(int anoCompetencia) { this.anoCompetencia = anoCompetencia; }
  
  public int getMesCompetencia() {
    return mesCompetencia;
  }
  
  public void setMesCompetencia(int mesCompetencia) { this.mesCompetencia = mesCompetencia; }
  
  public String getNuRecibo() {
    return nuRecibo;
  }
  
  public void setNuRecibo(String nurecibo) { nuRecibo = nurecibo; }
  
  public String getTipo() {
    return tipo;
  }
  
  public void setTipo(String tipo) { this.tipo = tipo; }
  
  public Long getId() {
    return id;
  }
  
  public void setId(Long id) { this.id = id; }
  
  public List getListaDeclaracaoEstabelecimentoTO() {
    if (listaDeclaracaoEstabelecimentoTO == null) {
      listaDeclaracaoEstabelecimentoTO = new ArrayList();
    }
    return listaDeclaracaoEstabelecimentoTO;
  }
  
  public void setListaDeclaracaoEstabelecimentoTO(List listaDeclaracaoEstabelecimentoTO) {
    this.listaDeclaracaoEstabelecimentoTO = listaDeclaracaoEstabelecimentoTO;
  }
  
  public String getDescricaoSituacao() { return descricaoSituacao; }
  
  public void setDescricaoSituacao(String descricaoSituacao) {
    this.descricaoSituacao = descricaoSituacao;
  }
  
  public int getCodigoSituacao() { return codigoSituacao; }
  
  public void setCodigoSituacao(int codigoSituacao) {
    this.codigoSituacao = codigoSituacao;
  }
}
