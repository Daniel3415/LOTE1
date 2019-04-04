package br.gov.dataprev.caged.captacao.negocio.to;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;












































public class MovimentacaoCAGEDTO
  implements Serializable
{
  private static final long serialVersionUID = 3714437410469515438L;
  private long id;
  private String competencia;
  private int atualizacao;
  private String nit;
  private String nome;
  private String numeroCtps;
  private String serieCtps;
  private String siglaUfCtps;
  private String cpf;
  private String dataNascimento;
  private int raca;
  private String descricaoRaca;
  private boolean ehDeficiente;
  private int tipoDeficiencia;
  private int sexo;
  private int grauInstrucao;
  private String descricaoInstrucao;
  private int codigoMovimento;
  private String descricaoTipoMovimento;
  private String dataAdmissao;
  private double remuneracao;
  private int horasContratuais;
  private int diaDesligamento;
  private boolean ehAprendiz;
  private String codigoCBO;
  private String descricaoCBO;
  private String descricaoDeficiencia;
  private DeclaracaoEstabelecimentoCAGEDTO estabelecimentoTO;
  private DeclaracaoCAGEDTO declaracao;
  
  public MovimentacaoCAGEDTO() {}
  
  public long getId()
  {
    return id;
  }
  
  public void setId(long id) {
    this.id = id;
  }
  
  public String getCompetencia() {
    return competencia;
  }
  
  public void setCompetencia(String competencia) {
    this.competencia = competencia;
  }
  
  public int getAtualizacao() {
    return atualizacao;
  }
  
  public void setAtualizacao(int atualizacao) {
    this.atualizacao = atualizacao;
  }
  
  public int getCodigoMovimento() {
    return codigoMovimento;
  }
  
  public void setCodigoMovimento(int codigoMovimento) {
    this.codigoMovimento = codigoMovimento;
  }
  
  public String getDescricaoTipoMovimento() {
    return descricaoTipoMovimento;
  }
  
  public void setDescricaoTipoMovimento(String descricaoTipoMovimento) {
    this.descricaoTipoMovimento = descricaoTipoMovimento;
  }
  
  public String getCodigoCBO() {
    return codigoCBO;
  }
  
  public void setCodigoCBO(String codigoCBO) {
    this.codigoCBO = codigoCBO;
  }
  
  public String getDescricaoCBO() {
    return descricaoCBO;
  }
  
  public void setDescricaoCBO(String descricaoCBO) {
    this.descricaoCBO = descricaoCBO;
  }
  
  public String getDataAdmissao() {
    return dataAdmissao;
  }
  
  public void setDataAdmissao(String dataAdmissao) {
    this.dataAdmissao = dataAdmissao;
  }
  
  public int getDiaDesligamento() {
    return diaDesligamento;
  }
  
  public void setDiaDesligamento(int diaDesligamento) {
    this.diaDesligamento = diaDesligamento;
  }
  
  public double getRemuneracao() {
    return remuneracao;
  }
  
  public void setRemuneracao(double remuneracao) {
    this.remuneracao = remuneracao;
  }
  
  public int getHorasContratuais() {
    return horasContratuais;
  }
  
  public void setHorasContratuais(int horasContratuais) {
    this.horasContratuais = horasContratuais;
  }
  
  public boolean isEhAprendiz() {
    return ehAprendiz;
  }
  
  public void setEhAprendiz(boolean ehAprendiz) {
    this.ehAprendiz = ehAprendiz;
  }
  
  public String getNit() {
    return nit;
  }
  
  public void setNit(String nit) {
    this.nit = nit;
  }
  
  public String getCpf() {
    return cpf;
  }
  
  public void setCpf(String cpf) {
    this.cpf = cpf;
  }
  
  public String getNome() {
    return nome;
  }
  
  public void setNome(String nome) {
    this.nome = nome;
  }
  
  public String getDataNascimento() {
    return dataNascimento;
  }
  
  public void setDataNascimento(String dataNascimento) {
    this.dataNascimento = dataNascimento;
  }
  
  public int getSexo() {
    return sexo;
  }
  
  public void setSexo(int sexo) {
    this.sexo = sexo;
  }
  
  public String getNumeroCtps() {
    return numeroCtps;
  }
  
  public void setNumeroCtps(String numeroCtps) {
    this.numeroCtps = numeroCtps;
  }
  
  public String getSerieCtps() {
    return serieCtps;
  }
  
  public void setSerieCtps(String serieCtps) {
    this.serieCtps = serieCtps;
  }
  
  public String getSiglaUfCtps() {
    return siglaUfCtps;
  }
  
  public void setSiglaUfCtps(String siglaUfCtps) {
    this.siglaUfCtps = siglaUfCtps;
  }
  
  public int getGrauInstrucao() {
    return grauInstrucao;
  }
  
  public void setGrauInstrucao(int grauInstrucao) {
    this.grauInstrucao = grauInstrucao;
  }
  
  public int getRaca() {
    return raca;
  }
  
  public void setRaca(int raca) {
    this.raca = raca;
  }
  
  public boolean isEhDeficiente() {
    return ehDeficiente;
  }
  
  public void setEhDeficiente(boolean ehDeficiente) {
    this.ehDeficiente = ehDeficiente;
  }
  
  public int getTipoDeficiencia() {
    return tipoDeficiencia;
  }
  
  public void setTipoDeficiencia(int tipoDeficiencia) {
    this.tipoDeficiencia = tipoDeficiencia;
  }
  
  public DeclaracaoEstabelecimentoCAGEDTO getEstabelecimentoTO() {
    return estabelecimentoTO;
  }
  
  public void setEstabelecimentoTO(DeclaracaoEstabelecimentoCAGEDTO estabelecimentoTO)
  {
    this.estabelecimentoTO = estabelecimentoTO;
  }
  
  public DeclaracaoCAGEDTO getDeclaracao() {
    return declaracao;
  }
  
  public void setDeclaracao(DeclaracaoCAGEDTO declaracao) {
    this.declaracao = declaracao;
  }
  
  public boolean equalsId(Object obj)
  {
    if ((obj != null) && ((obj instanceof MovimentacaoCAGEDTO)))
    {
      MovimentacaoCAGEDTO movimentacao = (MovimentacaoCAGEDTO)obj;
      
      if ((id == -1L) && (id == -1L)) {
        return super.equals(movimentacao);
      }
      return new EqualsBuilder().append(id, id).isEquals();
    }
    


    return false;
  }
  
  public String toString()
  {
    return new ToStringBuilder(this).append("nit", nit).append("nome", nome).append("numeroCtps", numeroCtps).append("serieCtps", serieCtps).append("siglaUfCtps", siglaUfCtps).append("cpf", cpf).append("dataNascimento", dataNascimento).append("raca", raca).append("ehDeficiente", ehDeficiente).append("tipoDeficiencia", tipoDeficiencia).append("sexo", sexo).append("grauInstrucao", grauInstrucao).append("codigoMovimento", codigoMovimento).append("descricaoTipoMovimento", descricaoTipoMovimento).append("dataAdmissao", dataAdmissao).append("remuneracao", remuneracao).append("horasContratuais", horasContratuais).append("diaDesligamento", diaDesligamento).append("ehAprendiz", ehAprendiz).append("codigoCBO", codigoCBO).append("descricaoCBO", descricaoCBO).toString();
  }
  





















  public String getDescricaoRaca()
  {
    return descricaoRaca;
  }
  
  public void setDescricaoRaca(String descricaoRaca) {
    this.descricaoRaca = descricaoRaca;
  }
  
  public String getDescricaoInstrucao() {
    return descricaoInstrucao;
  }
  
  public void setDescricaoInstrucao(String descricaoInstrucao) {
    this.descricaoInstrucao = descricaoInstrucao;
  }
  
  public String getDescricaoDeficiencia() {
    return descricaoDeficiencia;
  }
  
  public void setDescricaoDeficiencia(String descricaoDeficiencia) {
    this.descricaoDeficiencia = descricaoDeficiencia;
  }
  
  public boolean ehAcerto() {
    return (getAtualizacao() != 0) && (getCompetencia() != null) && (!getCompetencia().equals(""));
  }
  

  public int hashCode()
  {
    int prime = 31;
    int result = 1;
    result = 31 * result + atualizacao;
    result = 31 * result + (codigoCBO == null ? 0 : codigoCBO.hashCode());
    
    result = 31 * result + codigoMovimento;
    result = 31 * result + (competencia == null ? 0 : competencia.hashCode());
    
    result = 31 * result + (cpf == null ? 0 : cpf.hashCode());
    result = 31 * result + (dataAdmissao == null ? 0 : dataAdmissao.hashCode());
    
    result = 31 * result + (dataNascimento == null ? 0 : dataNascimento.hashCode());
    
    result = 31 * result + (descricaoCBO == null ? 0 : descricaoCBO.hashCode());
    
    result = 31 * result + (descricaoDeficiencia == null ? 0 : descricaoDeficiencia.hashCode());
    


    result = 31 * result + (descricaoInstrucao == null ? 0 : descricaoInstrucao.hashCode());
    


    result = 31 * result + (descricaoRaca == null ? 0 : descricaoRaca.hashCode());
    
    result = 31 * result + (descricaoTipoMovimento == null ? 0 : descricaoTipoMovimento.hashCode());
    


    result = 31 * result + diaDesligamento;
    result = 31 * result + (ehAprendiz ? 1231 : 1237);
    result = 31 * result + (ehDeficiente ? 1231 : 1237);
    result = 31 * result + grauInstrucao;
    result = 31 * result + horasContratuais;
    result = 31 * result + (int)(id ^ id >>> 32);
    result = 31 * result + (nit == null ? 0 : nit.hashCode());
    result = 31 * result + (nome == null ? 0 : nome.hashCode());
    result = 31 * result + (numeroCtps == null ? 0 : numeroCtps.hashCode());
    
    result = 31 * result + raca;
    
    long temp = Double.doubleToLongBits(remuneracao);
    result = 31 * result + (int)(temp ^ temp >>> 32);
    result = 31 * result + (serieCtps == null ? 0 : serieCtps.hashCode());
    
    result = 31 * result + sexo;
    result = 31 * result + (siglaUfCtps == null ? 0 : siglaUfCtps.hashCode());
    
    result = 31 * result + tipoDeficiencia;
    return result;
  }
  
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    MovimentacaoCAGEDTO other = (MovimentacaoCAGEDTO)obj;
    if (atualizacao != atualizacao)
      return false;
    if (codigoCBO == null) {
      if (codigoCBO != null)
        return false;
    } else if (!codigoCBO.equals(codigoCBO))
      return false;
    if (codigoMovimento != codigoMovimento)
      return false;
    if (competencia == null) {
      if (competencia != null)
        return false;
    } else if (!competencia.equals(competencia))
      return false;
    if (cpf == null) {
      if (cpf != null)
        return false;
    } else if (!cpf.equals(cpf))
      return false;
    if (dataAdmissao == null) {
      if (dataAdmissao != null)
        return false;
    } else if (!dataAdmissao.equals(dataAdmissao))
      return false;
    if (dataNascimento == null) {
      if (dataNascimento != null)
        return false;
    } else if (!dataNascimento.equals(dataNascimento))
      return false;
    if (descricaoCBO == null) {
      if (descricaoCBO != null)
        return false;
    } else if (!descricaoCBO.equals(descricaoCBO))
      return false;
    if (descricaoDeficiencia == null) {
      if (descricaoDeficiencia != null)
        return false;
    } else if (!descricaoDeficiencia.equals(descricaoDeficiencia))
      return false;
    if (descricaoInstrucao == null) {
      if (descricaoInstrucao != null)
        return false;
    } else if (!descricaoInstrucao.equals(descricaoInstrucao))
      return false;
    if (descricaoRaca == null) {
      if (descricaoRaca != null)
        return false;
    } else if (!descricaoRaca.equals(descricaoRaca))
      return false;
    if (descricaoTipoMovimento == null) {
      if (descricaoTipoMovimento != null)
        return false;
    } else if (!descricaoTipoMovimento.equals(descricaoTipoMovimento))
      return false;
    if (diaDesligamento != diaDesligamento)
      return false;
    if (ehAprendiz != ehAprendiz)
      return false;
    if (ehDeficiente != ehDeficiente)
      return false;
    if (grauInstrucao != grauInstrucao)
      return false;
    if (horasContratuais != horasContratuais)
      return false;
    if (id != id)
      return false;
    if (nit == null) {
      if (nit != null)
        return false;
    } else if (!nit.equals(nit))
      return false;
    if (nome == null) {
      if (nome != null)
        return false;
    } else if (!nome.equals(nome))
      return false;
    if (numeroCtps == null) {
      if (numeroCtps != null)
        return false;
    } else if (!numeroCtps.equals(numeroCtps))
      return false;
    if (raca != raca)
      return false;
    if (Double.doubleToLongBits(remuneracao) != Double.doubleToLongBits(remuneracao))
    {
      return false; }
    if (serieCtps == null) {
      if (serieCtps != null)
        return false;
    } else if (!serieCtps.equals(serieCtps))
      return false;
    if (sexo != sexo)
      return false;
    if (siglaUfCtps == null) {
      if (siglaUfCtps != null)
        return false;
    } else if (!siglaUfCtps.equals(siglaUfCtps))
      return false;
    if (tipoDeficiencia != tipoDeficiencia)
      return false;
    return true;
  }
}
