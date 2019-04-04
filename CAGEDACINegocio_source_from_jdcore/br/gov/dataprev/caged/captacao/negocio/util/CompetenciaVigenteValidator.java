package br.gov.dataprev.caged.captacao.negocio.util;

import br.gov.dataprev.caged.modelo.competencia.CompetenciaCAGED;
import java.util.HashSet;
import java.util.Set;

public class CompetenciaVigenteValidator
{
  public CompetenciaVigenteValidator() {}
  
  public boolean ehValida(CompetenciaCAGED competencia)
  {
    CompetenciaCAGED compAtual = new CompetenciaCAGED().obterCompetenciaAtual();
    
    CompetenciaCAGED compAnteriorAtual = null;
    
    if (compAtual.getMes() == 1) {
      compAnteriorAtual = new CompetenciaCAGED(12, compAtual.getAno() - 1);
    } else {
      compAnteriorAtual = new CompetenciaCAGED(compAtual.getMes() - 1, compAtual.getAno());
    }
    

    Set<CompetenciaCAGED> competenciasVigentes = new HashSet();
    competenciasVigentes.add(compAtual);
    competenciasVigentes.add(compAnteriorAtual);
    
    return competenciasVigentes.contains(competencia);
  }
}
