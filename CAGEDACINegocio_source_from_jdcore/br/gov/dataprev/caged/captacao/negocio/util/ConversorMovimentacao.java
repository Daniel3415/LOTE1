package br.gov.dataprev.caged.captacao.negocio.util;

import br.gov.dataprev.caged.captacao.integracao.desktop.service.SDCCaptacaoCAGEDServiceDesktop;
import br.gov.dataprev.caged.captacao.negocio.exceptions.BusinessException;
import br.gov.dataprev.caged.captacao.negocio.to.MovimentacaoCAGEDTO;
import br.gov.dataprev.caged.componente.analisador.integracao.service.ISDCAnalisadorCAGEDService;
import br.gov.dataprev.caged.componente.analisador.integracao.service.IntegrationException;
import br.gov.dataprev.caged.modelo.captacao.declaracao.movimentacao.AcertoCAGED;
import br.gov.dataprev.caged.modelo.captacao.declaracao.movimentacao.Movimentacao;
import br.gov.dataprev.caged.modelo.captacao.declaracao.movimentacao.MovimentacaoCAGED;
import br.gov.dataprev.caged.modelo.captacao.declaracao.movimentacao.trabalhador.AprendizCAGED;
import br.gov.dataprev.caged.modelo.captacao.declaracao.movimentacao.trabalhador.Trabalhador;
import br.gov.dataprev.caged.modelo.captacao.declaracao.movimentacao.trabalhador.TrabalhadorCAGED;
import br.gov.dataprev.caged.modelo.competencia.CompetenciaCAGED;
import br.gov.dataprev.caged.modelo.declaracao.movimentacao.TipoAdmissao;
import br.gov.dataprev.caged.modelo.declaracao.movimentacao.TipoDesligamento;
import br.gov.dataprev.caged.modelo.declaracao.movimentacao.TipoMovimento;
import br.gov.dataprev.caged.modelo.declaracao.movimentacao.trabalhador.CTPS;
import br.gov.dataprev.caged.modelo.declaracao.movimentacao.trabalhador.Deficiencia;
import br.gov.dataprev.caged.modelo.declaracao.movimentacao.trabalhador.GrauInstrucao;
import br.gov.dataprev.caged.modelo.declaracao.movimentacao.trabalhador.Raca;
import br.gov.dataprev.caged.modelo.declaracao.movimentacao.trabalhador.Sexo;
import br.gov.dataprev.caged.modelo.documentoidentificacao.CPF;
import br.gov.dataprev.caged.modelo.vinculo.CBO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;












public class ConversorMovimentacao
{
  private static final String PATTERN_DATE = "ddMMyyyy";
  private static final String PATTERN_COMP = "MMyyyy";
  private static SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
  
  private static SimpleDateFormat compFormat = new SimpleDateFormat("MMyyyy");
  private static ISDCAnalisadorCAGEDService sdcService;
  
  public ConversorMovimentacao() {}
  
  private static void alterarDescricaoTipoMovimento(List listaTipoMovimento, TipoMovimento tipoMovimento)
  {
    Iterator i = listaTipoMovimento.iterator();
    
    while (i.hasNext())
    {
      TipoMovimento tm = (TipoMovimento)i.next();
      
      if (tipoMovimento.getId().equals(tm.getId()))
      {
        tipoMovimento.setDescricao(tm.getDescricao());
        return;
      }
    }
  }
  
