package dataservices;

import datamodel.Checkin;
import datamodel.Hospede;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class CheckinDataService {

    public static void setCheckin(Session session, Checkin checkin) throws HibernateException {
        try {
            if (checkin.getId() == 0) {
                session.save(checkin);
            } else {
                session.update(checkin);
            }
        } catch (HibernateException e) {
            throw new HibernateException(e);
        }
    }

    public static Checkin getCheckin(Session session, int id) throws HibernateException {
        Checkin checkin = null;
        try {
            checkin = (Checkin) session.get(Checkin.class, id);
        } catch (HibernateException e) {
            throw new HibernateException(e);
        }
        return checkin;
    }

    public static List<Checkin> listCheckin(Session session, Hospede hospede) throws HibernateException {
        List<Checkin> listCheckin = new ArrayList<>();
        try {
            List results = session.createCriteria(Checkin.class)
                    .add(Restrictions.eq("hospede", hospede))
                    .addOrder(Order.asc("id"))
                    .list();
            for (int i = 0; i < results.size(); i++) {
                listCheckin.add((Checkin) results.get(i));
            }
        } catch (HibernateException e) {
            throw new HibernateException(e);
        }
        return listCheckin;
    }

    public static Checkin getCheckinAberto(Session session, Hospede hospede) throws HibernateException {
        Checkin checkin = null;
        try {
            List results = session.createCriteria(Checkin.class)
                    .add(Restrictions.eq("hospede", hospede))
                    .add(Restrictions.sqlRestriction("data_saida is null"))
                    .list();
            if (!results.isEmpty()) {
                checkin = (Checkin) results.get(0);
            }
        } catch (HibernateException e) {
            throw new HibernateException(e);
        }
        return checkin;
    }

}
