function uploadimg(input){
	if (window.FormData === undefined) {
		alert('В вашем браузере FormData не поддерживается')
	} else {
		var formData = new FormData();
		var id = input.getAttribute('value');
		formData.append('file', input.files[0]);
		formData.append('id', id);

		$.ajax({
			type: "POST",
			url: './api/image',
			cache: false,
			contentType: false,
			processData: false,
			data: formData,
			dataType : 'json',
			success: function(msg){
				if (msg.error == '') {
					$("#js-file").hide();
					$('#result').html(msg.success);
				} else {
					$('#result').html(msg.error);
				}
			}
		});
	}
};



function showimg(id){ //При клике по элементу с id=price выполнять...
        $.ajax({
            url: 'api/slider/' + id, //Путь к файлу, который нужно подгрузить
            type: 'GET',
            beforeSend: function(){
                $('#content').empty(); //Перед выполнением очищает содержимое блока с id=content
            },
            success: function(responce){
                    $('#content').append(responce); //Подгрузка внутрь блока с id=content
            },
            error: function(){
                alert('Error!');
            }
        });
    };
$('#content')
  .dblclick(
    function(){
     document.getElementById("content").innerHTML = "";
    }
  );
/*
   // асинхронная функция
          async function SendForm(e)
          {
              // останавливает действие по умолчанию
              e.preventDefault();

              // отправляем POST запрос на сервер
              let response = await fetch('api/addcow', {
                  method: 'POST',          // метод POST
                  dataType: "json",
                  contentType : "application/json",
                  body: new FormData(form) // в класс FormData передаем ссылку на форму
              });

              // получаем JSON
              let result = await response.json();

              console.log(result);
          };

          // при щелчке на кнопку отправки формы
          // отправляем форму на сервер
          form.onsubmit = SendForm;

*/
$( document ).ready(function() {
    $('#btn').click(
		function(){

		var tag1 = document.getElementById('tag').value;
		var type1 = document.getElementById('type').value;
		var color1 = document.getElementById('color').value;
		var birthday1 = document.getElementById('birthday').value;
		var sendInfo = {
                   tag: tag1,
                   type: type1,
                   color: color1,
                   birthday: birthday1
               };

		sendAjaxForm(sendInfo, 'api/addcow');
		setTimeout(sayHi, 1000);
			return false;
		}
	);
});


function sendAjaxForm(sendInfo, url) {
      $.ajax({
        url:     url, //url страницы
        type:     "POST", //метод отправки
        dataType: "json",
        contentType : "application/json",
        data: JSON.stringify(sendInfo),

        success: function(response) { //Данные отправлены успешно
        alert("записано");
        try {
        	result = $.parseJSON(response);
        } catch(e) {  }
    	},
    	error: function(response) { // Данные не отправлены
    	}
    	});
}

function sayHi() {
  location.reload();
}
/*
$( document ).ready(function() {
    $("#btn").click(
		function(){
			sendAjaxForm('result_form', 'ajax_form', 'api/addcow');
			return false;
		}
	);
});
function sendAjaxForm(result_form, ajax_form, url) {
    $.ajax({
        url:     url, //url страницы (action_ajax_form.php)
        type:     "POST", //метод отправки
        dataType: "html", //формат данных
        data: $("#"+ajax_form).serialize(),  // Сеарилизуем объект
        success: function(response) { //Данные отправлены успешно
        	result = $.parseJSON(response);
        	$('#result_form').html('Имя: '+result.name+'<br>Телефон: '+result.phonenumber);
    	},
    	error: function(response) { // Данные не отправлены
            $('#result_form').html('Ошибка. Данные не отправлены.');
    	}
 	});
}
*/


