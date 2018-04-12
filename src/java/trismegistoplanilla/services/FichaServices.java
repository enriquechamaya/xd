package trismegistoplanilla.services;

import org.json.JSONObject;
import pe.siso.webservicesseguridad.webservices.UsuarioBean;
import trismegistoplanilla.beans.EstadoFichaBean;
import trismegistoplanilla.beans.FichaBean;
import trismegistoplanilla.beans.ObservacionFichaBean;
import trismegistoplanilla.beans.PersonaBean;
import trismegistoplanilla.beans.TokenFichaBean;
import trismegistoplanilla.dao.DAOFactory;
import trismegistoplanilla.dao.FichaDAO;

public class FichaServices {

  DAOFactory factory = DAOFactory.getDAOFactory(DAOFactory.SQL_SERVER);
  FichaDAO objFichaDAO = factory.getFichaDAO();

  public JSONObject listarFichaDT(String draw, String length, String start, String search, int tipoDocumento, int criterio, UsuarioBean u) {
    JSONObject jsonObjctListarFichaDT = null;
    try {
      jsonObjctListarFichaDT = objFichaDAO.listarFichaDT(draw, length, start, search, tipoDocumento, criterio, u);
    } catch (Exception e) {
      System.out.println("Error listarFichaDT service: " + e.getMessage());
    }
    return jsonObjctListarFichaDT;
  }

  public JSONObject obtenerCodigoFicha(TokenFichaBean tf) {
    JSONObject jsonObjObtenerCodigoFicha = null;
    try {
      jsonObjObtenerCodigoFicha = objFichaDAO.obtenerCodigoFicha(tf);
    } catch (Exception e) {
      System.out.println("Error obtenerCodigoFicha service: " + e.getMessage());
    }
    return jsonObjObtenerCodigoFicha;
  }

  public JSONObject registrarFicha(JSONObject ficha, PersonaBean p, TokenFichaBean tf) {
    JSONObject jsonObjRegistrarFicha = null;
    try {
      jsonObjRegistrarFicha = objFichaDAO.registrarFicha(ficha, p, tf);
    } catch (Exception e) {
      System.out.println("Error registrarFicha service: " + e.getMessage());
    }
    return jsonObjRegistrarFicha;
  }

  public JSONObject listarDatosFicha(PersonaBean persona) {
    JSONObject jsonReturn = null;
    try {
      jsonReturn = objFichaDAO.listarDatosFicha(persona);
    } catch (Exception e) {
      System.out.println("Error listarDatosFicha service: " + e.getMessage());
    }
    return jsonReturn;
  }

  public JSONObject consultarFichasPorCriterio(String draw, int start, int length, String search, JSONObject criterioClient, UsuarioBean u) {
    JSONObject JOlistarFichas = null;
    try {
      JOlistarFichas = objFichaDAO.consultarFichasPorCriterio(draw, start, length, search, criterioClient, u);
    } catch (Exception e) {
      System.out.println("Error consultarFichasPorCriterio service: " + e.getMessage());
    }
    return JOlistarFichas;
  }

  public JSONObject listarDetalleEstadoFicha(FichaBean f) {
    JSONObject JOlistarDetalleEstadoFicha = null;
    try {
      JOlistarDetalleEstadoFicha = objFichaDAO.listarDetalleEstadoFicha(f);
    } catch (Exception e) {
      System.out.println("Error listarDetalleEstadoFicha service: " + e.getMessage());
    }
    return JOlistarDetalleEstadoFicha;
  }

  public JSONObject listarDatosAdministrativos(FichaBean f) {
    JSONObject JOlistarDatosAdministrativos = null;
    try {
      JOlistarDatosAdministrativos = objFichaDAO.listarDatosAdministrativos(f);
    } catch (Exception e) {
      System.out.println("Error listarDatosAdministrativos service: " + e.getMessage());
    }
    return JOlistarDatosAdministrativos;
  }

  public JSONObject rechazarFicha(FichaBean f, EstadoFichaBean ef) {
    JSONObject JOrechazarFicha = null;
    try {
      JOrechazarFicha = objFichaDAO.rechazarFicha(f, ef);
    } catch (Exception e) {
      System.out.println("Error rechazarFicha service: " + e.getMessage());
    }
    return JOrechazarFicha;
  }

