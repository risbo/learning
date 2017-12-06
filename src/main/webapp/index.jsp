<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
<title>Words</title>
<script type="text/javascript"
	src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<script src='https://code.responsivevoice.org/responsivevoice.js'></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script type="text/javascript">
	var abc;
	var myWords;

	$(function() {
		$("#date").datepicker();
		$("#learn").hide();
		$("#optionsSS").hide();
		//loadTest();

		$("#wait").dialog({
			autoOpen: false,
			resizable : false,
			height : "auto",
			width : 400,
			modal : true
		});

	});
	
	var popup;
	function showWord(word) {
        popup = window.open("https://translate.google.com.co/#en/es/"+word, "Popup", "width=1000,height=500");
        popup.focus();
    }

	function loadTestTemp() {
		$.ajax({
			url : "ProcessTextServlet",
			data : {
				text : $("#text").val(),
				idText : $("#idText").val(),
				process : "LIST"
			},
			success : function(res) {
				myWords = res;
				abc = res;
// 				abc = abc.sort(function() {
// 					return Math.random() - 0.5
// 				});	
				
				if (myWords != null) {
					$("#words tbody").html("");					
					$.each(myWords,
							function(index, obj) {										
								if (obj.spanish == null)
									obj.spanish = "";
								if (obj.example == null)
									obj.example = "";
								
								$("#words tbody").append("<tr id=\"trw"+ obj.id+ "\" ><td>"
										+ "<a href=\"javascript:showWord('"+obj.word+"')\">"+obj.word+"</a>"
										+ "</td><td><input type=\"text\" id=\"tw"
										+ obj.id
										+ "\" onchange=\"twSave('tw"
										+ obj.id
										+ "',"
										+ obj.id
										+ ")\" value=\""
										+ obj.spanish
										+ "\" class=\"tword\"/></td><td>  <a href=\"javascript:read('"+obj.word+"')\">Hear</a> |<a href=\"javascript:learned('"+obj.id+"')\">Learned</a> | <a href=\"javascript:wordOk('"+obj.id+"','"+obj.word+"')\">Ok</a> </td></tr>");
								
										
					});					
					$("#text").val("");
				}
			}
		});
	}
	
	function wordOk(id, word){
		read(word);
		$("#trw"+id).remove();
				
	}
	
	function loadTest() {
		$.ajax({
			url : "ProcessTextServlet",
			data : {
				text : $("#text").val(),
				idText : $("#idText").val(),
				process : "LIST"
			},
			success : function(data) {
				myWords = data;
				abc = data;
// 				abc = abc.sort(function() {
// 					return Math.random() - 0.5
// 				});

				nextQuestion();
				$("#wtolearn").html("Words: " + abc.length + " - " + index);
				
			}
		});
	}

	function saveTest() {
		$.ajax({
			type : "POST",
			url : "ProcessTextServlet",
			data : {
				text : $("#text").val(),
				process : "TEXT"
			},
			success : function(data) {
				$("#idText").val(data);
				loadTest();
			},
			beforeSend : function() {
				$("#wait").dialog("open");
			},
			complete : function() {
				$("#wait").dialog("close");
				$("#text").val("");
			}
		});
	}

	
	function hideWords() {
		$("#words tbody").html("");
		$("#wtolearn").html("");
		$("#spanish").html("");
		$("#english").html("");
		
		
	}
	function loadWords(idText) {
		$("#idText").val(idText);
		loadTest();		
		loadTestTemp();
		$("#optionsSS").show();
		
	}

	function deleteWord(id) {
		$.ajax({
			url : "ProcessTextServlet",
			data : {
				process : "DELWORD",
				id : id
			},
			success : function(data) {
				loadTest();
			}
		});
	}
	
	function learned(id){
		$.ajax({
			url : "ProcessTextServlet",
			data : {
				process : "LEARNED",
				idword : id
			},
			success : function(data) {
				$("#trw"+id).remove();
			}
		});
	}

	function twSave(input, id) {
		$.ajax({
			url : "ProcessTextServlet",
			data : {
				process : "UPD",
				spanish : $("#" + input).val(),
				idword : id
			},
			success : function(data) {
				//loadTest();
			}
		});
	}

	function loadText() {
		$
				.ajax({
					url : "ProcessTextServlet",
					data : {
						process : "TEXTS",
						date : $("#date").val()
					},
					success : function(data) {
						$("#table_phrases").html("");
						$
								.each(
										
										
										data,
										function(index, obj) {
											$("#table_phrases").append("<tr>");
											$("#table_phrases").append("<td><div id=\"voice"+obj.id+"\">" + obj.phrases + "</div>"
													+"<a href=\"javascript:hearText('voice" + obj.id + "')\">Hear US</a>"
													+"|<a href=\"javascript:hearTextUK('voice" + obj.id + "')\">HearUK</a>"
													+"|<a href=\"javascript:stop()\">Stop</a>"
													+"|<a href=\"javascript:loadWords('" + obj.id + "')\">Show Study</a>"
													+"|<a href=\"javascript:hideWords()\">Hide Study</a>"
													
													+"|<a href=\"javascript:delText(" + obj.id + ")\">Del</a>"
													
													+"<br/><br/></td>");
											$("#table_phrases").append("</tr>");
										});
					},
					beforeSend : function() {
						$("#wait").dialog("open");
					},
					complete : function() {
						$("#wait").dialog("close");
						$("#text").val("");
					}
				});
	}
	function stop() {
		responsiveVoice.pause();
	}
	function delText(id) {
		$.ajax({
			url : "ProcessTextServlet",
			data : {
				process : "DELTEXT",
				id : id
			},
			success : function(data) {
				loadText();
			}
		});
	}

	function hearText(id) {

		$("#heartext").html($("#" + id).html());
		$("#heartext").find("br").remove();
		read($("#heartext").html());
	}
	function hearTextUK(id) {

		$("#heartext").html($("#" + id).html());
		$("#heartext").find("br").remove();
		readUK($("#heartext").html());
	}
	function hearWord(id, idex) {
		read($("#" + id).html() + ", " + $("#" + idex).val());
	}
	function read(text) {		
		responsiveVoice.speak(text, "US English Female", {
			rate : 1
		});
	}
	function readUK(text) {		
		responsiveVoice.speak(text, "UK English Male", {
			rate : 1
		});
	}

	//////////////////////////////-----
	var index = 0;
	var count = 0;

	function nextQuestion() {
		$("#learn").hide();
		if (abc[index] == null) {
			index = 0;
		}
		
		$("#wtolearn").html("Words: " + abc.length + " - " + index);
		
		var possibleResponde = [ abc[index].word
// 			,abc[Math.floor(Math.random() * abc.length)].word
// 			,abc[Math.floor(Math.random() * abc.length)].word 
			];

// 		possibleResponde = possibleResponde.sort(function() {
// 			return Math.random() - 0.5
// 		});

		var htmlResponse = "<table><tr><td>";
		for (var i = 0; i < possibleResponde.length; i++) {
			if (possibleResponde[i] == abc[index].word) {
				htmlResponse += " <a id=\"response"
						+ index
						+ "\" href=\"javascript:win('"
						+ abc[index].word
						+ "');\" style=\"float:left;width: 110px;text-align: center;\"  > "
						+ possibleResponde[i] + "</a>";
			} else {
				htmlResponse += " <a href=\"javascript:lose('"
						+ possibleResponde[i]
						+ "','"
						+ abc[index].word
						+ "');\" style=\"float:left;width: 110px;text-align: center;\" > "
						+ possibleResponde[i] + "</a> ";
			}
		}
		htmlResponse += "</td></tr></table>";

		$('#english').html(htmlResponse);

		/*myTimer = setInterval(function() {
			$("#response" + (index - 1)).css({
				"background-color" : "rgb(212, 212, 212)"
			});
			clearInterval(myTimer);
		}, 500);*/

		$('#spanish').html(abc[index].spanish);

		index++;
	}
	
	var timer = null;
	
	function startInterval() {
		timer = setInterval(function(){
			nextQuestion();
		}, 2000);
	}
	function stopInterval() { 
	  clearInterval(timer);
	}
	
	

	function win(msg) {
		responsiveVoice.speak(msg);
		count++;
		nextQuestion();
	}
	function lose(msg, correct) {
		responsiveVoice.speak(msg);
		$("#response" + (index - 1)).css({
			"background-color" : "rgb(212, 212, 212)",
			"text-shadow" : "none"
		});
		$("#learn").show();
	}
	function validate() {
		var w1 = $("#response" + (index - 1)).html();
		var w2 = $("#learn").val();
		w1 = w1.replace(/\s/g, '');
		w2 = w2.replace(/\s/g, '');

		if (w1 == w2) {
			$("#learn").val("");
			$("#learn").hide();
			nextQuestion();
		}
	}
