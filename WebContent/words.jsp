<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
<title>Words</title>
<script type="text/javascript" src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<script src='https://code.responsivevoice.org/responsivevoice.js'></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script type="text/javascript">
	var abc;
	var myWords;

	$(function() {
		$("#date").datepicker();
		$("#learn").hide();
		loadTest();
	});

	function loadTest() {
		$.ajax({
			url : "ProcessTextServlet",
			data : {
				text : $("#text").val(),
				idText: $("#idText").val(),
				process : "LIST"
			},
			success : function(data) {
				myWords = data;
				abc = data;
				abc = abc.sort(function() {
					return Math.random() - 0.5
				});

				nextQuestion();				
				$("#wtolearn").html("Words: " + data.length + "");
				hideWords();
			}
		});
	}

	function loadWordText(idText){
		$("#idText").val(idText);
		loadTest();
	}
	function hideWords(){
		$("#words").html("");
	}
	function loadWords() {
		$("#words").html("");
		$
				.each(
						myWords,
						function(index, obj) {
							$("#words").append("<table>");
							if (obj.spanish == null)
								obj.spanish = "";
								if (obj.example == null)
									obj.example = "";	

							$("#words")
									.append(
											"<tr><td><div class=\"word\" id=\"dtv"+ obj.id+ "\">"
													+ obj.word
													+ "</div> </td><td><input type=\"text\" id=\"tw"
													+ obj.id
													+ "\" onchange=\"twSave('tw"+ obj.id+ "',"+ obj.id+ ",'te"+ obj.id+ "')\" value=\""
													+ obj.spanish
													+ "\" class=\"tword\"/></td><td><input type=\"text\" id=\"te"
													+ obj.id
													+ "\" onchange=\"twSave('tw"+ obj.id+ "',"+ obj.id+ ",'te"+ obj.id+ "')\" value=\""
													+ obj.example
													+ "\" class=\"tworde\"/></td><td>  <a class=\"add\" href=\"javascript:addWord("
													+ obj.id
													+ ")\">Add</a>|<a href=\"javascript:hearWord('dtv"+ obj.id+ "','te"+ obj.id+ "')\">Hear</a>|<a class=\"delete\" href=\"javascript:deleteWord("+ obj.id+ ")\">Del</a> </td></tr>");
							$("#words").append("</table>");
						});
		$("#text").val("");
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

	function twSave(input, id,inpute) {
		$.ajax({
			url : "ProcessTextServlet",
			data : {
				process : "UPD",
				spanish : $("#" + input).val(),
				example : $("#" + inpute).val(),
				idword : id
			},
			success : function(data) {
				loadTest();
			}
		});
	}

	function addWord(x) {
		$.ajax({
			url : "ProcessTextServlet",
			data : {
				process : "ADD",
				id : x
			},
			success : function(data) {
				loadTest();
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
											$("#table_phrases")
													.append(
															"<td><div id=\"voice"+obj.id+"\">"+ obj.phrases+ "</div><a href=\"javascript:hearText('voice"+ obj.id+ "')\">Hear</a>|<a href=\"javascript:loadWordText('"+ obj.id+ "')\">Load</a>|<a href=\"javascript:delText("
																	+ obj.id+ ")\">Del</a>|<a href=\"javascript:stop()\">Stop</a></td>");
											$("#table_phrases").append("</tr>");
										});
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
	function hearWord(id,idex) {
		read($("#" + id).html()+", "+$("#" + idex).val());
	}
	function read(text) {		
		console.log(text);
		responsiveVoice.speak(text, "US English Female", {
			rate : 0.7
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
		$("#wtolearn").html("Words: " + abc.length + " - "+index);
		var possibleResponde = [ abc[index].word,
				abc[Math.floor(Math.random() * abc.length)].word,
				abc[Math.floor(Math.random() * abc.length)].word ];

		possibleResponde = possibleResponde.sort(function() {
			return Math.random() - 0.5
		});

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

		myTimer = setInterval(function() {
			$("#response" + (index - 1)).css({
				"background-color" : "rgb(212, 212, 212)"				
			});
			clearInterval(myTimer);
		}, 500);
		 

		$('#spanish').html(abc[index].spanish);

		index++;
	}

	function clearInterval(interv){
		interv=null;
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
	width: 100px;
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
	width: 150px;
}

.tworde {
	border-width: 0px;
	border: none;
	outline: none;
	width: 420px;
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
</style>
</head>
<body>
	<form>
		<div id="wtolearn"></div>
		<center>
			<div id="spanish"></div>
			<div id="english"></div>
			<div>
				<input type="text" id="learn" onchange="validate()" />
			</div>
		</center>

		<textarea rows="5" cols="70" id="text" name="text" onchange="loadTest()"></textarea>
		<br /> <a href="javascript:loadWords()"> Show</a> - <a href="javascript:hideWords()">Hide</a>
		<table>
			<tr>
				<td><div id="words"></div></td>
				<td><div id="mywords"></div></td>
			</tr>
		</table>

		<input type="text" id="date" onchange="loadText()">
		<div style="height: 170px;"></div>
		<input type="hidden" id="idText" name="idText" value="0"/>
		<table id="table_phrases">
		</table>

<div id="heartext" style="display: none;"></div>

	</form>
</body>
</html>