  public JSONObject aceptarFicha(FichaBean f, EstadoFichaBean ef) {
    JSONObject JOaceptarFicha = null;
    try {
      JOaceptarFicha = objFichaDAO.aceptarFicha(f, ef);
    } catch (Exception e) {
      System.out.println("Error rechazarFicha service: " + e.getMessage());
    }
    return JOaceptarFicha;
  }

  public JSONObject listarFichasPresidenciaDT(String draw, String length, String start, JSONObject search) {
    JSONObject jsonReturn = null;
    try {
      jsonReturn = objFichaDAO.listarFichasPresidenciaDT(draw, length, start, search);
    } catch (Exception e) {
      System.out.println("Error listarFichasPresidenciaDT service: " + e.getMessage());
    }
    return jsonReturn;
  }

  public JSONObject actualizarFicha(PersonaBean p, TokenFichaBean tf) {
    JSONObject JOactualizarFicha = null;
    try {
      JOactualizarFicha = objFichaDAO.actualizarFicha(p, tf);
    } catch (Exception e) {
      System.out.println("Error actualizarFicha service: " + e.getMessage());
    }
    return JOactualizarFicha;
  }

  public JSONObject anularFicha(EstadoFichaBean ef, ObservacionFichaBean of) {
    JSONObject JOanularFicha = null;
    try {
      JOanularFicha = objFichaDAO.anularFicha(ef, of);
    } catch (Exception e) {
      System.out.println("Error anularFicha service: " + e.getMessage());
    }
    return JOanularFicha;
  }

  public JSONObject verObservacion(EstadoFichaBean ef) {
    JSONObject JOverObservacion = null;
    try {
      JOverObservacion = objFichaDAO.verObservacion(ef);
    } catch (Exception e) {
      System.out.println("Error anularFicha service: " + e.getMessage());
    }
    return JOverObservacion;
  }

  public JSONObject listarFichasPresidencia() {
    JSONObject jsonReturn = null;
    try {
      jsonReturn = objFichaDAO.listarFichasPresidencia();
    } catch (Exception e) {
      System.out.println("Error anularFicha service: " + e.getMessage());
    }
    return jsonReturn;
  }

  public JSONObject verificarExistenciaFichaAnulada(PersonaBean p) {
    JSONObject JOverificarExistenciaFichaAnulada = null;
    try {
      JOverificarExistenciaFichaAnulada = objFichaDAO.verificarExistenciaFichaAnulada(p);
    } catch (Exception e) {
      System.out.println("Error verificarExistenciaFichaAnulada service: " + e.getMessage());
    }
    return JOverificarExistenciaFichaAnulada;
  }

  public JSONObject obtenerCodigoPersonaPorTipoDocNroDoc(PersonaBean p) {
    JSONObject JOobtenerCodigoPersonaPorTipoDocNroDoc = null;
    try {
      JOobtenerCodigoPersonaPorTipoDocNroDoc = objFichaDAO.obtenerCodigoPersonaPorTipoDocNroDoc(p);
    } catch (Exception e) {
      System.out.println("Error obtenerCodigoPersonaPorTipoDocNroDoc service: " + e.getMessage());
    }
    return JOobtenerCodigoPersonaPorTipoDocNroDoc;
  }

  public JSONObject habilitarFicha(PersonaBean p, UsuarioBean u) {
    JSONObject JOhabilitarFicha = null;
    try {
      JOhabilitarFicha = objFichaDAO.habilitarFicha(p, u);
    } catch (Exception e) {
      System.out.println("Error habilitarFicha service: " + e.getMessage());
    }
    return JOhabilitarFicha;
  }

  public JSONObject validarFichaActiva(PersonaBean p) {
    JSONObject JOvalidarFichaActiva = null;
    try {
      JOvalidarFichaActiva = objFichaDAO.validarFichaActiva(p);
    } catch (Exception e) {
      System.out.println("Error validarFichaActiva service: " + e.getMessage());
    }
    return JOvalidarFichaActiva;
  }

}
