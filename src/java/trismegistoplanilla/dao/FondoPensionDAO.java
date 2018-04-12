/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trismegistoplanilla.dao;

import org.json.JSONObject;
import trismegistoplanilla.beans.FondoPensionBean;

/**
 *
 * @author sistem23user
 */
public interface FondoPensionDAO {

    public JSONObject listarFondoPension();

    public JSONObject validarExistenciaFondoPension(FondoPensionBean fondoPension);

}
