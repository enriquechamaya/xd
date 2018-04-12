package trismegistoplanilla.dao;

import trismegistoplanilla.sqlserverdao.SqlserverDAOFactory;

public abstract class DAOFactory {

  public static final int SQL_SERVER = 1;

  public static DAOFactory getDAOFactory(int whichFactory) {
    switch (whichFactory) {
      case SQL_SERVER:
        return new SqlserverDAOFactory();
      default:
        return null;
    }
  }

  /* TIPO DE DOCUMENTO */
  public abstract TipoDocumentoDAO getTipoDocumentoDAO();

  /* PERSONA */
  public abstract PersonaDAO getPersonaDAO();

  /* FICHA */
  public abstract FichaDAO getFichaDAO();

  /* TOKEN FICHA */
  public abstract TokenFichaDAO getTokenFichaDAO();

  /* ESTADO CIVIL*/
  public abstract EstadoCivilDAO getEstadoCivilDAO();

  /* NACIONALIDAD */
  public abstract NacionalidadDAO getNacionalidadDAO();

  /* UBIGEO */
  public abstract UbigeoDAO getUbigeoDAO();

  /* PARENTESCO */
  public abstract ParentescoDAO getParentescoDAO();

  /* CARGA FAMILIAR */
  public abstract CargaFamiliarDAO getCargaFamiliarDAO();

  /* CARRERA PROFESIONAL */
  public abstract CarreraProfesionalDAO getCarreraProfesionalDAO();

  /* NIVEL ESTUDIO*/
  public abstract NivelEstudioDAO getNivelEstudioDAO();

  /* ESTADO ESTUDIO */
  public abstract EstadoEstudioDAO getEstadoEstudioDAO();

  /* NIVEL ESTADO */
  public abstract NivelEstadoDAO getNivelEstadoDAO();

  /* FONDO PENSION */
  public abstract FondoPensionDAO getFondoPensionDAO();

  /* EXPERIENCIA LABORAL */
  public abstract ExperienciaLaboralDAO getExperienciaLaboralDAO();

  /* FORMACION ACADEMICA */
  public abstract FormacionAcademicaDAO getFormacionAcademicaDAO();

  /* SEDE */
  public abstract SedeDAO getSedeDAO();

  /* AREA */
  public abstract AreaDAO getAreaDAO();

  /* SEDE-AREA */
  public abstract SedeAreaDAO getSedeAreaDAO();

  /* TRABAJADOR RESPONSABLE */
  public abstract TrabajadorResponsableDAO getTrabajadorResponsableDAO();

  /* CARGO */
  public abstract CargoDAO getCargoDAO();

  /* ESCALAFON */
  public abstract EscalafonDAO getEscalafonDAO();

  /* ESTADO FICHA */
  public abstract EstadoFichaDAO getEstadoFichaDAO();

  /* FICHA LABORAL */
  public abstract FichaLaboralDAO getFichaLaboralDAO();

  /* AREA CARGO */
  public abstract AreaCargoDAO getAreaCargoDAO();

  /* TIPO PAGO */
  public abstract TipoPagoDAO getTipoPagoDAO();

  /* AREA CARGO TIPO PAGO */
  public abstract AreaCargoTipoPagoDAO getAreaCargoTipoPagoDAO();

  /* LOTE FICHA */
  public abstract LoteFichaDAO getLoteFichaDAO();

  /* DETALLE LOTE FICHA DOCENTE */
  public abstract DetalleLoteFichaDocenteDAO getDetalleLoteFichaDocenteDAO();

  /* TIPO ESTADO FICHA  */
  public abstract TipoEstadoFichaDAO getTipoEstadoFichaDAO();

  /* EXPEDIENTES */
  public abstract ExpedienteDAO getExpedienteDAO();

  /* LOTE APROBACION */
  public abstract LoteAprobacionDAO getLoteAprobacionDAO();

  /* TIPO EXPEDIENTE */
  public abstract TipoExpedienteDAO getTipoExpedienteDAO();
  
  /* CONFIGURACION FICHA */
   public abstract ConfiguracionFichaDAO getConfiguracionFichaDAO();

}
