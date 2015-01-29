<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="">
        <meta name="author" content="">

        <title>Execution Manager</title>

        <!-- Bootstrap core CSS -->
        <link href="<c:url value="/resources/bootstrap.min.css" />" rel="stylesheet">
        <link href="<c:url value="/resources/general_page.css" />" rel="stylesheet">
        <link href="<c:url value="/resources/jquery-ui-1.8.4.custom.css" />" rel="stylesheet">
        <link href="<c:url value="/resources/test_page.css" />" rel="stylesheet">
        <link href="<c:url value="/resources/style.min.css" />" rel="stylesheet">
        <link href="<c:url value="/resources/tree.css" />" rel="stylesheet">
        <link href="<c:url value="/resources/dashboard.css" />" rel="stylesheet">
        <link href="<c:url value="/resources/jquery-ui.css" />" rel="stylesheet">
        <link href="<c:url value="/resources/DataTables-1.10.4/media/css/jquery.dataTables.css" />" rel="stylesheet">
        
        <style type="text/css">
        	.agent {
        		opacity: 0.4;
        	}
        </style>              
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
                        <button type="button" onclick="executeParameterizedJob()">Execute</button>
                    </div>
                    <div class="input-group" style="padding:10px">
                        <input id="scenarioInput" type="text" class="form-control" placeholder="Enter Scenario..." style="width: 50%">
                        <input id="machineInput" type="text" class="form-control" placeholder="Enter Machine..." style="width: 50%">
                    </div>
                    <div id="tree">
                    </div>
                </div>
                <div class= "col-md-9 hidden-sm hidden-xs panel panel-default">
                    <div class="panel-heading">Registered Agents</div>
                        <div id="dynamictable" class="panel" style="border-bottom: 2px solid rgba(208, 188, 188, 0.32); padding-bottom: 30px ">                                                                                                                                                                                              
                        </div>
                        <div id="tWrapper" style="padding-top: 40px">
					<table id="table_id" class="display">
				    <thead>
				        <tr>
				        	<th>Execution No.</th>
				            <th>Scenario</th>
				            <th>Agent</th>
				            <th>Time Stamp</th>				         
				            <th>Status</th>
				        </tr>
				    </thead>
					</table>                	
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
        <script src="<c:url value="/resources/jquery-ui.js" />"></script>
        <script src="<c:url value="/resources/DataTables-1.10.4/media/js/jquery.dataTables.js" />"></script>          
        <script type="text/javascript" >
                            function populateTreeNew() {
                                //$.jstree.defaults.search.show_only_matches = true;
                                treeController($('#tree'));
                                $('#tree').bind('select_node.jstree', function(e, data) {
                                    /** 
                                     * Set direct-access to node 
                                     */
                                    //var a = data.node.a_attr;
                                    //var link = node.children('a'); 
                                    /** 
                                     * Add linking, because jstree catches all click-Events 
                                     */
                                    /* if (a.href !== '#' && a.target !== '') {
                                        var iframe = $('iframe#rightPane');
                                        iframe.attr('src', a.href);
                                    } */
                                    
                                    //alert(data.node.text)
                                    console.log(data.node.text);
                                    document.getElementById('scenarioInput').value = data.node.text;
                                });
                                /* $(function() {
                                    $("#tree").jstree({
                                    	"core" : {
                                    		"check_callback" : true
                                    	},
                                        "plugins": [ "dnd" ]
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
                                }); */

                            }                                                        
                            
                            function populateAgents() { 
                            	var obj;
                            	  $.ajax({url:"/testsexecutionserver/jenkins/getAgents",success:function(result){     
                            		  obj = jQuery.parseJSON(result);                            		                              	      
                              	      
                            		  $('#dynamictable').append('<table></table>');
                            	      var table = $('#dynamictable').children();
                            	      var trElem;
                            	      var tdElements = "";
                            	      var i; 
                            	      for (i = 0; i < obj.computer.length; i++) {

                            	    	  if (i % 4 == 0) {                            	    		  
                            	    		  trElem = document.createElement('tr');                            	    		   
                            	    	  }
                            	    	  
                            	    	  var agentStatus = obj.computer[i].offline;
                            	    	  if (agentStatus == true) {
                            	    		  tdElements += "<tr><td><a>" + obj.computer[i].displayName + "<img src=\"./resources/imgs/agent.png\" class=\"agent\"></a></td></tr>";
                            	    	  }
                            	    	  else {
                            	    		  tdElements += "<tr><td><a onClick=clickOnAgent(this)>" + obj.computer[i].displayName + "<img src=\"./resources/imgs/agent.png\"></a></td></tr>";  
                            	    	  }
                            	    	  
                            	    	  if(i % 4 == 3){
                            	    		trElem.innerHTML = tdElements;
                            	    		tdElements = "";
                            	    		table.append(trElem);
                            	    	  }                            	    	  
                            	      }
                            	      
                            	      if(i % 4 != 0){
                            	    	  trElem.innerHTML = tdElements;
                            	    	  table.append(trElem); 
                            	      }
                            	    }});                                                   	      
                            }                          
                            
                            function clickOnAgent(elem) {
                            	document.getElementById('machineInput').value = elem.innerText;
                            }
                            
                            function executeJob() {
                            	$.ajax({url:"/testsexecutionserver/jenkins/executeDefaultJob",dataType:"json",success:function(data){
                              	  console.log(data);
                          	    },fail:function(result) {
                          	    	alert('Failed to get scenario model!!')
                          	    }});
                            }
                            
                            function executeParameterizedJob() {
                            	var reqParams = {};
                            	reqParams.agent = document.getElementById('machineInput').value;
                            	reqParams.scenario = document.getElementById('scenarioInput').value;
                            	reqParams.nodeMark = document.getElementById('machineInput').value;
                            	console.log(reqParams);
                            	$.get("/testsexecutionserver/jenkins/executeParameterizedJob", reqParams, function(data) {
                            		console.log(data);
                            	});
                            }
                            
                            function cancelExecution(jobId) {
                            	var reqParams = {};
                            	reqParams.jobId = jobId;
                            	console.log("cancelling " + jobId)
                            	$.get("/testsexecutionserver/jenkins/cancelJob", reqParams, function(data) {
                            		console.log(data);
                            	});
                            }
                            
                            $(document).ready(function() {
                                populateTreeNew();
                                populateAgents();
                                var oTable = $('#table_id').DataTable({
                                	"processing": true,
                                    "serverSide": false,
                                    "ajax": "/testsexecutionserver/jenkins/getExecutionHistory",
                                    "columns": [
                                        { "data": "executionNumber" },
                                        { "data": "scenario" },
                                        { "data": "agent" },
                                        { "data": "timeStamp" },
                                        { "data": "status" },
                                    ],
                                    "initComplete": function () {
                                        var api = this.api();
                                        api.$('tr').click( function () {
                                        	if (this.getElementsByTagName('td')[4].innerHTML == 'RUNNING') {
                                        		if (confirm('Are you sure you want to cancel run #' + this.getElementsByTagName('td')[0].innerHTML) == true) {
                                        			cancelExecution(this.getElementsByTagName('td')[0].innerHTML);                                        			
                                        		}
                                        	}
                                        });
                                    }
                                });
                            });            
        </script>

    </body>
</html>



