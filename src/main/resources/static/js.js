function uploadimg(input) {
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
            dataType: 'json',
            success: function (msg) {
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


function showimg(id) { //При клике по элементу с id выполнять...
    console.log("здесь")
    $.ajax({
        url: '/api/slider/' + id, //Путь к файлу, который нужно подгрузить
        type: 'GET',
        beforeSend: function () {
            $('#content').empty(); //Перед выполнением очищает содержимое блока с id=content
        },
        success: function (responce) {
            $('#content').append(responce); //Подгрузка внутрь блока с id=content
        },
        error: function () {
            alert('Error!');
        }
    });
}
$('#content')
    .dblclick(
        function () {
            document.getElementById("content").innerHTML = "";
        }
    );

$(document).ready(function () {
    $('#btn').click(
        function () {
            var id1 = document.getElementById('cowid').value;
            if (id1 == null) {
                id1 = 0;
            }
            var tag1 = document.getElementById('tag').value;
            var type1 = document.querySelector('input[name="male"]:checked').value;
            var color1 = document.getElementById('color').value;
            var birthday1 = document.getElementById('birthday').value;
            var sendInfo = {
                id: id1,
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
        url: url, //url страницы
        type: "POST", //метод отправки
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(sendInfo),

        success: function (response) { //Данные отправлены успешно
            alert(response.massage);
            try {
                result = $.parseJSON(response);
            } catch (e) {
            }
        },
        error: function (response) {
            alert("Ошибка сервера");
        }
    });
}

function sayHi() {
    location.reload();
}

function change(id) {
    $.ajax({
        url: 'api/change/' + id,
        type: 'GET',
        dataType: 'json',
        success: function (data) {
            document.getElementById('cowid').value = data.id;
            document.getElementById('tag').value = data.tag;
            document.getElementById('color').value = data.color;
            document.getElementById('birthday').value = data.birthday;
            if (data.type == 'Б') {
                document.getElementById('type1').checked = true;
            } else {
                document.getElementById('type2').checked = true;
            }
        }
    });

}

$(function () {
    $('#traffic').on('click', '.morning', function () {
        var date = $(this).parent().find('.date').html();
        window.location.href = '/cow/search?date=' + date + '&type=morning';
    });
});
$(function () {
    $('#traffic').on('click', '.morningOut', function () {
        var date = $(this).parent().find('.date').html();
        window.location.href = '/cow/search?date=' + date + '&type=morningout';
    });
});
$(function () {
    $('#traffic').on('click', '.evening', function () {
        var date = $(this).parent().find('.date').html();
        window.location.href = '/cow/search?date=' + date + '&type=evening';
    });
});
$(function () {
    $('#traffic').on('click', '.eveningOut', function () {
        var date = $(this).parent().find('.date').html();
        window.location.href = '/cow/search?date=' + date + '&type=eveningout';
    });
});



