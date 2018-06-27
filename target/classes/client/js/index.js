"use strict";
"debugger";
function pageLoad() {
    checkLogin();
    loadMessages();
    resetForm();
    resetEditButtons();
    cancelEdits();
}
function resetForm() {
    const form = $('#messageForm');    
    form.unbind("submit");    
    form.on("submit", event => {    
        event.preventDefault();
        $.ajax({
            url: '/message/new',
            type: 'POST',
            data: form.serialize(),
            success: response => {
                if (response === 'OK') {
                    pageLoad();

                } else {
                    alert(response);
                }
            }
        });
    });
}

function loadMessages() {
    $("textarea[name='message']").val(""); console.log("Tried to clear it");
	$.ajax({
		url: '/message/list', 
		type: 'GET',
		success: messageList => {
            if (messageList.hasOwnProperty('error')) {
                alert(messageList.error);
            } else {
		    console.log(messageList);
              $('#messages').empty();
			for (let message of messageList) {
			    $('#messages').append(renderMessage(message));
            }
        }
        resetDeleteButtons();
        resetEditButtons();
        cancelEdits();
        }
    });
}

function renderMessage(message) {
    const messageDiv =
        $(`<div class="border border-primary p-2 m-2">` +
	         `<div>` +
	             `<span class="badge badge-primary mr-2 author"></span>` +
            `</div>` +
            
            `<div>` +
                `<span class="badge badge-info postDate"></span>` +
            `</div>` +
            `<div class="float-right">` +
            `<button class="deleteMessage btn btn-sm btn-danger ml-2" data-message-id="${message.id}">` +
            `Delete` +
            `</button>` +
            `<button class="editMessage btn btn-sm btn-secondary ml-2" data-message-id="${message.id}">` +
            `Edit` +
            `</button>` +
            `<button class="saveMessage btn btn-sm btn-success ml-2" data-message-id="${message.id}">` +
            `Save` +
            `</button>` +
            `<button class="cancelEditMessage btn btn-sm btn-warning ml-2" data-message-id="${message.id}">` +
            `Cancel` +
            `</button>` +
            `</div>` +

            `<div class="py-2 mx-2 messageText" id="text${message.id}"></div>` +
            `<input class="messageEditInput w-100 form-control" id="editInput${message.id}">` +
	     `</div>`);
    messageDiv.find('.author').text("Author: "+ message.author);
    messageDiv.find('.postDate').text("Date: "+message.postDate);
    messageDiv.find('.messageText').text(message.text);
    return messageDiv;
}
function resetDeleteButtons() {
    $('.deleteMessage').click(event => {
        const messageId = $(event.target).attr('data-message-id');
        $.ajax({
            url: '/message/delete',
            type: 'POST',
            data: {"messageId": messageId},
            success: response => {
                if (response === 'OK') {
                    pageLoad();
                } else {
                    alert(response);
                }
            }
        });
    });
}
function cancelEdits() {

    $(".messageEditInput").hide();
    $(".cancelEditMessage").hide();
    $(".saveMessage").hide();

    $(".messageText").show();
    $(".editMessage").show();
    $(".deleteMessage").show();
}
function resetEditButtons() {
    $('.editMessage').click(event => {

        cancelEdits();

        const editButton = $(event.target); console.log(event.target);
        const saveButton = editButton.next();
        const cancelButton = saveButton.next();
        const deleteButton = cancelButton.next();
        const messageId = editButton.attr("data-message-id");
        const textDiv = $("#text" + messageId);
        const currentText = textDiv.text();
        const editInput = textDiv.next();

        editButton.hide();
        saveButton.show(); console.log(saveButton.show());
        cancelButton.show(); console.log(cancelButton.show());

        deleteButton.hide();
        saveButton.click(event => saveEdit(event));
        cancelButton.click(event => cancelEdits()); //This is what fixed it, the click event needed to be passed properly, even if not used
        editInput.val(currentText);
        editInput.show();
        editInput.show().focus().select();
        textDiv.hide();

    });
}
function saveEdit(event) {

    const messageId = $(event.target).attr('data-message-id');
    const editedText = $("#editInput" + messageId).val();

    $.ajax({
        url: '/message/edit',
        type: 'POST',
        data: {"messageId": messageId, "messageText": editedText},
        success: response => {
            if (response === 'OK') {
                pageLoad();
            } else {
                alert(response);
            }
        }
    })

}
function checkLogin() {

    let token = Cookies.get("sessionToken");

    if (token === undefined) {
        window.location.href = "/client/login.html";
    } else {
        $.ajax({
            url: '/user/get',
            type: 'GET',
            success: username => {
                if (username === "") {
                    window.location.href = "/client/login.html";
                } else {
                    $("#username").html(username);
                }
            }
        });
    }

    $("#logout").click(event => {
        Cookies.remove("sessionToken");
        window.location.href = "/client/login.html";
    });
}
