package trismegistoplanilla.sqlserverdao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import trismegistoplanilla.dao.AreaCargoDAO;
import trismegistoplanilla.dao.AreaCargoTipoPagoDAO;
import trismegistoplanilla.dao.AreaDAO;
import trismegistoplanilla.dao.CargaFamiliarDAO;
import trismegistoplanilla.dao.CargoDAO;
import trismegistoplanilla.dao.CarreraProfesionalDAO;
import trismegistoplanilla.dao.ConfiguracionFichaDAO;
import trismegistoplanilla.dao.DAOFactory;
import trismegistoplanilla.dao.DetalleLoteFichaDocenteDAO;
import trismegistoplanilla.dao.EscalafonDAO;
import trismegistoplanilla.dao.EstadoCivilDAO;
import trismegistoplanilla.dao.EstadoEstudioDAO;
import trismegistoplanilla.dao.EstadoFichaDAO;
import trismegistoplanilla.dao.ExpedienteDAO;
import trismegistoplanilla.dao.ExperienciaLaboralDAO;
import trismegistoplanilla.dao.FichaDAO;
import trismegistoplanilla.dao.FichaLaboralDAO;
import trismegistoplanilla.dao.FondoPensionDAO;
import trismegistoplanilla.dao.FormacionAcademicaDAO;
import trismegistoplanilla.dao.LoteAprobacionDAO;
import trismegistoplanilla.dao.LoteFichaDAO;
import trismegistoplanilla.dao.NacionalidadDAO;
import trismegistoplanilla.dao.NivelEstadoDAO;
import trismegistoplanilla.dao.NivelEstudioDAO;
import trismegistoplanilla.dao.ParentescoDAO;
import trismegistoplanilla.dao.PersonaDAO;
import trismegistoplanilla.dao.SedeAreaDAO;
import trismegistoplanilla.dao.SedeDAO;
import trismegistoplanilla.dao.TipoDocumentoDAO;
import trismegistoplanilla.dao.TipoEstadoFichaDAO;
import trismegistoplanilla.dao.TipoExpedienteDAO;
import trismegistoplanilla.dao.TipoPagoDAO;
import trismegistoplanilla.dao.TokenFichaDAO;
import trismegistoplanilla.dao.TrabajadorResponsableDAO;
import trismegistoplanilla.dao.UbigeoDAO;

public class SqlserverDAOFactory extends DAOFactory {

  static {
    try {
      Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
    } catch (ClassNotFoundException ex) {
      System.out.println("Error Driver SQL SERVER " + ex.getMessage());
    }
  }

  public static Connection obtenerConexion(String base) {
    Connection conexion = null;
    String user = "";
    String pwd = "";
    String url = "";
    if (base.equalsIgnoreCase("planillabd")) {
      user = "sa";
      pwd = "sql";
//      pwd = "S3rv1d0r";
//      url = "jdbc:sqlserver://172.16.2.20:1433;databaseName=planillabd";
      url = "jdbc:sqlserver://localhost:1433;databaseName=planillabd";
      try {
        conexion = DriverManager.getConnection(url, user, pwd);
      } catch (SQLException e) {
        System.err.println("Error: no se pudo realizar la conexión a la base de datos: " + e.getMessage());
      }
    }
    return conexion;
  }

  @Override
  public TipoDocumentoDAO getTipoDocumentoDAO() {
    return new TipoDocumentoSqlserverDAO();
  }

  @Override
  public PersonaDAO getPersonaDAO() {
    return new PersonaSqlserverDAO();
  }

  @Override
  public FichaDAO getFichaDAO() {
    return new FichaSqlserverDAO();
  }

  @Override
  public TokenFichaDAO getTokenFichaDAO() {
    return new TokenFichaSqlserverDAO();
  }

  @Override
  public EstadoCivilDAO getEstadoCivilDAO() {
    return new EstadoCivilSqlserverDAO();
  }

