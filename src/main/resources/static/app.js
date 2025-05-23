const stompClient = new StompJs.Client({
//    brokerURL: 'ws://localhost:8080/gs-guide-websocket'
    brokerURL: 'wss://bridge-4204.onrender.com/gs-guide-websocket',
    reconnectDelay: 5000,   // Optionally add a reconnect delay
    // If using SockJS, set brokerURL to null and specify only the endpoint
//    webSocketFactory:function() {
//        return new SockJS('https://bridge-4204.onrender.com/gs-guide-websocket');
//    }
});

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/greetings', (greeting) => {
        console.log('Receive message from /topic/greetings : ' + greeting.body);
//        showGreeting(greeting.body);
//        showGreeting(JSON.parse(greeting.body).content);
        showGreeting(greeting.body);
    });
    stompClient.subscribe('/topic/login', (login) => {
        console.log('Receive message from /topic/login : ' + login.body);
//        showLogin(JSON.parse(login.body).content);
        showLogin(login.body);
    });
    stompClient.subscribe('/topic/entry', (entry) => {
        console.log('Receive message from /topic/entry : ' + entry.body);
        showLogin(entry.body);
        showGreeting(entry.body);
    });
    stompClient.subscribe('/topic/begin', (begin) => {
        console.log('Receive message from /topic/begin : ' + begin.body);
        showLogin(begin.body);
        showGreeting(begin.body);
    });
    stompClient.subscribe('/topic/shuffle', (shuffle) => {
        console.log('Receive message from /topic/shuffle : ' + shuffle.body);
        showGreeting(shuffle.body);
    });
    stompClient.subscribe('/topic/call', (call) => {
        console.log('Receive message from /topic/call : ' + call.body);
        showGreeting(call.body);
    });
    stompClient.subscribe('/topic/play', (play) => {
        console.log('Receive message from /topic/play : ' + play.body);
        showGreeting(play.body);
    });
    stompClient.subscribe('/topic/1/cards', (cards) => {
        console.log('Receive message from /topic/1/cards : ' + cards);
        showGreeting("Let's play!");
//        showGreeting(JSON.parse(cards.body).content);
    });

};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function subscribeToPlayer(playerId) {
    const playerTopic = `/topic/entry/${playerId}`;
    stompClient.subscribe(playerTopic, (message) => {
        console.log(`Received message from ${playerTopic}: ` + message.body);
    });
    console.log(`Subscribed to ${playerTopic}`);

    const beginTopic = `/topic/begin/${playerId}`;
    stompClient.subscribe(beginTopic, (message) => {
        console.log(`Received message from ${beginTopic}: ` + message.body);
    });
    console.log(`Subscribed to ${beginTopic}`);

    const shuffleTopic = `/topic/shuffle/${playerId}`;
    stompClient.subscribe(shuffleTopic, (message) => {
        console.log(`Received message from ${shuffleTopic}: ` + message.body);
    });
    console.log(`Subscribed to ${shuffleTopic}`);

    const callTopic = `/topic/call/${playerId}`;
    stompClient.subscribe(callTopic, (message) => {
        console.log(`Received message from ${callTopic}: ` + message.body);
    });
    console.log(`Subscribed to ${callTopic}`);

    const playTopic = `/topic/play/${playerId}`;
    stompClient.subscribe(playTopic, (message) => {
        console.log(`Received message from ${playTopic}: ` + message.body);
    });
    console.log(`Subscribed to ${playTopic}`);
}

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
    showLogin($("#id").val());
    subscribeToPlayer($("#id").val());
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
    $( "#send" ).click(() => sendName());
    $( "#login" ).click(() => login());
});