  public static Movimentacao loadBusinessObject(MovimentacaoCAGEDTO to, EntityManager em)
    throws BusinessException
  {
    sdcService = new SDCCaptacaoCAGEDServiceDesktop(em);
    

    Movimentacao bo;
    
    if ((to.getCompetencia() != null) && (!to.getCompetencia().equals(""))) {
      Movimentacao bo = new AcertoCAGED();
      try {
        ((AcertoCAGED)bo).setCompetencia(new CompetenciaCAGED(compFormat.parse(to.getCompetencia())));
      } catch (ParseException e) {
        throw new BusinessException(e.getMessage());
      }
      ((AcertoCAGED)bo).setAtualizacao(to.getAtualizacao());
    }
    else {
      bo = new MovimentacaoCAGED();
    }
    
    bo.setId(new Long(to.getId()));
    bo.setHorasContratuais(to.getHorasContratuais());
    bo.setDiaDesligamento(to.getDiaDesligamento());
    bo.setRemuneracao(to.getRemuneracao());
    try
    {
      bo.setDataAdmissao(dateFormat.parse(to.getDataAdmissao()));
    }
    catch (ParseException e) {
      throw new BusinessException(e.getMessage());
    }
    
    CBO cbo = new CBO();
    if (to.getCodigoCBO() != null) {
      cbo.setCodigo(to.getCodigoCBO().trim());
      if (to.getDescricaoCBO() != null) {
        cbo.setDescricao(to.getDescricaoCBO().trim());
      }
    }
    

    bo.setCbo(cbo);
    


    List tiposDesligamento = null;
    
    TipoMovimento tipoMovimento = new TipoDesligamento(to.getCodigoMovimento());
    try
    {
      tiposDesligamento = sdcService.obterListaTipoDesligamento();
      
      if (tiposDesligamento.contains(tipoMovimento)) tipoMovimento = new TipoDesligamento(to.getCodigoMovimento()); else {
        tipoMovimento = new TipoAdmissao(to.getCodigoMovimento());
      }
    } catch (IntegrationException e) {
      throw new BusinessException(e.getMessage());
    }
    
    alterarDescricaoTipoMovimento(tiposDesligamento, tipoMovimento);
    


    bo.setTipoMovimento(tipoMovimento);
    

    Trabalhador trabalhador;
    
    Trabalhador trabalhador;
    
    if (to.isEhAprendiz()) trabalhador = new AprendizCAGED(); else {
      trabalhador = new TrabalhadorCAGED();
    }
    trabalhador.setId(to.getId());
    trabalhador.setNome(to.getNome());
    trabalhador.setSexo(new Sexo(to.getSexo()));
    
    if (to.isEhDeficiente()) {
      trabalhador.setPortadorDeficiencia(1);
      trabalhador.setDeficiencia(new Deficiencia(to.getTipoDeficiencia()));
    }
    else {
      trabalhador.setPortadorDeficiencia(2);
      trabalhador.setDeficiencia(null);
    }
    
    trabalhador.setRaca(new Raca(to.getRaca()));
    trabalhador.setGrauInstrucao(new GrauInstrucao(to.getGrauInstrucao()));
    

    if (to.getNit() == null) {
      trabalhador.setNit("");
    }
    else {
      trabalhador.setNit(to.getNit());
    }
    





    if ((to.getCpf() != null) && (!to.getCpf().equals(""))) {
      CPF cpf = new CPF();
      cpf.setNumeroSemValidar(to.getCpf());
      trabalhador.setCpf(cpf);
    }
    





    CTPS ctps = new CTPS();
    ctps.setNumero(to.getNumeroCtps());
    ctps.setSerie(to.getSerieCtps());
    ctps.setSiglaUf(to.getSiglaUfCtps());
    trabalhador.setCtps(ctps);
    try
    {
      trabalhador.setDataNascimento(dateFormat.parse(to.getDataNascimento()));
    }
    catch (ParseException e)
    {
      throw new BusinessException(e.getMessage());
    }
    
    bo.setTrabalhador(trabalhador);
    
    return bo;
  }
  
