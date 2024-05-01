const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/gs-guide-websocket'
});

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/greetings', (greeting) => {
        showGreeting(JSON.parse(greeting.body).content);
    });
    stompClient.subscribe('/topic/login', (login) => {
        showLogin(JSON.parse(login.body).content);
    });
//    stompClient.subscribe('/topic/1/cards', (cards) => {
//        console.log('Receive message');
//        showGreeting("Let's play!");
//        showGreeting(JSON.parse(cards.body).content);
//    });

//    stompClient.subscribe('/topic/2/cards', (cards) => {
//        console.log('Receive player 2\'s cards');
//        showGreeting("Receive player 2\'s cards");
//    })
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
    $("#logins").html("");
}

function connect() {
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    showGreeting("click sendName");

    stompClient.onConnect = (frame) => {
        setConnected(true);
        console.log('Connected: ' + frame);

        const destination = '/topic/cards/1';
        destination = '/topic/2/cards';

        stompClient.subscribe(destination, (cards) => {
            console.log('Receive player\'s cards');
            showGreeting("Receive player\'s cards");
        })
    };

    stompClient.publish({
        destination: "/app/hello",
        body: JSON.stringify({'name': $("#name").val()})
    });
}

function login() {

    stompClient.publish({
        destination: "/app/login",
        body: JSON.stringify({'id': $("#id").val()})
    });
}

async function callLogin() {
    showGreeting("click callLogin");

    const username = "example_username"; // Replace with actual username or user input
    const response = await fetch('http://localhost:8080/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json; charset=UTF-8'
        },
        body: JSON.stringify({
            username: username ,
            name: 'this is name',
            id: "this is id"
        })
    });
//    showGreeting("after invoke login.");

    const data = await response.json();
    showGreeting("receive data : " + data);

    try {
    showGreeting("in try");
//            const response = await fetch('/login', {
//                method: 'POST',
//                headers: {
//                    'Content-Type': 'application/json'
//                },
//                body: JSON.stringify({ /* Provide necessary info for login */ })
//            });
//            const data = await response.json();
//            const userId = data.userId; // Assuming the server responds with the user ID
//            document.getElementById('userIdDisplay').textContent = userId;

            const socket = new WebSocket('ws://localhost:8080/gs-guide-websocket?userId=${data})');
            showGreeting('Subscribe ws://localhost:8080/gs-guide-websocket?userId=${data})');

            socket.onopen = function() {
                showGreeting('WebSocket connected');
                console.log('WebSocket connected');
            };
            socket.onmessage = function(event) {
                console.log('event : ' + event);
                const message = event.data;
                console.log('Message received:', message);
                document.getElementById('receivedMessageDisplay').textContent = message;
            };
            socket.onclose = function() {
                console.log('WebSocket closed');
            };
        } catch (error) {
            console.error('Error:', error);
        }

    // method 2
//    const socket = new SockJS('/websocket');
//    stompClient = Stomp.over(socket);
//    stompClient.connect({}, function() {
//        stompClient.subscribe('', function() {
//            showGreeting("inside function");
//        });
//    });

    //  method 1
    stompClient.onConnect = (frame) => {
        showGreeting("try to subscribe.");
        setConnected(true);
        console.log('Connected: ' + frame);

        const destination = '/topic/cards/' + data;

        stompClient.subscribe(destination, (cards) => {
            console.log("Subscribe to " + destination);
            showGreeting("Subscribe to " + destination);
        })
    };

    showGreeting("Subscribe to " + destination);

    const userId = data.userId; // Assuming the server responds with the user ID
    console.log('User ID:', userId);
    return userId;
}

// Usage:
callLogin().then(userId => {
    // Perform actions after successful login, such as subscribing to WebSocket
}).catch(error => {
    console.error('Login error:', error);
});

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

function showLogin(message) {
    $("#logins").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#connect" ).click(() => connect());
    $( "#disconnect" ).click(() => disconnect());
    $( "#send" ).click(() => callLogin());
    $( "#login" ).click(() => login());
});
