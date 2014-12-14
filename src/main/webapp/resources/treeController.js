/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function populateChildren(source, destination) {
    $(source).each(function() {
        switch (this.type) {
            case "scenario" :
                var children = new Array();
                destination.push({'text': this.name, 'icon': suiteIcon(this.status), 'children': children, 'type': this.status , 'a_attr':{'class' : this.status}});
                populateChildren(this.children, children);
                break;
            case "test":
                destination.push({'text': this.index + ". " + this.name, 'icon': testIcon(this.status), 'rel': this.status, 'type': this.status, 'a_attr': {'href': "tests/test_" + this.uid+"/test.html",'class':this.status}});
                break;
        }
    });
}

function populateNodes(node, destionation) {
	if (node.name) {
		$(node.childNodes).each(function(childIndex) {
			var children = new Array();
			destionation.push({'text': decideName(node), 'icon': decideIcon(node), 'children': children});
			populateNodes(node.childNodes[childIndex], children);
		});
		/*var children = new Array();
		destionation.push({'text': node.name, 'icon': decideIcon(node), 'children': children});
		populateNodes(node.childNodes[0], children);*/
	}
	else {
		destionation.push({'text': node.methodName, 'icon': decideIcon(node), 'children': []});
	}
}



function testIcon(status) {
    switch (status) {
        case "success":
            return "./resources/imgs/testok.gif";
        case "failure":
            return "./resources/imgs/testfail.gif";
        case "error":
            return "./resources/imgs/testerr.gif";
        case "warning":
            return "./resources/imgs/testwarning.gif";
        case "inProgress":
            return "./resources/imgs/testrun.gif";
    }
}



function suiteIcon(status) {
    switch (status) {
        case "success":
            return "./resources/imgs/tsuiteok.gif";
        case "failure":
            return "./resources/imgs/tsuitefail.gif";
        case "error":
            return "./resources/imgs/tsuiteerror.gif";
        case "warning":
            return "./resources/imgs/tsuiteWarning.gif";
    }
}

function decideName(node) {
	if (node.name) {
		return node.name;
	}
	else {
		return node.methodName;
	}
}

function decideIcon(node) {
	if (node.name) {
		return "./resources/imgs/tsuiteok.gif";
	}
	else {
		return "./resources/imgs/testok.gif";
	}
}

function treeController(element) {
	$.ajax({url:"http://localhost:8080/testsexecutionserver/scenarioComposer/getScenarioModel",dataType:"json",success:function(data){
    	  handleTree(data, element);
	    },fail:function(result) {
	    	alert('Failed to get scenario model!!')
	    }});
}

function handleTree(json, element) {
	/*console.log(json);
	console.log(element);*/
	var children = new Array();
    var tree = {'text': 'RootScenario', 'icon': './resources/imgs/tsuiteok.gif', 'children': children};
    var tree = {'text': 'RootScenario', 'icon': './resources/imgs/tsuiteok.gif', 'children': []};   
    $(json.childNodes).each(function(childIndex) {    	
    	var children = new Array();
    	tree.children[childIndex] = {'text': decideName(this), 'icon': decideIcon(this), 'children': children};
    	populateNodes(this, children);
    });
    core = {'core': {'data': [tree]}};
    core.plugins = ['search', 'state', 'types'];
    core.types = {'valid_children': 
                ['success'],
        'types':
                {'success':
                            {'icon':
                                        {'image': '/resources/imgs/jsystem_ico.gif'}
                            }
                }
    };
    $(element).jstree(core);
    //$(document.getElementById('tree')).jstree(core);
}




