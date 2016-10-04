var prefix = "http://localhost:8080";

var Smalltest = function() {
	var v = $("input:radio.:checked");
	alert(Object.getOwnPropertyNames(v)+"\n -"+
	v[0].value);
}


var MakeVis = function() {
	var obj = document.getElementById("analyze");
    obj.style.display = (obj.style.display == 'none') ? 'block' : 'none';
}

