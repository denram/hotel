package teste;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        try {

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            Date dataEntrada;
            Date dataSaida;

            Boolean adicionalVeiculo = false;

            BigDecimal valorTotal = BigDecimal.ZERO;

            dataEntrada = sdf.parse("05/11/2018 16:40:00");
            dataSaida = sdf.parse("07/11/2018 17:00:00");
            
            if(dataSaida == null){
                dataSaida = new Date();
            }

            List<Date> listData = new ArrayList<Date>();
            Date data;

            Calendar calendar = new GregorianCalendar();
            calendar.setTime(dataEntrada);

            Calendar limiteEntrada = Calendar.getInstance();
            limiteEntrada.set(Calendar.DATE, calendar.get(Calendar.DATE));
            limiteEntrada.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
            limiteEntrada.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
            limiteEntrada.set(Calendar.HOUR_OF_DAY, 16);
            limiteEntrada.set(Calendar.MINUTE, 29);
            limiteEntrada.set(Calendar.SECOND, 59);

            Calendar virada = Calendar.getInstance();
            virada.setTime(dataEntrada);
            virada.add(Calendar.DATE, 1);
            virada.set(Calendar.HOUR_OF_DAY, 0);
            virada.set(Calendar.MINUTE, 0);
            virada.set(Calendar.SECOND, 0);

            if (calendar.before(limiteEntrada)) {
                data = limiteEntrada.getTime();
                listData.add(data);
            }
            
            while (virada.getTime().before(dataSaida)) {
                data = virada.getTime();
                listData.add(data);
                virada.add(Calendar.DATE, 1);
            }

            calendar.setTime(dataSaida);
            Calendar limiteSaida = Calendar.getInstance();
            limiteSaida.set(Calendar.DATE, calendar.get(Calendar.DATE));
            limiteSaida.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
            limiteSaida.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
            limiteSaida.set(Calendar.HOUR_OF_DAY, 16);
            limiteSaida.set(Calendar.MINUTE, 30);
            limiteSaida.set(Calendar.SECOND, 59);

            if (calendar.after(limiteSaida)) {
                data = calendar.getTime();
                listData.add(data);
            }

            for (Date d : listData) {
                calendar = new GregorianCalendar();
                calendar.setTime(d);
                if ((calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) || (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)) {
                    valorTotal = valorTotal.add(new BigDecimal("150"));
                    if (adicionalVeiculo) {
                        valorTotal = valorTotal.add(new BigDecimal("20"));
                    }
                } else {
                    valorTotal = valorTotal.add(new BigDecimal("120"));
                    if (adicionalVeiculo) {
                        valorTotal = valorTotal.add(new BigDecimal("15"));
                    }
                }
                System.out.println(sdf.format(d) + " : " + valorTotal.toString());
            }

            System.out.println(valorTotal.toString());
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

}
