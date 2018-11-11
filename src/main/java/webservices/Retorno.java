package webservices;

import org.json.JSONArray;
import org.json.JSONObject;

public class Retorno {

    public static JSONObject set(int status, String mensagem, String classe, JSONObject jsonObject, JSONArray jsonArray) {
        JSONObject retorno = new JSONObject();
        retorno.put("status", status);
        retorno.put("mensagem", mensagem);
        if (!classe.isEmpty()) {
            if (jsonObject != null) {
                retorno.put(classe, jsonObject);
            } else if (jsonArray != null) {
                retorno.put(classe, jsonArray);
            }
        }
        return retorno;
    }

}
