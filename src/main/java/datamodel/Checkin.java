package datamodel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import javax.persistence.*;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Entity
@Table(name = "checkin")
public class Checkin implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "hospede", referencedColumnName = "id")
    private Hospede hospede;

    @Column(name = "data_entrada")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataEntrada;

    @Column(name = "data_saida")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataSaida;

    @Column(name = "adicional_veiculo")
    private Boolean adicionalVeiculo;

    @Transient
    private BigDecimal valorTotal;

    public void calcularValorTotal() throws Exception {
        try {

            valorTotal = BigDecimal.ZERO;

            Boolean saidaNula = dataSaida == null;
            if (saidaNula) {
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

            if (calendar.before(limiteEntrada)) {
                data = limiteEntrada.getTime();
                listData.add(data);
            }

            Calendar virada = Calendar.getInstance();
            virada.setTime(dataEntrada);
            virada.add(Calendar.DATE, 1);
            virada.set(Calendar.HOUR_OF_DAY, 0);
            virada.set(Calendar.MINUTE, 0);
            virada.set(Calendar.SECOND, 0);

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
            }

            if (saidaNula) {
                dataSaida = null;
            }

        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Hospede getHospede() {
        return hospede;
    }

    public void setHospede(Hospede hospede) {
        this.hospede = hospede;
    }

    public Date getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(Date dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public Date getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(Date dataSaida) {
        this.dataSaida = dataSaida;
    }

    public Boolean getAdicionalVeiculo() {
        return adicionalVeiculo;
    }

    public void setAdicionalVeiculo(Boolean adicionalVeiculo) {
        this.adicionalVeiculo = adicionalVeiculo;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

}
