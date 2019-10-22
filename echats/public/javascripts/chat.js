const host = 'http://localhost:8080';

function fetchHistory() {
    let url = `${host}/messages`;
    fetch(url, {
        method: 'GET',
        mode: 'cors',
        headers: { 'Content-Type': 'application/json', 'jwt': localStorage.getItem('jwt') }
    }).then(response => response.json())
        .then(messages => {
            messages.forEach(prependMessage);
        });
}

function prependMessage(message) {
    const messages = document.getElementById('messages');
    let messageView = document.createElement('div');
    messageView.classList.add('card')
    messageView.classList.add('mt-4');
    messageView.innerHTML = `
    <div class="card-body">
        <h6 class="card-subtitle mb-2 text-muted">by ${message.author.username}</h6>
        <p class="card-text">${message.text}</p>
    </div>`;
    messages.prepend(messageView);
}

function sendMessage() {
    let input = document.getElementById('message-input');
    let text = input.value;
    let url = `${host}/messages`;
    let message = { 'text': text };
    fetch(url, {
        method: 'POST',
        mode: 'cors',
        headers: { 'Content-Type': 'application/json', 'jwt': localStorage.getItem('jwt') },
        body: JSON.stringify(message)
    }).then(response => response.json())
        .then(message => {
            input.value = '';
        });
}

function waitForMessage() {
    let url = `${host}/messages/notification`;
    fetch(url, {
        method: 'GET',
        mode: 'cors',
        headers: { 'Content-Type': 'application/json', 'jwt': localStorage.getItem('jwt') }
    }).then(response => response.json())
        .then(message => {
            prependMessage(message);
            waitForMessage();
        });
}