  public static MovimentacaoCAGEDTO loadTransferObject(Movimentacao bo, EntityManager em)
    throws BusinessException
  {
    sdcService = new SDCCaptacaoCAGEDServiceDesktop(em);
    
    if (bo == null) { return null;
    }
    MovimentacaoCAGEDTO movTO = new MovimentacaoCAGEDTO();
    
    if ((bo instanceof AcertoCAGED))
    {
      AcertoCAGED acerto = (AcertoCAGED)bo;
      movTO.setAtualizacao(acerto.getAtualizacao());
      
      if (acerto.getCompetencia() != null) {
        movTO.setCompetencia(acerto.getCompetencia().toStringDate());
      }
    }
    if (bo.getId() != null) {
      movTO.setId(bo.getId().longValue());
    }
    
    if (bo.getCbo() != null) {
      movTO.setCodigoCBO(bo.getCbo().getCodigo());
      if ((bo.getCbo().getDescricao() == null) && (bo.getCbo().getCodigo() != null)) {
        try {
          bo.setCbo(sdcService.obter(new CBO(bo.getCbo().getCodigo())));
        } catch (IntegrationException e) {
          e.printStackTrace();
        }
      }
      movTO.setDescricaoCBO(bo.getCbo().getDescricao());
    }
    
    movTO.setDataAdmissao(bo.getDataAdmissao() == null ? "" : dateFormat.format(bo.getDataAdmissao()));
    movTO.setDiaDesligamento(bo.getDiaDesligamento());
    movTO.setHorasContratuais(bo.getHorasContratuais());
    movTO.setRemuneracao(bo.getRemuneracao());
    if ((bo.getTipoMovimento() != null) && (bo.getTipoMovimento().getId() != null)) {
      movTO.setCodigoMovimento(bo.getTipoMovimento().getId().intValue());
      if (bo.getTipoMovimento().getDescricao() == null) {
        try {
          bo.setTipoMovimento(sdcService.obterTipoMovimento(bo.getTipoMovimento().getId().intValue()));
        } catch (IntegrationException e) {
          e.printStackTrace();
        }
      }
      movTO.setDescricaoTipoMovimento(bo.getTipoMovimento().getDescricao());
    }
    
    if (bo.getTrabalhador() != null) {
      movTO.setNumeroCtps(bo.getTrabalhador().getCtps().getNumero());
      movTO.setSerieCtps(bo.getTrabalhador().getCtps().getSerie());
      movTO.setSiglaUfCtps(bo.getTrabalhador().getCtps().getSiglaUf());
      movTO.setDataNascimento(bo.getTrabalhador().getDataNascimento() == null ? "" : dateFormat.format(bo.getTrabalhador().getDataNascimento()));
      
      if (bo.getTrabalhador().getGrauInstrucao() != null) {
        movTO.setGrauInstrucao(bo.getTrabalhador().getGrauInstrucao().getCodigo());
        movTO.setDescricaoInstrucao(bo.getTrabalhador().getGrauInstrucao().getDescricao());
      }
      
      Deficiencia deficiencia = bo.getTrabalhador().getDeficiencia();
      movTO.setTipoDeficiencia(deficiencia == null ? 0 : deficiencia.getId());
      movTO.setDescricaoDeficiencia(deficiencia == null ? "" : deficiencia.getNome());
      
      if (bo.getTrabalhador().getNit().equals("")) {
        movTO.setNit(null);
      }
      else if (bo.getTrabalhador().getNit().equals("00000000000")) {
        movTO.setNit("00000000000");
      } else {
        movTO.setNit(bo.getTrabalhador().getNit());
      }
      movTO.setNome(bo.getTrabalhador().getNome());
      movTO.setRaca(bo.getTrabalhador().getRaca().getId());
      movTO.setDescricaoRaca(bo.getTrabalhador().getRaca().getNome());
      movTO.setSexo(bo.getTrabalhador().getSexo().getId());
      movTO.setEhDeficiente(bo.getTrabalhador().getPortadorDeficiencia() == 1);
      movTO.setEhAprendiz(bo.getTrabalhador() instanceof AprendizCAGED);
      
      if (bo.getTrabalhador().getCpf() != null) {
        movTO.setCpf(bo.getTrabalhador().getCpf().getNumero());
      }
    }
    
    return movTO;
  }
}
