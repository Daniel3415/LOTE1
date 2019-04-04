package br.gov.dataprev.caged.captacao.negocio.service;

import br.gov.dataprev.caged.captacao.negocio.to.DeclaracaoCAGEDTO;
import br.gov.dataprev.caged.captacao.negocio.to.DeclaracaoEstabelecimentoCAGEDTO;
import br.gov.dataprev.caged.captacao.negocio.to.MovimentacaoCAGEDTO;
import java.util.List;










public final class DeclaracaoAtivaSingleton
{
  private static DeclaracaoAtivaSingleton _instance = new DeclaracaoAtivaSingleton();
  
  private DeclaracaoCAGEDTO declaracaoAtiva;
  
  private DeclaracaoEstabelecimentoCAGEDTO estabelecimentoAtivoDasMovimentacoes;
  
  private DeclaracaoEstabelecimentoCAGEDTO estabelecimentoAtivoDoEstabelecimento;
  
  private DeclaracaoEstabelecimentoCAGEDTO estabelecimentoAtivoDosAcertos;
  
  private MovimentacaoCAGEDTO movimentacaoAtiva;
  
  private MovimentacaoCAGEDTO acertoAtivo;
  
  private String nomeArquivoAtivo;
  
  private DeclaracaoAtivaSingleton() {}
  
  public static DeclaracaoAtivaSingleton getInstance()
  {
    if (_instance == null) _instance = new DeclaracaoAtivaSingleton();
    return _instance;
  }
  


  public DeclaracaoCAGEDTO getDeclaracaoAtiva()
  {
    return declaracaoAtiva;
  }
  
  public void setDeclaracaoAtiva(DeclaracaoCAGEDTO declaracaoAtiva) {
    this.declaracaoAtiva = declaracaoAtiva;
  }
  
  public List getEstabelecimentosAtivosDaDeclaracao() {
    return declaracaoAtiva.getListaDeclaracaoEstabelecimentoTO();
  }
  


  public DeclaracaoEstabelecimentoCAGEDTO getEstabelecimentoAtivoDasMovimentacoes()
  {
    return estabelecimentoAtivoDasMovimentacoes;
  }
  
  public void setEstabelecimentoAtivoDasMovimentacoes(DeclaracaoEstabelecimentoCAGEDTO estabelecimentoAtivoDasMovimentacoes) {
    this.estabelecimentoAtivoDasMovimentacoes = estabelecimentoAtivoDasMovimentacoes;
  }
  
  public List getListaMovimentacoesAtivas() {
    return estabelecimentoAtivoDasMovimentacoes == null ? null : estabelecimentoAtivoDasMovimentacoes.getListaMovimentacoes();
  }
  
  public void setListaMovimentacoesAtivas(List listaMovimentacoes)
  {
    estabelecimentoAtivoDasMovimentacoes.setListaMovimentacoes(listaMovimentacoes);
  }
  
  public List<MovimentacaoCAGEDTO> getListaAcertosAtivos() {
    return estabelecimentoAtivoDosAcertos == null ? null : estabelecimentoAtivoDosAcertos.getListaAcertos();
  }
  
  public void setListaAcertosAtivos(List listaAcertos)
  {
    estabelecimentoAtivoDosAcertos.setListaAcertos(listaAcertos);
  }
  



  public DeclaracaoEstabelecimentoCAGEDTO getEstabelecimentoAtivoDosAcertos()
  {
    return estabelecimentoAtivoDosAcertos;
  }
  
  public void setEstabelecimentoAtivoDosAcertos(DeclaracaoEstabelecimentoCAGEDTO estabelecimentoAtivoDosAcertos) {
    this.estabelecimentoAtivoDosAcertos = estabelecimentoAtivoDosAcertos;
  }
  



  public MovimentacaoCAGEDTO getAcertoAtivo()
  {
    return acertoAtivo;
  }
  
  public void setAcertoAtivo(MovimentacaoCAGEDTO acertoAtivo) {
    this.acertoAtivo = acertoAtivo;
  }
  


  public MovimentacaoCAGEDTO getMovimentacaoAtiva()
  {
    return movimentacaoAtiva;
  }
  
  public void setMovimentacaoAtiva(MovimentacaoCAGEDTO movimentacaoAtiva) {
    this.movimentacaoAtiva = movimentacaoAtiva;
  }
  


  public DeclaracaoEstabelecimentoCAGEDTO getEstabelecimentoAtivoDoEstabelecimento()
  {
    return estabelecimentoAtivoDoEstabelecimento;
  }
  
  public void setEstabelecimentoAtivoDoEstabelecimento(DeclaracaoEstabelecimentoCAGEDTO estabelecimentoAtivoDoEstabelecimento) {
    this.estabelecimentoAtivoDoEstabelecimento = estabelecimentoAtivoDoEstabelecimento;
  }
  


  public String getNomeArquivoAtivo()
  {
    return nomeArquivoAtivo;
  }
  


  public void setNomeArquivoAtivo(String nomeArquivoAtivo)
  {
    this.nomeArquivoAtivo = nomeArquivoAtivo;
  }
}
