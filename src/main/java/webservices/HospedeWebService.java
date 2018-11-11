package webservices;

import datamodel.Hospede;
import dataservices.HibernateUtil;
import dataservices.HospedeDataService;
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

@Path("hospede")
public class HospedeWebService {

    @POST
    @Path("gravar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String gravar(String param) {
        Session session = null;
        Transaction transaction = null;
        JSONObject retorno = new JSONObject();
        try {

            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction().commit();

            JSONObject jsonHospede = new JSONObject(param);

            Iterator<String> keys = jsonHospede.keys();
            String key = keys.next();
            String value = jsonHospede.getString(key);

            Hospede hospede = HospedeDataService.getHospede(session, key, value);
            if (hospede == null) {
                hospede = new Hospede();
                hospede.setId(0);
            }
            hospede.setNome(jsonHospede.getString("nome"));
            hospede.setDocumento(jsonHospede.getString("documento"));
            hospede.setTelefone(jsonHospede.getString("telefone"));
            hospede.calcularValores(session);

            transaction = session.beginTransaction();
            HospedeDataService.setHospede(session, hospede);
            transaction.commit();

            retorno = Retorno.set(1, "Sucesso", "hospede", new JSONObject(hospede), null);

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
    @Path("excluir")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String excluir(String param) {
        Session session = null;
        Transaction transaction = null;
        JSONObject retorno = new JSONObject();
        try {

            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction().commit();

            JSONObject jsonHospede = new JSONObject(param);

            Iterator<String> keys = jsonHospede.keys();
            String key = keys.next();
            String value = jsonHospede.getString(key);

            Hospede hospede = HospedeDataService.getHospede(session, key, value);

            transaction = session.beginTransaction();
            HospedeDataService.delHospede(session, hospede);
            transaction.commit();

            retorno = Retorno.set(1, "Sucesso", "", null, null);

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
    @Path("consultar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String consultar(String param) {
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
            hospede.calcularValores(session);

            retorno = Retorno.set(1, "Sucesso", "hospede", new JSONObject(hospede), null);

        } catch (Exception e) {
            retorno = Retorno.set(0, e.getMessage(), "", null, null);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return retorno.toString();
    }

    @POST
    @Path("listarHospedes")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String listarHospedes(String param) {
        Session session = null;
        JSONObject retorno = new JSONObject();
        try {

            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction().commit();

            JSONObject jsonParam = new JSONObject(param);

            Boolean listarHospedados = jsonParam.getBoolean("listarHospedados");

            List<Hospede> listHospede = HospedeDataService.listHospededados(session, listarHospedados);

            JSONArray jsonArrayHospedes = new JSONArray();
            for (int i = 0; i < listHospede.size(); i++) {
                Hospede hospede = listHospede.get(i);
                hospede.calcularValores(session);
                jsonArrayHospedes.put(i, new JSONObject(hospede));
            }

            retorno = Retorno.set(1, "Sucesso", "listHospede", null, jsonArrayHospedes);

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
