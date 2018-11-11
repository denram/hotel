package dataservices;

import datamodel.Checkin;
import datamodel.Hospede;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

public class HospedeDataService {

    public static void setHospede(Session session, Hospede hospede) throws HibernateException {
        try {
            if (hospede.getId() == 0) {
                session.save(hospede);
            } else {
                session.update(hospede);
            }
        } catch (HibernateException e) {
            throw new HibernateException(e);
        }
    }

    public static void delHospede(Session session, Hospede hospede) throws HibernateException {
        try {

            session.delete(hospede);

        } catch (HibernateException e) {
            throw new HibernateException(e);
        }
    }

    public static Hospede getHospede(Session session, String atributo, String valor) throws HibernateException {
        Hospede hospede = null;
        try {
            Criteria criteria = session.createCriteria(Hospede.class);
            switch (atributo) {
                case "nome":
                    criteria.add(Restrictions.eq("nome", valor));
                    break;
                case "documento":
                    criteria.add(Restrictions.eq("documento", valor));
                    break;
                case "telefone":
                    criteria.add(Restrictions.eq("telefone", valor));
                    break;
                default:
                    break;
            }
            List results = criteria.list();
            if (!results.isEmpty()) {
                hospede = (Hospede) results.get(0);
            }
        } catch (HibernateException e) {
            throw new HibernateException(e);
        }
        return hospede;
    }

    public static List<Hospede> listHospededados(Session session, boolean listarHospedados) throws HibernateException {
        List<Hospede> listHospede = new ArrayList<>();
        try {
            Query query = session.createQuery("select max(checkin.id) as id"
                    + " from Checkin checkin"
                    + " group by checkin.hospede");
            query.setResultTransformer(Transformers.aliasToBean(Checkin.class));
            List<Checkin> list = query.list();
            for (int i = 0; i < list.size(); i++) {
                Checkin checkin = (Checkin) session.get(Checkin.class, list.get(i).getId());
                Hospede hospede = (Hospede) session.get(Hospede.class, checkin.getHospede().getId());
                if ((listarHospedados) && (checkin.getDataSaida() == null)) {
                    listHospede.add(hospede);
                } else if ((!listarHospedados) && (checkin.getDataSaida() != null)) {
                    listHospede.add(hospede);
                }
            }
        } catch (HibernateException e) {
            throw new HibernateException(e);
        }
        return listHospede;
    }

}
