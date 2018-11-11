package webservices;

import datamodel.Checkin;
import datamodel.Hospede;
import dataservices.CheckinDataService;
import dataservices.HibernateUtil;
import dataservices.HospedeDataService;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;
import util.DataUtil;

@Path("checkin")
public class CheckinWebService {

    @POST
    @Path("entrar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String entrar(String param) {
        Session session = null;
        Transaction transaction = null;
        JSONObject retorno = new JSONObject();
        try {

            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction().commit();

            JSONArray jsonParam = new JSONArray(param);
            JSONObject jsonHospede = jsonParam.getJSONObject(0);
            JSONObject jsonCheckin = jsonParam.getJSONObject(1);

            Iterator<String> keys = jsonHospede.keys();
            String key = keys.next();
            String value = jsonHospede.getString(key);

            Hospede hospede = HospedeDataService.getHospede(session, key, value);

            Checkin checkin = CheckinDataService.getCheckinAberto(session, hospede);
            if (checkin == null) {
                checkin = new Checkin();
                checkin.setId(0);
                checkin.setHospede(hospede);
                checkin.setDataEntrada(DataUtil.getData(jsonCheckin.getString("dataEntrada")));
                checkin.setDataSaida(null);
                checkin.setAdicionalVeiculo(jsonCheckin.getBoolean("adicionalVeiculo"));
                checkin.setValorTotal(BigDecimal.ZERO.setScale(2));
                transaction = session.beginTransaction();
                CheckinDataService.setCheckin(session, checkin);
                transaction.commit();
            } else {
                checkin.calcularValorTotal();
            }
            checkin.getHospede().calcularValores(session);

            retorno = new JSONObject(checkin);
            retorno.put("dataEntrada", DataUtil.getDataIso(checkin.getDataEntrada()));
            retorno.put("dataSaida", DataUtil.getDataIso(checkin.getDataSaida()));

            retorno = Retorno.set(1, "Sucesso", "checkin", retorno, null);

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            retorno = Retorno.set(0, e.getMessage(), "", null, null);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return retorno.toString();
    }

    @POST
    @Path("sair")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String sair(String param) {
        Session session = null;
        Transaction transaction = null;
        JSONObject retorno = new JSONObject();
        try {

            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction().commit();

            JSONArray jsonParam = new JSONArray(param);
            JSONObject jsonHospede = jsonParam.getJSONObject(0);
            JSONObject jsonCheckin = jsonParam.getJSONObject(1);

            Iterator<String> keys = jsonHospede.keys();
            String key = keys.next();
            String value = jsonHospede.getString(key);

            Hospede hospede = HospedeDataService.getHospede(session, key, value);

            Checkin checkin = CheckinDataService.getCheckinAberto(session, hospede);

            checkin.setDataSaida(DataUtil.getData(jsonCheckin.getString("dataSaida")));

            transaction = session.beginTransaction();
            CheckinDataService.setCheckin(session, checkin);
            transaction.commit();

            checkin.calcularValorTotal();
            checkin.getHospede().calcularValores(session);

            retorno = new JSONObject(checkin);
            retorno.put("dataEntrada", DataUtil.getDataIso(checkin.getDataEntrada()));
            retorno.put("dataSaida", DataUtil.getDataIso(checkin.getDataSaida()));

            retorno = Retorno.set(1, "Sucesso", "checkin", retorno, null);

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            retorno = Retorno.set(0, e.getMessage(), "", null, null);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return retorno.toString();
    }

    @POST
    @Path("consultarHistoricoHospede")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String consultarHistorico(String param) {
        Session session = null;
        JSONObject retorno = new JSONObject();
        try {

            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction().commit();

            JSONObject jsonHospede = new JSONObject(param);

            Iterator<String> keys = jsonHospede.keys();
            String key = keys.next();
            String value = jsonHospede.getString(key);

            Hospede hospede = HospedeDataService.getHospede(session, key, value);

            List<Checkin> listCheckin = CheckinDataService.listCheckin(session, hospede);

            JSONArray jsonArrayCheckin = new JSONArray();
            for (int i = 0; i < listCheckin.size(); i++) {
                Checkin checkin = listCheckin.get(i);
                checkin.calcularValorTotal();

                JSONObject jsonObjectCheckin = new JSONObject(listCheckin.get(i));
                jsonObjectCheckin.remove("hospede");
                jsonObjectCheckin.put("dataEntrada", DataUtil.getDataIso(checkin.getDataEntrada()));
                jsonObjectCheckin.put("dataSaida", DataUtil.getDataIso(checkin.getDataSaida()));

                jsonArrayCheckin.put(i, jsonObjectCheckin);
            }

            retorno = Retorno.set(1, "Sucesso", "listaCheckin", null, jsonArrayCheckin);

        } catch (Exception e) {
            retorno = Retorno.set(0, e.getMessage(), "", null, null);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return retorno.toString();
    }

}
