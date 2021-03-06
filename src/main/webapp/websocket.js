const messageWindow = document.getElementById("messages");
const sendButton = document.getElementById("send");

const messageInput = document.getElementById("message");
const fileInput = document.getElementById("file");

const sendImageButton = document.getElementById("sendImage");

const baseUri = window.location.href;
let wsUri = baseUri.concat("chat/");
wsUri = wsUri.replace("http", "ws");

const socket = new WebSocket(wsUri);

socket.binaryType = "arraybuffer";

socket.onopen = function (event) {
    addMessageToWindow("Connected");
};

socket.onmessage = function (event) {
    if (event.data instanceof ArrayBuffer) {
        addImageToWindow(event.data);
    } else {
        addMessageToWindow(event.data + "\n");
    }
};

sendButton.onclick = function (event) {
    sendMessage(messageInput.value);
    messageInput.value = "";
};

sendImageButton.onclick = function (event) {
    let file = fileInput.files[0];
    sendMessage(file);
    fileInput.value = null;
};

function sendMessage(message) {
    socket.send(message);
}

function addMessageToWindow(message) {
    messageWindow.innerHTML += `<div>${message}</div>`
}

function addImageToWindow(image) {
    let url = URL.createObjectURL(new Blob([image]));
    messageWindow.innerHTML += `<img src="${url}"/>`
}