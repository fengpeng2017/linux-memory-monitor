
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html>

<head>
    <title>服务器监控 - 超级管理员后台</title>
    <meta charset="utf-8">
    <!-- Bootstrap -->
    <link href="${pageContext.request.contextPath}/static/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
    <link href="${pageContext.request.contextPath}/static/bootstrap/css/bootstrap-responsive.min.css" rel="stylesheet" media="screen">
    <link href="${pageContext.request.contextPath}/static/css/monitor.css" rel="stylesheet" media="screen">
    <link href="${pageContext.request.contextPath}/static/assets/styles.css" rel="stylesheet" media="screen">
    <script src="${pageContext.request.contextPath}/static/vendors/modernizr-2.6.2-respond-1.1.0.min.js"></script>
</head>

<body>
<div class="navbar navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container-fluid">
            <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse"> <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </a>
            <a class="brand" href="#">超级管理员后台</a>
            <div class="nav-collapse collapse">
                <ul class="nav pull-right">
                    <li class="dropdown">
                        <a href="#" role="button" class="dropdown-toggle" data-toggle="dropdown"> <i class="icon-user"></i> ${user.username} <i class="caret"></i>

                        </a>
                        <ul class="dropdown-menu">
                            <li>
                                <a tabindex="-1" href="#">Profile</a>
                            </li>
                            <li class="divider"></li>
                            <li>
                                <a tabindex="-1" href="${pageContext.request.contextPath}/user/logout">Logout</a>
                            </li>
                        </ul>
                    </li>
                </ul>
                <ul class="nav">
                    <li class="active">
                        <a href="#">监控</a>
                    </li>
                   
                </ul>
            </div>
            <!--/.nav-collapse -->
        </div>
    </div>
</div>
<div class="container-fluid">
    <div class="row-fluid">
        <div class="span3" id="sidebar">
            <ul class="nav nav-list bs-docs-sidenav nav-collapse collapse">
                <li>
                    <a href="#"><i class="icon-chevron-right"></i> 服务器监控</a>
                </li>
                <li class="active">
                    <a href="#"><i class="icon-chevron-right"></i> 服务器监控</a>
                </li>
            </ul>
        </div>
        <!--/span-->
        <div class="span9" id="content">

            <!-- morris graph chart -->
            <div class="row-fluid section">
                <!-- block -->
                <div class="block">
                    <div class="navbar navbar-inner block-header">
                        <div class="muted pull-left">内存实时监控 <small></small></div>
                        <div class="pull-right"><span class="badge badge-warning">View More</span></div>
                    </div>
                    <div class="block-content collapse in">
                        <div id="console" class="robin_circle"></div>
                    </div>
                </div>
                <!-- /block -->
            </div>

        </div>
    </div>
    <hr>
    <footer>
        <p>&copy; jthinking.com 2015-2017</p>
    </footer>
</div>
<!--/.fluid-container-->

<script src="${pageContext.request.contextPath}/static/vendors/jquery-1.9.1.min.js"></script>
<script src="${pageContext.request.contextPath}/static/vendors/jquery.knob.js"></script>
<script src="${pageContext.request.contextPath}/static/vendors/raphael-min.js"></script>

<script src="${pageContext.request.contextPath}/static/bootstrap/js/bootstrap.min.js"></script>

<script src="${pageContext.request.contextPath}/static/assets/scripts.js"></script>
<script>
    var BASE_URL = "${pageContext.request.contextPath}";
    var WEB_SOCKET_URL = "${WEB_SOCKET_URL}";
    var PROJECT_NAME = "monitor";
    var MODULE_NAME = "memory";
</script>
<script type="text/javascript">
    $(document).ready(function () {
        var ws = null;
        if ('WebSocket' in window) {
            ws = new WebSocket(WEB_SOCKET_URL + "?project=" + PROJECT_NAME + "&module=" + MODULE_NAME);
        } else if ('MozWebSocket' in window) {
            ws = new MozWebSocket(WEB_SOCKET_URL + "?project=" + PROJECT_NAME + "&module=" + MODULE_NAME);
        } else {
            alert('WebSocket is not supported by this browser.');
            return;
        }
        ws.onopen = function () {
            console.log('Info: WebSocket connection opened.');
        };
        ws.onmessage = function (event) {
            var msg = event.data;

            if (msg.charAt(0) == 'd') {
                var id = msg.split("/")[1];
                $("#" + id).remove();
            }

            if (msg.charAt(0) == 'u') {
                var ip = msg.split("/")[2];
                var id = msg.split("/")[1];
                var i = parseInt(msg.split("/")[3]);
                if ($("#console").find("#cir"+id).length == 0) {
                    var content = '<div id="'+id+'" class="r_out">'+
                            '<div class="r_in">'+
                            '<div id="cir'+id+'" class="r_c c3"></div>'+
                            '<div id="num'+id+'" class="r_num">0%</div>'+
                            '</div>'+
                            '<p>' + ip + '</p>'+
                            '</div>';
                    $("#console").append(content);
                }

                if (i >= 0 && i <= 70) {
                    // 绿色
                    $("#cir"+id).css("background", "#87d7a5");
                } else if (i > 70 && i <= 90) {
                    // 棕色
                    $("#cir"+id).css("background", "#fbad4c");
                } else if (i > 90 && i <= 100) {
                    // 红色
                    $("#cir"+id).css("background", "red");
                }
                $("#cir"+id).animate({
                    height: i + "%"
                }, 1000);
                $("#num"+id).text(i + "%");
            }


        };
        ws.onclose = function (event) {
            console.log('Info: WebSocket connection closed, Code: ' + event.code + (event.reason == "" ? "" : ", Reason: " + event.reason));
        };
        //关闭浏览器窗口或刷新页面前触发
        window.onbeforeunload = function() {
            ws.onclose = function () {}; // disable onclose handler first
            if (ws != null) {
                ws.close();
                ws = null;
            }
        };

        jQuery.ajax({
            url: '/monitor/memory',
            type: 'GET',
            dataType: 'text',
            success: function(data) {
                console.log(data);
            },
            error: function(data) {
                console.log(data);
            }
        });


    });

</script>


</body>

</html>
