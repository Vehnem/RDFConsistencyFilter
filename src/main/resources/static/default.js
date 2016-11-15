var prefix = window.location.href;
var datakey = "";
var stats = "";

var RunQuery = function() {
	var endpoint = document.getElementById("endpoint").value;
	var limit = document.getElementById("limit").value;
	var query = document.getElementById("queryfield").value;
	$.ajax({
		type : 'POST',
		url : prefix + '/rdfcf/v1/query',
		data : {
			"query" : query,
			"limit" : limit,
			"endpoint" : endpoint
		},
		dataType : 'json',
		async : true,
		success : function(result) {
			datakey = result.datakey;
			if (datakey !== "") {
				document.getElementById("delete").style.display = "block";
				document.getElementById("label_datakey").innerHTML = result.datakey;
				document.getElementById("filter").style.display = "block";
				document.getElementById("download").style.display = "none";
				// btn_filter is switched in RDFAnalyze
				AnalyzeRDF();
			} else {
				alert("Query Error");
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			alert(jqXHR.status + ' ' + jqXHR.responseText);
		}
	});
}

var AnalyzeRDF = function(property) {
	$.ajax({
		type : 'GET',
		url : prefix + '/rdfcf/v1/analyze/' + datakey,
		dataType : 'json',
		async : true,
		success : function(result) {
			stats = result
			out = "<table style=\"witdh: 99%;\">";
			out +="<tr><th class=\"st_1\">Property</th><th class=\"st_1\">Datatype</th><th class=\"st_2\">Count</th></tr>";
			for (var i = 0; i < result.properties; i ++ ) {
				
				out += "<tr><td>"+result[i].property+"</td><td>";
				
				for (var ii = 0; ii < result[i].numberofdatatypes; ii++) {
					out += "<input type=\"checkbox\" class=\"datatypes\" ";
					out += "value=\""+result[i].datatypes[ii].name+"\"/>"+result[i].datatypes[ii].name+"<br>";
				}
				out += "</td><td>";
				for (var ii = 0; ii < result[i].numberofdatatypes; ii++) {
					out += result[i].datatypes[ii].value+"<br>";
				}
				out += "</td></tr>";
			}
			out += "</table>";
			document.getElementById("filter").style.display = "block";
			document.getElementById("statistics").innerHTML = out;

		},
		error : function(jqXHR, textStatus, errorThrown) {
			alert(jqXHR.status + ' ' + jqXHR.responseText);
		}
	});
}

var RunFilter = function() {
	var datatypes = $('.datatypes:checked').map(function() {
	    return this.value;
	}).get();
	document.getElementById("test").innerHTML = datatypes.join(",");
	var property = "";
		//$("input:checkbox.properties:checked");
	
	var remove_duplicates = false;
	if(document.getElementById("remove_duplicates").checked) {
		remove_duplicates = true;
	};
	var consistent = false;
	if(document.getElementById("consistent").checked) {
		consistent = true;
	};
	var rdfunit_params = "skip"
	if(document.getElementById("isRDFUnitSelected").checked) {
		rdfunit_params = document.getElementById("rdfunit_schema").value;
		rdfunit_params += document.getElementById("rdfunit_args").value;
	};
	document.getElementById("test").innerHTML = datatypes.join(",")+"---"+remove_duplicates+"---"+consistent+"---"+rdfunit_params;
	$.ajax({
		type : "POST",
		url : prefix + "/rdfcf/v1/filter/" + datakey,
		data : {
			"property" : property,
			"datatyp" : datatypes.join(","),
			"remove_duplicates" : remove_duplicates,
			"consistent" : consistent,
			"rdfunit_params" : rdfunit_params
		},
		dataType : 'json',
		async : true,
		success : function(result) {
			if (result.message == "filtered") {
				document.getElementById("download").style.display = "block";
				alert(result.rdfunit);
			} else {
				alert("Filter Error");
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			alert(jqXHR.status + ' 222' + jqXHR.responseText);
		}
	});
}

var DownloadRDF = function() {
	var formatlist = document.getElementById("menue_format");
	var format = formatlist.options[formatlist.selectedIndex].value;
	window.open(prefix + "/rdfcf/v1/show/" + datakey + "?format=" + format,
			"_blank");
}

var DeleteRDF = function() {
	$.ajax({
		type : "DELETE",
		url : prefix + "rdfcf/v1/delete/" + datakey,
		async : true,
		success : function(result) {
			if (result.message == "failed") {
				alert("Could not delete dataset. \n Dataset not found.");
			} else {
				alert("deleted");
				location.reload();
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			alert(jqXHR.status + ' 222' + jqXHR.responseText);
		}
	});
}



var Foobar = function() {
	var checkboxValues = $('.datatypes:checked').map(function() {
	    return this.value;
	}).get();
//	var datatypes = "";
//	$('.datatypes:checked').each(function() {
//		   
//		});
	
	document.getElementById("test").innerHTML = window.location.href  ;
}



