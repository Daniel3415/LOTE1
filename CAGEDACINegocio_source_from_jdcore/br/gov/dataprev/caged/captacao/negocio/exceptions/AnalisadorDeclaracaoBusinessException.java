package br.gov.dataprev.caged.captacao.negocio.exceptions;

import br.gov.dataprev.caged.componente.analisador.novalidacao.NoValidacao;

public class AnalisadorDeclaracaoBusinessException extends BusinessException
{
  private NoValidacao mensagens;
  
  public AnalisadorDeclaracaoBusinessException(NoValidacao listaMensagens)
  {
    mensagens = listaMensagens;
  }
  
  public NoValidacao getMensagens() {
    return mensagens;
  }
}
