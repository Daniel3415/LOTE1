package br.gov.dataprev.caged.captacao.negocio.to;

import java.io.Serializable;
import java.util.Date;

public class VinculoCAGEDTO
  implements Serializable
{
  private static final long serialVersionUID = 67832325689001771L;
  private Date dataAdmissao;
  private String numeroPISPASEP;
  private String numeroCBO;
  private Date dataDemissao;
  private boolean ativo;
  
  public VinculoCAGEDTO() {}
  
  public Date getDataAdmissao()
  {
    return dataAdmissao;
  }
  
  public void setDataAdmissao(Date dataAdmissao) {
    this.dataAdmissao = dataAdmissao;
  }
  
  public String getNumeroPISPASEP() {
    return numeroPISPASEP;
  }
  
  public void setNumeroPISPASEP(String numeroPISPASEP) {
    this.numeroPISPASEP = numeroPISPASEP;
  }
  
  public String getNumeroCBO() {
    return numeroCBO;
  }
  
  public void setNumeroCBO(String numeroCBO) {
    this.numeroCBO = numeroCBO;
  }
  
  public Date getDataDemissao() {
    return dataDemissao;
  }
  
  public void setDataDemissao(Date dataDemissao) {
    this.dataDemissao = dataDemissao;
  }
  
  public boolean isAtivo() {
    return ativo;
  }
  
  public void setAtivo(boolean ativo) {
    this.ativo = ativo;
  }
}
