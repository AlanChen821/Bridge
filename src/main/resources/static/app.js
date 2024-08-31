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
    const destination = '/app/cards/1';
    console.log("destination : " + destination)

    // Initialize WebSocket connection
    const socket = new WebSocket('ws://localhost:8080/gs-guide-websocket');

    socket.onopen = function() {
        showGreeting("WebSocket onopen.");
        console.log('WebSocket onopen');

        // Initialize Stomp Client
//        const stompClient = new StompJs.Client({
//            brokerURL: 'ws://localhost:8080/gs-guide-websocket'
//        });
        const stompClient = Stomp.over(socket);
//        const stompClient = Stomp.Client(socket);

        // Subscribe to the WebSocket channel
//        stompClient.activate();
//        stompClient.onConnect = function(frame) {
//        stompClient.connect = function(frame) {
        stompClient.connect({}, function(frame) {
            showGreeting("stompClient connect.");
            console.log('stompClient connect: ' + frame);

            stompClient.subscribe(destination, function(message) {
                console.log("Received message : " + message.body);
                showGreeting("Receive message from : " + destination);
                // Process incoming cards data here
            });
        });

        stompClient.onmessage = function(event) {
            console.log("message" + event);
            const message = event.data;
        };

        socket.onmessage = function(event) {
            console.log("message" + event);
            const message = event.data;
        };

    };
//    stompClient.activate();

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
