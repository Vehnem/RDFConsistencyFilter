

var prefix = "http://localhost:8080";
var query = "";
var sessionid = "film_runtime_100";

var ShowVar = function() {
  document.getElementById("demo").innerHTML = dataset;
}

var GetJson = function() {
	var endpoint = document.getElementById("endpoint").value;
	var limit = document.getElementById("limit").value;
	query = document.getElementById("queryfield").value;
        $.ajax({
        type: 'POST',
        url:  prefix + '/Rest/exec',
        data: {"query": query,
        	"limit" : limit,
        	"endpoint" : endpoint},
        dataType: 'json',
        async: true,
        success: function(result) {
            sessionid = result.sessionid;
            if(sessionid !== "error") {  
            	document.getElementById("properties").innerHTML = "";
            	document.getElementById("datatypes").innerHTML = "";
            	document.getElementById("analyze").style.display = "block";
            	document.getElementById("download").style.display = "none";
            	//btn_filter is switched in RDFAnalyze
            	RDFAnalyze("");
            }
        },
        error: function(jqXHR, textStatus, errorThrown) {
            alert("Query Error "+jqXHR.status + ' ' + jqXHR.responseText);
        }
    });
}

//RENAME in RDFAnalyze() and ButtonAnalyze()
var result;

var RDFAnalyze = function(property) {
	$.ajax({
        type: 'GET',
        url:  prefix + '/Rest/analyze/'+sessionid,
        dataType: 'json',
        async: true,
        success: function(data) {
        	var out = "Properties : <br>";
        	for(i = 0; i < data.properties; i++) {
            	out += "<input type=\"radio\" name=\"property\" class=\"r_property\" " +
            			"value=\""+data[i].property+"\"/>"+data[i].property+"<br>";
        	}
        	document.getElementById("test").innerHTML = out;
            result = data;
        },
        error: function(jqXHR, textStatus, errorThrown) {
            alert(jqXHR.status + ' ' + jqXHR.responseText);
        }
   });
}



var ButtonAnalyze = function() {
    var out = "";
    var property = $("input:radio.r_property:checked")[0].value;
    for(i = 0; i < result.properties; i++) {
    	
    	if(result[i].property == property) {
    		for(k = 0; k < result[i].numberofdatatypes; k++) {
    			out += result[i].datatypes[k].name+" "+result[i].datatypes[k].value+"<br>"
    		}
    	}
    
    }
    document.getElementById("test").innerHTML += out;
}

var FilterRDF = function() {
	var p = $("input:radio.r_property:checked");
	var o = $("input:radio.r_datatype:checked");
    $.ajax({
   		type: "POST",
   		url:  prefix + "/Rest/filter",
   		data: { "id" : sessionid ,
   				"p" : p[0].value,
   				"o" : o[0].value,
   				"duplicate_filter" : "0",
   				"query" : query }, 
   		dataType: 'json',
    	async: true,
    	success: function(result) {
    		if(result.answer == "true") {
    			document.getElementById("download").style.display = "block";
    		} else {
    			alert("Filter Error");
    		}
   		},
   		error: function(jqXHR, textStatus, errorThrown) {
    	    alert(jqXHR.status + ' 222' + jqXHR.responseText);
    	}
	});
}	

var DownloadRDF = function() {
	var formatlist = document.getElementById("menue_format");
	var format = formatlist.options[formatlist.selectedIndex].value;
	window.open(prefix+"/Rest/dataset/"+sessionid+"?format="+format,"_blank");
}
