<!-- Main navbar -->
<div class="navbar navbar-inverse header-highlight">
    <div class="navbar-header">
        <a class="navbar-brand" href="main.jsp">
            <img src="../plantilla/assets/images/logo_light.png" alt="">
            <!--<span class="text-bold">TRISMEGISTO-PLANILLA</span>-->
        </a>

        <ul class="nav navbar-nav visible-xs-block">
            <li><a data-toggle="collapse" data-target="#navbar-mobile"><i class="icon-tree5"></i></a></li>
            <li><a class="sidebar-mobile-main-toggle"><i class="icon-paragraph-justify3"></i></a></li>
        </ul>
    </div>

    <div class="navbar-collapse collapse" id="navbar-mobile">
        <ul class="nav navbar-nav">
            <li><a class="sidebar-control sidebar-main-toggle hidden-xs"><i class="icon-paragraph-justify3"></i></a></li>
        </ul>
        <ul class="nav navbar-nav navbar-right">
            <!--            <li class="dropdown">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                <i class="icon-earth"></i>
                                <span class="visible-xs-inline-block position-right">Notificaciones</span>
                                <span class="badge bg-blue-400">2</span>
                            </a>
            
                            <div class="dropdown-menu dropdown-content width-350">
                                <div class="dropdown-content-heading">
                                    Notificaciones
                                    <ul class="icons-list">
                                        <li><i class="icon-earth"></i></a></li>
                                    </ul>
                                </div>
            
                                <ul class="media-list dropdown-content-body">
                                    <li class="media">
                                        <div class="media-left">
                                            <img src="../img/enriquexd.png" class="img-circle img-sm" alt="">
                                        </div>
            
                                        <div class="media-body">
                                            <a href="#" class="media-heading">
                                                <span class="text-semibold">Enrique A. Chamaya de la Cruz</span>
                                                <span class="media-annotation pull-right">14:58</span>
                                            </a>
            
                                            <span class="text-muted">Ha terminado de registrar su ficha de datos de personal.</span>
                                        </div>
                                    </li>
                                    <li class="media">
                                        <div class="media-left">
                                            <img src="../img/jjxd.png" class="img-circle img-sm" alt="">
                                        </div>
            
                                        <div class="media-body">
                                            <a href="#" class="media-heading">
                                                <span class="text-semibold">Juan J. Rojas Rojas</span>
                                                <span class="media-annotation pull-right">23:12</span>
                                            </a>
            
                                            <span class="text-muted">Ha terminado de registrar su ficha de datos de personal.</span>
                                        </div>
                                    </li>
                                </ul>
                            </div>
                        </li>-->

            <li class="dropdown dropdown-user">
                <a class="dropdown-toggle" data-toggle="dropdown">
                    <img avatar="" alt="" class="tp-avatar" style="">
                    <span class="nombre-usuario text-semibold"></span>
                    <i class="caret"></i>
                </a>

                <ul class="dropdown-menu dropdown-menu-right">
                    <!--<li><a href="#"><i class="icon-user-plus"></i> Mi perfil</a></li>-->
                    <!--<li class="divider"></li>-->
                    <!--<li><a href="#"><i class="icon-cog5"></i> Configuracion</a></li>-->
                    <!--<li><a href="templates/close.jsp"><i class="icon-switch2"></i> Cerrar sesion</a></li>-->
                    <li><a onclick="javascript: invalidarSesion();"><i class="icon-switch2"></i> Cerrar sesion</a></li>
                </ul>
            </li>
        </ul>
    </div>
</div>
<!-- /main navbar -->