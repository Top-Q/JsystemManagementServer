<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="">
        <meta name="author" content="">

        <title>Execution Tree</title>

        <!-- Bootstrap core CSS -->
        <link href="<c:url value="/resources/bootstrap.min.css" />" rel="stylesheet">
        <link href="<c:url value="/resources/general_page.css" />" rel="stylesheet">
        <link href="<c:url value="/resources/jquery-ui-1.8.4.custom.css" />" rel="stylesheet">
        <link href="<c:url value="/resources/test_page.css" />" rel="stylesheet">
        <link href="<c:url value="/resources/style.min.css" />" rel="stylesheet">
        <link href="<c:url value="/resources/tree.css" />" rel="stylesheet">
        <link href="<c:url value="/resources/dashboard.css" />" rel="stylesheet">              
    </head>

    <body>

        <div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
            <div class="container-fluid">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand">Execution Manager</a>
                </div>
                <!-- <div class="navbar-collapse collapse">
                    <ul class="nav navbar-nav navbar-right">
                        <li><a href="index.html">Dashboard</a></li>
                        <li><a href="#">Execution Tree</a></li>
                        <li><a href="table.html">Execution Table</a></li>
                    </ul>
                </div> -->
            </div>
        </div>

        <div class="container-fluid">
            <div class="row">
                <div id="leftPane" class="col-md-3 ">
                    <div class="btn-group btn-group-sm" style="padding:10px ; ">
                        <button type="button" onclick="javascript:$('#tree').jstree('open_all')">Expand all</button>
                        <button type="button" onclick="javascript:$('#tree').jstree('close_all')">Collapse all</button>
                    </div>
                    <div class="input-group" style="padding:10px">
                        <input id="tree_q" type="text" class="form-control">
                    </div>
                    <div id="tree">
                    </div>
                </div>
                <div class= "col-md-9 hidden-sm hidden-xs panel panel-default">
                    <div class="panel-heading">Test Information</div>
                        <div class="panel-body">    
                            <iframe id="rightPane" class="col-md-12"  >
                            </iframe>
                        </div>
                    </div>
                </div>
            </div>

        <!-- Bootstrap core JavaScript
        ================================================== -->
        <!-- Placed at the end of the document so the pages load faster -->
        <script src="<c:url value="/resources/jquery.js" />"></script>
        <script src="<c:url value="/resources/bootstrap.min.js" />"></script>
        <script src="<c:url value="/resources/jstree.min.js" />"></script>
        <script src="<c:url value="/resources/docs.min.js" />"></script>
        <script src="<c:url value="/resources/treeController.js" />"></script>
        <script src="<c:url value="/resources/execution.js" />"></script>        
        <script type="text/javascript" >
                            function populateTreeNew() {
                                $.jstree.defaults.search.show_only_matches = true;
                                treeController($('#tree'));
                                $('#tree').bind('select_node.jstree', function(e, data) {
                                    /** 
                                     * Set direct-access to node 
                                     */
                                    var a = data.node.a_attr;
                                    //var link = node.children('a'); 
                                    /** 
                                     * Add linking, because jstree catches all click-Events 
                                     */
                                    if (a.href !== '#' && a.target !== '') {
                                        var iframe = $('iframe#rightPane');
                                        iframe.attr('src', a.href);
                                    }
                                });
                                $(function() {
                                    $("#tree").jstree({
                                        /* "plugins": ["search"] */
                                        "plugins": ["dnd"]
                                    });
                                    var to = false;
                                    $('#tree_q').keyup(function() {
                                        if (to) {
                                            clearTimeout(to);
                                        }
                                        to = setTimeout(function() {
                                            var v = $('#tree_q').val();
                                            $('#tree').jstree(true).search(v);
                                        }, 250);
                                    });
                                });



                            }


                            $(document).ready(function() {
                                populateTreeNew();
                            });
        </script>


    </body>
</html>