  @Override
  public NacionalidadDAO getNacionalidadDAO() {
    return new NacionalidadSqlserverDAO();
  }

  @Override
  public UbigeoDAO getUbigeoDAO() {
    return new UbigeoSqlserverDAO();
  }

  @Override
  public ParentescoDAO getParentescoDAO() {
    return new ParentescoSqlserverDAO();
  }

  @Override
  public CargaFamiliarDAO getCargaFamiliarDAO() {
    return new CargaFamiliarSqlserverDAO();
  }

  @Override
  public CarreraProfesionalDAO getCarreraProfesionalDAO() {
    return new CarreraProfesionalSqlserverDAO();
  }

  @Override
  public NivelEstudioDAO getNivelEstudioDAO() {
    return new NivelEstudioSqlserverDAO();
  }

  @Override
  public EstadoEstudioDAO getEstadoEstudioDAO() {
    return new EstadoEstudioSqlserverDAO();
  }

  @Override
  public NivelEstadoDAO getNivelEstadoDAO() {
    return new NivelEstadoSqlserverDAO();
  }

  @Override
  public FondoPensionDAO getFondoPensionDAO() {
    return new FondoPensionSqlserverDAO();
  }

  @Override
  public ExperienciaLaboralDAO getExperienciaLaboralDAO() {
    return new ExperienciaLaboralSqlserverDAO();
  }

  @Override
  public FormacionAcademicaDAO getFormacionAcademicaDAO() {
    return new FormacionAcademicaSqlserverDAO();
  }

  @Override
  public SedeDAO getSedeDAO() {
    return new SedeSqlserverDAO();
  }

  @Override
  public AreaDAO getAreaDAO() {
    return new AreaSqlserverDAO();
  }

  @Override
  public SedeAreaDAO getSedeAreaDAO() {
    return new SedeAreaSqlserverDAO();
  }

  @Override
  public TrabajadorResponsableDAO getTrabajadorResponsableDAO() {
    return new TrabajadorResponsableSqlserverDAO();
  }

  @Override
  public CargoDAO getCargoDAO() {
    return new CargoSqlserverDAO();
  }

  @Override
  public EscalafonDAO getEscalafonDAO() {
    return new EscalafonSqlserverDAO();
  }

  @Override
  public EstadoFichaDAO getEstadoFichaDAO() {
    return new EstadoFichaSqlserverDAO();
  }

  @Override
  public FichaLaboralDAO getFichaLaboralDAO() {
    return new FichaLaboralSqlserverDAO();
  }

  @Override
  public AreaCargoDAO getAreaCargoDAO() {
    return new AreaCargoSqlserverDAO();
  }

  @Override
  public TipoPagoDAO getTipoPagoDAO() {
    return new TipoPagoSqlserverDAO();
  }

  @Override
  public AreaCargoTipoPagoDAO getAreaCargoTipoPagoDAO() {
    return new AreaCargoTipoPagoSqlserverDAO();
  }

  @Override
  public LoteFichaDAO getLoteFichaDAO() {
    return new LoteFichaSqlserverDAO();
  }

  @Override
  public TipoEstadoFichaDAO getTipoEstadoFichaDAO() {
    return new TipoEstadoFichaSqlserverDAO();
  }

  @Override
  public DetalleLoteFichaDocenteDAO getDetalleLoteFichaDocenteDAO() {
    return new DetalleLoteFichaDocenteSqlserverDAO();
  }

  @Override
  public ExpedienteDAO getExpedienteDAO() {
    return new ExpedienteSqlserverDAO();
  }

  @Override
  public LoteAprobacionDAO getLoteAprobacionDAO() {
    return new LoteAprobacionSqlserverDAO();
  }

  @Override
  public TipoExpedienteDAO getTipoExpedienteDAO() {
    return new TipoExpedienteSqlserverDAO();
  }

  @Override
  public ConfiguracionFichaDAO getConfiguracionFichaDAO() {
    return new ConfiguracionFichaSqlserverDAO();
  }

}
