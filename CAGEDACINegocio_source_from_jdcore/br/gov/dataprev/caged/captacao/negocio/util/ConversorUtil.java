package br.gov.dataprev.caged.captacao.negocio.util;

import java.util.Calendar;
import java.util.Date;



















public class ConversorUtil
{
  public ConversorUtil() {}
  
  public static int yearsCalculate(Date begin, Date end)
  {
    Calendar calBegin = Calendar.getInstance();
    calBegin.setTime(begin);
    
    Calendar calEnd = Calendar.getInstance();
    calEnd.setTime(end == null ? new Date() : end);
    
    if (calEnd.get(1) <= calBegin.get(1)) { return 0;
    }
    int years = calEnd.get(1) - calBegin.get(1);
    
    if (calEnd.get(2) < calBegin.get(2)) { years--;
    } else if ((calEnd.get(2) == calBegin.get(2)) && (calEnd.get(5) < calBegin.get(5))) {
      years--;
    }
    return years;
  }
  










  public static String accentRemove(String word)
  {
    String cAcento = "áàãâäéèêëíìîïóòõôöúùûüçÁÀÃÂÄÉÈÊËÍÌÎÏÓÒÕÖÓÚÙÛÜÇ";
    String sAcento = "aaaaaeeeeiiiiooooouuuucAAAAAEEEEIIIIOOOOOUUUUC";
    String output = "";
    
    for (int i = 0; i < word.length(); i++)
    {
      if (cAcento.contains(word.substring(i, i + 1))) {
        int index = cAcento.indexOf(word.substring(i, i + 1));
        output = output + sAcento.substring(index, index + 1);
      }
      else {
        output = output + word.substring(i, i + 1);
      }
    }
    
    return output;
  }
}
