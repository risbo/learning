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
	var code;
	$(function() {

	});

	function saveQuestion() {
		var multiple = 0;
		if ($("#multiple").is(':checked')) {
			multiple = 1;
		}
		$.ajax({
			url : "ExamServlet",
			data : {
				process : "NQUESTION",
				multiple : multiple,
				exam : $("#exam").val(),
				tquestion : $("#tquestion").val()
			},
			success : function(data) {
				$("#tquestion").val("");
				$("#idquestion").val(data.id);
				$("#question").html(data.question);
			}

		});
	}

	function saveAnswer() {

		var correct = 0;
		if ($("#correct").is(':checked')) {
			correct = 1;
		}

		$.ajax({
			url : "ExamServlet",
			data : {
				process : "NASNWER",
				question : $("#idquestion").val(),
				correct : correct,
				tanswer : $("#tanswer").val()
			},
			success : function(data) {
				var html = "<div id=\"answer"+data.id+"\">" + data.answer
						+ " <a href=\"javascript:fdelete(" + data.id
						+ ")\">Del</a></div>";
				$("#answer").html($("#answer").html() + html);
				$("#tanswer").val("");
			}

		});
	}

	function fdelete(id) {
		$.ajax({
			url : "ExamServlet",
			data : {
				process : "DELANS",
				id : id
			},
			success : function(data) {
				$("#answer" + id).remove();
			}

		});
	}

	function study() {
		$.ajax({
			url : "ExamServlet",
			data : {
				process : "STUDY",
				exam : $("#exam").val()
			},
			success : function(data) {
				code = data;
				next();
			}

		});
	}

	var index = 0;
	var count = 0;

	function next() {
		$("#testanswers").html("");
		$("#testquestion").html("");

		if (code[index] == null) {
			code = code.sort(function() {
				return Math.random() - 0.5
			});

			index = 0;
		}
		$("#wtolearn").html("Questions: " + code.length + "/" + index);

		code[index].answers = code[index].answers.sort(function() {
			return Math.random() - 0.5
		});
		$("#testquestion").html(code[index].question);
		$
				.each(
						code[index].answers,
						function(i, item) {
							var html;
							var onclic = "";
							if (code[index].multiple == 1) {
								html = "<input type=\"checkbox\" onclick=\"nextCheck()\" name=\"answ\" value=\""
										+ item.id + "\"/> " + item.answer;
							} else {
								if (item.correct == 1) {
									onclic = "onclick=\"next()\"";
								}
								html = "<input type=\"radio\" "+onclic+" name=\"answ\"/> "
										+ item.answer;
							}

							$("#testanswers").html(
									$("#testanswers").html() + html + "<br/>");
						});

		index++;
	}

	function nextCheck() {
		var countCorrect = 0;
		var selected = 0;
		$.each(code[index - 1].answers, function(i, item) {
			if (item.correct == 1) {
				countCorrect++;

				jQuery("input[name='answ']").each(function() {
					if (this.value == item.id && this.checked) {
						selected++;
					}
				});
			}

		});

		if (countCorrect == selected) {
			next();
		}
	}
</script>

</head>
<body>
	<form>
		<table>
			<tr>
				<td>Examen: <select id="exam" name="exam"><option value="1">OCA</option>
						<option value="2">OCP</option></select></td>
				<td>Multiple?: <input type="checkbox" id="multiple" name="multiple" /></td>
			</tr>

			<tr>
				<td colspan="4">Pregunta</td>
			</tr>
			<tr>
				<td colspan="4"><textarea rows="5" cols="70" id="tquestion" name="tquestion"></textarea> <br />
					<a href="javascript:saveQuestion()"> Guardar</a></td>
			</tr>
			<tr>
				<td>Respuesta</td>
				<td>Correcta?<input type="checkbox" id="correct" name="correct" /></td>
				<td></td>
			</tr>
			<tr>
				<td><textarea rows="5" cols="70" id="tanswer" name="tanswer"></textarea><br /> <a
					href="javascript:saveAnswer()">Guardar</a></td>
				<td></td>
			</tr>
			<tr>
				<td colspan="4"><input type="hidden" id="idquestion" name="idquestion" />
					<div id="question"></div></td>
			</tr>
			<tr>
				<td colspan="4"><div id="answer"></div></td>
			</tr>
		</table>
		<br /> <br /> <br /> <a href="javascript:study()">Estudiar</a>


		<div id="wtolearn"></div>
		<div id="testquestion"></div>
		<div id="testanswers"></div>
	</form>
</body>
</html>