</script>
<style type="text/css">
.cont {
	height: 20px;
	width: 140px;
}

.word {
	height: 20px;
	width: 200px;
	border-width: 0px;
	border: none;
	outline: none;
}

a {
	color: inherit;
	text-decoration: none; /* no underline */
}

.tword {
	border-width: 0px;
	border: none;
	outline: none;
	width: 200px;
}


.delete {
	height: 20px;
	width: 30px;
}

textarea {
	outline: none;
}

.add {
	height: 20px;
	width: 30px;
}

table#words { box-sizing: border-box; border-bottom: 1px solid #e8e8e8; }
table#words tbody tr:hover {
    background-color:gray;
    
}
</style>
</head>
<body onload="stop();">
	<form>
		<div id="wtolearn"></div>
		<center>
			<div id="spanish"></div>
			<div id="english"></div>
			<br/>
			
			
			<div>
				<input type="text" id="learn" onchange="validate()" />
			</div>
		</center>

		<textarea rows="5" cols="70" id="text" name="text"
			onchange="saveTest()"></textarea>
		
		<table id="words"><tbody></tbody></table>

		<input type="text" id="date" onchange="loadText()">
		<div style="height: 170px;"></div>
		<input type="hidden" id="idText" name="idText" value="0" />
		<table id="table_phrases" style="width: 64%;">
		</table>

		<div id="heartext" style="display: none;"></div>

		<div id="wait" title="Message">please, wait</div>
	</form>
</body>
</html>