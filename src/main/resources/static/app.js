//const stompClient2 = new StompJs.Client({
//    brokerURL: 'ws://localhost:8080/gs-guide-websocket'
//});
//
//stompClient.onConnect = (frame) => {
//    setConnected(true);
//    console.log('Connected: ' + frame);
//    stompClient.subscribe('/topic/greetings', (greeting) => {
//        showGreeting(JSON.parse(greeting.body).content);
//    });
//    stompClient.subscribe('/topic/login', (login) => {
//        showLogin(JSON.parse(login.body).content);
//    });
//};
//
//stompClient.onWebSocketError = (error) => {
//    console.error('Error with websocket', error);
//};
//
//stompClient.onStompError = (frame) => {
//    console.error('Broker reported error: ' + frame.headers['message']);
//    console.error('Additional details: ' + frame.body);
//};

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

//function connect() {
//    stompClient.activate();
//}
//
//function disconnect() {
//    stompClient.deactivate();
//    setConnected(false);
//    console.log("Disconnected");
//}
//
//function sendName() {
//    showGreeting("click sendName");
//
//    stompClient.onConnect = (frame) => {
//        setConnected(true);
//        console.log('Connected: ' + frame);
//
//        const destination = '/topic/cards/1';
//        destination = '/topic/2/cards';
//
//        stompClient.subscribe(destination, (cards) => {
//            console.log('Receive player\'s cards');
//            showGreeting("Receive player\'s cards");
//        })
//    };
//
//    stompClient.publish({
//        destination: "/app/hello",
//        body: JSON.stringify({'name': $("#name").val()})
//    });
//}
//
//function login() {
//
//    stompClient.publish({
//        destination: "/app/login",
//        body: JSON.stringify({'id': $("#id").val()})
//    });
//}

async function callLogin() {

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
    const data = await response.json();

//    const destination = 'ws://localhost:8080/gs-guide-websocket/topic/cards/' + data; // Use userId obtained from login;
    const destination = '/app/cards/' + data;
    console.log("destination : " + destination)
//    showGreeting("receive data : " + data);

    // Initialize WebSocket connection
    const socket = new WebSocket('ws://localhost:8080/gs-guide-websocket');

    socket.onopen = function() {
        showGreeting("WebSocket onopen.");
        console.log('WebSocket onopen');

        // Initialize Stomp Client
        const stompClient2 = new StompJs.Client({
            brokerURL: 'ws://localhost:8080/gs-guide-websocket'
        });
//        const stompClient2 = Stomp.Client(socket); // Correct initialization method
//        const stompClient2 = Stomp.over(socket);

        // Subscribe to the WebSocket channel
//        showGreeting("Try to subscribe " + destination);

        stompClient2.activate();
        stompClient2.onConnect = function(frame) {
//        stompClient2.connect = function(frame) {
//        stompClient2.connect({}, function(frame) {
            showGreeting("StompClient2 connect.");
            console.log('StompClient2 connect: ' + frame);

            stompClient2.subscribe(destination, function(message) {
                console.log("Received message : " + message.body);
                showGreeting("Receive message from : " + destination);
                // Process incoming cards data here
            });

        };

        stompClient2.onmessage = function(event) {
            console.log("message" + event);
            const message = event.data;
        };

        socket.onmessage = function(event) {
            console.log("message" + event);
            const message = event.data;
        };

    };
//    stompClient2.activate();

    socket.onerror = function(error) {
        console.error('WebSocket Error:', error);
        showGreeting("WebSocket Error: " + error.message);
    };

    socket.onclose = function(event) {
        console.log('WebSocket closed:', event);
        showGreeting("WebSocket closed.");
    };

//    showGreeting("Subscribe to 2" + destination);

    console.log('set the web socket finish');
//    const userId = data.userId; // Assuming the server responds with the user ID
//    console.log('User ID:', userId);
//    return userId;
}

// Usage:
//callLogin().then(userId => {
//    // Perform actions after successful login, such as subscribing to WebSocket
//}).catch(error => {
//    console.error('Login error:', error);
//});

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
