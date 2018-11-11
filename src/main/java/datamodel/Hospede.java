package datamodel;

import dataservices.CheckinDataService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import javax.persistence.*;
import org.hibernate.Session;

@Entity
@Table(name = "hospede")
public class Hospede implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nome", length = 50)
    private String nome;

    @Column(name = "documento", length = 25)
    private String documento;

    @Column(name = "telefone", length = 25)
    private String telefone;

    @Transient
    private BigDecimal valorTotal;

    @Transient
    private BigDecimal valorUltimaHospedagem;

    public void calcularValores(Session session) throws Exception {
        try {

            valorTotal = BigDecimal.ZERO;
            valorUltimaHospedagem = BigDecimal.ZERO;

            List<Checkin> listCheckin = CheckinDataService.listCheckin(session, this);
            Checkin checkin;

            for (int i = 0; i < listCheckin.size(); i++) {
                checkin = listCheckin.get(i);
                checkin.calcularValorTotal();
                valorTotal = valorTotal.add(checkin.getValorTotal());
                if (i == listCheckin.size() - 1) {
                    valorUltimaHospedagem = checkin.getValorTotal();
                }
            }

            valorTotal.setScale(2, RoundingMode.HALF_UP);
            valorUltimaHospedagem.setScale(2, RoundingMode.HALF_UP);

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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public BigDecimal getValorUltimaHospedagem() {
        return valorUltimaHospedagem;
    }

    public void setValorUltimaHospedagem(BigDecimal valorUltimaHospedagem) {
        this.valorUltimaHospedagem = valorUltimaHospedagem;
    }

}
