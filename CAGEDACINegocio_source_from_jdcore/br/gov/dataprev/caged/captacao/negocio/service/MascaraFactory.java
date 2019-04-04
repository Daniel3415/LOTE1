package br.gov.dataprev.caged.captacao.negocio.service;

import java.text.ParseException;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;













public final class MascaraFactory
{
  private static MascaraFactory _instance = new MascaraFactory();
  
  private DefaultFormatterFactory factoryCEI;
  private DefaultFormatterFactory factoryCNPJ;
  private DefaultFormatterFactory factoryPISPASEP;
  private DefaultFormatterFactory factoryCPF;
  private DefaultFormatterFactory factoryCEP;
  private DefaultFormatterFactory factoryData;
  
  private MascaraFactory()
  {
    MaskFormatter mask = null;
    try
    {
      mask = new MaskFormatter("#####.#####-##");
      mask.setPlaceholderCharacter('_');
      factoryCEI = new DefaultFormatterFactory(mask);
      
      mask = new MaskFormatter("##.###.###/####-##");
      mask.setPlaceholderCharacter('_');
      factoryCNPJ = new DefaultFormatterFactory(mask);
      
      mask = new MaskFormatter("###.#####.##-#");
      mask.setPlaceholderCharacter('_');
      factoryPISPASEP = new DefaultFormatterFactory(mask);
      
      mask = new MaskFormatter("###.###.###-##");
      mask.setPlaceholderCharacter('_');
      factoryCPF = new DefaultFormatterFactory(mask);
      
      mask = new MaskFormatter("#####-###");
      mask.setPlaceholderCharacter('_');
      factoryCEP = new DefaultFormatterFactory(mask);
      
      mask = new MaskFormatter("##/##/####");
      mask.setPlaceholderCharacter('_');
      factoryData = new DefaultFormatterFactory(mask);
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }
  
  public static MascaraFactory getInstance() {
    if (_instance == null) _instance = new MascaraFactory();
    return _instance;
  }
  
  public DefaultFormatterFactory getFactoryCEI() {
    return factoryCEI;
  }
  
  public DefaultFormatterFactory getFactoryCNPJ() {
    return factoryCNPJ;
  }
  
  public DefaultFormatterFactory getFactoryCPF() {
    return factoryCPF;
  }
  
  public DefaultFormatterFactory getFactoryCEP() {
    return factoryCEP;
  }
  
  public DefaultFormatterFactory getFactoryData() {
    return factoryData;
  }
  
  public DefaultFormatterFactory getFactoryPISPASEP() {
    return factoryPISPASEP;
  }
}
